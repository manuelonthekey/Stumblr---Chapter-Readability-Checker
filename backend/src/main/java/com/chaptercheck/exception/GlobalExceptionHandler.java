package com.chaptercheck.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Centralized exception handler returning a consistent error shape:
 * { "error": "CODE", "message": "Human readable text" }
 *
 * SRP: only responsible for translating exceptions to HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TextTooShortException.class)
    public ResponseEntity<Map<String, String>> handleTooShort(TextTooShortException ex) {
        return ResponseEntity.badRequest().body(Map.of(
            "error",   "TEXT_TOO_SHORT",
            "message", "Please paste at least 50 words (300 characters) for a reliable score. " +
                       "Your text has " + ex.getWordCount() + " words."
        ));
    }

    @ExceptionHandler(TextTooLongException.class)
    public ResponseEntity<Map<String, String>> handleTooLong(TextTooLongException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(Map.of(
            "error",   "TEXT_TOO_LONG",
            "message", "Text must be under 100,000 characters. " +
                       "Your text has " + ex.getCharCount() + " characters."
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .findFirst()
            .orElse("Invalid request");
        return ResponseEntity.badRequest().body(Map.of(
            "error",   "INVALID_REQUEST",
            "message", msg
        ));
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<Map<String, String>> handleRateLimit(RateLimitExceededException ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Map.of(
            "error",   "RATE_LIMITED",
            "message", "Too many requests. Please wait a moment and try again."
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        return ResponseEntity.internalServerError().body(Map.of(
            "error",   "SERVER_ERROR",
            "message", "An unexpected error occurred. Please try again."
        ));
    }
}
