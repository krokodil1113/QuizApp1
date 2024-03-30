import { IQuestion, NewQuestion } from './question.model';

export const sampleWithRequiredData: IQuestion = {
  id: 5941,
  text: 'bland likewise joyously',
};

export const sampleWithPartialData: IQuestion = {
  id: 17782,
  text: 'validate',
};

export const sampleWithFullData: IQuestion = {
  id: 26557,
  text: 'although bah scold',
};

export const sampleWithNewData: NewQuestion = {
  text: 'longingly sparkling duh',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
