import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserAnswer, NewUserAnswer } from '../user-answer.model';

export type PartialUpdateUserAnswer = Partial<IUserAnswer> & Pick<IUserAnswer, 'id'>;

export type EntityResponseType = HttpResponse<IUserAnswer>;
export type EntityArrayResponseType = HttpResponse<IUserAnswer[]>;

@Injectable({ providedIn: 'root' })
export class UserAnswerService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-answers');

  create(userAnswer: NewUserAnswer): Observable<EntityResponseType> {
    return this.http.post<IUserAnswer>(this.resourceUrl, userAnswer, { observe: 'response' });
  }

  update(userAnswer: IUserAnswer): Observable<EntityResponseType> {
    return this.http.put<IUserAnswer>(`${this.resourceUrl}/${this.getUserAnswerIdentifier(userAnswer)}`, userAnswer, {
      observe: 'response',
    });
  }

  partialUpdate(userAnswer: PartialUpdateUserAnswer): Observable<EntityResponseType> {
    return this.http.patch<IUserAnswer>(`${this.resourceUrl}/${this.getUserAnswerIdentifier(userAnswer)}`, userAnswer, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserAnswer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserAnswer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserAnswerIdentifier(userAnswer: Pick<IUserAnswer, 'id'>): number {
    return userAnswer.id;
  }

  compareUserAnswer(o1: Pick<IUserAnswer, 'id'> | null, o2: Pick<IUserAnswer, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserAnswerIdentifier(o1) === this.getUserAnswerIdentifier(o2) : o1 === o2;
  }

  addUserAnswerToCollectionIfMissing<Type extends Pick<IUserAnswer, 'id'>>(
    userAnswerCollection: Type[],
    ...userAnswersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userAnswers: Type[] = userAnswersToCheck.filter(isPresent);
    if (userAnswers.length > 0) {
      const userAnswerCollectionIdentifiers = userAnswerCollection.map(userAnswerItem => this.getUserAnswerIdentifier(userAnswerItem));
      const userAnswersToAdd = userAnswers.filter(userAnswerItem => {
        const userAnswerIdentifier = this.getUserAnswerIdentifier(userAnswerItem);
        if (userAnswerCollectionIdentifiers.includes(userAnswerIdentifier)) {
          return false;
        }
        userAnswerCollectionIdentifiers.push(userAnswerIdentifier);
        return true;
      });
      return [...userAnswersToAdd, ...userAnswerCollection];
    }
    return userAnswerCollection;
  }
}
