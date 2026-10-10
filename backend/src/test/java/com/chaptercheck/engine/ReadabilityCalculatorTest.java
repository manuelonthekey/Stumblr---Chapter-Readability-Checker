package com.chaptercheck.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ReadabilityCalculator")
class ReadabilityCalculatorTest {

    private ReadabilityCalculator calc;

    @BeforeEach
    void setUp() {
        calc = new ReadabilityCalculator();
    }

    // ── Flesch Reading Ease ───────────────────────────────────────────────────

    @Test
    @DisplayName("Flesch score for known passage is within ±3 of reference")
    void fleschKnownPassage() {
        // Reference: Ernest Hemingway-style simple prose approximation
        // 100 words, 10 sentences, ~120 syllables → FRE ≈ 70±
        int words = 100, sentences = 10, syllables = 120;
        double score = calc.fleschReadingEase(words, sentences, syllables);
        // Expected: 206.835 - 1.015*(10) - 84.6*(1.2) = 206.835 - 10.15 - 101.52 = 95.165
        assertThat(score).isCloseTo(95.2, within(3.0));
    }

    @Test
    @DisplayName("Flesch score for complex text is in difficult range")
    void fleschComplexText() {
        // Long sentences, high syllables — academic style
        int words = 100, sentences = 4, syllables = 180;
        double score = calc.fleschReadingEase(words, sentences, syllables);
        // 206.835 - 1.015*25 - 84.6*1.8 = 206.835 - 25.375 - 152.28 = 29.18
        assertThat(score).isLessThan(50.0);
    }

    @Test
    @DisplayName("Flesch clamps to 0 on pathological input")
    void fleschClampLow() {
        int words = 1, sentences = 1, syllables = 10;
        double score = calc.fleschReadingEase(words, sentences, syllables);
        assertThat(score).isGreaterThanOrEqualTo(0.0);
    }

    @Test
    @DisplayName("Flesch zero-sentence guard returns 0")
    void fleschZeroSentences() {
        assertThat(calc.fleschReadingEase(100, 0, 150)).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Flesch zero-word guard returns 0")
    void fleschZeroWords() {
        assertThat(calc.fleschReadingEase(0, 10, 0)).isEqualTo(0.0);
    }

    // ── Flesch-Kincaid Grade ──────────────────────────────────────────────────

    @Test
    @DisplayName("FK grade for simple text is low")
    void fkGradeSimple() {
        double grade = calc.fleschKincaidGrade(100, 20, 110);
        // 0.39*5 + 11.8*1.1 - 15.59 = 1.95 + 12.98 - 15.59 = -0.66 → clamped to 0
        assertThat(grade).isGreaterThanOrEqualTo(0.0);
    }

    @Test
    @DisplayName("FK grade for complex text is higher than 10")
    void fkGradeComplex() {
        double grade = calc.fleschKincaidGrade(100, 3, 250);
        assertThat(grade).isGreaterThan(10.0);
    }

    // ── Gunning Fog ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("Gunning Fog typical value")
    void gunningFogTypical() {
        // 100 words, 5 sentences, 10 complex = 0.4*(20 + 10) = 12.0
        double fog = calc.gunningFog(100, 5, 10);
        assertThat(fog).isCloseTo(12.0, within(0.5));
    }

    @Test
    @DisplayName("Gunning Fog zero guard")
    void gunningFogZero() {
        assertThat(calc.gunningFog(0, 0, 0)).isEqualTo(0.0);
    }

    // ── Coleman-Liau ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Coleman-Liau typical computation")
    void colemanLiauTypical() {
        // 100 words, 5 sentences, 450 letters
        // L=450, S=5 → 0.0588*450 - 0.296*5 - 15.8 = 26.46 - 1.48 - 15.8 = 9.18
        double cli = calc.colemanLiau(100, 5, 450);
        assertThat(cli).isCloseTo(9.2, within(0.5));
    }

    // ── ARI ───────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("ARI typical computation")
    void ariTypical() {
        // 100 words, 5 sentences, 400 chars
        // 4.71*4 + 0.5*20 - 21.43 = 18.84 + 10 - 21.43 = 7.41
        double ari = calc.ari(100, 5, 400);
        assertThat(ari).isCloseTo(7.4, within(0.5));
    }

    // ── Labels ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Flesch labels map correctly")
    void fleschLabels() {
        assertThat(calc.fleschLabel(95)).isEqualTo("Very easy");
        assertThat(calc.fleschLabel(85)).isEqualTo("Easy");
        assertThat(calc.fleschLabel(75)).isEqualTo("Fairly easy");
        assertThat(calc.fleschLabel(65)).isEqualTo("Plain English");
        assertThat(calc.fleschLabel(55)).isEqualTo("Fairly difficult");
        assertThat(calc.fleschLabel(40)).isEqualTo("Difficult");
        assertThat(calc.fleschLabel(15)).isEqualTo("Very difficult");
    }
}
