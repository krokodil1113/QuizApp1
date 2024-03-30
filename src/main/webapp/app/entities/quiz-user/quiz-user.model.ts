import { IUser } from 'app/entities/user/user.model';

export interface IQuizUser {
  id: number;
  nickname?: string | null;
  bio?: string | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewQuizUser = Omit<IQuizUser, 'id'> & { id: null };
