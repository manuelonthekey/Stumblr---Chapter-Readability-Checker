import { useState, useRef, useEffect } from 'react';
import type { AnalyzeOptions } from '../../types/api';
import { sampleChapterText } from '../../utils/sampleChapter';
import { SlidersHorizontal, ChevronDown, Check } from 'lucide-react';

interface InputPanelProps {
  onAnalyze: (text: string, options: AnalyzeOptions) => void;
  isLoading: boolean;
  initialText?: string;
}

const GENRE_OPTIONS = [
  { value: 'GENERAL_FICTION', label: 'General Fiction' },
  { value: 'THRILLER', label: 'Thriller / Suspense' },
  { value: 'ROMANCE', label: 'Romance' },
  { value: 'YA', label: 'Young Adult (YA)' },
  { value: 'LITERARY_FICTION', label: 'Literary Fiction' },
  { value: 'NON_FICTION', label: 'Non-Fiction' },
];

export const InputPanel = ({ onAnalyze, isLoading, initialText = '' }: InputPanelProps) => {
  const [text, setText] = useState(initialText);
  const [genre, setGenre] = useState('GENERAL_FICTION');
  const [excludeDialogue, setExcludeDialogue] = useState(false);
  const [isGenreOpen, setIsGenreOpen] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setIsGenreOpen(false);
      }
    };
    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === 'Escape') {
        setIsGenreOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    document.addEventListener('keydown', handleKeyDown);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
      document.removeEventListener('keydown', handleKeyDown);
    };
  }, []);

  const selectedGenreLabel = GENRE_OPTIONS.find(g => g.value === genre)?.label || 'General Fiction';

  const charCount = text.length;
  const wordCount = text.trim().split(/\s+/).filter(w => w.length > 0).length;
  
  const isTooShort = charCount > 0 && charCount < 300 && wordCount < 50;
  const isTooLong = charCount > 100000;
  const canAnalyze = charCount > 0 && !isTooLong && !isLoading;

  const handleAnalyze = () => {
    if (canAnalyze) {
      onAnalyze(text, { genre, excludeDialogue });
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if ((e.ctrlKey || e.metaKey) && e.key === 'Enter') {
      handleAnalyze();
    }
  };

  const loadSample = () => {
    setText(sampleChapterText);
  };

  return (
    <div className="w-full flex flex-col gap-6">
      <div className="flex flex-wrap gap-4 items-center justify-between px-2">
        <div className="flex items-center space-x-3">
          
          {/* Custom Glass Genre Dropdown */}
          <div className="relative" ref={dropdownRef}>
            <button
              type="button"
              onClick={() => setIsGenreOpen(!isGenreOpen)}
              className="glass-tray border border-white/80 rounded-full px-4 py-2 hover:bg-white/80 transition-all flex items-center space-x-2.5 cursor-pointer shadow-xs active:scale-95 text-sm font-medium text-main"
              aria-haspopup="listbox"
              aria-expanded={isGenreOpen}
            >
              <SlidersHorizontal size={15} className="text-primary" />
              <span>{selectedGenreLabel}</span>
              <ChevronDown 
                size={14} 
                className={`text-muted transition-transform duration-200 ${isGenreOpen ? 'rotate-180' : ''}`} 
              />
            </button>

            {isGenreOpen && (
              <div 
                className="absolute top-full left-0 mt-2 w-56 glass-opaque rounded-2xl border border-white/90 shadow-xl p-1.5 z-50 backdrop-blur-xl animate-in fade-in duration-150"
                role="listbox"
              >
                {GENRE_OPTIONS.map((item) => {
                  const isSelected = genre === item.value;
                  return (
                    <button
                      key={item.value}
                      type="button"
                      role="option"
                      aria-selected={isSelected}
                      onClick={() => {
                        setGenre(item.value);
                        setIsGenreOpen(false);
                      }}
                      className={`w-full flex items-center justify-between px-3.5 py-2.5 rounded-xl text-sm font-medium transition-all cursor-pointer ${
                        isSelected 
                          ? 'bg-[var(--brand-primary)] text-white shadow-xs' 
                          : 'text-main hover:bg-white/60'
                      }`}
                    >
                      <span>{item.label}</span>
                      {isSelected && <Check size={14} className="text-white ml-2 shrink-0" />}
                    </button>
                  );
                })}
              </div>
            )}
          </div>
          
          {/* Custom Glass Exclude Dialogue Toggle */}
          <button
            type="button"
            role="checkbox"
            aria-checked={excludeDialogue}
            onClick={() => setExcludeDialogue(!excludeDialogue)}
            onKeyDown={(e) => {
              if (e.key === ' ' || e.key === 'Enter') {
                e.preventDefault();
                setExcludeDialogue(!excludeDialogue);
              }
            }}
            className="glass-tray border border-white/80 rounded-full px-4 py-2 hover:bg-white/80 transition-all flex items-center space-x-2.5 cursor-pointer shadow-xs select-none active:scale-95 text-sm font-medium"
          >
            <div 
              className={`w-4 h-4 rounded-md flex items-center justify-center transition-all ${
                excludeDialogue 
                  ? 'bg-[var(--brand-primary)] text-white shadow-xs' 
                  : 'border-2 border-[var(--brand-primary)]/40 bg-white/40'
              }`}
            >
              {excludeDialogue && <Check size={11} strokeWidth={3} />}
            </div>
            <span className={excludeDialogue ? 'text-primary font-semibold' : 'text-main'}>
              Exclude dialogue
            </span>
          </button>
        </div>
        
        <button 
          onClick={loadSample}
          className="text-sm font-semibold text-primary hover:text-accent transition-colors underline underline-offset-4 cursor-pointer"
        >
          Try a sample chapter
        </button>
      </div>
      
      <div className="relative flex-grow glass-opaque rounded-3xl overflow-hidden shadow-sm transition-colors duration-300">
        <textarea
          value={text}
          onChange={(e) => setText(e.target.value)}
          onKeyDown={handleKeyDown}
          placeholder="Paste your chapter here... (Ctrl+Enter to analyze)"
          className="w-full h-[55vh] min-h-[400px] p-8 resize-none focus:outline-none bg-transparent font-serif text-[17px] leading-[1.7] text-main placeholder-muted transition-colors"
          spellCheck="false"
        />
        
        {isTooLong && (
          <div className="absolute top-6 right-6 bg-[#C8574B]/20 text-[#C8574B] px-4 py-1.5 rounded-full text-sm font-bold shadow-sm backdrop-blur-md border border-[#C8574B]/30">
            Over 100,000 character limit
          </div>
        )}
      </div>
      
      <div className="flex items-center justify-between px-2 transition-colors duration-300">
        <div className="text-sm text-muted font-medium font-sans">
          <span className={isTooLong ? 'text-[#C8574B] font-bold' : ''}>{charCount.toLocaleString()}</span> chars
          {' · '}
          <span className={isTooShort ? 'text-[#E0A93B] font-bold' : ''}>{wordCount.toLocaleString()}</span> words
          
          {isTooShort && (
            <span className="ml-2 text-[#E0A93B]">
              (More text needed for reliable score)
            </span>
          )}
        </div>
        
        <div className="flex space-x-4">
          {text.length > 0 && (
            <button 
              onClick={() => setText('')}
              className="px-4 py-2 text-sm font-semibold text-muted hover:text-main cursor-pointer active:scale-95 transition-all font-sans tracking-wide"
            >
              Clear
            </button>
          )}
          <button
            onClick={handleAnalyze}
            disabled={!canAnalyze}
            className={`btn-primary shadow-lg ${!canAnalyze ? 'opacity-50 hover:translate-y-0 shadow-none cursor-not-allowed' : ''} ${isLoading ? 'cursor-wait' : ''}`}
          >
            {isLoading ? 'Analyzing...' : 'Analyze Text'}
          </button>
        </div>
      </div>
    </div>
  );
};
