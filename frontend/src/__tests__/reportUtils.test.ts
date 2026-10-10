import { describe, it, expect } from 'vitest';
import { generateReportText } from '../utils/reportUtils';
import { AnalyzeResponse } from '../types/api';

describe('reportUtils', () => {
  it('generates a well-formatted report text', () => {
    const mockResponse: AnalyzeResponse = {
      summary: {
        fleschReadingEase: 72.5,
        label: 'Fairly easy',
        fleschKincaidGrade: 7.0,
        gunningFog: 8.5,
        colemanLiau: 7.2,
        ari: 6.8,
        readingTimeMinutes: 5,
      },
      stats: {
        words: 1000,
        sentences: 100,
        paragraphs: 10,
        avgSentenceLength: 10,
        avgSyllablesPerWord: 1.4,
        longSentencePct: 5,
        complexWordPct: 8,
        passivePct: 2,
      },
      genreCheck: {
        genre: 'THRILLER',
        typicalMin: 70,
        typicalMax: 90,
        verdict: 'WITHIN_RANGE',
        message: 'Typical for Thriller is 70-90. Your chapter fits well.',
      },
      hardestSentences: [
        {
          rank: 1,
          text: 'This is a very complex and overly verbose sentence.',
          start: 0,
          end: 51,
          wordCount: 9,
          difficulty: 80,
          reasons: ['Passive voice'],
          hints: ['Simplify'],
        }
      ]
    };

    const text = generateReportText(mockResponse);
    expect(text).toContain('Score: 72.5 (Fairly easy)');
    expect(text).toContain('Top 1 Hardest Sentences:');
    expect(text).toContain('Difficulty Score: 80');
    expect(text).toContain('Hints: Simplify');
  });
});
