import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuizAttempt, NewQuizAttempt } from '../quiz-attempt.model';

export type PartialUpdateQuizAttempt = Partial<IQuizAttempt> & Pick<IQuizAttempt, 'id'>;

type RestOf<T extends IQuizAttempt | NewQuizAttempt> = Omit<T, 'startTime' | 'endTime'> & {
  startTime?: string | null;
  endTime?: string | null;
};

export type RestQuizAttempt = RestOf<IQuizAttempt>;

export type NewRestQuizAttempt = RestOf<NewQuizAttempt>;

export type PartialUpdateRestQuizAttempt = RestOf<PartialUpdateQuizAttempt>;

export type EntityResponseType = HttpResponse<IQuizAttempt>;
export type EntityArrayResponseType = HttpResponse<IQuizAttempt[]>;

@Injectable({ providedIn: 'root' })
export class QuizAttemptService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/quiz-attempts');

  createQuizAttempt(quizId: number, userId: number) {
    const attempt = {
      quizId: quizId,
      userId: userId,
      startTime: new Date(), // Current timestamp as the start time
    };
    return this.http.post(this.resourceUrl, attempt);
  }

  create(quizAttempt: NewQuizAttempt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(quizAttempt);
    return this.http
      .post<RestQuizAttempt>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(quizAttempt: IQuizAttempt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(quizAttempt);
    return this.http
      .put<RestQuizAttempt>(`${this.resourceUrl}/${this.getQuizAttemptIdentifier(quizAttempt)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(quizAttempt: PartialUpdateQuizAttempt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(quizAttempt);
    return this.http
      .patch<RestQuizAttempt>(`${this.resourceUrl}/${this.getQuizAttemptIdentifier(quizAttempt)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestQuizAttempt>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestQuizAttempt[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getQuizAttemptIdentifier(quizAttempt: Pick<IQuizAttempt, 'id'>): number {
    return quizAttempt.id;
  }

  compareQuizAttempt(o1: Pick<IQuizAttempt, 'id'> | null, o2: Pick<IQuizAttempt, 'id'> | null): boolean {
    return o1 && o2 ? this.getQuizAttemptIdentifier(o1) === this.getQuizAttemptIdentifier(o2) : o1 === o2;
  }

  addQuizAttemptToCollectionIfMissing<Type extends Pick<IQuizAttempt, 'id'>>(
    quizAttemptCollection: Type[],
    ...quizAttemptsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const quizAttempts: Type[] = quizAttemptsToCheck.filter(isPresent);
    if (quizAttempts.length > 0) {
      const quizAttemptCollectionIdentifiers = quizAttemptCollection.map(quizAttemptItem => this.getQuizAttemptIdentifier(quizAttemptItem));
      const quizAttemptsToAdd = quizAttempts.filter(quizAttemptItem => {
        const quizAttemptIdentifier = this.getQuizAttemptIdentifier(quizAttemptItem);
        if (quizAttemptCollectionIdentifiers.includes(quizAttemptIdentifier)) {
          return false;
        }
        quizAttemptCollectionIdentifiers.push(quizAttemptIdentifier);
        return true;
      });
      return [...quizAttemptsToAdd, ...quizAttemptCollection];
    }
    return quizAttemptCollection;
  }

  protected convertDateFromClient<T extends IQuizAttempt | NewQuizAttempt | PartialUpdateQuizAttempt>(quizAttempt: T): RestOf<T> {
    return {
      ...quizAttempt,
      startTime: quizAttempt.startTime?.toJSON() ?? null,
      endTime: quizAttempt.endTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restQuizAttempt: RestQuizAttempt): IQuizAttempt {
    return {
      ...restQuizAttempt,
      startTime: restQuizAttempt.startTime ? dayjs(restQuizAttempt.startTime) : undefined,
      endTime: restQuizAttempt.endTime ? dayjs(restQuizAttempt.endTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestQuizAttempt>): HttpResponse<IQuizAttempt> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestQuizAttempt[]>): HttpResponse<IQuizAttempt[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
