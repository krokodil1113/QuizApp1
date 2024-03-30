import dayjs from 'dayjs/esm';
import { IQuizUser } from 'app/entities/quiz-user/quiz-user.model';

export interface IFeedback {
  id: number;
  content?: string | null;
  createDate?: dayjs.Dayjs | null;
  user?: Pick<IQuizUser, 'id'> | null;
}

export type NewFeedback = Omit<IFeedback, 'id'> & { id: null };
