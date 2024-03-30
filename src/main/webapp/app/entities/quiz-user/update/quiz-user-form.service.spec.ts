import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../quiz-user.test-samples';

import { QuizUserFormService } from './quiz-user-form.service';

describe('QuizUser Form Service', () => {
  let service: QuizUserFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuizUserFormService);
  });

  describe('Service methods', () => {
    describe('createQuizUserFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQuizUserFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nickname: expect.any(Object),
            bio: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing IQuizUser should create a new form with FormGroup', () => {
        const formGroup = service.createQuizUserFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nickname: expect.any(Object),
            bio: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getQuizUser', () => {
      it('should return NewQuizUser for default QuizUser initial value', () => {
        const formGroup = service.createQuizUserFormGroup(sampleWithNewData);

        const quizUser = service.getQuizUser(formGroup) as any;

        expect(quizUser).toMatchObject(sampleWithNewData);
      });

      it('should return NewQuizUser for empty QuizUser initial value', () => {
        const formGroup = service.createQuizUserFormGroup();

        const quizUser = service.getQuizUser(formGroup) as any;

        expect(quizUser).toMatchObject({});
      });

      it('should return IQuizUser', () => {
        const formGroup = service.createQuizUserFormGroup(sampleWithRequiredData);

        const quizUser = service.getQuizUser(formGroup) as any;

        expect(quizUser).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQuizUser should not enable id FormControl', () => {
        const formGroup = service.createQuizUserFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQuizUser should disable id FormControl', () => {
        const formGroup = service.createQuizUserFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
