import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ICategory } from '../category.model';

@Component({
  standalone: true,
  selector: 'jhi-category-detail',
  templateUrl: './category-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CategoryDetailComponent {
  @Input() category: ICategory | null = null;

  previousState(): void {
    window.history.back();
  }
}
