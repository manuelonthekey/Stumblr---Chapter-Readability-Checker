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
    <div className="bg-surface rounded-xl shadow-sm border border-brand p-6 transition-colors duration-300">
      <h3 className="text-lg font-bold text-main mb-4 font-serif">
        Hardest Sentences
      </h3>
      
      <div className="space-y-4">
        {sentences.map((s) => (
          <div 
            key={s.rank}
            onClick={() => onSentenceClick(s.rank)}
            className="group relative flex flex-col p-4 rounded-lg border border-brand bg-main hover:bg-surface transition-all cursor-pointer reveal-stagger hover:-translate-y-[2px]"
            style={{ animationDelay: `${(s.rank - 1) * 150}ms` }}
          >
            <div className="flex items-start gap-3 mb-2">
              <span className={`flex-shrink-0 w-6 h-6 rounded-full flex items-center justify-center text-xs font-bold badge-${s.rank}`}>
                {s.rank}
              </span>
              <p className="text-main font-serif leading-[1.7] line-clamp-3 group-hover:line-clamp-none transition-colors">
                {s.text}
              </p>
            </div>
            
            <div className="ml-9 flex flex-col gap-2">
              <div className="flex flex-wrap gap-2">
                <span className="inline-block px-2 py-1 bg-surface rounded text-xs font-medium text-muted border border-brand">
                  Score: {s.difficulty}
                </span>
                {s.reasons.map((reason, idx) => (
                  <span 
                    key={idx}
                    className="inline-block px-2 py-1 bg-surface rounded text-xs font-medium text-[#C8574B] border border-[#C8574B]/30"
                  >
                    {reason}
                  </span>
                ))}
              </div>
              
              {s.hints && s.hints.length > 0 && (
                <div className="text-sm text-muted mt-1 font-sans">
                  <span className="font-bold text-main">Fix hint: </span>
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
