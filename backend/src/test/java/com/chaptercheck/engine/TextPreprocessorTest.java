package com.chaptercheck.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TextPreprocessor")
class TextPreprocessorTest {

    private TextPreprocessor preprocessor;

    @BeforeEach
    void setUp() {
        preprocessor = new TextPreprocessor();
    }

    @Test
    @DisplayName("curly double quotes are replaced with straight quotes")
    void curlyDoubleQuotes() {
        String input = "\u201CHello world\u201D";
        String normalized = preprocessor.applyNormalization(input);
        assertThat(normalized).isEqualTo("\"Hello world\"");
    }

    @Test
    @DisplayName("curly single quotes are replaced with straight apostrophes")
    void curlySingleQuotes() {
        String input = "It\u2019s a beautiful day";
        String normalized = preprocessor.applyNormalization(input);
        assertThat(normalized).isEqualTo("It's a beautiful day");
    }

    @Test
    @DisplayName("CRLF is normalized to LF")
    void crlfNormalization() {
        String input = "Line one\r\nLine two";
        String normalized = preprocessor.applyNormalization(input);
        assertThat(normalized).doesNotContain("\r\n");
        assertThat(normalized).contains("\n");
    }

    @Test
    @DisplayName("multiple blank lines collapse to double LF")
    void multipleBlankLines() {
        String input = "Para one\n\n\n\nPara two";
        String normalized = preprocessor.applyNormalization(input);
        assertThat(normalized).doesNotContain("\n\n\n");
    }

    @Test
    @DisplayName("paragraph count for two paragraphs")
    void paragraphCount() {
        String input = "First paragraph text.\n\nSecond paragraph text.";
        int count = preprocessor.countParagraphs(input);
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("single paragraph has count 1")
    void singleParagraph() {
        String input = "Just one paragraph here.";
        assertThat(preprocessor.countParagraphs(input)).isEqualTo(1);
    }

    @Test
    @DisplayName("preprocess returns a non-null result")
    void preprocessNotNull() {
        PreprocessedText result = preprocessor.preprocess("Some text here.");
        assertThat(result).isNotNull();
        assertThat(result.normalizedText()).isNotNull();
        assertThat(result.originalText()).isEqualTo("Some text here.");
    }

    @Test
    @DisplayName("multiple spaces collapse to one")
    void multipleSpacesCollapse() {
        String input = "Hello    world";
        String normalized = preprocessor.applyNormalization(input);
        assertThat(normalized).isEqualTo("Hello world");
    }
}
