package com.chaptercheck.api;

import com.chaptercheck.api.dto.AnalyzeRequest;
import com.chaptercheck.api.dto.AnalyzeResponse;
import com.chaptercheck.service.AnalysisService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for the analysis endpoint.
 * SRP: only responsible for HTTP mapping; delegates all logic to AnalysisService.
 */
@RestController
@RequestMapping("/api/v1")
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    /**
     * POST /api/v1/analyze
     * Analyzes pasted text and returns readability scores and hardest sentences.
     */
    @PostMapping("/analyze")
    public ResponseEntity<AnalyzeResponse> analyze(@Valid @RequestBody AnalyzeRequest request) {
        AnalyzeResponse response = analysisService.analyze(
            request.text(),
            request.options().excludeDialogue(),
            request.options().genre()
        );
        return ResponseEntity.ok(response);
    }
}
