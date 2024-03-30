import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUserAnswer, NewUserAnswer } from '../user-answer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserAnswer for edit and NewUserAnswerFormGroupInput for create.
 */
type UserAnswerFormGroupInput = IUserAnswer | PartialWithRequiredKeyOf<NewUserAnswer>;

type UserAnswerFormDefaults = Pick<NewUserAnswer, 'id'>;

type UserAnswerFormGroupContent = {
  id: FormControl<IUserAnswer['id'] | NewUserAnswer['id']>;
  customAnswerText: FormControl<IUserAnswer['customAnswerText']>;
  attempt: FormControl<IUserAnswer['attempt']>;
  question: FormControl<IUserAnswer['question']>;
  selectedAnswer: FormControl<IUserAnswer['selectedAnswer']>;
};

export type UserAnswerFormGroup = FormGroup<UserAnswerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserAnswerFormService {
  createUserAnswerFormGroup(userAnswer: UserAnswerFormGroupInput = { id: null }): UserAnswerFormGroup {
    const userAnswerRawValue = {
      ...this.getFormDefaults(),
      ...userAnswer,
    };
    return new FormGroup<UserAnswerFormGroupContent>({
      id: new FormControl(
        { value: userAnswerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      customAnswerText: new FormControl(userAnswerRawValue.customAnswerText),
      attempt: new FormControl(userAnswerRawValue.attempt),
      question: new FormControl(userAnswerRawValue.question),
      selectedAnswer: new FormControl(userAnswerRawValue.selectedAnswer),
    });
  }

  getUserAnswer(form: UserAnswerFormGroup): IUserAnswer | NewUserAnswer {
    return form.getRawValue() as IUserAnswer | NewUserAnswer;
  }

  resetForm(form: UserAnswerFormGroup, userAnswer: UserAnswerFormGroupInput): void {
    const userAnswerRawValue = { ...this.getFormDefaults(), ...userAnswer };
    form.reset(
      {
        ...userAnswerRawValue,
        id: { value: userAnswerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserAnswerFormDefaults {
    return {
      id: null,
    };
  }
}
