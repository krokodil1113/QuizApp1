import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IGameSession, NewGameSession } from '../game-session.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGameSession for edit and NewGameSessionFormGroupInput for create.
 */
type GameSessionFormGroupInput = IGameSession | PartialWithRequiredKeyOf<NewGameSession>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IGameSession | NewGameSession> = Omit<T, 'startTime' | 'endTime'> & {
  startTime?: string | null;
  endTime?: string | null;
};

type GameSessionFormRawValue = FormValueOf<IGameSession>;

type NewGameSessionFormRawValue = FormValueOf<NewGameSession>;

type GameSessionFormDefaults = Pick<NewGameSession, 'id' | 'startTime' | 'endTime'>;

type GameSessionFormGroupContent = {
  id: FormControl<GameSessionFormRawValue['id'] | NewGameSession['id']>;
  startTime: FormControl<GameSessionFormRawValue['startTime']>;
  endTime: FormControl<GameSessionFormRawValue['endTime']>;
  status: FormControl<GameSessionFormRawValue['status']>;
  currentQuestionIndex: FormControl<GameSessionFormRawValue['currentQuestionIndex']>;
};

export type GameSessionFormGroup = FormGroup<GameSessionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GameSessionFormService {
  createGameSessionFormGroup(gameSession: GameSessionFormGroupInput = { id: null }): GameSessionFormGroup {
    const gameSessionRawValue = this.convertGameSessionToGameSessionRawValue({
      ...this.getFormDefaults(),
      ...gameSession,
    });
    return new FormGroup<GameSessionFormGroupContent>({
      id: new FormControl(
        { value: gameSessionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      startTime: new FormControl(gameSessionRawValue.startTime),
      endTime: new FormControl(gameSessionRawValue.endTime),
      status: new FormControl(gameSessionRawValue.status),
      currentQuestionIndex: new FormControl(gameSessionRawValue.currentQuestionIndex),
    });
  }

  getGameSession(form: GameSessionFormGroup): IGameSession | NewGameSession {
    return this.convertGameSessionRawValueToGameSession(form.getRawValue() as GameSessionFormRawValue | NewGameSessionFormRawValue);
  }

  resetForm(form: GameSessionFormGroup, gameSession: GameSessionFormGroupInput): void {
    const gameSessionRawValue = this.convertGameSessionToGameSessionRawValue({ ...this.getFormDefaults(), ...gameSession });
    form.reset(
      {
        ...gameSessionRawValue,
        id: { value: gameSessionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): GameSessionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startTime: currentTime,
      endTime: currentTime,
    };
  }

  private convertGameSessionRawValueToGameSession(
    rawGameSession: GameSessionFormRawValue | NewGameSessionFormRawValue,
  ): IGameSession | NewGameSession {
    return {
      ...rawGameSession,
      startTime: dayjs(rawGameSession.startTime, DATE_TIME_FORMAT),
      endTime: dayjs(rawGameSession.endTime, DATE_TIME_FORMAT),
    };
  }

  private convertGameSessionToGameSessionRawValue(
    gameSession: IGameSession | (Partial<NewGameSession> & GameSessionFormDefaults),
  ): GameSessionFormRawValue | PartialWithRequiredKeyOf<NewGameSessionFormRawValue> {
    return {
      ...gameSession,
      startTime: gameSession.startTime ? gameSession.startTime.format(DATE_TIME_FORMAT) : undefined,
      endTime: gameSession.endTime ? gameSession.endTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
