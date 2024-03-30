import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { QuizAttemptComponent } from './list/quiz-attempt.component';
import { QuizAttemptDetailComponent } from './detail/quiz-attempt-detail.component';
import { QuizAttemptUpdateComponent } from './update/quiz-attempt-update.component';
import QuizAttemptResolve from './route/quiz-attempt-routing-resolve.service';

const quizAttemptRoute: Routes = [
  {
    path: '',
    component: QuizAttemptComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QuizAttemptDetailComponent,
    resolve: {
      quizAttempt: QuizAttemptResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QuizAttemptUpdateComponent,
    resolve: {
      quizAttempt: QuizAttemptResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QuizAttemptUpdateComponent,
    resolve: {
      quizAttempt: QuizAttemptResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default quizAttemptRoute;
