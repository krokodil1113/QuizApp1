import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuizAnalytics } from '../quiz-analytics.model';
import { QuizAnalyticsService } from '../service/quiz-analytics.service';

const quizAnalyticsResolve = (route: ActivatedRouteSnapshot): Observable<null | IQuizAnalytics> => {
  const id = route.params['id'];
  if (id) {
    return inject(QuizAnalyticsService)
      .find(id)
      .pipe(
        mergeMap((quizAnalytics: HttpResponse<IQuizAnalytics>) => {
          if (quizAnalytics.body) {
            return of(quizAnalytics.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default quizAnalyticsResolve;
