# ChapterCheck

ChapterCheck is a readability analyzer tailored exclusively for fiction writers. It computes Flesch Reading Ease scores, breaks down complex vocabulary and passive voice, and ranks the 5 hardest sentences in any given text, guiding writers on where to simplify their prose.

## Features

- **Flesch Reading Ease** & **US Grade Level** checks.
- **Rule-based sentence scoring:** Sentences are scored based on clause density, complex syllable structures, word length, and passive voice.
- **Dialogue Exclusion:** Strip quotes out of the text before analysis, keeping the score accurate for the prose rather than choppy dialogue.
- **Genre Targeting:** Compare your Flesch score to the expected range for your genre (e.g. YA vs Literary Fiction).
- **Offline / Private:** Does not use AI, does not train models on your work, and does not store your unpublished text.

## Tech Stack

- **Backend:** Java 21, Spring Boot 3.3.4
- **Frontend:** React 19, TypeScript, Tailwind CSS v4, Vite
- **Testing:** JUnit 5 / MockMvc for backend, Vitest / React Testing Library for frontend.

## Local Development

### 1. Backend

1. Navigate to the `backend/` directory.
2. Ensure you have Java 21+ installed (set `JAVA_HOME` if necessary).
3. Run the Spring Boot application:
   \`\`\`bash
   ./mvnw.cmd spring-boot:run
   \`\`\`
   The backend will start at `http://localhost:8080`.
4. Run tests:
   \`\`\`bash
   ./mvnw.cmd test
   \`\`\`

### 2. Frontend

1. Navigate to the `frontend/` directory.
2. Install dependencies:
   \`\`\`bash
   npm install
   \`\`\`
3. Start the Vite development server:
   \`\`\`bash
   npm run dev
   \`\`\`
   The UI will start at `http://localhost:5173`.
4. Run tests:
   \`\`\`bash
   npm test
   \`\`\`

## Deployment

The backend is configured to be deployed on Render via Docker (`render.yaml`). The frontend is configured for Netlify (`netlify.toml`).

*Built as part of an autonomous agent session.*
