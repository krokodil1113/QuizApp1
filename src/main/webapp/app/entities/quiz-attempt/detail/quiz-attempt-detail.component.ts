import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IQuizAttempt } from '../quiz-attempt.model';

@Component({
  standalone: true,
  selector: 'jhi-quiz-attempt-detail',
  templateUrl: './quiz-attempt-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class QuizAttemptDetailComponent {
  @Input() quizAttempt: IQuizAttempt | null = null;

  previousState(): void {
    window.history.back();
  }
}
