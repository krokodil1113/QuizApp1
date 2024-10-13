import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IGameSession } from '../game-session.model';

@Component({
  standalone: true,
  selector: 'jhi-game-session-detail',
  templateUrl: './game-session-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class GameSessionDetailComponent {
  @Input() gameSession: IGameSession | null = null;

  previousState(): void {
    window.history.back();
  }
}
