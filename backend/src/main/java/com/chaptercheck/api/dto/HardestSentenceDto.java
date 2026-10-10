package com.chaptercheck.api.dto;

import java.util.List;

/**
 * One of the top hardest sentences.
 */
public record HardestSentenceDto(
    int rank,
    String text,
    int start,
    int end,
    int wordCount,
    double difficulty,
    List<String> reasons,
    List<String> hints
) {}
