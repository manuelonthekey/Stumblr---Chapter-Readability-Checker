import type { StatsDto, SummaryDto } from '../../types/api';

interface StatsGridProps {
  stats: StatsDto;
  summary: SummaryDto;
}

export const StatsGrid = ({ stats, summary }: StatsGridProps) => {
  const statItems = [
    { label: 'Words', value: stats.words.toLocaleString() },
    { label: 'Sentences', value: stats.sentences.toLocaleString() },
    { label: 'Paragraphs', value: stats.paragraphs.toLocaleString() },
    { label: 'Reading Time', value: `~${summary.readingTimeMinutes} min` },
    { label: 'Avg Sentence', value: `${stats.avgSentenceLength} words` },
    { label: 'Avg Syllables', value: `${stats.avgSyllablesPerWord} / word` },
    { label: 'Long Sentences', value: `${stats.longSentencePct}%`, tooltip: '> 25 words' },
    { label: 'Complex Words', value: `${stats.complexWordPct}%`, tooltip: '3+ syllables' },
    { label: 'Passive Voice', value: `${stats.passivePct}%` },
  ];

  return (
    <div className="bg-surface rounded-xl shadow-sm border border-brand p-6 transition-colors duration-300">
      <h3 className="text-sm font-bold tracking-wide text-muted uppercase mb-4 font-sans">
        Metrics
      </h3>
      <div className="grid grid-cols-2 md:grid-cols-3 gap-y-6 gap-x-4">
        {statItems.map((item, idx) => (
          <div key={idx} className="flex flex-col">
            <span 
              className="text-xs text-muted font-bold uppercase tracking-wider cursor-help font-sans"
              title={item.tooltip}
            >
              {item.label}
            </span>
            <span className="text-lg font-bold text-main mt-1 font-serif">
              {item.value}
            </span>
          </div>
        ))}
      </div>
      
      <div className="mt-6 pt-4 border-t border-brand flex space-x-4 text-xs text-muted font-bold font-sans">
        <span title="Gunning Fog Index">Fog: {summary.gunningFog}</span>
        <span title="Coleman-Liau Index">CLI: {summary.colemanLiau}</span>
        <span title="Automated Readability Index">ARI: {summary.ari}</span>
      </div>
    </div>
  );
};
