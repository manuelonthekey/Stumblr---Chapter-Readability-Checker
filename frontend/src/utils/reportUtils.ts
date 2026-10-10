import { AnalyzeResponse } from '../types/api';

export const generateReportText = (response: AnalyzeResponse): string => {
  const { summary, stats, genreCheck, hardestSentences } = response;
  
  let report = `ChapterCheck Readability Report\n`;
  report += `===============================\n\n`;
  
  report += `Score: ${summary.fleschReadingEase} (${summary.label})\n`;
  report += `Reading level: US Grade ${summary.fleschKincaidGrade}\n`;
  report += `Reading time: ~${summary.readingTimeMinutes} mins\n`;
  report += `Genre target (${genreCheck.genre}): ${genreCheck.message}\n\n`;
  
  report += `Stats:\n`;
  report += `- Words: ${stats.words}\n`;
  report += `- Sentences: ${stats.sentences}\n`;
  report += `- Paragraphs: ${stats.paragraphs}\n`;
  report += `- Avg sentence length: ${stats.avgSentenceLength} words\n`;
  report += `- Avg syllables per word: ${stats.avgSyllablesPerWord}\n`;
  report += `- Long sentences: ${stats.longSentencePct}%\n`;
  report += `- Complex words: ${stats.complexWordPct}%\n`;
  report += `- Passive voice: ${stats.passivePct}%\n\n`;
  
  report += `Top ${hardestSentences.length} Hardest Sentences:\n`;
  report += `---------------------------------\n`;
  
  hardestSentences.forEach((s) => {
    report += `\n[${s.rank}] Difficulty Score: ${s.difficulty}\n`;
    report += `"${s.text}"\n`;
    report += `Reasons: ${s.reasons.join(', ')}\n`;
    if (s.hints && s.hints.length > 0) {
      report += `Hints: ${s.hints.join(' | ')}\n`;
    }
  });
  
  report += `\n===============================\n`;
  report += `Analyzed privately via ChapterCheck\n`;
  
  return report;
};

export const downloadReport = (text: string, filename: string = 'readability-report.txt') => {
  const blob = new Blob([text], { type: 'text/plain;charset=utf-8' });
  const url = URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.setAttribute('href', url);
  link.setAttribute('download', filename);
  link.style.display = 'none';
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  URL.revokeObjectURL(url);
};
