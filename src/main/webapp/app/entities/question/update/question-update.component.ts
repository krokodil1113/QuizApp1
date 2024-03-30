import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IQuiz } from 'app/entities/quiz/quiz.model';
import { QuizService } from 'app/entities/quiz/service/quiz.service';
import { IQuestion } from '../question.model';
import { QuestionService } from '../service/question.service';
import { QuestionFormService, QuestionFormGroup } from './question-form.service';

@Component({
  standalone: true,
  selector: 'jhi-question-update',
  templateUrl: './question-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class QuestionUpdateComponent implements OnInit {
  isSaving = false;
  question: IQuestion | null = null;

  quizzesSharedCollection: IQuiz[] = [];

  protected questionService = inject(QuestionService);
  protected questionFormService = inject(QuestionFormService);
  protected quizService = inject(QuizService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: QuestionFormGroup = this.questionFormService.createQuestionFormGroup();

  compareQuiz = (o1: IQuiz | null, o2: IQuiz | null): boolean => this.quizService.compareQuiz(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ question }) => {
      this.question = question;
      if (question) {
        this.updateForm(question);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const question = this.questionFormService.getQuestion(this.editForm);
    if (question.id !== null) {
      this.subscribeToSaveResponse(this.questionService.update(question));
    } else {
      this.subscribeToSaveResponse(this.questionService.create(question));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestion>>): void {
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

  protected updateForm(question: IQuestion): void {
    this.question = question;
    this.questionFormService.resetForm(this.editForm, question);

    this.quizzesSharedCollection = this.quizService.addQuizToCollectionIfMissing<IQuiz>(this.quizzesSharedCollection, question.quiz);
  }

  protected loadRelationshipsOptions(): void {
    this.quizService
      .query()
      .pipe(map((res: HttpResponse<IQuiz[]>) => res.body ?? []))
      .pipe(map((quizzes: IQuiz[]) => this.quizService.addQuizToCollectionIfMissing<IQuiz>(quizzes, this.question?.quiz)))
      .subscribe((quizzes: IQuiz[]) => (this.quizzesSharedCollection = quizzes));
  }
}
