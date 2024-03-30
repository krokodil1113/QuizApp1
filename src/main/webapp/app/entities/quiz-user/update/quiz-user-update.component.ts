import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { QuizUserService } from '../service/quiz-user.service';
import { IQuizUser } from '../quiz-user.model';
import { QuizUserFormService, QuizUserFormGroup } from './quiz-user-form.service';

@Component({
  standalone: true,
  selector: 'jhi-quiz-user-update',
  templateUrl: './quiz-user-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class QuizUserUpdateComponent implements OnInit {
  isSaving = false;
  quizUser: IQuizUser | null = null;

  usersSharedCollection: IUser[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected quizUserService = inject(QuizUserService);
  protected quizUserFormService = inject(QuizUserFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: QuizUserFormGroup = this.quizUserFormService.createQuizUserFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quizUser }) => {
      this.quizUser = quizUser;
      if (quizUser) {
        this.updateForm(quizUser);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('quizApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const quizUser = this.quizUserFormService.getQuizUser(this.editForm);
    if (quizUser.id !== null) {
      this.subscribeToSaveResponse(this.quizUserService.update(quizUser));
    } else {
      this.subscribeToSaveResponse(this.quizUserService.create(quizUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuizUser>>): void {
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

  protected updateForm(quizUser: IQuizUser): void {
    this.quizUser = quizUser;
    this.quizUserFormService.resetForm(this.editForm, quizUser);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, quizUser.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.quizUser?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
