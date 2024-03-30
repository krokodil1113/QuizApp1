import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserStatistics } from '../user-statistics.model';
import { UserStatisticsService } from '../service/user-statistics.service';

const userStatisticsResolve = (route: ActivatedRouteSnapshot): Observable<null | IUserStatistics> => {
  const id = route.params['id'];
  if (id) {
    return inject(UserStatisticsService)
      .find(id)
      .pipe(
        mergeMap((userStatistics: HttpResponse<IUserStatistics>) => {
          if (userStatistics.body) {
            return of(userStatistics.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default userStatisticsResolve;
