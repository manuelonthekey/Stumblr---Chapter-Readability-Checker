import { useState } from 'react';
import type { AnalyzeOptions } from '../../types/api';
import { sampleChapterText } from '../../utils/sampleChapter';
import { Settings2 } from 'lucide-react';

interface InputPanelProps {
  onAnalyze: (text: string, options: AnalyzeOptions) => void;
  isLoading: boolean;
  initialText?: string;
}

export const InputPanel = ({ onAnalyze, isLoading, initialText = '' }: InputPanelProps) => {
  const [text, setText] = useState(initialText);
  const [genre, setGenre] = useState('GENERAL_FICTION');
  const [excludeDialogue, setExcludeDialogue] = useState(false);

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
        <div className="flex items-center space-x-4">
          
          <div className="flex items-center space-x-2 bg-transparent border-2 border-[var(--brand-primary)] rounded-full px-4 py-1.5 focus-within:ring-2 focus-within:ring-[var(--brand-primary)]/20 transition-all">
            <Settings2 size={16} className="text-primary" />
            <select 
              value={genre}
              onChange={(e) => setGenre(e.target.value)}
              className="bg-transparent text-sm font-medium text-main focus:outline-none appearance-none cursor-pointer pr-4"
            >
              <option value="GENERAL_FICTION">General Fiction</option>
              <option value="THRILLER">Thriller / Suspense</option>
              <option value="ROMANCE">Romance</option>
              <option value="YA">Young Adult (YA)</option>
              <option value="LITERARY_FICTION">Literary Fiction</option>
              <option value="NON_FICTION">Non-Fiction</option>
            </select>
          </div>
          
          <label className="flex items-center space-x-2 text-sm text-main cursor-pointer bg-transparent border-2 border-[var(--brand-primary)] rounded-full px-4 py-1.5 transition-all hover:bg-[var(--brand-primary)]/10">
            <input 
              type="checkbox" 
              checked={excludeDialogue}
              onChange={(e) => setExcludeDialogue(e.target.checked)}
              className="rounded-sm border-2 border-primary text-primary focus:ring-0 bg-transparent w-4 h-4 cursor-pointer"
            />
            <span className="font-medium">Exclude dialogue</span>
          </label>
        </div>
        
        <button 
          onClick={loadSample}
          className="text-sm font-semibold text-primary hover:text-accent transition-colors underline underline-offset-4"
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
              className="px-4 py-2 text-sm font-bold text-muted hover:text-main transition-colors"
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
