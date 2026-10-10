package com.chaptercheck.engine;

/**
 * Represents a single preprocessed sentence with its original text offsets.
 * start and end are UTF-16 character indices matching JavaScript's String.substring().
 */
public record Sentence(
    String text,
    int start,
    int end
) {
    public Sentence {
        if (text == null) throw new IllegalArgumentException("Sentence text must not be null");
        if (start < 0)   throw new IllegalArgumentException("start must be >= 0");
        if (end < start) throw new IllegalArgumentException("end must be >= start");
    }
}
