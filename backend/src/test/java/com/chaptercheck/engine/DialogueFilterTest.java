package com.chaptercheck.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("DialogueFilter")
class DialogueFilterTest {

    private DialogueFilter filter;

    @BeforeEach
    void setUp() {
        filter = new DialogueFilter();
    }

    @Test
    @DisplayName("replaces straight double-quoted text with spaces")
    void straightQuotes() {
        String input = "He said \"hello world\" and left.";
        String filtered = filter.filter(input);
        assertThat(filtered).doesNotContain("hello world");
        assertThat(filtered).hasSize(input.length()); // same length
    }

    @Test
    @DisplayName("replaces curly double-quoted text with spaces")
    void curlyQuotes() {
        String input = "She said \u201Cgoodbye\u201D and walked away.";
        String filtered = filter.filter(input);
        assertThat(filtered).doesNotContain("goodbye");
        assertThat(filtered).hasSize(input.length());
    }

    @Test
    @DisplayName("text without quotes is unchanged")
    void noQuotes() {
        String input = "Plain text with no dialogue.";
        assertThat(filter.filter(input)).isEqualTo(input);
    }

    @Test
    @DisplayName("multiple dialogue segments all replaced")
    void multipleSegments() {
        String input = "\"Hello,\" she said. \"How are you?\" he replied.";
        String filtered = filter.filter(input);
        assertThat(filtered).doesNotContain("Hello");
        assertThat(filtered).doesNotContain("How are you");
        assertThat(filtered).hasSize(input.length());
    }

    @Test
    @DisplayName("null input returns empty string")
    void nullInput() {
        assertThat(filter.filter(null)).isEqualTo("");
    }
}
