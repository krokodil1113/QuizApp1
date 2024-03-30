import { IQuiz } from 'app/entities/quiz/quiz.model';

export interface IQuestion {
  id: number;
  text?: string | null;
  quiz?: Pick<IQuiz, 'id'> | null;
}

export type NewQuestion = Omit<IQuestion, 'id'> & { id: null };
