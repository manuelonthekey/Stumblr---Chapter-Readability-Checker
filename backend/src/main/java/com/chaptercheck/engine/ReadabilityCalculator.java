package com.chaptercheck.engine;

import org.springframework.stereotype.Component;

/**
 * Implements all five readability formulas.
 *
 * SRP: only responsible for formula computation.
 * All inputs are pre-computed aggregates; this class does no text processing.
 * Guards against divide-by-zero. Results rounded to 1 decimal.
 */
@Component
public class ReadabilityCalculator {

    /**
     * Flesch Reading Ease (0–100; higher = easier).
     * Formula: 206.835 - 1.015*(words/sentences) - 84.6*(syllables/words)
     */
    public double fleschReadingEase(int words, int sentences, int syllables) {
        if (sentences == 0 || words == 0) return 0.0;
        double score = 206.835
            - 1.015  * ((double) words / sentences)
            - 84.6   * ((double) syllables / words);
        return round1(Math.max(0.0, Math.min(100.0, score)));
    }

    /**
     * Flesch-Kincaid Grade Level.
     * Formula: 0.39*(words/sentences) + 11.8*(syllables/words) - 15.59
     */
    public double fleschKincaidGrade(int words, int sentences, int syllables) {
        if (sentences == 0 || words == 0) return 0.0;
        double grade = 0.39 * ((double) words / sentences)
                     + 11.8 * ((double) syllables / words)
                     - 15.59;
        return round1(Math.max(0.0, grade));
    }

    /**
     * Gunning Fog Index.
     * Formula: 0.4 * ((words/sentences) + 100*(complexWords/words))
     */
    public double gunningFog(int words, int sentences, int complexWords) {
        if (sentences == 0 || words == 0) return 0.0;
        double fog = 0.4 * ((double) words / sentences
                    + 100.0 * ((double) complexWords / words));
        return round1(Math.max(0.0, fog));
    }

    /**
     * Coleman-Liau Index.
     * Formula: 0.0588*L - 0.296*S - 15.8
     * L = average number of letters per 100 words
     * S = average number of sentences per 100 words
     */
    public double colemanLiau(int words, int sentences, int letters) {
        if (words == 0) return 0.0;
        double L = (double) letters / words * 100.0;
        double S = (double) sentences / words * 100.0;
        double cli = 0.0588 * L - 0.296 * S - 15.8;
        return round1(Math.max(0.0, cli));
    }

    /**
     * Automated Readability Index (ARI).
     * Formula: 4.71*(characters/words) + 0.5*(words/sentences) - 21.43
     * characters = total letters only
     */
    public double ari(int words, int sentences, int characters) {
        if (sentences == 0 || words == 0) return 0.0;
        double score = 4.71 * ((double) characters / words)
                     + 0.5  * ((double) words / sentences)
                     - 21.43;
        return round1(Math.max(0.0, score));
    }

    /**
     * Returns the plain-English label for a Flesch Reading Ease score.
     */
    public String fleschLabel(double score) {
        if (score >= 90) return "Very easy";
        if (score >= 80) return "Easy";
        if (score >= 70) return "Fairly easy";
        if (score >= 60) return "Plain English";
        if (score >= 50) return "Fairly difficult";
        if (score >= 30) return "Difficult";
        return "Very difficult";
    }

    private static double round1(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
