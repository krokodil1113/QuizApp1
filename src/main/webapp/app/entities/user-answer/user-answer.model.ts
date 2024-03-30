import { IQuizAttempt } from 'app/entities/quiz-attempt/quiz-attempt.model';
import { IQuestion } from 'app/entities/question/question.model';
import { IAnswer } from 'app/entities/answer/answer.model';

export interface IUserAnswer {
  id: number;
  customAnswerText?: string | null;
  attempt?: Pick<IQuizAttempt, 'id'> | null;
  question?: Pick<IQuestion, 'id'> | null;
  selectedAnswer?: Pick<IAnswer, 'id'> | null;
}

export type NewUserAnswer = Omit<IUserAnswer, 'id'> & { id: null };
