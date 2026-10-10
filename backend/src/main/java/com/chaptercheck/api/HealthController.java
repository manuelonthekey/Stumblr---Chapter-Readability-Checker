package com.chaptercheck.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Health check endpoint.
 * GET /api/v1/health → {"status":"ok"}
 * Used by Render for health-check probes and by the frontend warm-up call.
 */
@RestController
@RequestMapping("/api/v1")
public class HealthController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }
}
