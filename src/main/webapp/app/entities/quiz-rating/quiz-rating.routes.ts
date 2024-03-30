import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { QuizRatingComponent } from './list/quiz-rating.component';
import { QuizRatingDetailComponent } from './detail/quiz-rating-detail.component';
import { QuizRatingUpdateComponent } from './update/quiz-rating-update.component';
import QuizRatingResolve from './route/quiz-rating-routing-resolve.service';

const quizRatingRoute: Routes = [
  {
    path: '',
    component: QuizRatingComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QuizRatingDetailComponent,
    resolve: {
      quizRating: QuizRatingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QuizRatingUpdateComponent,
    resolve: {
      quizRating: QuizRatingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QuizRatingUpdateComponent,
    resolve: {
      quizRating: QuizRatingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default quizRatingRoute;
