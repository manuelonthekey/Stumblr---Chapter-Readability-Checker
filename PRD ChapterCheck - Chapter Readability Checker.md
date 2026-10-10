# PRD: ChapterCheck - Free Chapter Readability Checker

*Paste this whole document into Antigravity as the build brief.*

## 0. Instructions for the AI Agent (read first)

You are building and deploying a complete, free, public web tool from scratch. Work in phases and show a plan before coding.

1. Phase 1: scaffold the repo, backend skeleton, frontend skeleton, health check.
2. Phase 2: build the readability engine in Spring Boot with unit tests.
3. Phase 3: build the frontend (input, results, highlighting).
4. Phase 4: polish, accessibility, error states, privacy page.
5. Phase 5: deploy backend and frontend, verify the live link end to end.

Rules:

- Keep the product small. Do not add accounts, databases, payments, or AI/LLM calls. The analysis must be fully rule-based and deterministic.
- Never store, log, or persist the text a user pastes. Do not write chapter text to logs, even on errors.
- Maintain a file `AI_LOG.md` at the repo root. Every time you make a mistake, a wrong assumption, a failing test caused by your own code, or a deployment error, add an entry: what you did, what went wrong, how it was found, how it was fixed. Be honest and specific. This log is a required deliverable.
- After deployment, test the live URL with a real sample chapter and report results.

## 1. Product Overview

**Name:** ChapterCheck

**One-liner:** Paste a chapter, get a readability score and the five hardest sentences highlighted, so an author knows exactly where readers will stumble.

**Problem:** Authors (indie, self-published, Wattpad, Royal Road, fanfiction, students) have no quick, free, private way to see how readable a chapter is. Existing tools are either cluttered, paywalled, built for SEO writers, or only give one number with no guidance on what to fix.

**Solution:** A single-page tool. Paste text, click Analyze, and see (a) an overall readability score with plain-English meaning, (b) supporting metrics, and (c) the five hardest sentences highlighted in the text and listed with the reason each is hard and a rule-based fix hint.

## 2. Goals and Non-Goals

**Goals**

- Free, no signup, usable in under 10 seconds from landing.
- Accurate, explainable scoring using standard readability formulas.
- Clear visual highlighting of the five hardest sentences.
- Works well on long chapters (up to roughly 15,000 words) and on fiction with dialogue.
- Private by design: text is processed in memory and discarded.
- Deployed on free tiers with a public link.

**Non-Goals (do not build)**

- User accounts, saved history, or a database.
- AI rewriting or LLM suggestions.
- File upload parsing (docx/pdf) in v1. Paste only.
- Grammar or spell checking.
- Multi-language support (English only in v1).

## 3. Target Users and Use Cases

- **Indie novelist** pastes a draft chapter to find passages that read heavy.
- **Wattpad / Royal Road serial writer** checks each chapter before posting.
- **Student or non-native English writer** checks if their text is too complex.
- **Editor or beta reader** quickly flags the densest sentences.

## 4. Functional Requirements

### 4.1 Input

- Large textarea with placeholder text, live character and word count.
- Max input: 100,000 characters. Show a clear error above the limit. Minimum: 300 characters or 50 words, otherwise show a friendly message that more text is needed for a reliable score.
- Buttons: **Analyze**, **Clear**, **Try a sample chapter** (loads a built-in \~500 word sample).
- Option toggle: **Exclude dialogue from scoring** (default off). When on, text inside straight or curly double quotes is ignored for scoring and ranking.
- Option dropdown: **Genre target** (General fiction default, Thriller / Romance / YA, Literary fiction, Non-fiction). Used only to show whether the score sits inside the typical range for that genre.
- Keyboard shortcut: Ctrl/Cmd + Enter triggers Analyze.

### 4.2 Analysis output

**A. Overall score card**

- Flesch Reading Ease (primary score, 0-100, clamp display to 0-100).
- Plain-English label: 90-100 Very easy, 80-89 Easy, 70-79 Fairly easy, 60-69 Plain English, 50-59 Fairly difficult, 30-49 Difficult, 0-29 Very difficult.
- Flesch-Kincaid Grade Level shown as 'US grade X'.
- Genre check: a one-line message like 'Typical for thrillers is 70-90. Your chapter is slightly harder than that.'

**B. Supporting metrics (small stat grid)**

- Words, sentences, paragraphs.
- Average sentence length (words).
- Average syllables per word.
- Percentage of long sentences (over 25 words).
- Percentage of complex words (3 or more syllables, excluding common suffix inflections).
- Gunning Fog index, Coleman-Liau index, and ARI shown as secondary numbers with a tooltip explaining each.
- Estimated reading time at 230 words per minute.
- Passive voice rate (heuristic, percentage of sentences).

**C. Five hardest sentences**

- Ranked 1 to 5, hardest first.
- Each card shows: rank badge, the sentence text, word count, difficulty score (0-100), and 1-3 reason tags such as 'Very long (47 words)', 'Many commas (6)', 'Passive voice', 'Heavy vocabulary'.
- Each card includes a rule-based **fix hint**, for example 'Split at the semicolon into two sentences' or 'Try replacing: utilize, approximately, notwithstanding'. Generate hints from rules only.
- Clicking a card scrolls to and briefly pulses that sentence in the highlighted text view.
- Only sentences of 8 or more words are eligible for ranking. If fewer than five qualify, show as many as exist.

**D. Highlighted text view**

- Shows the full pasted text with the five hardest sentences highlighted.
- Highlight must not rely on color alone: use background tint plus an underline style and a small numbered badge (1-5) placed before each sentence.
- Intensity can vary by rank (rank 1 strongest).
- Preserve original paragraph breaks and whitespace.
- Hover or focus on a highlighted sentence shows a tooltip with its reasons.

**E. Actions**

- **Copy results** (plain-text summary to clipboard).
- **Download report** (.txt or .md with score, metrics, and the five sentences).
- **Share result card** (optional, should-have): generates a clean image-style summary card using only score and metrics, never the user's text.
- **Analyze again** keeps text and lets users edit and re-run. After a re-run, show the score change (for example '+6 points easier').

### 4.3 Static pages

- **How it works**: explains formulas and the hardest-sentence score in plain English.
- **Privacy**: states that text is processed in memory, never stored, never logged, never sent to third parties.
- **Feedback** link (a mailto or a Google Form link placeholder; make it a single config value).

## 5. Analysis Engine Specification (Spring Boot)

Implement as pure, well-tested Java classes with no framework coupling inside the core logic.

### 5.1 Text preprocessing

- Normalize line endings and curly quotes. Collapse repeated spaces but keep paragraph breaks (blank lines) for paragraph counting.
- Keep a mapping from normalized text to original character offsets so highlights line up with the user's original text. Return character start and end offsets (UTF-16 indices as used by JavaScript strings) for each ranked sentence.

### 5.2 Sentence splitting

Use a custom rule-based splitter, not a naive split on periods. It must handle:

- Abbreviations: Mr., Mrs., Ms., Dr., Prof., St., Jr., Sr., vs., etc., e.g., i.e., a.m., p.m.
- Initials like J. K. Rowling and decimals like 3.14.
- Ellipses (...) and em-dashes.
- Terminal punctuation followed by closing quotes or brackets, for example: He said, 'Go.' She went.
- Question marks and exclamation marks.
- Paragraph breaks always end a sentence.
- Dialogue: a closing quote after punctuation ends the sentence only if followed by a capital letter or a new paragraph.

Use java.text.BreakIterator as a fallback only if helpful, but the rules above must be verified by tests.

### 5.3 Word and syllable counting

- Words: sequences of letters with internal apostrophes or hyphens, for example don't, well-known. Numbers count as words.
- Syllable heuristic: lowercase, strip non-letters, count vowel groups (a, e, i, o, u, y), subtract a silent trailing 'e' (but not 'le' after a consonant), handle 'ed' endings that are silent, minimum 1 per word. Add a small exception dictionary for common problem words. Document that the heuristic is approximate.
- Complex word: 3 or more syllables, not counting proper nouns mid-sentence, and not counting -es, -ed, -ing inflection as an added syllable.

### 5.4 Formulas

- Flesch Reading Ease = 206.835 - 1.015 \* (words / sentences) - 84.6 \* (syllables / words)
- Flesch-Kincaid Grade = 0.39 \* (words / sentences) + 11.8 \* (syllables / words) - 15.59
- Gunning Fog = 0.4 \* ((words / sentences) + 100 \* (complexWords / words))
- Coleman-Liau = 0.0588 \* L - 0.296 \* S - 15.8 (L = letters per 100 words, S = sentences per 100 words)
- ARI = 4.71 \* (characters / words) + 0.5 \* (words / sentences) - 21.43
- Guard against divide-by-zero. Round to one decimal.

### 5.5 Sentence difficulty score (0-100)

For each eligible sentence compute a weighted sum of normalized parts (each normalized to 0-1 and capped):

- Length: wordCount / 45, weight 0.35
- Word weight: average syllables per word in the sentence, scaled between 1.2 (0) and 2.2 (1), weight 0.25
- Complex word share: complexWords / words, scaled so 0.25 equals 1, weight 0.20
- Clause density: commas + semicolons + colons + dashes, scaled so 7 equals 1, weight 0.10
- Passive voice flag: 1 if a form of 'to be' is followed within 3 words by a past participle (regular -ed or a list of common irregular participles), weight 0.10

Difficulty = 100 \* sum of weighted parts. Rank descending. Break ties by longer word count. Make weights constants in one config class so they are easy to tune.

### 5.6 Reason tags and fix hints (rule-based)

- Length over 30 words: 'Very long (N words)'. Hint: suggest splitting at the nearest semicolon, 'and', 'but', or 'which'.
- Clause density 5 or more: 'Many clauses'. Hint: pull a side clause into its own sentence.
- Passive voice: 'Passive voice'. Hint: name who or what is doing the action.
- Heavy vocabulary: list up to 3 of the longest words with a short plain alternative from a small built-in map (utilize to use, approximately to about, commence to start, however to but, and so on).
- Never show a hint that is not triggered by a rule.

## 6. API Specification

Base path: `/api/v1`

### POST /analyze

Request:

```json
{
  "text": "string",
  "options": { "excludeDialogue": false, "genre": "GENERAL_FICTION" }
}
```

Response 200:

```json
{
  "summary": { "fleschReadingEase": 71.4, "label": "Fairly easy", "fleschKincaidGrade": 7.2, "gunningFog": 9.1, "colemanLiau": 8.0, "ari": 6.9, "readingTimeMinutes": 14 },
  "stats": { "words": 3250, "sentences": 241, "paragraphs": 52, "avgSentenceLength": 13.5, "avgSyllablesPerWord": 1.42, "longSentencePct": 8.3, "complexWordPct": 6.1, "passivePct": 9.5 },
  "genreCheck": { "genre": "GENERAL_FICTION", "typicalMin": 70, "typicalMax": 90, "verdict": "BELOW_RANGE", "message": "string" },
  "hardestSentences": [
    { "rank": 1, "text": "string", "start": 1042, "end": 1230, "wordCount": 47, "difficulty": 88, "reasons": ["Very long (47 words)", "Many clauses"], "hints": ["Split at the semicolon into two sentences."] }
  ]
}
```

Error responses use one consistent shape: `{ "error": "CODE", "message": "Human readable text" }` with codes TEXT\_TOO\_SHORT, TEXT\_TOO\_LONG, INVALID\_REQUEST, RATE\_LIMITED. Status codes: 400, 413, 429, 500.

### GET /health

Returns `{ "status": "ok" }`. Used by the frontend warm-up call and by the hosting platform.

## 7. Tech Stack

**Backend (required): Java Spring Boot**

- Java 21, Spring Boot 3.x, Maven.
- Spring Web, Spring Validation, Spring Boot Actuator (health only).
- Bucket4j (or a small in-memory token bucket) for per-IP rate limiting: 30 requests per minute per IP, return 429 with a friendly message.
- Request size limit configured to match the 100,000 character cap.
- CORS restricted to the deployed frontend origin through an environment variable (`CORS_ALLOWED_ORIGINS`).
- No database. Stateless.
- Logging: log request count, timing, and text length only. Never log text.
- Tests: JUnit 5 and AssertJ for engine tests; Spring MockMvc for controller tests.
- Dockerfile (multi-stage build) for deployment.

**Frontend (your choice, recommended):** React + Vite + TypeScript + Tailwind CSS. Single page, no router needed beyond simple sections or three small static routes. Use Vitest for a few tests on the highlight-rendering logic.

**Deployment (free tiers):**

- Backend on Render (Docker web service) with `/api/v1/health` as the health check path.
- Frontend on Netlify, with `VITE_API_BASE_URL` set to the Render URL.
- Provide `render.yaml` and `netlify.toml`.
- Render free services sleep when idle. Handle this in the UI: ping `/health` when the page loads, and if Analyze takes longer than 3 seconds show 'Waking up the server, this can take up to 50 seconds on the first use.' Never show a bare error for a cold start.

## 8. UX and Design Requirements

- Clean, calm, writer-friendly design. Generous whitespace, readable serif for the highlighted chapter text, sans-serif for UI.
- Light and dark mode, following the system setting with a manual toggle.
- Fully responsive. On mobile, results stack: score card, hardest sentences, then highlighted text.
- Loading state with a skeleton. Empty state with the sample-chapter button.
- Accessibility: WCAG AA contrast, keyboard navigation, visible focus rings, aria-live region announcing 'Analysis complete', highlights distinguishable without color, tooltip content also available on focus.
- Microcopy should be friendly and encouraging, never judgmental ('Here is where readers may slow down', not 'Your writing is bad').
- Page metadata: title, description, Open Graph tags, favicon, and a social preview image.

## 9. Non-Functional Requirements

- Performance: analyze a 15,000-word chapter in under 1.5 seconds server-side once warm.
- Security: input validation, no HTML rendering of user text (escape everything; highlighting is done with safe text segments, never dangerouslySetInnerHTML with raw input), security headers, no secrets in the repo.
- Privacy: no cookies, no third-party trackers. If analytics are added, use a privacy-friendly cookieless option (such as Plausible or Umami) and never send text.
- Reliability: clear error messages for every failure path.

## 10. Testing and Acceptance Criteria

**Engine tests (must pass)**

- Known reference passages produce Flesch scores within plus or minus 3 points of a trusted calculator.
- Splitter handles abbreviations, ellipses, dialogue, initials, decimals, and paragraph breaks.
- Syllable counter passes a table of at least 60 words with known counts, with at least 90 percent accuracy.
- Offsets: for every ranked sentence, `text.substring(start, end)` equals the sentence text exactly, including text with curly quotes and emoji.
- Empty, very short, and very long inputs return the correct error codes.

**Product acceptance**

- A user can paste a chapter, press Analyze, and see five numbered highlights and the score within 3 seconds when the server is warm.
- Clicking a hardest-sentence card scrolls to and highlights it in the text.
- Exclude-dialogue toggle changes scores and rankings.
- Works on the latest Chrome, Safari, Firefox, and on mobile.
- Lighthouse accessibility score of 90 or higher.
- Live link loads publicly without login.

## 11. Repository and Documentation

```
chaptercheck/
  backend/        (Spring Boot, Maven, Dockerfile)
  frontend/       (React + Vite + TS)
  render.yaml
  netlify.toml
  AI_LOG.md
  README.md
```

README must contain: live link, one-paragraph description, screenshots, how to run locally, API docs, the AI tools used, and a short 'Where the AI got it wrong' section copied from the best entry in AI\_LOG.md.

## 12. Final Deliverables Checklist

1. Live public link (frontend), plus the backend health URL.
2. GitHub repository link.
3. List of AI tools used (for example Antigravity, plus any models it used, and any other tool consulted).
4. One concrete place where the AI got it wrong, taken from AI\_LOG.md. Good examples: a sentence splitter that broke on 'Dr.' or on dialogue, wrong highlight offsets caused by curly quotes or emoji, a CORS misconfiguration, or a Render deployment failure. Must be a real event from this build, not invented.
5. The 150-word growth plan below, edited in the builder's own voice.

## 13. Growth Plan: First 100 Users (150 words, draft)

> Authors gather where they already want feedback, so I'd go to them. First, I'd run my own chapters through the tool and post before-and-after examples in r/writing, r/selfpublish, and r/DestructiveReaders, offering free analyses: 'send me a paragraph and I'll show its hardest sentences.' Their screenshots become my demo. Second, I'd message about 30 Wattpad and Royal Road authors and writing-group admins, ask one question, 'what would make you use this weekly?', and publicly ship their fixes. Third, every result gets a shareable score card, so each user advertises the tool. Finally, I'd post a build-in-public thread and a 'how I built it with Spring Boot and AI' write-up on dev.to and LinkedIn to reach developers who know writers. I'd track weekly returning users, not signups, aiming for 100 in four weeks.

## 14. Future Ideas (Out of Scope for v1)

- Upload .docx and .txt files.
- Chapter-by-chapter trend charts for whole manuscripts.
- Adverb and filter-word detection, repeated-word detection.
- Browser extension for Google Docs.
- Optional AI rewrite suggestions behind an explicit opt-in.
