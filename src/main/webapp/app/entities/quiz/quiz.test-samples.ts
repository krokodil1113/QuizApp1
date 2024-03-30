import dayjs from 'dayjs/esm';

import { IQuiz, NewQuiz } from './quiz.model';

export const sampleWithRequiredData: IQuiz = {
  id: 18149,
  title: 'subvert unlearn phew',
  isPublished: false,
};

export const sampleWithPartialData: IQuiz = {
  id: 4531,
  title: 'abstract',
  difficultyLevel: 14698,
  isPublished: true,
  publishDate: dayjs('2024-03-26'),
};

export const sampleWithFullData: IQuiz = {
  id: 19623,
  title: 'now disavow that',
  description: '../fake-data/blob/hipster.txt',
  difficultyLevel: 21043,
  isPublished: true,
  publishDate: dayjs('2024-03-26'),
};

export const sampleWithNewData: NewQuiz = {
  title: 'an',
  isPublished: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
