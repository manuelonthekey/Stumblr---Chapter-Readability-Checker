package com.chaptercheck.exception;

/**
 * Thrown when the submitted text is too short to produce a reliable score.
 */
public class TextTooShortException extends RuntimeException {
    private final int wordCount;

    public TextTooShortException(int wordCount) {
        super("Text too short: " + wordCount + " words");
        this.wordCount = wordCount;
    }

    public int getWordCount() {
        return wordCount;
    }
}
