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
    <div className="glass-tray rounded-3xl border border-glass p-6 md:p-8 transition-colors duration-300 w-full mb-6">
      <h3 className="text-sm font-bold tracking-widest text-primary uppercase mb-6 font-sans">
        Metrics & Details
      </h3>
      <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-y-8 gap-x-6">
        {statItems.map((item, idx) => (
          <div key={idx} className="flex flex-col">
            <span 
              className="text-[11px] text-muted font-bold uppercase tracking-wider cursor-help font-sans mb-1"
              title={item.tooltip}
            >
              {item.label}
            </span>
            <span className="text-xl font-bold text-main font-serif">
              {item.value}
            </span>
          </div>
        ))}
      </div>
      
      <div className="mt-8 pt-5 border-t border-glass flex flex-wrap gap-6 text-xs text-muted font-bold font-sans uppercase tracking-wide">
        <span title="Gunning Fog Index">Fog: <span className="text-main">{summary.gunningFog}</span></span>
        <span title="Coleman-Liau Index">CLI: <span className="text-main">{summary.colemanLiau}</span></span>
        <span title="Automated Readability Index">ARI: <span className="text-main">{summary.ari}</span></span>
      </div>
    </div>
  );
};
