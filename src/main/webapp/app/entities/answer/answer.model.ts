import { IQuestion } from 'app/entities/question/question.model';

export interface IAnswer {
  id: number;
  text?: string | null;
  isCorrect?: boolean | null;
  question?: Pick<IQuestion, 'id'> | null;
}

export type NewAnswer = Omit<IAnswer, 'id'> & { id: null };
