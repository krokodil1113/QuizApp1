import dayjs from 'dayjs/esm';

import { IFeedback, NewFeedback } from './feedback.model';

export const sampleWithRequiredData: IFeedback = {
  id: 3157,
  content: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: IFeedback = {
  id: 13405,
  content: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IFeedback = {
  id: 18462,
  content: '../fake-data/blob/hipster.txt',
  createDate: dayjs('2024-03-26'),
};

export const sampleWithNewData: NewFeedback = {
  content: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
