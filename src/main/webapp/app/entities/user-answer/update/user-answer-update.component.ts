import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IQuizAttempt } from 'app/entities/quiz-attempt/quiz-attempt.model';
import { QuizAttemptService } from 'app/entities/quiz-attempt/service/quiz-attempt.service';
import { IQuestion } from 'app/entities/question/question.model';
import { QuestionService } from 'app/entities/question/service/question.service';
import { IAnswer } from 'app/entities/answer/answer.model';
import { AnswerService } from 'app/entities/answer/service/answer.service';
import { UserAnswerService } from '../service/user-answer.service';
import { IUserAnswer } from '../user-answer.model';
import { UserAnswerFormService, UserAnswerFormGroup } from './user-answer-form.service';

@Component({
  standalone: true,
  selector: 'jhi-user-answer-update',
  templateUrl: './user-answer-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserAnswerUpdateComponent implements OnInit {
  isSaving = false;
  userAnswer: IUserAnswer | null = null;

  quizAttemptsSharedCollection: IQuizAttempt[] = [];
  questionsSharedCollection: IQuestion[] = [];
  answersSharedCollection: IAnswer[] = [];

  protected userAnswerService = inject(UserAnswerService);
  protected userAnswerFormService = inject(UserAnswerFormService);
  protected quizAttemptService = inject(QuizAttemptService);
  protected questionService = inject(QuestionService);
  protected answerService = inject(AnswerService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UserAnswerFormGroup = this.userAnswerFormService.createUserAnswerFormGroup();

  compareQuizAttempt = (o1: IQuizAttempt | null, o2: IQuizAttempt | null): boolean => this.quizAttemptService.compareQuizAttempt(o1, o2);

  compareQuestion = (o1: IQuestion | null, o2: IQuestion | null): boolean => this.questionService.compareQuestion(o1, o2);

  compareAnswer = (o1: IAnswer | null, o2: IAnswer | null): boolean => this.answerService.compareAnswer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userAnswer }) => {
      this.userAnswer = userAnswer;
      if (userAnswer) {
        this.updateForm(userAnswer);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userAnswer = this.userAnswerFormService.getUserAnswer(this.editForm);
    if (userAnswer.id !== null) {
      this.subscribeToSaveResponse(this.userAnswerService.update(userAnswer));
    } else {
      this.subscribeToSaveResponse(this.userAnswerService.create(userAnswer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserAnswer>>): void {
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

  protected updateForm(userAnswer: IUserAnswer): void {
    this.userAnswer = userAnswer;
    this.userAnswerFormService.resetForm(this.editForm, userAnswer);

    this.quizAttemptsSharedCollection = this.quizAttemptService.addQuizAttemptToCollectionIfMissing<IQuizAttempt>(
      this.quizAttemptsSharedCollection,
      userAnswer.attempt,
    );
    this.questionsSharedCollection = this.questionService.addQuestionToCollectionIfMissing<IQuestion>(
      this.questionsSharedCollection,
      userAnswer.question,
    );
    this.answersSharedCollection = this.answerService.addAnswerToCollectionIfMissing<IAnswer>(
      this.answersSharedCollection,
      userAnswer.selectedAnswer,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.quizAttemptService
      .query()
      .pipe(map((res: HttpResponse<IQuizAttempt[]>) => res.body ?? []))
      .pipe(
        map((quizAttempts: IQuizAttempt[]) =>
          this.quizAttemptService.addQuizAttemptToCollectionIfMissing<IQuizAttempt>(quizAttempts, this.userAnswer?.attempt),
        ),
      )
      .subscribe((quizAttempts: IQuizAttempt[]) => (this.quizAttemptsSharedCollection = quizAttempts));

    this.questionService
      .query()
      .pipe(map((res: HttpResponse<IQuestion[]>) => res.body ?? []))
      .pipe(
        map((questions: IQuestion[]) =>
          this.questionService.addQuestionToCollectionIfMissing<IQuestion>(questions, this.userAnswer?.question),
        ),
      )
      .subscribe((questions: IQuestion[]) => (this.questionsSharedCollection = questions));

    this.answerService
      .query()
      .pipe(map((res: HttpResponse<IAnswer[]>) => res.body ?? []))
      .pipe(
        map((answers: IAnswer[]) => this.answerService.addAnswerToCollectionIfMissing<IAnswer>(answers, this.userAnswer?.selectedAnswer)),
      )
      .subscribe((answers: IAnswer[]) => (this.answersSharedCollection = answers));
  }
}
