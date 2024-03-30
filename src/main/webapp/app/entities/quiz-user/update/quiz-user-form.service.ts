import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IQuizUser, NewQuizUser } from '../quiz-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuizUser for edit and NewQuizUserFormGroupInput for create.
 */
type QuizUserFormGroupInput = IQuizUser | PartialWithRequiredKeyOf<NewQuizUser>;

type QuizUserFormDefaults = Pick<NewQuizUser, 'id'>;

type QuizUserFormGroupContent = {
  id: FormControl<IQuizUser['id'] | NewQuizUser['id']>;
  nickname: FormControl<IQuizUser['nickname']>;
  bio: FormControl<IQuizUser['bio']>;
  user: FormControl<IQuizUser['user']>;
};

export type QuizUserFormGroup = FormGroup<QuizUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QuizUserFormService {
  createQuizUserFormGroup(quizUser: QuizUserFormGroupInput = { id: null }): QuizUserFormGroup {
    const quizUserRawValue = {
      ...this.getFormDefaults(),
      ...quizUser,
    };
    return new FormGroup<QuizUserFormGroupContent>({
      id: new FormControl(
        { value: quizUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nickname: new FormControl(quizUserRawValue.nickname),
      bio: new FormControl(quizUserRawValue.bio),
      user: new FormControl(quizUserRawValue.user),
    });
  }

  getQuizUser(form: QuizUserFormGroup): IQuizUser | NewQuizUser {
    return form.getRawValue() as IQuizUser | NewQuizUser;
  }

  resetForm(form: QuizUserFormGroup, quizUser: QuizUserFormGroupInput): void {
    const quizUserRawValue = { ...this.getFormDefaults(), ...quizUser };
    form.reset(
      {
        ...quizUserRawValue,
        id: { value: quizUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): QuizUserFormDefaults {
    return {
      id: null,
    };
  }
}
