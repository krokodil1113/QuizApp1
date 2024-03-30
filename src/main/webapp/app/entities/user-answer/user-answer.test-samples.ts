import { IUserAnswer, NewUserAnswer } from './user-answer.model';

export const sampleWithRequiredData: IUserAnswer = {
  id: 22626,
};

export const sampleWithPartialData: IUserAnswer = {
  id: 224,
};

export const sampleWithFullData: IUserAnswer = {
  id: 9336,
  customAnswerText: 'around',
};

export const sampleWithNewData: NewUserAnswer = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
