import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuizUser, NewQuizUser } from '../quiz-user.model';

export type PartialUpdateQuizUser = Partial<IQuizUser> & Pick<IQuizUser, 'id'>;

export type EntityResponseType = HttpResponse<IQuizUser>;
export type EntityArrayResponseType = HttpResponse<IQuizUser[]>;

@Injectable({ providedIn: 'root' })
export class QuizUserService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/quiz-users');

  create(quizUser: NewQuizUser): Observable<EntityResponseType> {
    return this.http.post<IQuizUser>(this.resourceUrl, quizUser, { observe: 'response' });
  }

  update(quizUser: IQuizUser): Observable<EntityResponseType> {
    return this.http.put<IQuizUser>(`${this.resourceUrl}/${this.getQuizUserIdentifier(quizUser)}`, quizUser, { observe: 'response' });
  }

  partialUpdate(quizUser: PartialUpdateQuizUser): Observable<EntityResponseType> {
    return this.http.patch<IQuizUser>(`${this.resourceUrl}/${this.getQuizUserIdentifier(quizUser)}`, quizUser, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuizUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuizUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getQuizUserIdentifier(quizUser: Pick<IQuizUser, 'id'>): number {
    return quizUser.id;
  }

  compareQuizUser(o1: Pick<IQuizUser, 'id'> | null, o2: Pick<IQuizUser, 'id'> | null): boolean {
    return o1 && o2 ? this.getQuizUserIdentifier(o1) === this.getQuizUserIdentifier(o2) : o1 === o2;
  }

  addQuizUserToCollectionIfMissing<Type extends Pick<IQuizUser, 'id'>>(
    quizUserCollection: Type[],
    ...quizUsersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const quizUsers: Type[] = quizUsersToCheck.filter(isPresent);
    if (quizUsers.length > 0) {
      const quizUserCollectionIdentifiers = quizUserCollection.map(quizUserItem => this.getQuizUserIdentifier(quizUserItem));
      const quizUsersToAdd = quizUsers.filter(quizUserItem => {
        const quizUserIdentifier = this.getQuizUserIdentifier(quizUserItem);
        if (quizUserCollectionIdentifiers.includes(quizUserIdentifier)) {
          return false;
        }
        quizUserCollectionIdentifiers.push(quizUserIdentifier);
        return true;
      });
      return [...quizUsersToAdd, ...quizUserCollection];
    }
    return quizUserCollection;
  }
}
