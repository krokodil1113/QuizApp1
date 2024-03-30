import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuizRating } from '../quiz-rating.model';
import { QuizRatingService } from '../service/quiz-rating.service';

const quizRatingResolve = (route: ActivatedRouteSnapshot): Observable<null | IQuizRating> => {
  const id = route.params['id'];
  if (id) {
    return inject(QuizRatingService)
      .find(id)
      .pipe(
        mergeMap((quizRating: HttpResponse<IQuizRating>) => {
          if (quizRating.body) {
            return of(quizRating.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default quizRatingResolve;
