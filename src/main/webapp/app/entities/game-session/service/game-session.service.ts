import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGameSession, NewGameSession } from '../game-session.model';

export type PartialUpdateGameSession = Partial<IGameSession> & Pick<IGameSession, 'id'>;

type RestOf<T extends IGameSession | NewGameSession> = Omit<T, 'startTime' | 'endTime'> & {
  startTime?: string | null;
  endTime?: string | null;
};

export type RestGameSession = RestOf<IGameSession>;

export type NewRestGameSession = RestOf<NewGameSession>;

export type PartialUpdateRestGameSession = RestOf<PartialUpdateGameSession>;

export type EntityResponseType = HttpResponse<IGameSession>;
export type EntityArrayResponseType = HttpResponse<IGameSession[]>;

@Injectable({ providedIn: 'root' })
export class GameSessionService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/game-sessions');

  create(gameSession: NewGameSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(gameSession);
    return this.http
      .post<RestGameSession>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(gameSession: IGameSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(gameSession);
    return this.http
      .put<RestGameSession>(`${this.resourceUrl}/${this.getGameSessionIdentifier(gameSession)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(gameSession: PartialUpdateGameSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(gameSession);
    return this.http
      .patch<RestGameSession>(`${this.resourceUrl}/${this.getGameSessionIdentifier(gameSession)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestGameSession>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestGameSession[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGameSessionIdentifier(gameSession: Pick<IGameSession, 'id'>): number {
    return gameSession.id;
  }

  compareGameSession(o1: Pick<IGameSession, 'id'> | null, o2: Pick<IGameSession, 'id'> | null): boolean {
    return o1 && o2 ? this.getGameSessionIdentifier(o1) === this.getGameSessionIdentifier(o2) : o1 === o2;
  }

  addGameSessionToCollectionIfMissing<Type extends Pick<IGameSession, 'id'>>(
    gameSessionCollection: Type[],
    ...gameSessionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const gameSessions: Type[] = gameSessionsToCheck.filter(isPresent);
    if (gameSessions.length > 0) {
      const gameSessionCollectionIdentifiers = gameSessionCollection.map(gameSessionItem => this.getGameSessionIdentifier(gameSessionItem));
      const gameSessionsToAdd = gameSessions.filter(gameSessionItem => {
        const gameSessionIdentifier = this.getGameSessionIdentifier(gameSessionItem);
        if (gameSessionCollectionIdentifiers.includes(gameSessionIdentifier)) {
          return false;
        }
        gameSessionCollectionIdentifiers.push(gameSessionIdentifier);
        return true;
      });
      return [...gameSessionsToAdd, ...gameSessionCollection];
    }
    return gameSessionCollection;
  }

  protected convertDateFromClient<T extends IGameSession | NewGameSession | PartialUpdateGameSession>(gameSession: T): RestOf<T> {
    return {
      ...gameSession,
      startTime: gameSession.startTime?.toJSON() ?? null,
      endTime: gameSession.endTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restGameSession: RestGameSession): IGameSession {
    return {
      ...restGameSession,
      startTime: restGameSession.startTime ? dayjs(restGameSession.startTime) : undefined,
      endTime: restGameSession.endTime ? dayjs(restGameSession.endTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestGameSession>): HttpResponse<IGameSession> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestGameSession[]>): HttpResponse<IGameSession[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
