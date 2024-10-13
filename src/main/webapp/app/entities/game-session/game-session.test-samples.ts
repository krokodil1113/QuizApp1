import dayjs from 'dayjs/esm';

import { IGameSession, NewGameSession } from './game-session.model';

export const sampleWithRequiredData: IGameSession = {
  id: 12214,
};

export const sampleWithPartialData: IGameSession = {
  id: 32102,
  currentQuestionIndex: 30112,
};

export const sampleWithFullData: IGameSession = {
  id: 28811,
  startTime: dayjs('2024-10-11T16:55'),
  endTime: dayjs('2024-10-11T15:07'),
  status: 'whoever drat',
  currentQuestionIndex: 6411,
};

export const sampleWithNewData: NewGameSession = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
