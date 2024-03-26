import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 13273,
  login: 'P~K5M@75R\\wL\\@IjEukz\\GFwQ\\6EZPp',
};

export const sampleWithPartialData: IUser = {
  id: 3191,
  login: '31TQ7',
};

export const sampleWithFullData: IUser = {
  id: 5554,
  login: 'quW@o8g\\[x3UOm\\5G',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
