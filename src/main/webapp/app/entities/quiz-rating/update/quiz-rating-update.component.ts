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
import { IQuiz } from 'app/entities/quiz/quiz.model';
import { QuizService } from 'app/entities/quiz/service/quiz.service';
import { IQuizUser } from 'app/entities/quiz-user/quiz-user.model';
import { QuizUserService } from 'app/entities/quiz-user/service/quiz-user.service';
import { QuizRatingService } from '../service/quiz-rating.service';
import { IQuizRating } from '../quiz-rating.model';
import { QuizRatingFormService, QuizRatingFormGroup } from './quiz-rating-form.service';

@Component({
  standalone: true,
  selector: 'jhi-quiz-rating-update',
  templateUrl: './quiz-rating-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class QuizRatingUpdateComponent implements OnInit {
  isSaving = false;
  quizRating: IQuizRating | null = null;

  quizzesSharedCollection: IQuiz[] = [];
  quizUsersSharedCollection: IQuizUser[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected quizRatingService = inject(QuizRatingService);
  protected quizRatingFormService = inject(QuizRatingFormService);
  protected quizService = inject(QuizService);
  protected quizUserService = inject(QuizUserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: QuizRatingFormGroup = this.quizRatingFormService.createQuizRatingFormGroup();

  compareQuiz = (o1: IQuiz | null, o2: IQuiz | null): boolean => this.quizService.compareQuiz(o1, o2);

  compareQuizUser = (o1: IQuizUser | null, o2: IQuizUser | null): boolean => this.quizUserService.compareQuizUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quizRating }) => {
      this.quizRating = quizRating;
      if (quizRating) {
        this.updateForm(quizRating);
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
    const quizRating = this.quizRatingFormService.getQuizRating(this.editForm);
    if (quizRating.id !== null) {
      this.subscribeToSaveResponse(this.quizRatingService.update(quizRating));
    } else {
      this.subscribeToSaveResponse(this.quizRatingService.create(quizRating));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuizRating>>): void {
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

  protected updateForm(quizRating: IQuizRating): void {
    this.quizRating = quizRating;
    this.quizRatingFormService.resetForm(this.editForm, quizRating);

    this.quizzesSharedCollection = this.quizService.addQuizToCollectionIfMissing<IQuiz>(this.quizzesSharedCollection, quizRating.quiz);
    this.quizUsersSharedCollection = this.quizUserService.addQuizUserToCollectionIfMissing<IQuizUser>(
      this.quizUsersSharedCollection,
      quizRating.user,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.quizService
      .query()
      .pipe(map((res: HttpResponse<IQuiz[]>) => res.body ?? []))
      .pipe(map((quizzes: IQuiz[]) => this.quizService.addQuizToCollectionIfMissing<IQuiz>(quizzes, this.quizRating?.quiz)))
      .subscribe((quizzes: IQuiz[]) => (this.quizzesSharedCollection = quizzes));

    this.quizUserService
      .query()
      .pipe(map((res: HttpResponse<IQuizUser[]>) => res.body ?? []))
      .pipe(
        map((quizUsers: IQuizUser[]) => this.quizUserService.addQuizUserToCollectionIfMissing<IQuizUser>(quizUsers, this.quizRating?.user)),
      )
      .subscribe((quizUsers: IQuizUser[]) => (this.quizUsersSharedCollection = quizUsers));
  }
}
