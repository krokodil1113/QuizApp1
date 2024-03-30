import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUserStatistics, NewUserStatistics } from '../user-statistics.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserStatistics for edit and NewUserStatisticsFormGroupInput for create.
 */
type UserStatisticsFormGroupInput = IUserStatistics | PartialWithRequiredKeyOf<NewUserStatistics>;

type UserStatisticsFormDefaults = Pick<NewUserStatistics, 'id'>;

type UserStatisticsFormGroupContent = {
  id: FormControl<IUserStatistics['id'] | NewUserStatistics['id']>;
  totalQuizzesTaken: FormControl<IUserStatistics['totalQuizzesTaken']>;
  totalScore: FormControl<IUserStatistics['totalScore']>;
  averageScore: FormControl<IUserStatistics['averageScore']>;
  quizUser: FormControl<IUserStatistics['quizUser']>;
};

export type UserStatisticsFormGroup = FormGroup<UserStatisticsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserStatisticsFormService {
  createUserStatisticsFormGroup(userStatistics: UserStatisticsFormGroupInput = { id: null }): UserStatisticsFormGroup {
    const userStatisticsRawValue = {
      ...this.getFormDefaults(),
      ...userStatistics,
    };
    return new FormGroup<UserStatisticsFormGroupContent>({
      id: new FormControl(
        { value: userStatisticsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      totalQuizzesTaken: new FormControl(userStatisticsRawValue.totalQuizzesTaken),
      totalScore: new FormControl(userStatisticsRawValue.totalScore),
      averageScore: new FormControl(userStatisticsRawValue.averageScore),
      quizUser: new FormControl(userStatisticsRawValue.quizUser),
    });
  }

  getUserStatistics(form: UserStatisticsFormGroup): IUserStatistics | NewUserStatistics {
    return form.getRawValue() as IUserStatistics | NewUserStatistics;
  }

  resetForm(form: UserStatisticsFormGroup, userStatistics: UserStatisticsFormGroupInput): void {
    const userStatisticsRawValue = { ...this.getFormDefaults(), ...userStatistics };
    form.reset(
      {
        ...userStatisticsRawValue,
        id: { value: userStatisticsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserStatisticsFormDefaults {
    return {
      id: null,
    };
  }
}
