import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IUserAnswer } from '../user-answer.model';

@Component({
  standalone: true,
  selector: 'jhi-user-answer-detail',
  templateUrl: './user-answer-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class UserAnswerDetailComponent {
  @Input() userAnswer: IUserAnswer | null = null;

  previousState(): void {
    window.history.back();
  }
}
