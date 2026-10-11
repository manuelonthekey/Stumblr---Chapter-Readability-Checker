import { useEffect, useState } from 'react';

export const LoadingScreen = () => {
  const [progress, setProgress] = useState(0);

  useEffect(() => {
    let startTime: number;
    const duration = 2500; // Match the delay in useAnalysis

    const animateProgress = (timestamp: number) => {
      if (!startTime) startTime = timestamp;
      const elapsed = timestamp - startTime;
      
      const currentProgress = Math.min((elapsed / duration) * 100, 100);
      setProgress(currentProgress);

      if (elapsed < duration) {
        requestAnimationFrame(animateProgress);
      }
    };

    const animationId = requestAnimationFrame(animateProgress);
    return () => cancelAnimationFrame(animationId);
  }, []);

  return (
    <div className="w-full h-[60vh] min-h-[500px] flex flex-col items-center justify-center space-y-8 glass-opaque rounded-3xl animate-in fade-in duration-500 shadow-sm border border-glass">
      
      {/* CSS Spinner */}
      <div className="relative flex items-center justify-center w-24 h-24">
        <svg className="animate-spin w-20 h-20 text-[var(--brand-primary)]" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
          <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="3"></circle>
          <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
        </svg>
        <div className="absolute font-sans font-bold text-[var(--brand-primary)] text-sm">
          {Math.floor(progress)}%
        </div>
      </div>

      <div className="text-center space-y-3 max-w-sm w-full px-6">
        <h2 className="text-xl font-serif font-bold text-main">Analyzing your text</h2>
        <p className="text-sm text-muted">Calculating reading difficulty, syllables, and highlighting complex sentences...</p>
        
        {/* Progress Bar */}
        <div className="w-full h-2 bg-[var(--brand-primary)]/10 rounded-full overflow-hidden mt-6">
          <div 
            className="h-full bg-[var(--brand-primary)] rounded-full transition-all duration-75"
            style={{ width: `${progress}%` }}
          />
        </div>
      </div>
      
    </div>
  );
};
