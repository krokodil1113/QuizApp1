import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IQuizAnalytics } from '../quiz-analytics.model';

@Component({
  standalone: true,
  selector: 'jhi-quiz-analytics-detail',
  templateUrl: './quiz-analytics-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class QuizAnalyticsDetailComponent {
  @Input() quizAnalytics: IQuizAnalytics | null = null;

  previousState(): void {
    window.history.back();
  }
}
