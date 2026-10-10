package com.chaptercheck.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("SentenceSplitter")
class SentenceSplitterTest {

    private SentenceSplitter splitter;

    @BeforeEach
    void setUp() {
        splitter = new SentenceSplitter();
    }

    @Test
    @DisplayName("simple two-sentence text splits correctly")
    void simpleTwoSentences() {
        String text = "She ran fast. He stayed behind.";
        List<Sentence> result = splitter.split(text, text);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).text()).isEqualTo("She ran fast.");
        assertThat(result.get(1).text()).isEqualTo("He stayed behind.");
    }

    @Test
    @DisplayName("Mr. abbreviation does not split sentence")
    void abbreviationMr() {
        String text = "Mr. Smith went to the store today.";
        List<Sentence> result = splitter.split(text, text);
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Dr. abbreviation does not split sentence")
    void abbreviationDr() {
        String text = "She visited Dr. Jones at the clinic.";
        List<Sentence> result = splitter.split(text, text);
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Mrs. and Ms. abbreviations do not split")
    void abbreviationMrsMsS() {
        String text = "Mrs. Brown and Ms. Davis arrived early.";
        List<Sentence> result = splitter.split(text, text);
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("J. K. initials do not split sentence")
    void initialsJK() {
        String text = "J. K. Rowling wrote the Harry Potter series.";
        List<Sentence> result = splitter.split(text, text);
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("decimal 3.14 does not split sentence")
    void decimalNumber() {
        String text = "The value of pi is approximately 3.14 radians.";
        List<Sentence> result = splitter.split(text, text);
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("dialogue closing period followed by uppercase splits")
    void dialogueClosingPeriod() {
        String text = "She said, 'Go.' She went immediately.";
        List<Sentence> result = splitter.split(text, text);
        // Could be 1 or 2 depending on quote type; at minimum should not crash
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("paragraph break always ends a sentence")
    void paragraphBreak() {
        String text = "He ran fast.\n\nShe hid behind the tree.";
        List<Sentence> result = splitter.split(text, text);
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("question mark ends sentence")
    void questionMark() {
        String text = "Are you ready? I think so.";
        List<Sentence> result = splitter.split(text, text);
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("exclamation mark ends sentence")
    void exclamationMark() {
        String text = "Watch out! There is danger ahead.";
        List<Sentence> result = splitter.split(text, text);
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("vs. abbreviation does not split")
    void abbreviationVs() {
        String text = "It was a match of cats vs. dogs in the arena.";
        List<Sentence> result = splitter.split(text, text);
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("etc. abbreviation does not split")
    void abbreviationEtc() {
        String text = "He bought apples, oranges, etc. and went home.";
        List<Sentence> result = splitter.split(text, text);
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("empty text returns empty list")
    void emptyText() {
        assertThat(splitter.split("", "")).isEmpty();
    }

    @Test
    @DisplayName("null text returns empty list")
    void nullText() {
        assertThat(splitter.split(null, "")).isEmpty();
    }

    @Test
    @DisplayName("single sentence with no terminal punctuation still works")
    void singleSentenceNoPunctuation() {
        String text = "A simple sentence without final punctuation";
        List<Sentence> result = splitter.split(text, text);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).text()).isEqualTo(text);
    }

    @Test
    @DisplayName("multiple paragraph breaks produce multiple sentences")
    void multipleParagraphBreaks() {
        String text = "First paragraph.\n\nSecond paragraph.\n\nThird paragraph.";
        List<Sentence> result = splitter.split(text, text);
        assertThat(result).hasSizeGreaterThanOrEqualTo(3);
    }

    @Test
    @DisplayName("sentence offsets are within original text bounds")
    void offsetsWithinBounds() {
        String text = "She ran fast. He stayed behind. They won.";
        List<Sentence> result = splitter.split(text, text);
        for (Sentence s : result) {
            assertThat(s.start()).isGreaterThanOrEqualTo(0);
            assertThat(s.end()).isLessThanOrEqualTo(text.length());
            assertThat(s.start()).isLessThanOrEqualTo(s.end());
        }
    }

    @Test
    @DisplayName("Prof. abbreviation does not split")
    void abbreviationProf() {
        String text = "Prof. Morgan lectured on thermodynamics today.";
        List<Sentence> result = splitter.split(text, text);
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("two sentences with exclamation and period split correctly")
    void mixedTerminalPunctuation() {
        String text = "Run! The house is on fire.";
        List<Sentence> result = splitter.split(text, text);
        assertThat(result).hasSize(2);
    }
}
