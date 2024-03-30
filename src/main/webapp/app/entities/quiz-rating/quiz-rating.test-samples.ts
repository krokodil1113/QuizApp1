import { IQuizRating, NewQuizRating } from './quiz-rating.model';

export const sampleWithRequiredData: IQuizRating = {
  id: 21288,
};

export const sampleWithPartialData: IQuizRating = {
  id: 31099,
  comment: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IQuizRating = {
  id: 3306,
  rating: 23990,
  comment: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewQuizRating = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
