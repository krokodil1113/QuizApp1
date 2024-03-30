import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuizRating, NewQuizRating } from '../quiz-rating.model';

export type PartialUpdateQuizRating = Partial<IQuizRating> & Pick<IQuizRating, 'id'>;

export type EntityResponseType = HttpResponse<IQuizRating>;
export type EntityArrayResponseType = HttpResponse<IQuizRating[]>;

@Injectable({ providedIn: 'root' })
export class QuizRatingService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/quiz-ratings');

  create(quizRating: NewQuizRating): Observable<EntityResponseType> {
    return this.http.post<IQuizRating>(this.resourceUrl, quizRating, { observe: 'response' });
  }

  update(quizRating: IQuizRating): Observable<EntityResponseType> {
    return this.http.put<IQuizRating>(`${this.resourceUrl}/${this.getQuizRatingIdentifier(quizRating)}`, quizRating, {
      observe: 'response',
    });
  }

  partialUpdate(quizRating: PartialUpdateQuizRating): Observable<EntityResponseType> {
    return this.http.patch<IQuizRating>(`${this.resourceUrl}/${this.getQuizRatingIdentifier(quizRating)}`, quizRating, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuizRating>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuizRating[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getQuizRatingIdentifier(quizRating: Pick<IQuizRating, 'id'>): number {
    return quizRating.id;
  }

  compareQuizRating(o1: Pick<IQuizRating, 'id'> | null, o2: Pick<IQuizRating, 'id'> | null): boolean {
    return o1 && o2 ? this.getQuizRatingIdentifier(o1) === this.getQuizRatingIdentifier(o2) : o1 === o2;
  }

  addQuizRatingToCollectionIfMissing<Type extends Pick<IQuizRating, 'id'>>(
    quizRatingCollection: Type[],
    ...quizRatingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const quizRatings: Type[] = quizRatingsToCheck.filter(isPresent);
    if (quizRatings.length > 0) {
      const quizRatingCollectionIdentifiers = quizRatingCollection.map(quizRatingItem => this.getQuizRatingIdentifier(quizRatingItem));
      const quizRatingsToAdd = quizRatings.filter(quizRatingItem => {
        const quizRatingIdentifier = this.getQuizRatingIdentifier(quizRatingItem);
        if (quizRatingCollectionIdentifiers.includes(quizRatingIdentifier)) {
          return false;
        }
        quizRatingCollectionIdentifiers.push(quizRatingIdentifier);
        return true;
      });
      return [...quizRatingsToAdd, ...quizRatingCollection];
    }
    return quizRatingCollection;
  }
}
