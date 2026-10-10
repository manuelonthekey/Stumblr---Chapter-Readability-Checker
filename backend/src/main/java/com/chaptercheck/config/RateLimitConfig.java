package com.chaptercheck.config;

import com.chaptercheck.exception.RateLimitExceededException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Per-IP rate limiting filter using Bucket4j in-memory token buckets.
 * Limit: 30 requests per minute per IP.
 *
 * SRP: only responsible for rate limiting.
 */
@Component
public class RateLimitConfig extends OncePerRequestFilter {

    private static final int REQUESTS_PER_MINUTE = 30;

    /** IP → Bucket mapping; entries are created on first request. */
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        // Only rate-limit the analyze endpoint
        if (!request.getRequestURI().contains("/analyze")) {
            filterChain.doFilter(request, response);
            return;
        }

        String ip = resolveClientIp(request);
        Bucket bucket = buckets.computeIfAbsent(ip, this::newBucket);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write(
                "{\"error\":\"RATE_LIMITED\",\"message\":\"Too many requests. Please wait a moment and try again.\"}"
            );
        }
    }

    private Bucket newBucket(String ip) {
        Bandwidth limit = Bandwidth.classic(
            REQUESTS_PER_MINUTE,
            Refill.greedy(REQUESTS_PER_MINUTE, Duration.ofMinutes(1))
        );
        return Bucket.builder().addLimit(limit).build();
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
