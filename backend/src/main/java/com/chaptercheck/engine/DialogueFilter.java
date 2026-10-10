package com.chaptercheck.engine;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Filters dialogue from text before scoring when excludeDialogue=true.
 * Replaces text inside straight or curly double quotes with spaces to preserve offsets.
 *
 * SRP: only responsible for dialogue stripping.
 */
@Component
public class DialogueFilter {

    /** Matches content in straight or curly double quotes (non-greedy) */
    private static final Pattern DIALOGUE_PATTERN = Pattern.compile(
        "\"[^\"]*\"|" +             // straight double quotes
        "\u201C[^\u201D]*\u201D"    // curly double quotes (left " right ")
    );

    /**
     * Replaces dialogue portions with spaces, preserving text length and offsets.
     * Returns the filtered text.
     */
    public String filter(String text) {
        if (text == null) return "";
        StringBuilder sb = new StringBuilder(text);
        var matcher = DIALOGUE_PATTERN.matcher(text);
        while (matcher.find()) {
            int start = matcher.start();
            int end   = matcher.end();
            for (int i = start; i < end; i++) {
                sb.setCharAt(i, ' ');
            }
        }
        return sb.toString();
    }
}
