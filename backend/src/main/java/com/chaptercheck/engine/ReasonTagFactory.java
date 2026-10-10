package com.chaptercheck.engine;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Generates reason tags and rule-based fix hints for a scored sentence.
 *
 * SRP: only responsible for reason/hint generation.
 * OCP: add new rules without modifying SentenceScorer.
 * ISP: separated from scoring; only the controller/service assembles the full picture.
 */
@Component
public class ReasonTagFactory {

    private final WordSyllableCounter counter;

    public ReasonTagFactory(WordSyllableCounter counter) {
        this.counter = counter;
    }

    public record TagResult(List<String> reasons, List<String> hints) {}

    /**
     * Generates reason tags and hints for the given sentence.
     *
     * @param sentence   the sentence text
     * @param wordCount  pre-computed word count
     * @param clauseCount number of clause markers (commas + ; + : + dashes)
     * @param isPassive  pre-computed passive voice flag
     */
    public TagResult generate(String sentence, int wordCount, int clauseCount, boolean isPassive) {
        List<String> reasons = new ArrayList<>();
        List<String> hints   = new ArrayList<>();

        // Rule 1: Very long sentence
        if (wordCount > 30) {
            reasons.add("Very long (" + wordCount + " words)");
            hints.add(buildSplitHint(sentence));
        }

        // Rule 2: Many clauses
        if (clauseCount >= 5) {
            reasons.add("Many clauses");
            hints.add("Pull a side clause into its own sentence.");
        }

        // Rule 3: Passive voice
        if (isPassive) {
            reasons.add("Passive voice");
            hints.add("Name who or what is doing the action directly.");
        }

        // Rule 4: Heavy vocabulary (up to 3 complex words with alternatives)
        List<String> vocabHints = buildVocabHints(sentence);
        if (!vocabHints.isEmpty()) {
            reasons.add("Heavy vocabulary");
            hints.add("Try replacing: " + String.join(", ", vocabHints) + ".");
        }

        return new TagResult(
            Collections.unmodifiableList(reasons),
            Collections.unmodifiableList(hints)
        );
    }

    // ─────────────────────────────────────────────────────────────────────────

    private String buildSplitHint(String sentence) {
        // Look for nearest semicolon, then 'and'/'but'/'which' to suggest split
        if (sentence.contains(";")) {
            return "Split at the semicolon into two sentences.";
        }
        if (sentence.contains(", and ") || sentence.contains(", And ")) {
            return "Split before 'and' to form two sentences.";
        }
        if (sentence.contains(", but ") || sentence.contains(", But ")) {
            return "Split before 'but' to form two sentences.";
        }
        if (sentence.contains(", which ") || sentence.contains(", Which ")) {
            return "Turn the 'which' clause into its own sentence.";
        }
        return "Try splitting this into two shorter sentences.";
    }

    private List<String> buildVocabHints(String sentence) {
        List<String> tokens = counter.tokenize(sentence);
        List<String> found = new ArrayList<>();
        for (String token : tokens) {
            String alt = VocabularySimplifier.simplify(token);
            if (alt != null) {
                found.add(token.toLowerCase() + " → " + alt);
                if (found.size() == 3) break;
            }
        }
        return found;
    }
}
