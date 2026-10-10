import { useState, useEffect } from 'react';
import { useAnalysis } from './hooks/useAnalysis';
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
import { BackgroundBlobs } from './components/common/BackgroundBlobs';
import { Sidebar } from './components/common/Sidebar';

function App() {
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
    <div className="min-h-screen flex items-center justify-center p-0 md:p-6 overflow-hidden">
      <BackgroundBlobs />
      
      <div className="glass-panel w-full h-full md:w-[95vw] md:max-w-[1400px] md:h-[92vh] md:rounded-[36px] flex flex-col md:flex-row overflow-hidden relative">
        <Sidebar view={view} setView={setView} onClear={clear} />
        
        <main className="flex-1 overflow-y-auto px-6 py-10 md:px-12 md:py-12 scroll-smooth pb-24 md:pb-12">
          <div className="max-w-4xl mx-auto w-full">
            
            {isColdStart && (
              <div className="mb-6 p-4 bg-amber-500/20 border border-amber-500/30 rounded-xl text-amber-700 dark:text-amber-200 text-sm flex items-center backdrop-blur-md">
                <span className="animate-pulse mr-2">⏳</span> 
                Waking up the server. This can take up to 50 seconds on the first use.
              </div>
            )}
            
            {error && (
              <div className="mb-6 p-4 bg-red-500/20 border border-red-500/30 rounded-xl text-red-700 dark:text-red-200 text-sm font-medium backdrop-blur-md">
                Error: {error}
              </div>
            )}

            {view === 'how' && <HowItWorks />}
            {view === 'privacy' && <Privacy />}
            
            {view === 'app' && !result && !isLoading && (
              <div className="space-y-8 animate-in fade-in duration-500">
                <div className="text-left mb-8 pl-2">
                  <h1 className="text-4xl md:text-5xl font-bold text-main mb-2 tracking-tight font-serif">
                    Check your readability
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
              <div className="space-y-8 animate-in slide-in-from-bottom-8 duration-700">
                <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                  <h1 className="text-3xl font-bold text-main font-serif">Analysis Results</h1>
                  <div className="flex space-x-3">
                    <button onClick={handleCopy} className="pill-control hover:bg-[var(--glass-surface)]">
                      Copy
                    </button>
                    <button onClick={handleDownload} className="pill-control hover:bg-[var(--glass-surface)]">
                      Download
                    </button>
                    <button onClick={() => clear()} className="btn-primary">
                      Analyze Again
                    </button>
                  </div>
                </div>

                {/* Mobile orders: ScoreCard goes first per responsive requirement */}
                <ScoreCard 
                  summary={result.summary} 
                  genreCheck={result.genreCheck} 
                  scoreChange={scoreChange}
                />
                
                <StatsGrid stats={result.stats} summary={result.summary} />

                <div className="py-4">
                  <HardestSentencesList 
                    sentences={result.hardestSentences} 
                    onSentenceClick={setActiveRank}
                  />
                </div>
                
                <div className="pb-10">
                  <HighlightedText 
                    originalText={lastAnalyzedText} 
                    hardestSentences={result.hardestSentences}
                    activeRank={activeRank}
                  />
                </div>
              </div>
            )}
            
          </div>
        </main>
      </div>
    </div>
  );
}

export default App;
