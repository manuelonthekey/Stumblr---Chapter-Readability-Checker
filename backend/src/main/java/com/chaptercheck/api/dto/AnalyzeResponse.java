package com.chaptercheck.api.dto;

import java.util.List;

/**
 * Full analysis response.
 */
public record AnalyzeResponse(
    SummaryDto summary,
    StatsDto stats,
    GenreCheckDto genreCheck,
    List<HardestSentenceDto> hardestSentences
) {}
