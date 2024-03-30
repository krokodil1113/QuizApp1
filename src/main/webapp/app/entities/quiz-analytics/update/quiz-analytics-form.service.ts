import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IQuizAnalytics, NewQuizAnalytics } from '../quiz-analytics.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuizAnalytics for edit and NewQuizAnalyticsFormGroupInput for create.
 */
type QuizAnalyticsFormGroupInput = IQuizAnalytics | PartialWithRequiredKeyOf<NewQuizAnalytics>;

type QuizAnalyticsFormDefaults = Pick<NewQuizAnalytics, 'id'>;

type QuizAnalyticsFormGroupContent = {
  id: FormControl<IQuizAnalytics['id'] | NewQuizAnalytics['id']>;
  totalAttempts: FormControl<IQuizAnalytics['totalAttempts']>;
  averageScore: FormControl<IQuizAnalytics['averageScore']>;
  completionRate: FormControl<IQuizAnalytics['completionRate']>;
};

export type QuizAnalyticsFormGroup = FormGroup<QuizAnalyticsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QuizAnalyticsFormService {
  createQuizAnalyticsFormGroup(quizAnalytics: QuizAnalyticsFormGroupInput = { id: null }): QuizAnalyticsFormGroup {
    const quizAnalyticsRawValue = {
      ...this.getFormDefaults(),
      ...quizAnalytics,
    };
    return new FormGroup<QuizAnalyticsFormGroupContent>({
      id: new FormControl(
        { value: quizAnalyticsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      totalAttempts: new FormControl(quizAnalyticsRawValue.totalAttempts),
      averageScore: new FormControl(quizAnalyticsRawValue.averageScore),
      completionRate: new FormControl(quizAnalyticsRawValue.completionRate),
    });
  }

  getQuizAnalytics(form: QuizAnalyticsFormGroup): IQuizAnalytics | NewQuizAnalytics {
    return form.getRawValue() as IQuizAnalytics | NewQuizAnalytics;
  }

  resetForm(form: QuizAnalyticsFormGroup, quizAnalytics: QuizAnalyticsFormGroupInput): void {
    const quizAnalyticsRawValue = { ...this.getFormDefaults(), ...quizAnalytics };
    form.reset(
      {
        ...quizAnalyticsRawValue,
        id: { value: quizAnalyticsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): QuizAnalyticsFormDefaults {
    return {
      id: null,
    };
  }
}
