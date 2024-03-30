import dayjs from 'dayjs/esm';
import { IQuiz } from 'app/entities/quiz/quiz.model';
import { IQuizUser } from 'app/entities/quiz-user/quiz-user.model';

export interface IQuizAttempt {
  id: number;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  score?: number | null;
  quiz?: Pick<IQuiz, 'id'> | null;
  user?: Pick<IQuizUser, 'id'> | null;
}

export type NewQuizAttempt = Omit<IQuizAttempt, 'id'> & { id: null };
