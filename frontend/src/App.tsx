import { useState, useEffect } from 'react';
import { useAnalysis } from './hooks/useAnalysis';
import { useTheme } from './hooks/useTheme';
import { checkHealth } from './services/apiClient';
import { generateReportText, downloadReport } from './utils/reportUtils';

import { InputPanel } from './components/InputPanel/InputPanel';
import { ScoreCard } from './components/ResultsPanel/ScoreCard';
import { StatsGrid } from './components/ResultsPanel/StatsGrid';
import { HardestSentencesList } from './components/ResultsPanel/HardestSentencesList';
import { HighlightedText } from './components/ResultsPanel/HighlightedText';
import { HowItWorks } from './components/StaticPages/HowItWorks';
import { Privacy } from './components/StaticPages/Privacy';
import { TypingTagline } from './components/TypingTagline';
import { SkeletonResults } from './components/Skeleton';

function App() {
  const { theme, toggleTheme } = useTheme();
  const { analyze, clear, isLoading, isColdStart, error, result, scoreChange } = useAnalysis();
  
  const [activeRank, setActiveRank] = useState<number | null>(null);
  const [view, setView] = useState<'app' | 'how' | 'privacy'>('app');
  const [lastAnalyzedText, setLastAnalyzedText] = useState('');

  useEffect(() => {
    checkHealth();
  }, []);

  const handleAnalyze = (text: string, options: any) => {
    setLastAnalyzedText(text);
    setActiveRank(null);
    analyze(text, options);
  };

  const handleCopy = () => {
    if (result) {
      navigator.clipboard.writeText(generateReportText(result));
      alert('Report copied to clipboard!');
    }
  };

  const handleDownload = () => {
    if (result) {
      downloadReport(generateReportText(result));
    }
  };

  return (
    <div className="min-h-screen flex flex-col font-sans">
      <header className="border-b border-brand bg-surface sticky top-0 z-10 transition-colors duration-300">
        <div className="max-w-6xl mx-auto px-4 h-16 flex items-center justify-between">
          <div 
            className="flex items-center space-x-3 cursor-pointer group"
            onClick={() => { setView('app'); clear(); }}
          >
            <div className="w-9 h-9 bg-primary rounded-lg flex items-center justify-center text-white font-bold font-serif text-xl shadow-sm transition-transform group-hover:-translate-y-[1px]">
              S
            </div>
            <span className="font-bold text-xl tracking-tight text-main font-serif">
              Stumblr
            </span>
          </div>
          
          <div className="flex items-center space-x-6 text-sm font-medium">
            <button onClick={() => setView('how')} className="text-muted hover:text-primary transition-colors">
              How it works
            </button>
            <button onClick={toggleTheme} className="text-muted hover:bg-main p-2 rounded-full transition-colors" title="Toggle Theme">
              {theme === 'dark' ? '☀️' : '🌙'}
            </button>
          </div>
        </div>
      </header>

      <main className="flex-grow max-w-6xl mx-auto w-full px-4 py-8">
        {isColdStart && (
          <div className="mb-6 p-4 bg-amber-50 dark:bg-amber-900/20 border border-amber-200 dark:border-amber-800/50 rounded-lg text-amber-800 dark:text-amber-200 text-sm flex items-center shadow-sm">
            <span className="animate-pulse mr-2">⏳</span> 
            Waking up the server. This can take up to 50 seconds on the first use.
          </div>
        )}
        
        {error && (
          <div className="mb-6 p-4 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800/50 rounded-lg text-red-800 dark:text-red-200 text-sm font-medium shadow-sm">
            Error: {error}
          </div>
        )}

        {view === 'how' && <HowItWorks />}
        {view === 'privacy' && <Privacy />}
        
        {view === 'app' && !result && !isLoading && (
          <div className="space-y-6 animate-in fade-in duration-500">
            <div className="text-center max-w-2xl mx-auto mb-10">
              <h1 className="text-3xl md:text-4xl font-bold text-main mb-3 tracking-tight font-serif">
                Check your chapter's readability
              </h1>
              <TypingTagline trigger={view} />
            </div>
            
            <InputPanel 
              onAnalyze={handleAnalyze} 
              isLoading={isLoading} 
              initialText={lastAnalyzedText}
            />
          </div>
        )}

        {view === 'app' && isLoading && (
          <SkeletonResults />
        )}

        {view === 'app' && result && !isLoading && (
          <div className="space-y-8 animate-in slide-in-from-bottom-4 duration-500 pb-20">
            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
              <h1 className="text-2xl font-bold text-main font-serif">Analysis Results</h1>
              <div className="flex space-x-3">
                <button onClick={handleCopy} className="btn-secondary">
                  Copy
                </button>
                <button onClick={handleDownload} className="btn-secondary">
                  Download
                </button>
                <button onClick={() => clear()} className="btn-primary">
                  Analyze Again
                </button>
              </div>
            </div>

            <ScoreCard 
              summary={result.summary} 
              genreCheck={result.genreCheck} 
              scoreChange={scoreChange}
            />
            
            <div className="grid grid-cols-1 lg:grid-cols-12 gap-8 items-start">
              <div className="lg:col-span-4 space-y-8 sticky top-24">
                <StatsGrid stats={result.stats} summary={result.summary} />
                <HardestSentencesList 
                  sentences={result.hardestSentences} 
                  onSentenceClick={setActiveRank}
                />
              </div>
              
              <div className="lg:col-span-8">
                <HighlightedText 
                  originalText={lastAnalyzedText} 
                  hardestSentences={result.hardestSentences}
                  activeRank={activeRank}
                />
              </div>
            </div>
          </div>
        )}
      </main>

      <footer className="border-t border-brand py-8 mt-auto bg-surface transition-colors duration-300">
        <div className="max-w-6xl mx-auto px-4 flex flex-col md:flex-row items-center justify-between text-sm text-muted">
          <p>© {new Date().getFullYear()} Stumblr.</p>
          <div className="flex space-x-6 mt-4 md:mt-0">
            <button onClick={() => setView('privacy')} className="hover:text-main transition-colors">Privacy</button>
            <a href="mailto:feedback@chaptercheck.app" className="hover:text-main transition-colors">Feedback</a>
          </div>
        </div>
      </footer>
    </div>
  );
}

export default App;
