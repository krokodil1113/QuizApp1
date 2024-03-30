import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IUserStatistics } from '../user-statistics.model';

@Component({
  standalone: true,
  selector: 'jhi-user-statistics-detail',
  templateUrl: './user-statistics-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class UserStatisticsDetailComponent {
  @Input() userStatistics: IUserStatistics | null = null;

  previousState(): void {
    window.history.back();
  }
}
