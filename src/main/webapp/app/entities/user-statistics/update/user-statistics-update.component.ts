import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IQuizUser } from 'app/entities/quiz-user/quiz-user.model';
import { QuizUserService } from 'app/entities/quiz-user/service/quiz-user.service';
import { IUserStatistics } from '../user-statistics.model';
import { UserStatisticsService } from '../service/user-statistics.service';
import { UserStatisticsFormService, UserStatisticsFormGroup } from './user-statistics-form.service';

@Component({
  standalone: true,
  selector: 'jhi-user-statistics-update',
  templateUrl: './user-statistics-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserStatisticsUpdateComponent implements OnInit {
  isSaving = false;
  userStatistics: IUserStatistics | null = null;

  quizUsersCollection: IQuizUser[] = [];

  protected userStatisticsService = inject(UserStatisticsService);
  protected userStatisticsFormService = inject(UserStatisticsFormService);
  protected quizUserService = inject(QuizUserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UserStatisticsFormGroup = this.userStatisticsFormService.createUserStatisticsFormGroup();

  compareQuizUser = (o1: IQuizUser | null, o2: IQuizUser | null): boolean => this.quizUserService.compareQuizUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userStatistics }) => {
      this.userStatistics = userStatistics;
      if (userStatistics) {
        this.updateForm(userStatistics);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userStatistics = this.userStatisticsFormService.getUserStatistics(this.editForm);
    if (userStatistics.id !== null) {
      this.subscribeToSaveResponse(this.userStatisticsService.update(userStatistics));
    } else {
      this.subscribeToSaveResponse(this.userStatisticsService.create(userStatistics));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserStatistics>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(userStatistics: IUserStatistics): void {
    this.userStatistics = userStatistics;
    this.userStatisticsFormService.resetForm(this.editForm, userStatistics);

    this.quizUsersCollection = this.quizUserService.addQuizUserToCollectionIfMissing<IQuizUser>(
      this.quizUsersCollection,
      userStatistics.quizUser,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.quizUserService
      .query({ filter: 'userstatistics-is-null' })
      .pipe(map((res: HttpResponse<IQuizUser[]>) => res.body ?? []))
      .pipe(
        map((quizUsers: IQuizUser[]) =>
          this.quizUserService.addQuizUserToCollectionIfMissing<IQuizUser>(quizUsers, this.userStatistics?.quizUser),
        ),
      )
      .subscribe((quizUsers: IQuizUser[]) => (this.quizUsersCollection = quizUsers));
  }
}
