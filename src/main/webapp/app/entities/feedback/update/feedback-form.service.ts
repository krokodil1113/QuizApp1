import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFeedback, NewFeedback } from '../feedback.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFeedback for edit and NewFeedbackFormGroupInput for create.
 */
type FeedbackFormGroupInput = IFeedback | PartialWithRequiredKeyOf<NewFeedback>;

type FeedbackFormDefaults = Pick<NewFeedback, 'id'>;

type FeedbackFormGroupContent = {
  id: FormControl<IFeedback['id'] | NewFeedback['id']>;
  content: FormControl<IFeedback['content']>;
  createDate: FormControl<IFeedback['createDate']>;
  user: FormControl<IFeedback['user']>;
};

export type FeedbackFormGroup = FormGroup<FeedbackFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FeedbackFormService {
  createFeedbackFormGroup(feedback: FeedbackFormGroupInput = { id: null }): FeedbackFormGroup {
    const feedbackRawValue = {
      ...this.getFormDefaults(),
      ...feedback,
    };
    return new FormGroup<FeedbackFormGroupContent>({
      id: new FormControl(
        { value: feedbackRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      content: new FormControl(feedbackRawValue.content, {
        validators: [Validators.required],
      }),
      createDate: new FormControl(feedbackRawValue.createDate),
      user: new FormControl(feedbackRawValue.user),
    });
  }

  getFeedback(form: FeedbackFormGroup): IFeedback | NewFeedback {
    return form.getRawValue() as IFeedback | NewFeedback;
  }

  resetForm(form: FeedbackFormGroup, feedback: FeedbackFormGroupInput): void {
    const feedbackRawValue = { ...this.getFormDefaults(), ...feedback };
    form.reset(
      {
        ...feedbackRawValue,
        id: { value: feedbackRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FeedbackFormDefaults {
    return {
      id: null,
    };
  }
}
