import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUserStatistics } from '../user-statistics.model';
import { UserStatisticsService } from '../service/user-statistics.service';

@Component({
  standalone: true,
  templateUrl: './user-statistics-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UserStatisticsDeleteDialogComponent {
  userStatistics?: IUserStatistics;

  protected userStatisticsService = inject(UserStatisticsService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userStatisticsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
