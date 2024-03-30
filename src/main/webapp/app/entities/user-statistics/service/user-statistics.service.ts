import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserStatistics, NewUserStatistics } from '../user-statistics.model';

export type PartialUpdateUserStatistics = Partial<IUserStatistics> & Pick<IUserStatistics, 'id'>;

export type EntityResponseType = HttpResponse<IUserStatistics>;
export type EntityArrayResponseType = HttpResponse<IUserStatistics[]>;

@Injectable({ providedIn: 'root' })
export class UserStatisticsService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-statistics');

  create(userStatistics: NewUserStatistics): Observable<EntityResponseType> {
    return this.http.post<IUserStatistics>(this.resourceUrl, userStatistics, { observe: 'response' });
  }

  update(userStatistics: IUserStatistics): Observable<EntityResponseType> {
    return this.http.put<IUserStatistics>(`${this.resourceUrl}/${this.getUserStatisticsIdentifier(userStatistics)}`, userStatistics, {
      observe: 'response',
    });
  }

  partialUpdate(userStatistics: PartialUpdateUserStatistics): Observable<EntityResponseType> {
    return this.http.patch<IUserStatistics>(`${this.resourceUrl}/${this.getUserStatisticsIdentifier(userStatistics)}`, userStatistics, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserStatistics>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserStatistics[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserStatisticsIdentifier(userStatistics: Pick<IUserStatistics, 'id'>): number {
    return userStatistics.id;
  }

  compareUserStatistics(o1: Pick<IUserStatistics, 'id'> | null, o2: Pick<IUserStatistics, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserStatisticsIdentifier(o1) === this.getUserStatisticsIdentifier(o2) : o1 === o2;
  }

  addUserStatisticsToCollectionIfMissing<Type extends Pick<IUserStatistics, 'id'>>(
    userStatisticsCollection: Type[],
    ...userStatisticsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userStatistics: Type[] = userStatisticsToCheck.filter(isPresent);
    if (userStatistics.length > 0) {
      const userStatisticsCollectionIdentifiers = userStatisticsCollection.map(userStatisticsItem =>
        this.getUserStatisticsIdentifier(userStatisticsItem),
      );
      const userStatisticsToAdd = userStatistics.filter(userStatisticsItem => {
        const userStatisticsIdentifier = this.getUserStatisticsIdentifier(userStatisticsItem);
        if (userStatisticsCollectionIdentifiers.includes(userStatisticsIdentifier)) {
          return false;
        }
        userStatisticsCollectionIdentifiers.push(userStatisticsIdentifier);
        return true;
      });
      return [...userStatisticsToAdd, ...userStatisticsCollection];
    }
    return userStatisticsCollection;
  }
}
