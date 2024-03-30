import { IQuizUser, NewQuizUser } from './quiz-user.model';

export const sampleWithRequiredData: IQuizUser = {
  id: 28287,
};

export const sampleWithPartialData: IQuizUser = {
  id: 27432,
  nickname: 'wild',
};

export const sampleWithFullData: IQuizUser = {
  id: 21937,
  nickname: 'vastly lopsided',
  bio: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewQuizUser = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
