package com.chaptercheck.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request body for POST /api/v1/analyze.
 */
public record AnalyzeRequest(
    @NotNull(message = "text must not be null")
    @Size(min = 1, message = "text must not be empty")
    String text,

    @Valid
    AnalyzeOptions options
) {
    /** Defaults when options is omitted. */
    public AnalyzeRequest {
        if (options == null) {
            options = new AnalyzeOptions(false, "GENERAL_FICTION");
        }
    }

    public record AnalyzeOptions(
        boolean excludeDialogue,
        String genre
    ) {
        public AnalyzeOptions {
            if (genre == null || genre.isBlank()) {
                genre = "GENERAL_FICTION";
            }
        }
    }
}
