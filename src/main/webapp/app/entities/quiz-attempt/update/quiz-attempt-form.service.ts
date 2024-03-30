import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IQuizAttempt, NewQuizAttempt } from '../quiz-attempt.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuizAttempt for edit and NewQuizAttemptFormGroupInput for create.
 */
type QuizAttemptFormGroupInput = IQuizAttempt | PartialWithRequiredKeyOf<NewQuizAttempt>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IQuizAttempt | NewQuizAttempt> = Omit<T, 'startTime' | 'endTime'> & {
  startTime?: string | null;
  endTime?: string | null;
};

type QuizAttemptFormRawValue = FormValueOf<IQuizAttempt>;

type NewQuizAttemptFormRawValue = FormValueOf<NewQuizAttempt>;

type QuizAttemptFormDefaults = Pick<NewQuizAttempt, 'id' | 'startTime' | 'endTime'>;

type QuizAttemptFormGroupContent = {
  id: FormControl<QuizAttemptFormRawValue['id'] | NewQuizAttempt['id']>;
  startTime: FormControl<QuizAttemptFormRawValue['startTime']>;
  endTime: FormControl<QuizAttemptFormRawValue['endTime']>;
  score: FormControl<QuizAttemptFormRawValue['score']>;
  quiz: FormControl<QuizAttemptFormRawValue['quiz']>;
  user: FormControl<QuizAttemptFormRawValue['user']>;
};

export type QuizAttemptFormGroup = FormGroup<QuizAttemptFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QuizAttemptFormService {
  createQuizAttemptFormGroup(quizAttempt: QuizAttemptFormGroupInput = { id: null }): QuizAttemptFormGroup {
    const quizAttemptRawValue = this.convertQuizAttemptToQuizAttemptRawValue({
      ...this.getFormDefaults(),
      ...quizAttempt,
    });
    return new FormGroup<QuizAttemptFormGroupContent>({
      id: new FormControl(
        { value: quizAttemptRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      startTime: new FormControl(quizAttemptRawValue.startTime),
      endTime: new FormControl(quizAttemptRawValue.endTime),
      score: new FormControl(quizAttemptRawValue.score),
      quiz: new FormControl(quizAttemptRawValue.quiz),
      user: new FormControl(quizAttemptRawValue.user),
    });
  }

  getQuizAttempt(form: QuizAttemptFormGroup): IQuizAttempt | NewQuizAttempt {
    return this.convertQuizAttemptRawValueToQuizAttempt(form.getRawValue() as QuizAttemptFormRawValue | NewQuizAttemptFormRawValue);
  }

  resetForm(form: QuizAttemptFormGroup, quizAttempt: QuizAttemptFormGroupInput): void {
    const quizAttemptRawValue = this.convertQuizAttemptToQuizAttemptRawValue({ ...this.getFormDefaults(), ...quizAttempt });
    form.reset(
      {
        ...quizAttemptRawValue,
        id: { value: quizAttemptRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): QuizAttemptFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startTime: currentTime,
      endTime: currentTime,
    };
  }

  private convertQuizAttemptRawValueToQuizAttempt(
    rawQuizAttempt: QuizAttemptFormRawValue | NewQuizAttemptFormRawValue,
  ): IQuizAttempt | NewQuizAttempt {
    return {
      ...rawQuizAttempt,
      startTime: dayjs(rawQuizAttempt.startTime, DATE_TIME_FORMAT),
      endTime: dayjs(rawQuizAttempt.endTime, DATE_TIME_FORMAT),
    };
  }

  private convertQuizAttemptToQuizAttemptRawValue(
    quizAttempt: IQuizAttempt | (Partial<NewQuizAttempt> & QuizAttemptFormDefaults),
  ): QuizAttemptFormRawValue | PartialWithRequiredKeyOf<NewQuizAttemptFormRawValue> {
    return {
      ...quizAttempt,
      startTime: quizAttempt.startTime ? quizAttempt.startTime.format(DATE_TIME_FORMAT) : undefined,
      endTime: quizAttempt.endTime ? quizAttempt.endTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
