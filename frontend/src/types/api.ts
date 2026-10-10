export interface AnalyzeOptions {
  excludeDialogue: boolean;
  genre: string;
}

export interface AnalyzeRequest {
  text: string;
  options: AnalyzeOptions;
}

export interface SummaryDto {
  fleschReadingEase: number;
  label: string;
  fleschKincaidGrade: number;
  gunningFog: number;
  colemanLiau: number;
  ari: number;
  readingTimeMinutes: number;
}

export interface StatsDto {
  words: number;
  sentences: number;
  paragraphs: number;
  avgSentenceLength: number;
  avgSyllablesPerWord: number;
  longSentencePct: number;
  complexWordPct: number;
  passivePct: number;
}

export interface GenreCheckDto {
  genre: string;
  typicalMin: number;
  typicalMax: number;
  verdict: 'WITHIN_RANGE' | 'BELOW_RANGE' | 'ABOVE_RANGE';
  message: string;
}

export interface HardestSentenceDto {
  rank: number;
  text: string;
  start: number;
  end: number;
  wordCount: number;
  difficulty: number;
  reasons: string[];
  hints: string[];
}

export interface AnalyzeResponse {
  summary: SummaryDto;
  stats: StatsDto;
  genreCheck: GenreCheckDto;
  hardestSentences: HardestSentenceDto[];
}

export interface ApiError {
  error: string;
  message: string;
}
