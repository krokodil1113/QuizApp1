import { IQuizAnalytics, NewQuizAnalytics } from './quiz-analytics.model';

export const sampleWithRequiredData: IQuizAnalytics = {
  id: 5529,
};

export const sampleWithPartialData: IQuizAnalytics = {
  id: 5290,
  averageScore: 24466.07,
  completionRate: 25440.83,
};

export const sampleWithFullData: IQuizAnalytics = {
  id: 27283,
  totalAttempts: 12993,
  averageScore: 1185.12,
  completionRate: 15356.49,
};

export const sampleWithNewData: NewQuizAnalytics = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
