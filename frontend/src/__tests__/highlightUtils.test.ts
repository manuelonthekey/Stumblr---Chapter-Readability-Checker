import { describe, it, expect } from 'vitest';
import { buildHighlights } from '../utils/highlightUtils';

describe('buildHighlights', () => {
  it('handles empty string safely', () => {
    expect(buildHighlights('', [])).toEqual([]);
  });

  it('handles no hardest sentences safely', () => {
    const text = 'Just a normal sentence.';
    expect(buildHighlights(text, [])).toEqual([{ text, rank: null }]);
  });

  it('highlights a single sentence correctly', () => {
    const text = 'First sentence. Hard sentence. Last sentence.';
    const hardest = [{ start: 16, end: 30, rank: 1 }];
    
    const result = buildHighlights(text, hardest);
    
    expect(result).toEqual([
      { text: 'First sentence. ', rank: null },
      { text: 'Hard sentence.', rank: 1 },
      { text: ' Last sentence.', rank: null }
    ]);
  });

  it('highlights multiple sentences correctly', () => {
    const text = 'S1. S2. S3. S4.';
    const hardest = [
      { start: 4, end: 7, rank: 2 },
      { start: 12, end: 15, rank: 1 }
    ];
    
    const result = buildHighlights(text, hardest);
    
    expect(result).toEqual([
      { text: 'S1. ', rank: null },
      { text: 'S2.', rank: 2 },
      { text: ' S3. ', rank: null },
      { text: 'S4.', rank: 1 }
    ]);
  });

  it('ignores invalid overlaps safely', () => {
    const text = 'S1. S2. S3.';
    const hardest = [
      { start: 0, end: 3, rank: 1 },
      { start: 2, end: 7, rank: 2 } // overlaps with the first one
    ];
    
    const result = buildHighlights(text, hardest);
    
    // It should skip the overlapping one based on the logic
    expect(result).toEqual([
      { text: 'S1.', rank: 1 },
      { text: ' S2. S3.', rank: null }
    ]);
  });
});
