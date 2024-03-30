import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IQuiz } from 'app/entities/quiz/quiz.model';
import { QuizService } from 'app/entities/quiz/service/quiz.service';
import { IQuizUser } from 'app/entities/quiz-user/quiz-user.model';
import { QuizUserService } from 'app/entities/quiz-user/service/quiz-user.service';
import { QuizAttemptService } from '../service/quiz-attempt.service';
import { IQuizAttempt } from '../quiz-attempt.model';
import { QuizAttemptFormService, QuizAttemptFormGroup } from './quiz-attempt-form.service';

@Component({
  standalone: true,
  selector: 'jhi-quiz-attempt-update',
  templateUrl: './quiz-attempt-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class QuizAttemptUpdateComponent implements OnInit {
  isSaving = false;
  quizAttempt: IQuizAttempt | null = null;

  quizzesSharedCollection: IQuiz[] = [];
  quizUsersSharedCollection: IQuizUser[] = [];

  protected quizAttemptService = inject(QuizAttemptService);
  protected quizAttemptFormService = inject(QuizAttemptFormService);
  protected quizService = inject(QuizService);
  protected quizUserService = inject(QuizUserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: QuizAttemptFormGroup = this.quizAttemptFormService.createQuizAttemptFormGroup();

  compareQuiz = (o1: IQuiz | null, o2: IQuiz | null): boolean => this.quizService.compareQuiz(o1, o2);

  compareQuizUser = (o1: IQuizUser | null, o2: IQuizUser | null): boolean => this.quizUserService.compareQuizUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quizAttempt }) => {
      this.quizAttempt = quizAttempt;
      if (quizAttempt) {
        this.updateForm(quizAttempt);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const quizAttempt = this.quizAttemptFormService.getQuizAttempt(this.editForm);
    if (quizAttempt.id !== null) {
      this.subscribeToSaveResponse(this.quizAttemptService.update(quizAttempt));
    } else {
      this.subscribeToSaveResponse(this.quizAttemptService.create(quizAttempt));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuizAttempt>>): void {
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

  protected updateForm(quizAttempt: IQuizAttempt): void {
    this.quizAttempt = quizAttempt;
    this.quizAttemptFormService.resetForm(this.editForm, quizAttempt);

    this.quizzesSharedCollection = this.quizService.addQuizToCollectionIfMissing<IQuiz>(this.quizzesSharedCollection, quizAttempt.quiz);
    this.quizUsersSharedCollection = this.quizUserService.addQuizUserToCollectionIfMissing<IQuizUser>(
      this.quizUsersSharedCollection,
      quizAttempt.user,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.quizService
      .query()
      .pipe(map((res: HttpResponse<IQuiz[]>) => res.body ?? []))
      .pipe(map((quizzes: IQuiz[]) => this.quizService.addQuizToCollectionIfMissing<IQuiz>(quizzes, this.quizAttempt?.quiz)))
      .subscribe((quizzes: IQuiz[]) => (this.quizzesSharedCollection = quizzes));

    this.quizUserService
      .query()
      .pipe(map((res: HttpResponse<IQuizUser[]>) => res.body ?? []))
      .pipe(
        map((quizUsers: IQuizUser[]) =>
          this.quizUserService.addQuizUserToCollectionIfMissing<IQuizUser>(quizUsers, this.quizAttempt?.user),
        ),
      )
      .subscribe((quizUsers: IQuizUser[]) => (this.quizUsersSharedCollection = quizUsers));
  }
}
