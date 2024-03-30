import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuizUser } from '../quiz-user.model';
import { QuizUserService } from '../service/quiz-user.service';

const quizUserResolve = (route: ActivatedRouteSnapshot): Observable<null | IQuizUser> => {
  const id = route.params['id'];
  if (id) {
    return inject(QuizUserService)
      .find(id)
      .pipe(
        mergeMap((quizUser: HttpResponse<IQuizUser>) => {
          if (quizUser.body) {
            return of(quizUser.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default quizUserResolve;
