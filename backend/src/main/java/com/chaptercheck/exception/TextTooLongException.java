package com.chaptercheck.exception;

/**
 * Thrown when the submitted text exceeds the maximum allowed length.
 */
public class TextTooLongException extends RuntimeException {
    private final int charCount;

    public TextTooLongException(int charCount) {
        super("Text too long: " + charCount + " characters");
        this.charCount = charCount;
    }

    public int getCharCount() {
        return charCount;
    }
}
