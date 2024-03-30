import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../user-statistics.test-samples';

import { UserStatisticsFormService } from './user-statistics-form.service';

describe('UserStatistics Form Service', () => {
  let service: UserStatisticsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserStatisticsFormService);
  });

  describe('Service methods', () => {
    describe('createUserStatisticsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserStatisticsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            totalQuizzesTaken: expect.any(Object),
            totalScore: expect.any(Object),
            averageScore: expect.any(Object),
            quizUser: expect.any(Object),
          }),
        );
      });

      it('passing IUserStatistics should create a new form with FormGroup', () => {
        const formGroup = service.createUserStatisticsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            totalQuizzesTaken: expect.any(Object),
            totalScore: expect.any(Object),
            averageScore: expect.any(Object),
            quizUser: expect.any(Object),
          }),
        );
      });
    });

    describe('getUserStatistics', () => {
      it('should return NewUserStatistics for default UserStatistics initial value', () => {
        const formGroup = service.createUserStatisticsFormGroup(sampleWithNewData);

        const userStatistics = service.getUserStatistics(formGroup) as any;

        expect(userStatistics).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserStatistics for empty UserStatistics initial value', () => {
        const formGroup = service.createUserStatisticsFormGroup();

        const userStatistics = service.getUserStatistics(formGroup) as any;

        expect(userStatistics).toMatchObject({});
      });

      it('should return IUserStatistics', () => {
        const formGroup = service.createUserStatisticsFormGroup(sampleWithRequiredData);

        const userStatistics = service.getUserStatistics(formGroup) as any;

        expect(userStatistics).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserStatistics should not enable id FormControl', () => {
        const formGroup = service.createUserStatisticsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserStatistics should disable id FormControl', () => {
        const formGroup = service.createUserStatisticsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
