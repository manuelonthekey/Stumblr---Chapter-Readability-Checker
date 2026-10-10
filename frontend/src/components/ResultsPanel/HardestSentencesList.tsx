import type { HardestSentenceDto } from '../../types/api';

interface HardestSentencesListProps {
  sentences: HardestSentenceDto[];
  onSentenceClick: (rank: number) => void;
}

export const HardestSentencesList = ({ sentences, onSentenceClick }: HardestSentencesListProps) => {
  if (sentences.length === 0) {
    return null;
  }

  return (
    <div className="glass-tray rounded-3xl p-6 transition-colors duration-300 w-full overflow-hidden">
      <h3 className="text-lg font-bold text-main mb-4 font-serif px-2">
        You might want to review
      </h3>
      
      {/* Horizontal scroll container on desktop, stacked on mobile */}
      <div className="flex flex-col md:flex-row gap-4 overflow-x-auto pb-4 px-2 snap-x [&::-webkit-scrollbar]:hidden [-ms-overflow-style:none] [scrollbar-width:none]">
        {sentences.map((s) => (
          <div 
            key={s.rank}
            onClick={() => onSentenceClick(s.rank)}
            className="group relative flex flex-col p-5 rounded-2xl border border-glass bg-[var(--glass-surface)] hover:bg-[var(--glass-opaque)] transition-all duration-300 cursor-pointer reveal-stagger hover:-translate-y-1 shadow-sm hover:shadow-md md:min-w-[320px] md:max-w-[320px] snap-start shrink-0"
            style={{ animationDelay: `${(s.rank - 1) * 150}ms` }}
          >
            <div className="flex items-start gap-3 mb-4">
              <div className="relative flex-shrink-0 mt-0.5">
                <span className={`w-6 h-6 rounded-full flex items-center justify-center text-xs font-bold badge-${s.rank}`}>
                  {s.rank}
                </span>
                {/* Tiny notification dot detail */}
                <span className="absolute -top-1 -right-1 w-2.5 h-2.5 bg-[var(--brand-accent)] rounded-full border-2 border-white dark:border-[#1F2229]"></span>
              </div>
              <p className="text-main font-serif leading-[1.6] line-clamp-4 group-hover:line-clamp-none transition-all">
                {s.text}
              </p>
            </div>
            
            <div className="mt-auto flex flex-col gap-2 pt-2 border-t border-glass">
              <div className="flex flex-wrap gap-2">
                <span className="inline-block px-2.5 py-1 bg-[var(--glass-surface)] rounded-full text-[11px] font-bold text-muted uppercase tracking-wide">
                  Score {s.difficulty}
                </span>
                {s.reasons.map((reason, idx) => (
                  <span 
                    key={idx}
                    className="inline-block px-2.5 py-1 bg-[var(--brand-accent)]/10 rounded-full text-[11px] font-bold text-[var(--brand-accent)] uppercase tracking-wide"
                  >
                    {reason}
                  </span>
                ))}
              </div>
              
              {s.hints && s.hints.length > 0 && (
                <div className="text-xs text-muted mt-1 font-sans">
                  <span className="font-bold text-primary">Hint: </span>
                  {s.hints.join(' | ')}
                </div>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
