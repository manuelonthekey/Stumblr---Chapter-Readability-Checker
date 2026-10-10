# AI_LOG.md â€” ChapterCheck Build Log

This file records every mistake, wrong assumption, failing test caused by code issues, or deployment error during the build of ChapterCheck.
Required deliverable per PRD section 0.

---

## Entry 001 â€” Maven not installed locally

**Date:** 2026-10-10
**What I did:** Started implementing the Spring Boot backend.
**What went wrong:** Assumed Maven would be globally installed. The machine has JDK 24 but no `mvn` command.
**How it was found:** `mvn -version` returned "not recognized" in PowerShell.
**How it was fixed:** Switched to Maven Wrapper (`.mvn/wrapper/`, `mvnw.cmd`). Downloaded the wrapper JAR directly from Maven Central. All subsequent build commands use `.\mvnw.cmd` instead of `mvn`.

---

<!-- New entries will be appended here as the build progresses -->

## Entry 002 — PassiveVoiceDetector test failure

**Date:** 2026-10-10
**What I did:** Created PassiveVoiceDetector and ran unit tests.
**What went wrong:** Tests failed with NoClassDefFoundError: Could not initialize class com.chaptercheck.engine.PassiveVoiceDetector.
**How it was found:** Ran Maven tests.
**How it was fixed:** Realized I used Set.of() with duplicate entries ('known', 'taken', 'hidden'). Set.of throws IllegalArgumentException on duplicates during static initialization. Removed the duplicates.

## Entry 003 — WordSyllableCounterTest.wordCount failure

**Date:** 2026-10-10
**What I did:** Wrote WordSyllableCounterTest.
**What went wrong:** The test expected 7 words for the string 'Hello world, this is a test.' which only has 6 words.
**How it was found:** Maven test failure (expected: 7 but was: 6).
**How it was fixed:** Updated the assertion to expect 6 words.

## Entry 004 — Frontend build failed due to verbatimModuleSyntax

**Date:** 2026-10-10
**What I did:** Ran npm run build on the frontend.
**What went wrong:** TypeScript failed with '...must be imported using a type-only import when verbatimModuleSyntax is enabled'.
**How it was found:** npm run build output.
**How it was fixed:** Set verbatimModuleSyntax to false in tsconfig.app.json.

## Entry 005 — Glassmorphism Refactor

**Date:** 2026-10-10
**What I did:** Completely refactored the frontend UI to match a glassmorphism (Bookavy) aesthetic with dreamy pastel gradients and floating panels.
**What went wrong:** Nothing broke during the restyle, but ensuring the readability of the main text area required creating a custom .glass-opaque utility that sits above 85% opacity, while the surrounding app uses .glass-surface (~40% opacity).
**How it was found:** Design prompt constraints for WCAG AA contrast.
**How it was fixed:** Used distinct token classes for readable areas versus structural areas.
