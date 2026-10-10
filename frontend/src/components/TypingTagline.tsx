import { useEffect, useState } from 'react';

export const TypingTagline = ({ trigger }: { trigger?: any }) => {
  const fullText = "See where readers stumble.";
  const [displayed, setDisplayed] = useState('');
  const [highlighted, setHighlighted] = useState(false);

  useEffect(() => {
    setDisplayed('');
    setHighlighted(false);
    
    const prefersReduced = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    if (prefersReduced) {
      setDisplayed(fullText);
      setHighlighted(true);
      return;
    }

    let i = 0;
    const interval = setInterval(() => {
      setDisplayed(fullText.substring(0, i + 1));
      i++;
      if (i === fullText.length) {
        clearInterval(interval);
        setTimeout(() => setHighlighted(true), 300);
      }
    }, 45); // Typing speed
    
    return () => clearInterval(interval);
  }, [trigger]);

  const baseText = displayed.replace("stumble.", "");
  const endWord = displayed.includes("stumble.") ? "stumble." : displayed.substring(baseText.length);

  return (
    <p className="text-lg text-muted h-8 font-sans">
      {baseText}
      <span className={`transition-all duration-700 ${highlighted ? 'highlight-rank-1 rounded-sm' : ''}`}>
        {endWord}
      </span>
    </p>
  );
};
