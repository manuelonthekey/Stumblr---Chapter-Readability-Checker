package com.chaptercheck.config;

import org.springframework.stereotype.Component;

/**
 * Externalized constants for the sentence difficulty scoring algorithm.
 * OCP: changing weights requires only editing this file, not the scorer.
 * All weights must sum to 1.0.
 */
@Component
public class ScoringWeightsConfig {

    // ── Weights (must sum to 1.0) ───────────────────────────────────────────
    public static final double LENGTH_WEIGHT         = 0.35;
    public static final double WORD_WEIGHT           = 0.25;
    public static final double COMPLEX_WORD_WEIGHT   = 0.20;
    public static final double CLAUSE_DENSITY_WEIGHT = 0.10;
    public static final double PASSIVE_VOICE_WEIGHT  = 0.10;

    // ── Normalisation denominators ──────────────────────────────────────────
    /** Length normalised: wordCount / LENGTH_NORM → 0–1 (capped at 1) */
    public static final int    LENGTH_NORM            = 45;

    /** Word weight: syllables/word scaled from MIN_SYLLABLES (→0) to MAX_SYLLABLES (→1) */
    public static final double WORD_SYLLABLES_MIN     = 1.2;
    public static final double WORD_SYLLABLES_MAX     = 2.2;

    /**
     * Complex word share: complexWords/words — score of COMPLEX_NORM maps to 1.0.
     * E.g., 25 % complex words → score 1.
     */
    public static final double COMPLEX_WORD_NORM      = 0.25;

    /**
     * Clause density: (commas + semicolons + colons + dashes) — CLAUSE_NORM maps to 1.0.
     */
    public static final double CLAUSE_DENSITY_NORM    = 7.0;

    // ── Reason-tag thresholds ───────────────────────────────────────────────
    public static final int    LONG_SENTENCE_WORDS    = 30;
    public static final int    CLAUSE_DENSITY_TAG     = 5;
    public static final int    MIN_ELIGIBLE_WORDS     = 8;
    public static final int    TOP_SENTENCES_COUNT    = 5;

    // ── Global input limits ─────────────────────────────────────────────────
    public static final int    MAX_CHARS              = 100_000;
    public static final int    MIN_CHARS              = 300;
    public static final int    MIN_WORDS              = 50;

    // ── Reading speed (words per minute) ───────────────────────────────────
    public static final double READING_WPM            = 230.0;
}
