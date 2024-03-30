import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuizAnalytics, NewQuizAnalytics } from '../quiz-analytics.model';

export type PartialUpdateQuizAnalytics = Partial<IQuizAnalytics> & Pick<IQuizAnalytics, 'id'>;

export type EntityResponseType = HttpResponse<IQuizAnalytics>;
export type EntityArrayResponseType = HttpResponse<IQuizAnalytics[]>;

@Injectable({ providedIn: 'root' })
export class QuizAnalyticsService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/quiz-analytics');

  create(quizAnalytics: NewQuizAnalytics): Observable<EntityResponseType> {
    return this.http.post<IQuizAnalytics>(this.resourceUrl, quizAnalytics, { observe: 'response' });
  }

  update(quizAnalytics: IQuizAnalytics): Observable<EntityResponseType> {
    return this.http.put<IQuizAnalytics>(`${this.resourceUrl}/${this.getQuizAnalyticsIdentifier(quizAnalytics)}`, quizAnalytics, {
      observe: 'response',
    });
  }

  partialUpdate(quizAnalytics: PartialUpdateQuizAnalytics): Observable<EntityResponseType> {
    return this.http.patch<IQuizAnalytics>(`${this.resourceUrl}/${this.getQuizAnalyticsIdentifier(quizAnalytics)}`, quizAnalytics, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuizAnalytics>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuizAnalytics[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getQuizAnalyticsIdentifier(quizAnalytics: Pick<IQuizAnalytics, 'id'>): number {
    return quizAnalytics.id;
  }

  compareQuizAnalytics(o1: Pick<IQuizAnalytics, 'id'> | null, o2: Pick<IQuizAnalytics, 'id'> | null): boolean {
    return o1 && o2 ? this.getQuizAnalyticsIdentifier(o1) === this.getQuizAnalyticsIdentifier(o2) : o1 === o2;
  }

  addQuizAnalyticsToCollectionIfMissing<Type extends Pick<IQuizAnalytics, 'id'>>(
    quizAnalyticsCollection: Type[],
    ...quizAnalyticsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const quizAnalytics: Type[] = quizAnalyticsToCheck.filter(isPresent);
    if (quizAnalytics.length > 0) {
      const quizAnalyticsCollectionIdentifiers = quizAnalyticsCollection.map(quizAnalyticsItem =>
        this.getQuizAnalyticsIdentifier(quizAnalyticsItem),
      );
      const quizAnalyticsToAdd = quizAnalytics.filter(quizAnalyticsItem => {
        const quizAnalyticsIdentifier = this.getQuizAnalyticsIdentifier(quizAnalyticsItem);
        if (quizAnalyticsCollectionIdentifiers.includes(quizAnalyticsIdentifier)) {
          return false;
        }
        quizAnalyticsCollectionIdentifiers.push(quizAnalyticsIdentifier);
        return true;
      });
      return [...quizAnalyticsToAdd, ...quizAnalyticsCollection];
    }
    return quizAnalyticsCollection;
  }
}
