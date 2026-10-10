package com.chaptercheck.config;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Genre readability ranges (Flesch Reading Ease).
 * OCP: add genres here without modifying the analysis service.
 */
@Component
public class GenreRangesConfig {

    public record GenreRange(int min, int max, String label) {}

    private static final Map<String, GenreRange> RANGES = Map.of(
        "GENERAL_FICTION",  new GenreRange(70, 90, "General fiction"),
        "THRILLER",         new GenreRange(70, 90, "Thriller"),
        "ROMANCE",          new GenreRange(75, 90, "Romance"),
        "YA",               new GenreRange(70, 85, "YA"),
        "LITERARY_FICTION", new GenreRange(50, 70, "Literary fiction"),
        "NON_FICTION",      new GenreRange(55, 75, "Non-fiction")
    );

    public GenreRange getRange(String genre) {
        return RANGES.getOrDefault(genre, RANGES.get("GENERAL_FICTION"));
    }

    public boolean isValid(String genre) {
        return RANGES.containsKey(genre);
    }
}
