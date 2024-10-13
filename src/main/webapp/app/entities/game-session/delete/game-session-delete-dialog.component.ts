import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IGameSession } from '../game-session.model';
import { GameSessionService } from '../service/game-session.service';

@Component({
  standalone: true,
  templateUrl: './game-session-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class GameSessionDeleteDialogComponent {
  gameSession?: IGameSession;

  protected gameSessionService = inject(GameSessionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gameSessionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
