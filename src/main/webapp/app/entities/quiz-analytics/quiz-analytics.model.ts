export interface IQuizAnalytics {
  id: number;
  totalAttempts?: number | null;
  averageScore?: number | null;
  completionRate?: number | null;
}

export type NewQuizAnalytics = Omit<IQuizAnalytics, 'id'> & { id: null };
