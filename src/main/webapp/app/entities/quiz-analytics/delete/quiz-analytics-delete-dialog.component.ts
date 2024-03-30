import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IQuizAnalytics } from '../quiz-analytics.model';
import { QuizAnalyticsService } from '../service/quiz-analytics.service';

@Component({
  standalone: true,
  templateUrl: './quiz-analytics-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class QuizAnalyticsDeleteDialogComponent {
  quizAnalytics?: IQuizAnalytics;

  protected quizAnalyticsService = inject(QuizAnalyticsService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.quizAnalyticsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
