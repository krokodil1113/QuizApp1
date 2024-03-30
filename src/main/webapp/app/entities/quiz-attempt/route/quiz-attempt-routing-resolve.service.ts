import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuizAttempt } from '../quiz-attempt.model';
import { QuizAttemptService } from '../service/quiz-attempt.service';

const quizAttemptResolve = (route: ActivatedRouteSnapshot): Observable<null | IQuizAttempt> => {
  const id = route.params['id'];
  if (id) {
    return inject(QuizAttemptService)
      .find(id)
      .pipe(
        mergeMap((quizAttempt: HttpResponse<IQuizAttempt>) => {
          if (quizAttempt.body) {
            return of(quizAttempt.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default quizAttemptResolve;
