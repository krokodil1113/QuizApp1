import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../quiz-rating.test-samples';

import { QuizRatingFormService } from './quiz-rating-form.service';

describe('QuizRating Form Service', () => {
  let service: QuizRatingFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuizRatingFormService);
  });

  describe('Service methods', () => {
    describe('createQuizRatingFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQuizRatingFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rating: expect.any(Object),
            comment: expect.any(Object),
            quiz: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing IQuizRating should create a new form with FormGroup', () => {
        const formGroup = service.createQuizRatingFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rating: expect.any(Object),
            comment: expect.any(Object),
            quiz: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getQuizRating', () => {
      it('should return NewQuizRating for default QuizRating initial value', () => {
        const formGroup = service.createQuizRatingFormGroup(sampleWithNewData);

        const quizRating = service.getQuizRating(formGroup) as any;

        expect(quizRating).toMatchObject(sampleWithNewData);
      });

      it('should return NewQuizRating for empty QuizRating initial value', () => {
        const formGroup = service.createQuizRatingFormGroup();

        const quizRating = service.getQuizRating(formGroup) as any;

        expect(quizRating).toMatchObject({});
      });

      it('should return IQuizRating', () => {
        const formGroup = service.createQuizRatingFormGroup(sampleWithRequiredData);

        const quizRating = service.getQuizRating(formGroup) as any;

        expect(quizRating).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQuizRating should not enable id FormControl', () => {
        const formGroup = service.createQuizRatingFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQuizRating should disable id FormControl', () => {
        const formGroup = service.createQuizRatingFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
