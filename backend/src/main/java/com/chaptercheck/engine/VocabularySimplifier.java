package com.chaptercheck.engine;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps heavy/complex vocabulary to simpler plain-English alternatives.
 * SRP: sole responsibility is the lookup table.
 * OCP: add words without touching hint generation logic.
 */
public final class VocabularySimplifier {

    private VocabularySimplifier() {}

    /** heavy word (lowercase) → plain alternative */
    private static final Map<String, String> SIMPLIFICATIONS = new HashMap<>();

    static {
        SIMPLIFICATIONS.put("utilize",         "use");
        SIMPLIFICATIONS.put("utilise",         "use");
        SIMPLIFICATIONS.put("approximately",   "about");
        SIMPLIFICATIONS.put("commence",        "start");
        SIMPLIFICATIONS.put("however",         "but");
        SIMPLIFICATIONS.put("subsequently",    "then");
        SIMPLIFICATIONS.put("additionally",    "also");
        SIMPLIFICATIONS.put("furthermore",     "also");
        SIMPLIFICATIONS.put("nevertheless",    "still");
        SIMPLIFICATIONS.put("notwithstanding", "despite");
        SIMPLIFICATIONS.put("endeavour",       "try");
        SIMPLIFICATIONS.put("endeavor",        "try");
        SIMPLIFICATIONS.put("facilitate",      "help");
        SIMPLIFICATIONS.put("terminate",       "end");
        SIMPLIFICATIONS.put("initiate",        "start");
        SIMPLIFICATIONS.put("implement",       "use");
        SIMPLIFICATIONS.put("demonstrate",     "show");
        SIMPLIFICATIONS.put("communicate",     "tell");
        SIMPLIFICATIONS.put("comprehend",      "understand");
        SIMPLIFICATIONS.put("regarding",       "about");
        SIMPLIFICATIONS.put("concerning",      "about");
        SIMPLIFICATIONS.put("insufficient",    "not enough");
        SIMPLIFICATIONS.put("modification",    "change");
        SIMPLIFICATIONS.put("requirement",     "need");
        SIMPLIFICATIONS.put("specifications",  "details");
        SIMPLIFICATIONS.put("configuration",   "setup");
        SIMPLIFICATIONS.put("extraordinary",   "unusual");
        SIMPLIFICATIONS.put("immediately",     "now");
        SIMPLIFICATIONS.put("unfortunately",   "sadly");
        SIMPLIFICATIONS.put("particular",      "specific");
        SIMPLIFICATIONS.put("characteristic",  "feature");
        SIMPLIFICATIONS.put("considerable",    "large");
        SIMPLIFICATIONS.put("considerably",    "much");
        SIMPLIFICATIONS.put("established",     "known");
        SIMPLIFICATIONS.put("acknowledge",     "admit");
        SIMPLIFICATIONS.put("anticipate",      "expect");
        SIMPLIFICATIONS.put("approximately",   "about");
        SIMPLIFICATIONS.put("assistance",      "help");
        SIMPLIFICATIONS.put("beneficial",      "helpful");
        SIMPLIFICATIONS.put("catastrophic",    "terrible");
        SIMPLIFICATIONS.put("demonstrate",     "show");
        SIMPLIFICATIONS.put("fundamental",     "basic");
        SIMPLIFICATIONS.put("generation",      "era");
        SIMPLIFICATIONS.put("illuminate",      "light up");
        SIMPLIFICATIONS.put("investigate",     "look into");
        SIMPLIFICATIONS.put("modification",    "change");
        SIMPLIFICATIONS.put("opportunity",     "chance");
        SIMPLIFICATIONS.put("possession",      "ownership");
        SIMPLIFICATIONS.put("prioritize",      "rank");
        SIMPLIFICATIONS.put("subsequently",    "later");
        SIMPLIFICATIONS.put("sufficient",      "enough");
        SIMPLIFICATIONS.put("transformation",  "change");
        SIMPLIFICATIONS.put("unavoidable",     "certain");
    }

    /**
     * Returns the simpler alternative for the word, or null if none known.
     */
    public static String simplify(String word) {
        if (word == null) return null;
        return SIMPLIFICATIONS.get(word.toLowerCase());
    }

    public static Map<String, String> all() {
        return java.util.Collections.unmodifiableMap(SIMPLIFICATIONS);
    }
}
