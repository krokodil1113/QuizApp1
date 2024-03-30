import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { QuizAnalyticsComponent } from './list/quiz-analytics.component';
import { QuizAnalyticsDetailComponent } from './detail/quiz-analytics-detail.component';
import { QuizAnalyticsUpdateComponent } from './update/quiz-analytics-update.component';
import QuizAnalyticsResolve from './route/quiz-analytics-routing-resolve.service';

const quizAnalyticsRoute: Routes = [
  {
    path: '',
    component: QuizAnalyticsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QuizAnalyticsDetailComponent,
    resolve: {
      quizAnalytics: QuizAnalyticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QuizAnalyticsUpdateComponent,
    resolve: {
      quizAnalytics: QuizAnalyticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QuizAnalyticsUpdateComponent,
    resolve: {
      quizAnalytics: QuizAnalyticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default quizAnalyticsRoute;
