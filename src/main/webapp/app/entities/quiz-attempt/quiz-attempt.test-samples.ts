import dayjs from 'dayjs/esm';

import { IQuizAttempt, NewQuizAttempt } from './quiz-attempt.model';

export const sampleWithRequiredData: IQuizAttempt = {
  id: 21120,
};

export const sampleWithPartialData: IQuizAttempt = {
  id: 1201,
};

export const sampleWithFullData: IQuizAttempt = {
  id: 16070,
  startTime: dayjs('2024-03-26T20:13'),
  endTime: dayjs('2024-03-26T09:53'),
  score: 22161,
};

export const sampleWithNewData: NewQuizAttempt = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
