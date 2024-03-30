import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../quiz-analytics.test-samples';

import { QuizAnalyticsFormService } from './quiz-analytics-form.service';

describe('QuizAnalytics Form Service', () => {
  let service: QuizAnalyticsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuizAnalyticsFormService);
  });

  describe('Service methods', () => {
    describe('createQuizAnalyticsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQuizAnalyticsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            totalAttempts: expect.any(Object),
            averageScore: expect.any(Object),
            completionRate: expect.any(Object),
          }),
        );
      });

      it('passing IQuizAnalytics should create a new form with FormGroup', () => {
        const formGroup = service.createQuizAnalyticsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            totalAttempts: expect.any(Object),
            averageScore: expect.any(Object),
            completionRate: expect.any(Object),
          }),
        );
      });
    });

    describe('getQuizAnalytics', () => {
      it('should return NewQuizAnalytics for default QuizAnalytics initial value', () => {
        const formGroup = service.createQuizAnalyticsFormGroup(sampleWithNewData);

        const quizAnalytics = service.getQuizAnalytics(formGroup) as any;

        expect(quizAnalytics).toMatchObject(sampleWithNewData);
      });

      it('should return NewQuizAnalytics for empty QuizAnalytics initial value', () => {
        const formGroup = service.createQuizAnalyticsFormGroup();

        const quizAnalytics = service.getQuizAnalytics(formGroup) as any;

        expect(quizAnalytics).toMatchObject({});
      });

      it('should return IQuizAnalytics', () => {
        const formGroup = service.createQuizAnalyticsFormGroup(sampleWithRequiredData);

        const quizAnalytics = service.getQuizAnalytics(formGroup) as any;

        expect(quizAnalytics).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQuizAnalytics should not enable id FormControl', () => {
        const formGroup = service.createQuizAnalyticsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQuizAnalytics should disable id FormControl', () => {
        const formGroup = service.createQuizAnalyticsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
