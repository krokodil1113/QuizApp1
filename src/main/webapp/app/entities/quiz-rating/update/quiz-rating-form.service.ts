import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IQuizRating, NewQuizRating } from '../quiz-rating.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuizRating for edit and NewQuizRatingFormGroupInput for create.
 */
type QuizRatingFormGroupInput = IQuizRating | PartialWithRequiredKeyOf<NewQuizRating>;

type QuizRatingFormDefaults = Pick<NewQuizRating, 'id'>;

type QuizRatingFormGroupContent = {
  id: FormControl<IQuizRating['id'] | NewQuizRating['id']>;
  rating: FormControl<IQuizRating['rating']>;
  comment: FormControl<IQuizRating['comment']>;
  quiz: FormControl<IQuizRating['quiz']>;
  user: FormControl<IQuizRating['user']>;
};

export type QuizRatingFormGroup = FormGroup<QuizRatingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QuizRatingFormService {
  createQuizRatingFormGroup(quizRating: QuizRatingFormGroupInput = { id: null }): QuizRatingFormGroup {
    const quizRatingRawValue = {
      ...this.getFormDefaults(),
      ...quizRating,
    };
    return new FormGroup<QuizRatingFormGroupContent>({
      id: new FormControl(
        { value: quizRatingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      rating: new FormControl(quizRatingRawValue.rating),
      comment: new FormControl(quizRatingRawValue.comment),
      quiz: new FormControl(quizRatingRawValue.quiz),
      user: new FormControl(quizRatingRawValue.user),
    });
  }

  getQuizRating(form: QuizRatingFormGroup): IQuizRating | NewQuizRating {
    return form.getRawValue() as IQuizRating | NewQuizRating;
  }

  resetForm(form: QuizRatingFormGroup, quizRating: QuizRatingFormGroupInput): void {
    const quizRatingRawValue = { ...this.getFormDefaults(), ...quizRating };
    form.reset(
      {
        ...quizRatingRawValue,
        id: { value: quizRatingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): QuizRatingFormDefaults {
    return {
      id: null,
    };
  }
}
