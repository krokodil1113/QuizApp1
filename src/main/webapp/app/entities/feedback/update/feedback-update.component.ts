import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IQuizUser } from 'app/entities/quiz-user/quiz-user.model';
import { QuizUserService } from 'app/entities/quiz-user/service/quiz-user.service';
import { FeedbackService } from '../service/feedback.service';
import { IFeedback } from '../feedback.model';
import { FeedbackFormService, FeedbackFormGroup } from './feedback-form.service';

@Component({
  standalone: true,
  selector: 'jhi-feedback-update',
  templateUrl: './feedback-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FeedbackUpdateComponent implements OnInit {
  isSaving = false;
  feedback: IFeedback | null = null;

  quizUsersSharedCollection: IQuizUser[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected feedbackService = inject(FeedbackService);
  protected feedbackFormService = inject(FeedbackFormService);
  protected quizUserService = inject(QuizUserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FeedbackFormGroup = this.feedbackFormService.createFeedbackFormGroup();

  compareQuizUser = (o1: IQuizUser | null, o2: IQuizUser | null): boolean => this.quizUserService.compareQuizUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feedback }) => {
      this.feedback = feedback;
      if (feedback) {
        this.updateForm(feedback);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('quizApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const feedback = this.feedbackFormService.getFeedback(this.editForm);
    if (feedback.id !== null) {
      this.subscribeToSaveResponse(this.feedbackService.update(feedback));
    } else {
      this.subscribeToSaveResponse(this.feedbackService.create(feedback));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeedback>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(feedback: IFeedback): void {
    this.feedback = feedback;
    this.feedbackFormService.resetForm(this.editForm, feedback);

    this.quizUsersSharedCollection = this.quizUserService.addQuizUserToCollectionIfMissing<IQuizUser>(
      this.quizUsersSharedCollection,
      feedback.user,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.quizUserService
      .query()
      .pipe(map((res: HttpResponse<IQuizUser[]>) => res.body ?? []))
      .pipe(
        map((quizUsers: IQuizUser[]) => this.quizUserService.addQuizUserToCollectionIfMissing<IQuizUser>(quizUsers, this.feedback?.user)),
      )
      .subscribe((quizUsers: IQuizUser[]) => (this.quizUsersSharedCollection = quizUsers));
  }
}
