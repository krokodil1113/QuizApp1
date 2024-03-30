import dayjs from 'dayjs/esm';
import { IQuizAnalytics } from 'app/entities/quiz-analytics/quiz-analytics.model';
import { IQuizUser } from 'app/entities/quiz-user/quiz-user.model';
import { ICategory } from 'app/entities/category/category.model';

export interface IQuiz {
  id: number;
  title?: string | null;
  description?: string | null;
  difficultyLevel?: number | null;
  isPublished?: boolean | null;
  publishDate?: dayjs.Dayjs | null;
  quizAnalytics?: Pick<IQuizAnalytics, 'id'> | null;
  creator?: Pick<IQuizUser, 'id'> | null;
  category?: Pick<ICategory, 'id'> | null;
}

export type NewQuiz = Omit<IQuiz, 'id'> & { id: null };
