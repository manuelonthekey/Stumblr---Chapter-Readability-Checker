export interface HighlightSegment {
  text: string;
  rank: number | null; // 1-5 for hardest sentences, null for normal text
}

export const buildHighlights = (originalText: string, hardestSentences: { start: number, end: number, rank: number }[]): HighlightSegment[] => {
  if (!originalText) return [];
  if (!hardestSentences || hardestSentences.length === 0) {
    return [{ text: originalText, rank: null }];
  }

  // Sort by start position
  const sorted = [...hardestSentences].sort((a, b) => a.start - b.start);
  const segments: HighlightSegment[] = [];
  
  let currentOffset = 0;
  
  for (const sentence of sorted) {
    // If there's overlap or invalid bounds, skip to avoid breaking text
    if (sentence.start < currentOffset) continue;
    
    // Add plain text before this sentence
    if (sentence.start > currentOffset) {
      segments.push({
        text: originalText.substring(currentOffset, sentence.start),
        rank: null
      });
    }
    
    // Add the highlighted sentence
    segments.push({
      text: originalText.substring(sentence.start, sentence.end),
      rank: sentence.rank
    });
    
    currentOffset = sentence.end;
  }
  
  // Add remaining text
  if (currentOffset < originalText.length) {
    segments.push({
      text: originalText.substring(currentOffset),
      rank: null
    });
  }
  
  return segments;
};
