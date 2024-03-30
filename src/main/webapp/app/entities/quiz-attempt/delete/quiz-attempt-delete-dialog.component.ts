import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IQuizAttempt } from '../quiz-attempt.model';
import { QuizAttemptService } from '../service/quiz-attempt.service';

@Component({
  standalone: true,
  templateUrl: './quiz-attempt-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class QuizAttemptDeleteDialogComponent {
  quizAttempt?: IQuizAttempt;

  protected quizAttemptService = inject(QuizAttemptService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.quizAttemptService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
