import { IAnswer, NewAnswer } from './answer.model';

export const sampleWithRequiredData: IAnswer = {
  id: 14857,
  text: 'next nor',
  isCorrect: true,
};

export const sampleWithPartialData: IAnswer = {
  id: 31821,
  text: 'athlete if under',
  isCorrect: false,
};

export const sampleWithFullData: IAnswer = {
  id: 16388,
  text: 'lest',
  isCorrect: true,
};

export const sampleWithNewData: NewAnswer = {
  text: 'consequently oof',
  isCorrect: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
