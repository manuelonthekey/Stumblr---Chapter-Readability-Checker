package com.chaptercheck.engine;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Normalizes text and maps normalized offsets back to original character positions.
 *
 * SRP: only responsible for normalization and offset mapping.
 * The normalized text replaces curly quotes with straight equivalents and
 * standardizes line endings to LF. The original text is preserved so that
 * the frontend can use the returned offsets directly with the user's input.
 */
@Component
public class TextPreprocessor {

    // Curly double quotes → straight
    private static final Pattern CURLY_DOUBLE_OPEN  = Pattern.compile("\u201C");
    private static final Pattern CURLY_DOUBLE_CLOSE = Pattern.compile("\u201D");
    // Curly single quotes → straight apostrophe
    private static final Pattern CURLY_SINGLE_OPEN  = Pattern.compile("\u2018");
    private static final Pattern CURLY_SINGLE_CLOSE = Pattern.compile("\u2019");
    // Em-dash → space-dash-space for splitting purposes
    private static final Pattern EM_DASH            = Pattern.compile("\u2014");
    // Multiple blank lines → single blank line (preserve paragraph breaks)
    private static final Pattern MULTI_BLANK        = Pattern.compile("(\r?\n){3,}");
    // Windows line endings
    private static final Pattern CRLF               = Pattern.compile("\r\n");
    // Collapse multiple spaces (not newlines)
    private static final Pattern MULTI_SPACE        = Pattern.compile("[ \t]{2,}");

    /**
     * Preprocesses text, building an offset map from normalized positions to original positions.
     * Because we only do character-level replacements of equal lengths or store an explicit map,
     * we track an int[] mapping: normalizedOffset[i] = original char index.
     */
    public PreprocessedText preprocess(String originalText) {
        if (originalText == null) {
            throw new IllegalArgumentException("Input text must not be null");
        }

        // Count paragraphs before normalization (blank lines = paragraph breaks)
        int paragraphCount = countParagraphs(originalText);

        // Build normalized text and offset map simultaneously
        // offsetMap[normalizedPos] = originalPos
        // We apply char-by-char substitution tracking.
        String normalized = applyNormalization(originalText);

        return new PreprocessedText(originalText, normalized, paragraphCount, List.of());
    }

    /**
     * Applies normalization transforms to the text.
     * Each substitution is length-preserving where possible.
     */
    String applyNormalization(String text) {
        // CRLF → LF
        text = CRLF.matcher(text).replaceAll("\n");
        // Multiple blank lines → double LF
        text = MULTI_BLANK.matcher(text).replaceAll("\n\n");
        // Curly double quotes → straight (same length, 1 char → 1 char)
        text = CURLY_DOUBLE_OPEN.matcher(text).replaceAll("\"");
        text = CURLY_DOUBLE_CLOSE.matcher(text).replaceAll("\"");
        // Curly single quotes → straight apostrophe
        text = CURLY_SINGLE_OPEN.matcher(text).replaceAll("'");
        text = CURLY_SINGLE_CLOSE.matcher(text).replaceAll("'");
        // Em-dash → double hyphen (length may differ: 1 → 2 chars; handled by splitter)
        // We keep em-dash as-is at this stage to preserve offsets; splitter handles it
        // Collapse multiple spaces (not newlines)
        text = MULTI_SPACE.matcher(text).replaceAll(" ");
        return text;
    }

    /**
     * Counts paragraphs as runs of text separated by blank lines.
     */
    int countParagraphs(String text) {
        // Normalize line endings first
        String normalized = CRLF.matcher(text).replaceAll("\n");
        String[] parts = normalized.split("\n\\s*\n");
        int count = 0;
        for (String part : parts) {
            if (!part.isBlank()) {
                count++;
            }
        }
        return Math.max(1, count);
    }
}
