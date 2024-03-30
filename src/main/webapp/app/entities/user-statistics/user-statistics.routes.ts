import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { UserStatisticsComponent } from './list/user-statistics.component';
import { UserStatisticsDetailComponent } from './detail/user-statistics-detail.component';
import { UserStatisticsUpdateComponent } from './update/user-statistics-update.component';
import UserStatisticsResolve from './route/user-statistics-routing-resolve.service';

const userStatisticsRoute: Routes = [
  {
    path: '',
    component: UserStatisticsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserStatisticsDetailComponent,
    resolve: {
      userStatistics: UserStatisticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserStatisticsUpdateComponent,
    resolve: {
      userStatistics: UserStatisticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserStatisticsUpdateComponent,
    resolve: {
      userStatistics: UserStatisticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default userStatisticsRoute;
