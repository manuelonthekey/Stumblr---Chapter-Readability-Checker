package com.chaptercheck.api.dto;

/**
 * Supporting statistics grid.
 */
public record StatsDto(
    int words,
    int sentences,
    int paragraphs,
    double avgSentenceLength,
    double avgSyllablesPerWord,
    double longSentencePct,
    double complexWordPct,
    double passivePct
) {}
