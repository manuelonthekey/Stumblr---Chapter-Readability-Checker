package com.chaptercheck.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("AnalysisController (MockMvc)")
class AnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String SAMPLE_TEXT =
        "The wind howled through the narrow canyon, carrying with it the scent of pine and distant rain. " +
        "Sarah pulled her jacket tighter, her eyes scanning the treeline for any sign of movement. " +
        "She had been tracking the wolf pack for three days now, following their prints through mud and snow. " +
        "The forest was silent except for the sound of her own breathing and the crunch of leaves beneath her boots. " +
        "Somewhere ahead, the wolves were resting, unaware that a researcher was slowly closing in. " +
        "She checked her notebook, confirmed the GPS coordinates, and moved forward with careful, measured steps. " +
        "The light was fading fast, and she knew she had perhaps an hour before true darkness fell. " +
        "Finding shelter would become the priority if she did not locate the pack within the next thirty minutes. " +
        "Her radio crackled once and then went silent, as it had done for the past two days. " +
        "She was on her own out here, and she had never felt more alive.";

    @Test
    @DisplayName("GET /api/v1/health returns ok")
    void healthCheck() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    @DisplayName("POST /api/v1/analyze with valid text returns 200")
    void analyzeValidText() throws Exception {
        String body = "{\"text\": \"" + SAMPLE_TEXT.replace("\"", "\\\"") + "\", " +
                      "\"options\": {\"excludeDialogue\": false, \"genre\": \"GENERAL_FICTION\"}}";

        mockMvc.perform(post("/api/v1/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.summary").exists())
            .andExpect(jsonPath("$.summary.fleschReadingEase").isNumber())
            .andExpect(jsonPath("$.summary.label").isString())
            .andExpect(jsonPath("$.stats").exists())
            .andExpect(jsonPath("$.genreCheck").exists())
            .andExpect(jsonPath("$.hardestSentences").isArray());
    }

    @Test
    @DisplayName("POST /api/v1/analyze with too-short text returns 400")
    void analyzeTooShortText() throws Exception {
        String body = "{\"text\": \"Too short.\", \"options\": {\"excludeDialogue\": false, \"genre\": \"GENERAL_FICTION\"}}";

        mockMvc.perform(post("/api/v1/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("TEXT_TOO_SHORT"));
    }

    @Test
    @DisplayName("POST /api/v1/analyze with missing text returns 400")
    void analyzeNullText() throws Exception {
        String body = "{\"options\": {\"excludeDialogue\": false}}";

        mockMvc.perform(post("/api/v1/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/analyze with too-long text returns 413")
    void analyzeTooLongText() throws Exception {
        String longText = "a ".repeat(51_000); // 102,000 chars
        String body = "{\"text\": \"" + longText + "\", \"options\": {\"excludeDialogue\": false}}";

        mockMvc.perform(post("/api/v1/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isPayloadTooLarge())
            .andExpect(jsonPath("$.error").value("TEXT_TOO_LONG"));
    }

    @Test
    @DisplayName("Flesch score is within valid range 0-100")
    void fleschScoreRange() throws Exception {
        String body = "{\"text\": \"" + SAMPLE_TEXT.replace("\"", "\\\"") + "\"}";

        mockMvc.perform(post("/api/v1/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.summary.fleschReadingEase").value(
                org.hamcrest.Matchers.allOf(
                    org.hamcrest.Matchers.greaterThanOrEqualTo(0.0),
                    org.hamcrest.Matchers.lessThanOrEqualTo(100.0)
                )
            ));
    }
}
