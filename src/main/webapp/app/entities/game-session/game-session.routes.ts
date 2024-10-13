import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { GameSessionComponent } from './list/game-session.component';
import { GameSessionDetailComponent } from './detail/game-session-detail.component';
import { GameSessionUpdateComponent } from './update/game-session-update.component';
import GameSessionResolve from './route/game-session-routing-resolve.service';

const gameSessionRoute: Routes = [
  {
    path: '',
    component: GameSessionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GameSessionDetailComponent,
    resolve: {
      gameSession: GameSessionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GameSessionUpdateComponent,
    resolve: {
      gameSession: GameSessionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GameSessionUpdateComponent,
    resolve: {
      gameSession: GameSessionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default gameSessionRoute;
