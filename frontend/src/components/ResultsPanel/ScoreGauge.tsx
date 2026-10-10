import { useEffect, useState } from 'react';

// Custom hook for the 800ms count up
function useCountUp(end: number, durationMs: number = 800) {
  const [count, setCount] = useState(0);
  
  useEffect(() => {
    const prefersReduced = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    if (prefersReduced || end === 0) {
      setCount(end);
      return;
    }
    
    let startTimestamp: number;
    let animationFrameId: number;
    
    const step = (timestamp: number) => {
      if (!startTimestamp) startTimestamp = timestamp;
      const progress = Math.min((timestamp - startTimestamp) / durationMs, 1);
      // easeOutQuad
      const easeOut = 1 - (1 - progress) * (1 - progress);
      setCount(Math.round(easeOut * end));
      
      if (progress < 1) {
        animationFrameId = requestAnimationFrame(step);
      } else {
        setCount(Math.round(end)); // Ensure exact final value
      }
    };
    
    animationFrameId = requestAnimationFrame(step);
    return () => cancelAnimationFrame(animationFrameId);
  }, [end, durationMs]);
  
  return count;
}

interface ScoreGaugeProps {
  score: number;
}

export const ScoreGauge = ({ score }: ScoreGaugeProps) => {
  const displayScore = useCountUp(score);
  
  // Determine color based on score
  let colorClass = 'text-hard'; // < 50
  if (score >= 70) colorClass = 'text-easy';
  else if (score >= 50) colorClass = 'text-medium';

  // SVG Gauge Calculations
  const radius = 40;
  const circumference = Math.PI * radius; // Half circle
  // Mapping score (0-100) to dashoffset (circumference to 0)
  const clampedScore = Math.max(0, Math.min(100, displayScore));
  const dashoffset = circumference - (clampedScore / 100) * circumference;

  return (
    <div className="relative flex flex-col items-center justify-center w-32 h-20 overflow-hidden">
      <svg 
        className="absolute top-0 w-32 h-32" 
        viewBox="0 0 100 100" 
        preserveAspectRatio="xMidYMid meet"
      >
        {/* Background track */}
        <path
          d="M 10 50 A 40 40 0 0 1 90 50"
          fill="none"
          stroke="var(--brand-border)"
          strokeWidth="8"
          strokeLinecap="round"
        />
        {/* Progress arc */}
        <path
          d="M 10 50 A 40 40 0 0 1 90 50"
          fill="none"
          className={colorClass}
          stroke="currentColor"
          strokeWidth="8"
          strokeLinecap="round"
          strokeDasharray={circumference}
          strokeDashoffset={dashoffset}
          style={{ transition: 'stroke-dashoffset 0.1s linear' }}
        />
      </svg>
      
      <div className="absolute bottom-0 flex items-baseline space-x-1">
        <span className="text-4xl font-bold text-main font-sans">
          {displayScore}
        </span>
        <span className="text-sm font-medium text-muted">
          / 100
        </span>
      </div>
    </div>
  );
};
