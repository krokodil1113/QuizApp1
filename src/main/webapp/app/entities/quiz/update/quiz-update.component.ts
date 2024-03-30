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
import { IQuizAnalytics } from 'app/entities/quiz-analytics/quiz-analytics.model';
import { QuizAnalyticsService } from 'app/entities/quiz-analytics/service/quiz-analytics.service';
import { IQuizUser } from 'app/entities/quiz-user/quiz-user.model';
import { QuizUserService } from 'app/entities/quiz-user/service/quiz-user.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { QuizService } from '../service/quiz.service';
import { IQuiz } from '../quiz.model';
import { QuizFormService, QuizFormGroup } from './quiz-form.service';

@Component({
  standalone: true,
  selector: 'jhi-quiz-update',
  templateUrl: './quiz-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class QuizUpdateComponent implements OnInit {
  isSaving = false;
  quiz: IQuiz | null = null;

  quizAnalyticsCollection: IQuizAnalytics[] = [];
  quizUsersSharedCollection: IQuizUser[] = [];
  categoriesSharedCollection: ICategory[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected quizService = inject(QuizService);
  protected quizFormService = inject(QuizFormService);
  protected quizAnalyticsService = inject(QuizAnalyticsService);
  protected quizUserService = inject(QuizUserService);
  protected categoryService = inject(CategoryService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: QuizFormGroup = this.quizFormService.createQuizFormGroup();

  compareQuizAnalytics = (o1: IQuizAnalytics | null, o2: IQuizAnalytics | null): boolean =>
    this.quizAnalyticsService.compareQuizAnalytics(o1, o2);

  compareQuizUser = (o1: IQuizUser | null, o2: IQuizUser | null): boolean => this.quizUserService.compareQuizUser(o1, o2);

  compareCategory = (o1: ICategory | null, o2: ICategory | null): boolean => this.categoryService.compareCategory(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quiz }) => {
      this.quiz = quiz;
      if (quiz) {
        this.updateForm(quiz);
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
    const quiz = this.quizFormService.getQuiz(this.editForm);
    if (quiz.id !== null) {
      this.subscribeToSaveResponse(this.quizService.update(quiz));
    } else {
      this.subscribeToSaveResponse(this.quizService.create(quiz));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuiz>>): void {
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

  protected updateForm(quiz: IQuiz): void {
    this.quiz = quiz;
    this.quizFormService.resetForm(this.editForm, quiz);

    this.quizAnalyticsCollection = this.quizAnalyticsService.addQuizAnalyticsToCollectionIfMissing<IQuizAnalytics>(
      this.quizAnalyticsCollection,
      quiz.quizAnalytics,
    );
    this.quizUsersSharedCollection = this.quizUserService.addQuizUserToCollectionIfMissing<IQuizUser>(
      this.quizUsersSharedCollection,
      quiz.creator,
    );
    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing<ICategory>(
      this.categoriesSharedCollection,
      quiz.category,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.quizAnalyticsService
      .query({ filter: 'quiz-is-null' })
      .pipe(map((res: HttpResponse<IQuizAnalytics[]>) => res.body ?? []))
      .pipe(
        map((quizAnalytics: IQuizAnalytics[]) =>
          this.quizAnalyticsService.addQuizAnalyticsToCollectionIfMissing<IQuizAnalytics>(quizAnalytics, this.quiz?.quizAnalytics),
        ),
      )
      .subscribe((quizAnalytics: IQuizAnalytics[]) => (this.quizAnalyticsCollection = quizAnalytics));

    this.quizUserService
      .query()
      .pipe(map((res: HttpResponse<IQuizUser[]>) => res.body ?? []))
      .pipe(
        map((quizUsers: IQuizUser[]) => this.quizUserService.addQuizUserToCollectionIfMissing<IQuizUser>(quizUsers, this.quiz?.creator)),
      )
      .subscribe((quizUsers: IQuizUser[]) => (this.quizUsersSharedCollection = quizUsers));

    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) => this.categoryService.addCategoryToCollectionIfMissing<ICategory>(categories, this.quiz?.category)),
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));
  }
}
