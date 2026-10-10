import { useMemo, useRef, useEffect } from 'react';
import type { HardestSentenceDto } from '../../types/api';
import { buildHighlights } from '../../utils/highlightUtils';

interface HighlightedTextProps {
  originalText: string;
  hardestSentences: HardestSentenceDto[];
  activeRank?: number | null;
}

export const HighlightedText = ({ originalText, hardestSentences, activeRank }: HighlightedTextProps) => {
  const containerRef = useRef<HTMLDivElement>(null);
  
  const segments = useMemo(() => {
    const formattedSentences = hardestSentences.map(s => ({
      start: s.start,
      end: s.end,
      rank: s.rank
    }));
    return buildHighlights(originalText, formattedSentences);
  }, [originalText, hardestSentences]);

  // Scroll to active rank when it changes
  useEffect(() => {
    if (activeRank && containerRef.current) {
      const el = containerRef.current.querySelector(`[data-rank="${activeRank}"]`);
      if (el) {
        el.scrollIntoView({ behavior: 'smooth', block: 'center' });
        
        // Add a temporary pulse effect
        el.classList.add('pulse-glow');
        setTimeout(() => el.classList.remove('pulse-glow'), 1500);
      }
    }
  }, [activeRank]);

  // Get reason string for tooltip
  const getTooltip = (rank: number) => {
    const s = hardestSentences.find(s => s.rank === rank);
    if (!s) return '';
    return `${s.reasons.join(', ')}\nHints: ${s.hints.join(' | ')}`;
  };

  return (
    <div 
      ref={containerRef}
      className="glass-opaque rounded-3xl shadow-sm border border-glass p-8 md:p-12 font-serif text-[17px] leading-[1.8] text-main whitespace-pre-wrap overflow-y-auto max-h-[800px] transition-colors duration-300"
    >
      {segments.map((segment, idx) => {
        if (segment.rank === null) {
          return <span key={idx}>{segment.text}</span>;
        }
        
        return (
          <span 
            key={idx} 
            data-rank={segment.rank}
            title={getTooltip(segment.rank)}
            className={`relative inline rounded-sm transition-colors duration-300 highlight-rank-${segment.rank} reveal-stagger
              ${activeRank === segment.rank ? 'ring-2 ring-offset-2 ring-primary dark:ring-offset-surface' : ''}`}
            style={{ animationDelay: `${(segment.rank - 1) * 150}ms` }}
          >
            <span className={`inline-flex items-center justify-center w-5 h-5 text-[10px] font-bold rounded-full badge-${segment.rank} mr-1 align-middle shadow-sm font-sans`}>
              {segment.rank}
            </span>
            {segment.text}
          </span>
        );
      })}
    </div>
  );
};
