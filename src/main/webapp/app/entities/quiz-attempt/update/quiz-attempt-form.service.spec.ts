import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../quiz-attempt.test-samples';

import { QuizAttemptFormService } from './quiz-attempt-form.service';

describe('QuizAttempt Form Service', () => {
  let service: QuizAttemptFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuizAttemptFormService);
  });

  describe('Service methods', () => {
    describe('createQuizAttemptFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQuizAttemptFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            score: expect.any(Object),
            quiz: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing IQuizAttempt should create a new form with FormGroup', () => {
        const formGroup = service.createQuizAttemptFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            score: expect.any(Object),
            quiz: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getQuizAttempt', () => {
      it('should return NewQuizAttempt for default QuizAttempt initial value', () => {
        const formGroup = service.createQuizAttemptFormGroup(sampleWithNewData);

        const quizAttempt = service.getQuizAttempt(formGroup) as any;

        expect(quizAttempt).toMatchObject(sampleWithNewData);
      });

      it('should return NewQuizAttempt for empty QuizAttempt initial value', () => {
        const formGroup = service.createQuizAttemptFormGroup();

        const quizAttempt = service.getQuizAttempt(formGroup) as any;

        expect(quizAttempt).toMatchObject({});
      });

      it('should return IQuizAttempt', () => {
        const formGroup = service.createQuizAttemptFormGroup(sampleWithRequiredData);

        const quizAttempt = service.getQuizAttempt(formGroup) as any;

        expect(quizAttempt).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQuizAttempt should not enable id FormControl', () => {
        const formGroup = service.createQuizAttemptFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQuizAttempt should disable id FormControl', () => {
        const formGroup = service.createQuizAttemptFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
