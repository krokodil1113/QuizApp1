import dayjs from 'dayjs/esm';

export interface IGameSession {
  id: number;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  status?: string | null;
  currentQuestionIndex?: number | null;
}

export type NewGameSession = Omit<IGameSession, 'id'> & { id: null };
