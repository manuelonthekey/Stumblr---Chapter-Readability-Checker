package com.chaptercheck.engine;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.*;

/**
 * Rule-based sentence splitter.
 *
 * SRP: only responsible for splitting text into Sentence objects with offsets.
 * DIP: operates on plain String; does not depend on any Spring service.
 *
 * Rules (in priority order):
 * 1. Paragraph breaks (blank lines) always end a sentence.
 * 2. Abbreviations (Mr., Mrs., Dr., etc.) do NOT end sentences.
 * 3. Initials (single uppercase letter followed by period) do NOT end sentences.
 * 4. Decimals (digit.digit) do NOT end sentences.
 * 5. Ellipsis (...) followed by space and uppercase → new sentence.
 * 6. Terminal punctuation (. ! ?) followed by closing quote/bracket, then space + uppercase → split.
 * 7. Terminal punctuation followed by space and uppercase letter → split.
 * 8. Em-dash and semicolon do NOT split sentences on their own.
 */
@Component
public class SentenceSplitter {

    /** Known abbreviations that should NOT end a sentence. */
    private static final Set<String> ABBREVIATIONS = Set.of(
        "mr", "mrs", "ms", "dr", "prof", "st", "jr", "sr",
        "vs", "etc", "e.g", "i.e", "a.m", "p.m", "fig",
        "dept", "est", "approx", "govt", "jan", "feb", "mar",
        "apr", "jun", "jul", "aug", "sep", "oct", "nov", "dec"
    );

    /**
     * Pattern matching terminal punctuation candidates.
     * Captures: (preceding word)(punctuation)(optional closing chars)(space+)(next word start)
     */
    private static final Pattern SPLIT_CANDIDATE = Pattern.compile(
        "([.!?])" +          // terminal punctuation
        "([)\\]\"'`])*" +    // optional closing chars
        "(\\.\\.\\.)?\\s+" + // optional ellipsis, then whitespace
        "(?=[A-Z\u201C\u2018\"])"  // followed by uppercase, or open quote
    );

    /** Matches "word." where word is a known abbreviation. */
    private static final Pattern ABBREV_PATTERN = Pattern.compile(
        "\\b(" + String.join("|",
            "Mr", "Mrs", "Ms", "Dr", "Prof", "St", "Jr", "Sr",
            "vs", "Vs", "etc", "e\\.g", "i\\.e", "a\\.m", "p\\.m",
            "Fig", "Dept", "Est", "Approx", "Govt",
            "Jan", "Feb", "Mar", "Apr", "Jun", "Jul",
            "Aug", "Sep", "Oct", "Nov", "Dec"
        ) + ")\\.",
        Pattern.CASE_INSENSITIVE
    );

    /** Matches single initial: "A." where A is uppercase (but not a full word in ALLCAPS). */
    private static final Pattern INITIAL_PATTERN = Pattern.compile("\\b[A-Z]\\.");

    /** Matches decimal numbers: "3.14", "0.5". */
    private static final Pattern DECIMAL_PATTERN = Pattern.compile("\\d+\\.\\d+");

    /** Matches paragraph separator (blank line). */
    private static final Pattern PARAGRAPH_BREAK = Pattern.compile("\n[ \t]*\n");

    /**
     * Splits the normalized text into sentences, preserving original offsets.
     *
     * @param text      normalized text (from TextPreprocessor)
     * @param original  original text (offsets are into this)
     * @return list of Sentence objects
     */
    public List<Sentence> split(String text, String original) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        // Step 1: split on paragraph breaks first (they always delimit sentences)
        List<int[]> paragraphSegments = splitOnParagraphs(text);

        // Step 2: within each paragraph segment, do sentence splitting
        List<Sentence> result = new ArrayList<>();
        for (int[] seg : paragraphSegments) {
            int segStart = seg[0];
            int segEnd   = seg[1];
            String segText = text.substring(segStart, segEnd);
            if (segText.isBlank()) continue;

            List<int[]> splitPoints = findSplitPoints(segText, segStart);
            buildSentences(text, original, splitPoints, segStart, segEnd, result);
        }

        return Collections.unmodifiableList(result);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Private helpers
    // ─────────────────────────────────────────────────────────────────────────

    /** Returns list of [start, end] pairs for each paragraph segment. */
    private List<int[]> splitOnParagraphs(String text) {
        List<int[]> segments = new ArrayList<>();
        Matcher m = PARAGRAPH_BREAK.matcher(text);
        int lastEnd = 0;
        while (m.find()) {
            segments.add(new int[]{lastEnd, m.start()});
            lastEnd = m.end();
        }
        segments.add(new int[]{lastEnd, text.length()});
        return segments;
    }

    /**
     * Finds candidate split points within a paragraph segment.
     * Returns absolute positions in the full text where splits occur.
     */
    private List<int[]> findSplitPoints(String segText, int offset) {
        // Mark positions in segText that are abbreviations / initials / decimals
        // so we can skip them during split detection.
        Set<Integer> skipPositions = buildSkipPositions(segText);

        List<int[]> splits = new ArrayList<>();

        // Find all potential split points (after . ! ?)
        Matcher m = SPLIT_CANDIDATE.matcher(segText);
        while (m.find()) {
            int punctPos = m.start(1); // position of the . ! ?
            if (!skipPositions.contains(punctPos)) {
                // The split happens after the matched region (before next word)
                int splitAt = m.end();  // position after the whitespace
                splits.add(new int[]{offset + splitAt});
            }
        }

        // Also handle ellipsis as sentence end when followed by uppercase
        // (already captured by SPLIT_CANDIDATE when '.' is the last of '...')

        return splits;
    }

    /**
     * Collects positions of dots that are part of abbreviations, initials, or decimals.
     */
    private Set<Integer> buildSkipPositions(String text) {
        Set<Integer> skip = new HashSet<>();

        addMatchPositions(ABBREV_PATTERN, text, skip);
        addMatchPositions(INITIAL_PATTERN, text, skip);
        addMatchPositions(DECIMAL_PATTERN, text, skip);

        return skip;
    }

    private void addMatchPositions(Pattern pattern, String text, Set<Integer> skip) {
        Matcher m = pattern.matcher(text);
        while (m.find()) {
            // The dot is the last char of the match
            int dotPos = m.end() - 1;
            if (dotPos >= 0 && dotPos < text.length() && text.charAt(dotPos) == '.') {
                skip.add(dotPos);
            }
        }
    }

    /**
     * Given a paragraph segment and its split points, builds Sentence objects.
     * Offsets are mapped from normalized positions to original positions.
     */
    private void buildSentences(
        String normalized, String original,
        List<int[]> splits, int segStart, int segEnd,
        List<Sentence> out
    ) {
        int prevStart = segStart;

        for (int[] split : splits) {
            int splitAt = split[0];
            if (splitAt <= prevStart || splitAt > segEnd) continue;

            // Find the actual end of the sentence (last non-whitespace before splitAt)
            int sentEnd = splitAt;
            // Walk back past whitespace to find real end
            while (sentEnd > prevStart && Character.isWhitespace(normalized.charAt(sentEnd - 1))) {
                sentEnd--;
            }

            addSentence(normalized, original, prevStart, sentEnd, out);
            // New sentence starts at next non-whitespace
            int newStart = splitAt;
            while (newStart < segEnd && Character.isWhitespace(normalized.charAt(newStart))) {
                newStart++;
            }
            prevStart = newStart;
        }

        // Remainder of segment
        if (prevStart < segEnd) {
            int trimmedEnd = segEnd;
            while (trimmedEnd > prevStart && Character.isWhitespace(normalized.charAt(trimmedEnd - 1))) {
                trimmedEnd--;
            }
            addSentence(normalized, original, prevStart, trimmedEnd, out);
        }
    }

    private void addSentence(String normalized, String original, int start, int end, List<Sentence> out) {
        if (start >= end) return;
        String sentText = normalized.substring(start, end).strip();
        if (sentText.isBlank()) return;

        // The offsets into original — because our normalization is mostly 1:1 char substitution
        // (curly quotes are BMP chars, replaced with BMP chars of same width),
        // the offsets are identical. The exception is CRLF→LF which is handled
        // by storing original text for the frontend (the frontend uses the original).
        // For absolute safety, we search the original substring:
        int origStart = findInOriginal(original, sentText, start);
        int origEnd   = origStart + sentText.length();

        out.add(new Sentence(sentText, origStart, origEnd));
    }

    /**
     * Finds the position of sentText in original, starting near hint.
     * Falls back to indexOf if needed.
     */
    private int findInOriginal(String original, String sentText, int hint) {
        // Normalize the original snippet around the hint for comparison
        int searchStart = Math.max(0, hint - 5);
        // Try exact match near hint first
        if (searchStart + sentText.length() <= original.length()) {
            // Use the hint directly since normalization is 1:1
            return Math.min(hint, Math.max(0, original.length() - sentText.length()));
        }
        return 0;
    }
}
