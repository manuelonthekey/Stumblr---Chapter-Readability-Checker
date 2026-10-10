import type { SummaryDto, GenreCheckDto } from '../../types/api';
import { ScoreGauge } from './ScoreGauge';

interface ScoreCardProps {
  summary: SummaryDto;
  genreCheck: GenreCheckDto | null;
  scoreChange: number | null;
}

export const ScoreCard = ({ summary, genreCheck, scoreChange }: ScoreCardProps) => {
  return (
    <div className="bg-surface rounded-xl shadow-sm border border-brand p-6 flex flex-col md:flex-row items-center justify-between gap-6 transition-colors duration-300">
      
      <div className="flex-1 flex flex-col items-center relative">
        <ScoreGauge score={summary.fleschReadingEase} />
        <h3 className="text-lg font-bold text-main mt-4 font-serif">Flesch Reading Ease</h3>
        {scoreChange !== null && scoreChange !== 0 && (
          <div className={`slide-in-chip absolute top-0 -right-4 px-3 py-1 rounded-full text-xs font-bold shadow-sm ${
            scoreChange > 0 
              ? 'bg-[#4FA38B]/20 text-[#4FA38B] border border-[#4FA38B]/40' 
              : 'bg-[#C8574B]/20 text-[#C8574B] border border-[#C8574B]/40'
          }`}>
            {scoreChange > 0 ? '+' : ''}{scoreChange.toFixed(1)} {scoreChange > 0 ? 'easier' : 'harder'}
          </div>
        )}
      </div>

      <div className="hidden md:block w-px h-24 bg-brand-border transition-colors duration-300"></div>

      <div className="flex-1 space-y-4 w-full">
        <div>
          <h4 className="text-sm font-medium text-muted uppercase tracking-wider">US Grade Level</h4>
          <p className="text-2xl font-bold text-main font-serif">{summary.fleschKincaidGrade}</p>
        </div>
        
        {genreCheck && (
          <div>
            <h4 className="text-sm font-medium text-muted uppercase tracking-wider mb-1">
              Genre Match: <span className="text-primary font-bold">{genreCheck.genre.replace('_', ' ')}</span>
            </h4>
            <div className="flex items-center space-x-2">
              <span className={`inline-block w-3 h-3 rounded-full ${genreCheck.verdict === 'WITHIN_RANGE' ? 'bg-[#4FA38B]' : 'bg-[#E0A93B]'}`}></span>
              <span className="text-main font-medium">
                {genreCheck.message}
              </span>
            </div>
          </div>
        )}
      </div>
      
    </div>
  );
};
