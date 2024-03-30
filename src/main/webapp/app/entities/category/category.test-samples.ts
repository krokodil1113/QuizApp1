import { ICategory, NewCategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: 11461,
  name: 'upright',
};

export const sampleWithPartialData: ICategory = {
  id: 30927,
  name: 'than kindly',
  description: 'ah conduct per',
};

export const sampleWithFullData: ICategory = {
  id: 21626,
  name: 'softly',
  description: 'violent',
};

export const sampleWithNewData: NewCategory = {
  name: 'sulfur phew',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
