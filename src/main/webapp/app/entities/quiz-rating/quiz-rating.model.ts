import { IQuiz } from 'app/entities/quiz/quiz.model';
import { IQuizUser } from 'app/entities/quiz-user/quiz-user.model';

export interface IQuizRating {
  id: number;
  rating?: number | null;
  comment?: string | null;
  quiz?: Pick<IQuiz, 'id'> | null;
  user?: Pick<IQuizUser, 'id'> | null;
}

export type NewQuizRating = Omit<IQuizRating, 'id'> & { id: null };
