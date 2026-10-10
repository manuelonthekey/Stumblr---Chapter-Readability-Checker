package com.chaptercheck.engine;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.*;

/**
 * Detects passive voice in sentences using a heuristic:
 * a form of "to be" followed within 3 words by a past participle.
 *
 * SRP: only responsible for passive voice detection.
 * This is a heuristic, not a full parse; documented as approximate.
 */
@Component
public class PassiveVoiceDetector {

    /** Forms of "to be" */
    private static final Set<String> TO_BE = Set.of(
        "is", "are", "was", "were", "be", "been", "being",
        "am", "will be", "shall be", "has been", "have been", "had been",
        "will have been", "would be", "could be", "should be", "might be", "may be"
    );

    private static final Set<String> TO_BE_SINGLE = Set.of(
        "is", "are", "was", "were", "be", "been", "being", "am"
    );

    /** Common irregular past participles */
    private static final Set<String> IRREGULAR_PARTICIPLES = Set.of(
        "written", "seen", "done", "known", "given", "taken", "made",
        "found", "thought", "told", "said", "brought", "bought", "caught",
        "taught", "fought", "sought", "built", "felt", "kept", "left",
        "lent", "meant", "sent", "set", "led", "read", "met", "let",
        "put", "cut", "hit", "hurt", "held", "worn", "born", "grown",
        "shown", "thrown", "blown", "drawn", "driven", "fallen",
        "flown", "frozen", "hidden", "ridden", "risen", "run", "shaken",
        "spoken", "stolen", "sworn", "torn", "woken", "woven", "broken",
        "chosen", "forgotten", "gotten", "beaten", "bitten", "eaten",
        "forbidden", "mistaken", "proven", "rotten", "shrunken",
        "smitten", "stricken", "striven", "struck", "sunken", "swollen",
        "undertaken", "overridden", "overseen", "overlooked"
    );

    /** Matches a regular past participle: ends in -ed */
    private static final Pattern REGULAR_PARTICIPLE = Pattern.compile("\\w+ed$", Pattern.CASE_INSENSITIVE);

    /** Tokenizer */
    private static final Pattern WORD_PATTERN = Pattern.compile("\\b[a-zA-Z]+\\b");

    /**
     * Returns true if the sentence appears to contain passive voice.
     */
    public boolean isPassive(String sentence) {
        if (sentence == null || sentence.isBlank()) return false;

        List<String> tokens = tokenize(sentence);

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i).toLowerCase();
            if (TO_BE_SINGLE.contains(token)) {
                // Look within next 3 words for a past participle
                int lookAhead = Math.min(i + 4, tokens.size());
                for (int j = i + 1; j < lookAhead; j++) {
                    String next = tokens.get(j).toLowerCase();
                    if (isPastParticiple(next)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isPastParticiple(String word) {
        if (IRREGULAR_PARTICIPLES.contains(word)) return true;
        if (REGULAR_PARTICIPLE.matcher(word).matches()) {
            // Avoid false positives: common adjectives ending in -ed that aren't participles
            // e.g., "red", "bed" — too short
            return word.length() > 4;
        }
        return false;
    }

    private List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();
        Matcher m = WORD_PATTERN.matcher(text);
        while (m.find()) tokens.add(m.group());
        return tokens;
    }
}
