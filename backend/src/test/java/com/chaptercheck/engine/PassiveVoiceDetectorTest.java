package com.chaptercheck.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PassiveVoiceDetector")
class PassiveVoiceDetectorTest {

    private PassiveVoiceDetector detector;

    @BeforeEach
    void setUp() {
        detector = new PassiveVoiceDetector();
    }

    @Test
    @DisplayName("is + past participle = passive")
    void isPassiveSimple() {
        assertThat(detector.isPassive("The book was written by the author.")).isTrue();
    }

    @Test
    @DisplayName("was + irregular participle = passive")
    void wasIrregularParticiple() {
        assertThat(detector.isPassive("The letter was sent by courier.")).isTrue();
    }

    @Test
    @DisplayName("were + past participle = passive")
    void werePassive() {
        assertThat(detector.isPassive("The windows were broken overnight.")).isTrue();
    }

    @Test
    @DisplayName("active voice sentence = not passive")
    void activeVoice() {
        assertThat(detector.isPassive("She wrote the letter quickly.")).isFalse();
    }

    @Test
    @DisplayName("is + adjective = not passive")
    void isAdjective() {
        assertThat(detector.isPassive("The sky is blue today.")).isFalse();
    }

    @Test
    @DisplayName("is + regular past participle within 3 words")
    void isRegularPassive() {
        assertThat(detector.isPassive("The door is locked now.")).isTrue();
    }

    @Test
    @DisplayName("null text returns false")
    void nullText() {
        assertThat(detector.isPassive(null)).isFalse();
    }

    @Test
    @DisplayName("empty text returns false")
    void emptyText() {
        assertThat(detector.isPassive("")).isFalse();
    }
}
