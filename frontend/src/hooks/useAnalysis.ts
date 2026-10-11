import { useState, useCallback } from 'react';
import { AnalyzeRequest, AnalyzeResponse, AnalyzeOptions } from '../types/api';
import { analyzeText, ApiClientError } from '../services/apiClient';

interface AnalysisState {
  isLoading: boolean;
  isColdStart: boolean;
  error: string | null;
  result: AnalyzeResponse | null;
  scoreChange: number | null;
}

export const useAnalysis = () => {
  const [state, setState] = useState<AnalysisState>({
    isLoading: false,
    isColdStart: false,
    error: null,
    result: null,
    scoreChange: null,
  });

  const analyze = useCallback(async (text: string, options: AnalyzeOptions) => {
    setState(prev => ({ ...prev, isLoading: true, error: null, isColdStart: false }));
    
    // Timer to show cold start warning if > 3 seconds
    const coldStartTimer = setTimeout(() => {
      setState(prev => prev.isLoading ? { ...prev, isColdStart: true } : prev);
    }, 3000);

    try {
      const request: AnalyzeRequest = { text, options };
      
      // Wait for both the API call and the minimum 2.5s visual loading timer
      const [result] = await Promise.all([
        analyzeText(request),
        new Promise(resolve => setTimeout(resolve, 2500))
      ]);
      
      clearTimeout(coldStartTimer);
      
      setState(prev => {
        let scoreChange = null;
        if (prev.result) {
          scoreChange = result.summary.fleschReadingEase - prev.result.summary.fleschReadingEase;
          // round to 1 decimal
          scoreChange = Math.round(scoreChange * 10) / 10;
        }
        return {
          isLoading: false,
          isColdStart: false,
          error: null,
          result,
          scoreChange,
        };
      });
    } catch (err) {
      clearTimeout(coldStartTimer);
      let errorMsg = 'An unexpected error occurred.';
      if (err instanceof ApiClientError) {
        errorMsg = err.message;
      }
      setState(prev => ({
        ...prev,
        isLoading: false,
        isColdStart: false,
        error: errorMsg,
      }));
    }
  }, []);

  const clear = useCallback(() => {
    setState({
      isLoading: false,
      isColdStart: false,
      error: null,
      result: null,
      scoreChange: null,
    });
  }, []);

  return { ...state, analyze, clear };
};
