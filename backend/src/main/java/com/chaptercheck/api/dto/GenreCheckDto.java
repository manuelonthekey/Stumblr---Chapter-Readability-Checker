package com.chaptercheck.api.dto;

/**
 * Genre range check result.
 */
public record GenreCheckDto(
    String genre,
    int typicalMin,
    int typicalMax,
    String verdict,   // WITHIN_RANGE | BELOW_RANGE | ABOVE_RANGE
    String message
) {}
