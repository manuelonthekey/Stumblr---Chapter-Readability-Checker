package com.chaptercheck.engine;

import java.util.List;

/**
 * Result of text preprocessing.
 * normalizedText has curly quotes flattened and line endings normalized.
 * sentences preserve original character offsets.
 * paragraphCount reflects blank-line-separated paragraphs in the original text.
 */
public record PreprocessedText(
    String originalText,
    String normalizedText,
    int paragraphCount,
    List<Sentence> sentences
) {}
