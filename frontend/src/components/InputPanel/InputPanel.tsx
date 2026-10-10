import { useState } from 'react';
import type { AnalyzeOptions } from '../../types/api';
import { sampleChapterText } from '../../utils/sampleChapter';

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
    <div className="w-full max-w-4xl mx-auto bg-surface rounded-xl shadow-sm border border-brand overflow-hidden flex flex-col transition-colors duration-300">
      <div className="p-4 border-b border-brand bg-main flex flex-wrap gap-4 items-center justify-between transition-colors duration-300">
        <div className="flex items-center space-x-4">
          <label className="flex items-center space-x-2 text-sm text-main">
            <span className="font-bold">Genre Target:</span>
            <select 
              value={genre}
              onChange={(e) => setGenre(e.target.value)}
              className="bg-surface border border-brand rounded px-2 py-1 text-sm focus:ring-2 focus:ring-primary outline-none transition-colors"
            >
              <option value="GENERAL_FICTION">General Fiction</option>
              <option value="THRILLER">Thriller / Suspense</option>
              <option value="ROMANCE">Romance</option>
              <option value="YA">Young Adult (YA)</option>
              <option value="LITERARY_FICTION">Literary Fiction</option>
              <option value="NON_FICTION">Non-Fiction</option>
            </select>
          </label>
          
          <label className="flex items-center space-x-2 text-sm text-main cursor-pointer">
            <input 
              type="checkbox" 
              checked={excludeDialogue}
              onChange={(e) => setExcludeDialogue(e.target.checked)}
              className="rounded border-brand text-primary focus:ring-primary bg-surface"
            />
            <span className="font-medium">Exclude dialogue</span>
          </label>
        </div>
        
        <button 
          onClick={loadSample}
          className="text-sm font-medium text-primary hover:underline transition-colors"
        >
          Try a sample chapter
        </button>
      </div>
      
      <div className="relative flex-grow">
        <textarea
          value={text}
          onChange={(e) => setText(e.target.value)}
          onKeyDown={handleKeyDown}
          placeholder="Paste your chapter here... (Ctrl+Enter to analyze)"
          className="w-full h-[500px] p-8 resize-y focus:outline-none bg-transparent font-serif text-[17px] leading-[1.7] text-main placeholder-muted transition-colors"
          spellCheck="false"
        />
        
        {isTooLong && (
          <div className="absolute top-4 right-4 bg-red-100 text-red-800 px-3 py-1 rounded text-sm font-bold shadow-sm">
            Over 100,000 character limit
          </div>
        )}
      </div>
      
      <div className="p-4 border-t border-brand bg-main flex items-center justify-between transition-colors duration-300">
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
        
        <div className="flex space-x-3">
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
            className={`btn-primary ${!canAnalyze ? 'opacity-50 hover:translate-y-0 shadow-none cursor-not-allowed' : ''} ${isLoading ? 'cursor-wait' : ''}`}
          >
            {isLoading ? 'Analyzing...' : 'Analyze'}
          </button>
        </div>
      </div>
    </div>
  );
};
