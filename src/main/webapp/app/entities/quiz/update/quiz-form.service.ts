import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IQuiz, NewQuiz } from '../quiz.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuiz for edit and NewQuizFormGroupInput for create.
 */
type QuizFormGroupInput = IQuiz | PartialWithRequiredKeyOf<NewQuiz>;

type QuizFormDefaults = Pick<NewQuiz, 'id' | 'isPublished'>;

type QuizFormGroupContent = {
  id: FormControl<IQuiz['id'] | NewQuiz['id']>;
  title: FormControl<IQuiz['title']>;
  description: FormControl<IQuiz['description']>;
  difficultyLevel: FormControl<IQuiz['difficultyLevel']>;
  isPublished: FormControl<IQuiz['isPublished']>;
  publishDate: FormControl<IQuiz['publishDate']>;
  quizAnalytics: FormControl<IQuiz['quizAnalytics']>;
  creator: FormControl<IQuiz['creator']>;
  category: FormControl<IQuiz['category']>;
};

export type QuizFormGroup = FormGroup<QuizFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QuizFormService {
  createQuizFormGroup(quiz: QuizFormGroupInput = { id: null }): QuizFormGroup {
    const quizRawValue = {
      ...this.getFormDefaults(),
      ...quiz,
    };
    return new FormGroup<QuizFormGroupContent>({
      id: new FormControl(
        { value: quizRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(quizRawValue.title, {
        validators: [Validators.required],
      }),
      description: new FormControl(quizRawValue.description),
      difficultyLevel: new FormControl(quizRawValue.difficultyLevel),
      isPublished: new FormControl(quizRawValue.isPublished, {
        validators: [Validators.required],
      }),
      publishDate: new FormControl(quizRawValue.publishDate),
      quizAnalytics: new FormControl(quizRawValue.quizAnalytics),
      creator: new FormControl(quizRawValue.creator),
      category: new FormControl(quizRawValue.category),
    });
  }

  getQuiz(form: QuizFormGroup): IQuiz | NewQuiz {
    return form.getRawValue() as IQuiz | NewQuiz;
  }

  resetForm(form: QuizFormGroup, quiz: QuizFormGroupInput): void {
    const quizRawValue = { ...this.getFormDefaults(), ...quiz };
    form.reset(
      {
        ...quizRawValue,
        id: { value: quizRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): QuizFormDefaults {
    return {
      id: null,
      isPublished: false,
    };
  }
}
