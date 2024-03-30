import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IQuizAnalytics } from '../quiz-analytics.model';
import { QuizAnalyticsService } from '../service/quiz-analytics.service';
import { QuizAnalyticsFormService, QuizAnalyticsFormGroup } from './quiz-analytics-form.service';

@Component({
  standalone: true,
  selector: 'jhi-quiz-analytics-update',
  templateUrl: './quiz-analytics-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class QuizAnalyticsUpdateComponent implements OnInit {
  isSaving = false;
  quizAnalytics: IQuizAnalytics | null = null;

  protected quizAnalyticsService = inject(QuizAnalyticsService);
  protected quizAnalyticsFormService = inject(QuizAnalyticsFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: QuizAnalyticsFormGroup = this.quizAnalyticsFormService.createQuizAnalyticsFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quizAnalytics }) => {
      this.quizAnalytics = quizAnalytics;
      if (quizAnalytics) {
        this.updateForm(quizAnalytics);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const quizAnalytics = this.quizAnalyticsFormService.getQuizAnalytics(this.editForm);
    if (quizAnalytics.id !== null) {
      this.subscribeToSaveResponse(this.quizAnalyticsService.update(quizAnalytics));
    } else {
      this.subscribeToSaveResponse(this.quizAnalyticsService.create(quizAnalytics));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuizAnalytics>>): void {
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

  protected updateForm(quizAnalytics: IQuizAnalytics): void {
    this.quizAnalytics = quizAnalytics;
    this.quizAnalyticsFormService.resetForm(this.editForm, quizAnalytics);
  }
}
