import { IQuizUser } from 'app/entities/quiz-user/quiz-user.model';

export interface IUserStatistics {
  id: number;
  totalQuizzesTaken?: number | null;
  totalScore?: number | null;
  averageScore?: number | null;
  quizUser?: Pick<IQuizUser, 'id'> | null;
}

export type NewUserStatistics = Omit<IUserStatistics, 'id'> & { id: null };
