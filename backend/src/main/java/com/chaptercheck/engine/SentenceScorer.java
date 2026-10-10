package com.chaptercheck.engine;

import com.chaptercheck.config.ScoringWeightsConfig;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Scores individual sentences on a 0–100 difficulty scale.
 *
 * SRP: only responsible for computing per-sentence difficulty scores.
 * OCP: weights are externalized in ScoringWeightsConfig.
 * DIP: depends on WordSyllableCounter and PassiveVoiceDetector interfaces-by-convention.
 */
@Component
public class SentenceScorer {

    private final WordSyllableCounter counter;
    private final PassiveVoiceDetector passiveDetector;
    private final ReasonTagFactory tagFactory;

    public SentenceScorer(
        WordSyllableCounter counter,
        PassiveVoiceDetector passiveDetector,
        ReasonTagFactory tagFactory
    ) {
        this.counter        = counter;
        this.passiveDetector = passiveDetector;
        this.tagFactory     = tagFactory;
    }

    /** Clause markers: comma, semicolon, colon, em-dash, hyphen-dash */
    private static final Pattern CLAUSE_MARKER = Pattern.compile("[,;:\u2014-]");

    public record ScoredSentence(
        Sentence sentence,
        int wordCount,
        double difficulty,
        List<String> reasons,
        List<String> hints
    ) {}

    /**
     * Scores all sentences and returns them sorted by difficulty descending.
     * Only sentences of MIN_ELIGIBLE_WORDS or more words are eligible.
     */
    public List<ScoredSentence> scoreAndRank(List<Sentence> sentences) {
        List<ScoredSentence> scored = new ArrayList<>();

        for (Sentence s : sentences) {
            List<String> tokens = counter.tokenize(s.text());
            int wordCount = tokens.size();
            if (wordCount < ScoringWeightsConfig.MIN_ELIGIBLE_WORDS) continue;

            double difficulty = computeDifficulty(s.text(), tokens, wordCount);
            int clauseCount = countClauseMarkers(s.text());
            boolean passive  = passiveDetector.isPassive(s.text());

            ReasonTagFactory.TagResult tags = tagFactory.generate(
                s.text(), wordCount, clauseCount, passive
            );

            scored.add(new ScoredSentence(s, wordCount, difficulty, tags.reasons(), tags.hints()));
        }

        // Sort: difficulty descending; tie-break: word count descending
        scored.sort(Comparator
            .comparingDouble(ScoredSentence::difficulty).reversed()
            .thenComparingInt(ScoredSentence::wordCount).reversed()
        );

        return Collections.unmodifiableList(scored);
    }

    // ─────────────────────────────────────────────────────────────────────────

    double computeDifficulty(String text, List<String> tokens, int wordCount) {
        // 1. Length component
        double lengthScore = Math.min(1.0, (double) wordCount / ScoringWeightsConfig.LENGTH_NORM);

        // 2. Average syllables per word
        int syllables = counter.countTotalSyllables(tokens);
        double avgSyl = wordCount > 0 ? (double) syllables / wordCount : 1.0;
        double wordScore = clamp01(
            (avgSyl - ScoringWeightsConfig.WORD_SYLLABLES_MIN)
            / (ScoringWeightsConfig.WORD_SYLLABLES_MAX - ScoringWeightsConfig.WORD_SYLLABLES_MIN)
        );

        // 3. Complex word share
        int complexWords = counter.countComplexWords(tokens);
        double complexScore = Math.min(1.0,
            (double) complexWords / wordCount / ScoringWeightsConfig.COMPLEX_WORD_NORM
        );

        // 4. Clause density
        int clauseCount = countClauseMarkers(text);
        double clauseScore = Math.min(1.0, clauseCount / ScoringWeightsConfig.CLAUSE_DENSITY_NORM);

        // 5. Passive voice
        double passiveScore = passiveDetector.isPassive(text) ? 1.0 : 0.0;

        double raw = ScoringWeightsConfig.LENGTH_WEIGHT         * lengthScore
                   + ScoringWeightsConfig.WORD_WEIGHT           * wordScore
                   + ScoringWeightsConfig.COMPLEX_WORD_WEIGHT   * complexScore
                   + ScoringWeightsConfig.CLAUSE_DENSITY_WEIGHT * clauseScore
                   + ScoringWeightsConfig.PASSIVE_VOICE_WEIGHT  * passiveScore;

        return Math.round(raw * 100.0 * 10.0) / 10.0; // 0–100, 1 decimal
    }

    private int countClauseMarkers(String text) {
        int count = 0;
        var m = CLAUSE_MARKER.matcher(text);
        while (m.find()) count++;
        return count;
    }

    private static double clamp01(double v) {
        return Math.max(0.0, Math.min(1.0, v));
    }
}
