package com.chaptercheck.exception;

/**
 * Thrown when a client exceeds the per-IP rate limit.
 */
public class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException() {
        super("Rate limit exceeded");
    }
}
