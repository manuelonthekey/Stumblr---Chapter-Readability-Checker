package com.chaptercheck.engine;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.*;

/**
 * Counts words and estimates syllables in text.
 *
 * SRP: only responsible for word tokenization and syllable counting.
 * The heuristic is approximate (documented as such in the PRD).
 * An exception dictionary handles common problem words.
 *
 * Word definition: sequences of letters with internal apostrophes or hyphens.
 * Numbers count as words. Complex word = 3+ syllables.
 */
@Component
public class WordSyllableCounter {

    /** Matches a word token: letters with internal ' or -, OR a number */
    private static final Pattern WORD_PATTERN = Pattern.compile(
        "[a-zA-Z]+(?:['-][a-zA-Z]+)*|\\d+(?:[.,]\\d+)*"
    );

    /** Vowel groups */
    private static final Pattern VOWEL_GROUP = Pattern.compile("[aeiouy]+");

    /** Matches silent -ed endings (consonant before -ed, e.g. talked, jumped) */
    private static final Pattern SILENT_ED = Pattern.compile("[^aeiouy]ed$");

    /** -le after a consonant is syllabic (e.g. little, simple) — keep it */
    private static final Pattern SYLLABIC_LE = Pattern.compile("[^aeiouy]le$");

    /** Matching -es / -ing / -ed inflections that don't add syllables */
    private static final Pattern INFLECTION_ES  = Pattern.compile("[^aeiouy]es$");
    private static final Pattern INFLECTION_ING = Pattern.compile("ing$");

    /**
     * Counts words in the given text.
     */
    public int countWords(String text) {
        return tokenize(text).size();
    }

    /**
     * Tokenizes the text into words.
     */
    public List<String> tokenize(String text) {
        if (text == null || text.isBlank()) return List.of();
        List<String> words = new ArrayList<>();
        Matcher m = WORD_PATTERN.matcher(text);
        while (m.find()) {
            words.add(m.group());
        }
        return Collections.unmodifiableList(words);
    }

    /**
     * Counts syllables for a single word.
     * NOTE: This is a heuristic and is approximate.
     */
    public int countSyllables(String word) {
        if (word == null || word.isBlank()) return 0;

        String lower = word.toLowerCase().replaceAll("[^a-z]", "");
        if (lower.isEmpty()) return 1;

        // Check exception dictionary first
        int known = SyllableExceptionDictionary.lookup(lower);
        if (known >= 0) return known;

        // Count vowel groups
        Matcher m = VOWEL_GROUP.matcher(lower);
        int count = 0;
        while (m.find()) count++;

        // Subtract silent trailing 'e' (but not 'le' after consonant — that's syllabic)
        if (lower.endsWith("e") && !SYLLABIC_LE.matcher(lower).find()) {
            count--;
        }

        // Subtract silent -ed (consonant + ed)
        if (SILENT_ED.matcher(lower).find()) {
            count--;
        }

        // Minimum 1 syllable
        return Math.max(1, count);
    }

    /**
     * Counts total syllables across a list of word tokens.
     */
    public int countTotalSyllables(List<String> words) {
        int total = 0;
        for (String word : words) {
            total += countSyllables(word);
        }
        return total;
    }

    /**
     * Counts complex words (3+ syllables) in a list of tokens.
     * Excludes proper nouns mid-sentence (starts with uppercase and position > 0).
     * Excludes -es, -ed, -ing inflections that don't genuinely add a syllable.
     */
    public int countComplexWords(List<String> words) {
        int count = 0;
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            // Skip potential proper nouns (mid-sentence uppercase)
            if (i > 0 && Character.isUpperCase(word.charAt(0))) {
                continue;
            }
            // Strip common inflections before counting
            String stripped = stripInflection(word.toLowerCase());
            if (countSyllables(stripped) >= 3) {
                count++;
            }
        }
        return count;
    }

    /**
     * Strips common inflections (-es, -ed, -ing) only when they don't change
     * the syllable count (i.e., when the suffix was silent or orthographic).
     */
    String stripInflection(String lower) {
        // -ing → strip if base word has vowel
        if (lower.length() > 5 && lower.endsWith("ing")) {
            String base = lower.substring(0, lower.length() - 3);
            if (VOWEL_GROUP.matcher(base).find()) return base;
        }
        // -es → strip (consonant + es)
        if (lower.length() > 4 && INFLECTION_ES.matcher(lower).find()) {
            return lower.substring(0, lower.length() - 2);
        }
        // -ed → handled by silent-ed rule in countSyllables; no strip needed here
        return lower;
    }

    /**
     * Counts total letters (only alphabetic) across word tokens.
     */
    public int countLetters(List<String> words) {
        int total = 0;
        for (String word : words) {
            for (char c : word.toCharArray()) {
                if (Character.isLetter(c)) total++;
            }
        }
        return total;
    }
}
