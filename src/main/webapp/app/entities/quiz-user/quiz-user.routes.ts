import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { QuizUserComponent } from './list/quiz-user.component';
import { QuizUserDetailComponent } from './detail/quiz-user-detail.component';
import { QuizUserUpdateComponent } from './update/quiz-user-update.component';
import QuizUserResolve from './route/quiz-user-routing-resolve.service';

const quizUserRoute: Routes = [
  {
    path: '',
    component: QuizUserComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QuizUserDetailComponent,
    resolve: {
      quizUser: QuizUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QuizUserUpdateComponent,
    resolve: {
      quizUser: QuizUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QuizUserUpdateComponent,
    resolve: {
      quizUser: QuizUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default quizUserRoute;
