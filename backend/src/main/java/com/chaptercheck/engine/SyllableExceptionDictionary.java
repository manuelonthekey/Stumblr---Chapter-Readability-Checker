package com.chaptercheck.engine;

import java.util.HashMap;
import java.util.Map;

/**
 * Exception dictionary for common words whose syllable count the heuristic gets wrong.
 * SRP: only holds the lookup table; counting logic lives in WordSyllableCounter.
 * OCP: add entries without touching counting logic.
 */
public final class SyllableExceptionDictionary {

    private SyllableExceptionDictionary() {}

    /** word (lowercase) → syllable count */
    private static final Map<String, Integer> EXCEPTIONS = new HashMap<>();

    static {
        // Common problem words
        EXCEPTIONS.put("the",         1);
        EXCEPTIONS.put("every",       3);
        EXCEPTIONS.put("everything",  4);
        EXCEPTIONS.put("everyone",    3);
        EXCEPTIONS.put("everywhere",  3);
        EXCEPTIONS.put("beautiful",   3);
        EXCEPTIONS.put("business",    2);
        EXCEPTIONS.put("area",        3);
        EXCEPTIONS.put("idea",        3);
        EXCEPTIONS.put("being",       2);
        EXCEPTIONS.put("saying",      2);
        EXCEPTIONS.put("going",       2);
        EXCEPTIONS.put("having",      2);
        EXCEPTIONS.put("making",      2);
        EXCEPTIONS.put("evening",     3);
        EXCEPTIONS.put("ion",         2);
        EXCEPTIONS.put("poem",        2);
        EXCEPTIONS.put("poet",        2);
        EXCEPTIONS.put("poetry",      3);
        EXCEPTIONS.put("people",      2);
        EXCEPTIONS.put("really",      3);
        EXCEPTIONS.put("world",       1);
        EXCEPTIONS.put("around",      2);
        EXCEPTIONS.put("orange",      2);
        EXCEPTIONS.put("purple",      2);
        EXCEPTIONS.put("whole",       1);
        EXCEPTIONS.put("while",       1);
        EXCEPTIONS.put("where",       1);
        EXCEPTIONS.put("there",       1);
        EXCEPTIONS.put("here",        1);
        EXCEPTIONS.put("were",        1);
        EXCEPTIONS.put("these",       1);
        EXCEPTIONS.put("those",       1);
        EXCEPTIONS.put("come",        1);
        EXCEPTIONS.put("some",        1);
        EXCEPTIONS.put("done",        1);
        EXCEPTIONS.put("none",        1);
        EXCEPTIONS.put("one",         1);
        EXCEPTIONS.put("gone",        1);
        EXCEPTIONS.put("once",        1);
        EXCEPTIONS.put("scene",       1);
        EXCEPTIONS.put("queue",       1);
        EXCEPTIONS.put("hour",        1);
        EXCEPTIONS.put("fire",        1);
        EXCEPTIONS.put("hire",        1);
        EXCEPTIONS.put("tire",        1);
        EXCEPTIONS.put("wire",        1);
        EXCEPTIONS.put("desire",      3);
        EXCEPTIONS.put("entire",      3);
        EXCEPTIONS.put("empire",      3);
        EXCEPTIONS.put("inspire",     3);
        EXCEPTIONS.put("require",     3);
        EXCEPTIONS.put("acquire",     3);
        EXCEPTIONS.put("inquire",     3);
        EXCEPTIONS.put("admire",      3);
        EXCEPTIONS.put("conspire",    3);
        EXCEPTIONS.put("perspire",    3);
        EXCEPTIONS.put("aspire",      3);
        EXCEPTIONS.put("expire",      3);
        EXCEPTIONS.put("buried",      2);
        EXCEPTIONS.put("carried",     2);
        EXCEPTIONS.put("married",     2);
        EXCEPTIONS.put("hurried",     2);
        EXCEPTIONS.put("worried",     2);
        EXCEPTIONS.put("studied",     2);
        EXCEPTIONS.put("series",      3);
        EXCEPTIONS.put("species",     3);
        EXCEPTIONS.put("genuine",     3);
        EXCEPTIONS.put("engine",      2);
        EXCEPTIONS.put("determine",   4);
        EXCEPTIONS.put("discipline",  3);
        EXCEPTIONS.put("medicine",    3);
        EXCEPTIONS.put("imagine",     3);
        EXCEPTIONS.put("genuine",     3);
        EXCEPTIONS.put("chocolate",   3);
        EXCEPTIONS.put("comfortable", 4);
        EXCEPTIONS.put("vegetable",   3);
        EXCEPTIONS.put("separate",    3);
        EXCEPTIONS.put("different",   3);
        EXCEPTIONS.put("interested",  4);
        EXCEPTIONS.put("temperature", 4);
        EXCEPTIONS.put("camera",      3);
        EXCEPTIONS.put("family",      3);
        EXCEPTIONS.put("history",     3);
        EXCEPTIONS.put("memory",      3);
        EXCEPTIONS.put("mystery",     3);
        EXCEPTIONS.put("victory",     3);
        EXCEPTIONS.put("factory",     3);
        EXCEPTIONS.put("territory",   4);
        EXCEPTIONS.put("category",    4);
        EXCEPTIONS.put("laboratory",  5);
        EXCEPTIONS.put("library",     3);
        EXCEPTIONS.put("secretary",   4);
        EXCEPTIONS.put("necessary",   4);
        EXCEPTIONS.put("elementary",  5);
        EXCEPTIONS.put("ordinary",    4);
        EXCEPTIONS.put("secondary",   4);
        EXCEPTIONS.put("dictionary",  4);
        EXCEPTIONS.put("visionary",   4);
        EXCEPTIONS.put("sanctuary",   4);
        EXCEPTIONS.put("boundary",    3);
        EXCEPTIONS.put("jewelry",     3);
        EXCEPTIONS.put("evening",     3);
        EXCEPTIONS.put("praying",     2);
    }

    /**
     * Returns the known syllable count for the word, or -1 if not found.
     */
    public static int lookup(String word) {
        return EXCEPTIONS.getOrDefault(word.toLowerCase(), -1);
    }
}
