import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGameSession } from '../game-session.model';
import { GameSessionService } from '../service/game-session.service';

const gameSessionResolve = (route: ActivatedRouteSnapshot): Observable<null | IGameSession> => {
  const id = route.params['id'];
  if (id) {
    return inject(GameSessionService)
      .find(id)
      .pipe(
        mergeMap((gameSession: HttpResponse<IGameSession>) => {
          if (gameSession.body) {
            return of(gameSession.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default gameSessionResolve;
