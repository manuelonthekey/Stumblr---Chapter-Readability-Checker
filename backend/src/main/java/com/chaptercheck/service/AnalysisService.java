package com.chaptercheck.service;

import com.chaptercheck.api.dto.*;
import com.chaptercheck.config.GenreRangesConfig;
import com.chaptercheck.config.ScoringWeightsConfig;
import com.chaptercheck.engine.*;
import com.chaptercheck.exception.TextTooLongException;
import com.chaptercheck.exception.TextTooShortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Orchestrates all engine components to produce an AnalyzeResponse.
 *
 * SRP: orchestration only; no formula logic lives here.
 * DIP: depends on engine component interfaces, injected by Spring.
 * Privacy: text length is logged but NEVER the text itself.
 */
@Service
public class AnalysisService {

    private static final Logger log = LoggerFactory.getLogger(AnalysisService.class);

    private final TextPreprocessor    preprocessor;
    private final SentenceSplitter    splitter;
    private final WordSyllableCounter counter;
    private final ReadabilityCalculator calculator;
    private final SentenceScorer      scorer;
    private final PassiveVoiceDetector passiveDetector;
    private final DialogueFilter      dialogueFilter;
    private final GenreRangesConfig   genreRanges;

    public AnalysisService(
        TextPreprocessor preprocessor,
        SentenceSplitter splitter,
        WordSyllableCounter counter,
        ReadabilityCalculator calculator,
        SentenceScorer scorer,
        PassiveVoiceDetector passiveDetector,
        DialogueFilter dialogueFilter,
        GenreRangesConfig genreRanges
    ) {
        this.preprocessor    = preprocessor;
        this.splitter        = splitter;
        this.counter         = counter;
        this.calculator      = calculator;
        this.scorer          = scorer;
        this.passiveDetector = passiveDetector;
        this.dialogueFilter  = dialogueFilter;
        this.genreRanges     = genreRanges;
    }

    public AnalyzeResponse analyze(String text, boolean excludeDialogue, String genre) {
        long startMs = System.currentTimeMillis();

        // ── Input validation ─────────────────────────────────────────────────
        if (text == null) text = "";
        int charCount = text.length();

        if (charCount > ScoringWeightsConfig.MAX_CHARS) {
            throw new TextTooLongException(charCount);
        }
        if (charCount < ScoringWeightsConfig.MIN_CHARS) {
            int wc = counter.countWords(text);
            if (wc < ScoringWeightsConfig.MIN_WORDS) {
                throw new TextTooShortException(wc);
            }
        }

        // ── Preprocessing ────────────────────────────────────────────────────
        PreprocessedText preprocessed = preprocessor.preprocess(text);
        String workingText = preprocessed.normalizedText();

        // ── Optionally filter dialogue before scoring ─────────────────────────
        String scoringText = excludeDialogue
            ? dialogueFilter.filter(workingText)
            : workingText;

        // ── Sentence splitting ───────────────────────────────────────────────
        List<Sentence> sentences = splitter.split(scoringText, text);
        int sentenceCount = Math.max(1, sentences.size());

        // ── Word/syllable counts ─────────────────────────────────────────────
        List<String> allWords  = counter.tokenize(scoringText);
        int wordCount          = allWords.size();
        int syllableCount      = counter.countTotalSyllables(allWords);
        int complexWordCount   = counter.countComplexWords(allWords);
        int letterCount        = counter.countLetters(allWords);
        int paragraphCount     = preprocessed.paragraphCount();

        if (wordCount < ScoringWeightsConfig.MIN_WORDS) {
            throw new TextTooShortException(wordCount);
        }

        // ── Readability formulas ─────────────────────────────────────────────
        double flesch    = calculator.fleschReadingEase(wordCount, sentenceCount, syllableCount);
        String label     = calculator.fleschLabel(flesch);
        double fkGrade   = calculator.fleschKincaidGrade(wordCount, sentenceCount, syllableCount);
        double fog       = calculator.gunningFog(wordCount, sentenceCount, complexWordCount);
        double cli       = calculator.colemanLiau(wordCount, sentenceCount, letterCount);
        double ari       = calculator.ari(wordCount, sentenceCount, letterCount);
        double readingMin = round1((double) wordCount / ScoringWeightsConfig.READING_WPM);

        // ── Supporting stats ─────────────────────────────────────────────────
        double avgSentLen = round1((double) wordCount / sentenceCount);
        double avgSylPerWord = wordCount > 0 ? round1((double) syllableCount / wordCount) : 0;
        long longSentences = sentences.stream()
            .filter(s -> counter.countWords(s.text()) > 25).count();
        double longSentPct = round1(100.0 * longSentences / sentenceCount);
        double complexPct  = wordCount > 0 ? round1(100.0 * complexWordCount / wordCount) : 0;
        long passiveCount  = sentences.stream()
            .filter(s -> passiveDetector.isPassive(s.text())).count();
        double passivePct  = round1(100.0 * passiveCount / sentenceCount);

        // ── Genre check ──────────────────────────────────────────────────────
        GenreRangesConfig.GenreRange range = genreRanges.getRange(genre);
        String verdict;
        String genreMessage;
        if (flesch >= range.min() && flesch <= range.max()) {
            verdict = "WITHIN_RANGE";
            genreMessage = String.format(
                "Typical for %s is %d–%d. Your chapter fits well.",
                range.label(), range.min(), range.max()
            );
        } else if (flesch < range.min()) {
            verdict = "BELOW_RANGE";
            genreMessage = String.format(
                "Typical for %s is %d–%d. Your chapter is harder than typical.",
                range.label(), range.min(), range.max()
            );
        } else {
            verdict = "ABOVE_RANGE";
            genreMessage = String.format(
                "Typical for %s is %d–%d. Your chapter is easier than typical.",
                range.label(), range.min(), range.max()
            );
        }

        // ── Hardest sentences ────────────────────────────────────────────────
        List<SentenceScorer.ScoredSentence> ranked = scorer.scoreAndRank(sentences);
        List<HardestSentenceDto> hardest = new ArrayList<>();
        int limit = Math.min(ScoringWeightsConfig.TOP_SENTENCES_COUNT, ranked.size());
        for (int i = 0; i < limit; i++) {
            SentenceScorer.ScoredSentence ss = ranked.get(i);
            hardest.add(new HardestSentenceDto(
                i + 1,
                ss.sentence().text(),
                ss.sentence().start(),
                ss.sentence().end(),
                ss.wordCount(),
                ss.difficulty(),
                ss.reasons(),
                ss.hints()
            ));
        }

        long elapsed = System.currentTimeMillis() - startMs;
        // Log: length and timing only — NEVER the text
        log.info("Analysis complete: chars={} words={} sentences={} elapsed={}ms",
            charCount, wordCount, sentenceCount, elapsed);

        return new AnalyzeResponse(
            new SummaryDto(flesch, label, fkGrade, fog, cli, ari, readingMin),
            new StatsDto(wordCount, sentenceCount, paragraphCount, avgSentLen,
                avgSylPerWord, longSentPct, complexPct, passivePct),
            new GenreCheckDto(genre, range.min(), range.max(), verdict, genreMessage),
            hardest
        );
    }

    private static double round1(double v) {
        return Math.round(v * 10.0) / 10.0;
    }
}
