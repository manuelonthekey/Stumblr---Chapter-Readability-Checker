package com.chaptercheck.api.dto;

/**
 * Overall readability scores summary.
 */
public record SummaryDto(
    double fleschReadingEase,
    String label,
    double fleschKincaidGrade,
    double gunningFog,
    double colemanLiau,
    double ari,
    double readingTimeMinutes
) {}
