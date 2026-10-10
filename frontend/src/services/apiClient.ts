import { AnalyzeRequest, AnalyzeResponse, ApiError } from '../types/api';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';

export class ApiClientError extends Error {
  public code: string;

  constructor(message: string, code: string = 'SERVER_ERROR') {
    super(message);
    this.code = code;
    this.name = 'ApiClientError';
  }
}

export const analyzeText = async (request: AnalyzeRequest): Promise<AnalyzeResponse> => {
  try {
    const response = await fetch(`${API_BASE_URL}/analyze`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(request),
    });

    if (!response.ok) {
      let errorData: ApiError;
      try {
        errorData = await response.json();
      } catch (e) {
        throw new ApiClientError('An unexpected server error occurred.', 'SERVER_ERROR');
      }
      throw new ApiClientError(errorData.message, errorData.error);
    }

    return await response.json();
  } catch (error) {
    if (error instanceof ApiClientError) {
      throw error;
    }
    // Handle network errors (like CORS or offline)
    throw new ApiClientError(
      'Unable to connect to the server. Please check your internet connection and try again.',
      'NETWORK_ERROR'
    );
  }
};

export const checkHealth = async (): Promise<boolean> => {
  try {
    const response = await fetch(`${API_BASE_URL}/health`);
    return response.ok;
  } catch {
    return false;
  }
};
