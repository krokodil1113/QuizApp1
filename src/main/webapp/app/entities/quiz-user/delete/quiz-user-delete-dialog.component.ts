import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IQuizUser } from '../quiz-user.model';
import { QuizUserService } from '../service/quiz-user.service';

@Component({
  standalone: true,
  templateUrl: './quiz-user-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class QuizUserDeleteDialogComponent {
  quizUser?: IQuizUser;

  protected quizUserService = inject(QuizUserService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.quizUserService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
