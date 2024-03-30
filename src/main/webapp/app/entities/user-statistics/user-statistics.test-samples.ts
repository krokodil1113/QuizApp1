import { IUserStatistics, NewUserStatistics } from './user-statistics.model';

export const sampleWithRequiredData: IUserStatistics = {
  id: 25514,
};

export const sampleWithPartialData: IUserStatistics = {
  id: 2969,
  totalQuizzesTaken: 26302,
  totalScore: 16873,
};

export const sampleWithFullData: IUserStatistics = {
  id: 30212,
  totalQuizzesTaken: 18166,
  totalScore: 17258,
  averageScore: 5732.73,
};

export const sampleWithNewData: NewUserStatistics = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
