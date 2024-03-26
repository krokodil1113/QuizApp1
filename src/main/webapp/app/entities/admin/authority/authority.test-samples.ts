import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '12d1a285-bd0b-462a-b4ff-d9d8ffc1a7e8',
};

export const sampleWithPartialData: IAuthority = {
  name: 'defa53cb-a808-49a6-bf14-b921d70861a2',
};

export const sampleWithFullData: IAuthority = {
  name: '53077e5b-de38-4271-80ba-39cc1fccc87b',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
