import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'quizApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'quiz',
    data: { pageTitle: 'quizApp.quiz.home.title' },
    loadChildren: () => import('./quiz/quiz.routes'),
  },
  {
    path: 'category',
    data: { pageTitle: 'quizApp.category.home.title' },
    loadChildren: () => import('./category/category.routes'),
  },
  {
    path: 'question',
    data: { pageTitle: 'quizApp.question.home.title' },
    loadChildren: () => import('./question/question.routes'),
  },
  {
    path: 'answer',
    data: { pageTitle: 'quizApp.answer.home.title' },
    loadChildren: () => import('./answer/answer.routes'),
  },
  {
    path: 'quiz-attempt',
    data: { pageTitle: 'quizApp.quizAttempt.home.title' },
    loadChildren: () => import('./quiz-attempt/quiz-attempt.routes'),
  },
  {
    path: 'user-answer',
    data: { pageTitle: 'quizApp.userAnswer.home.title' },
    loadChildren: () => import('./user-answer/user-answer.routes'),
  },
  {
    path: 'quiz-rating',
    data: { pageTitle: 'quizApp.quizRating.home.title' },
    loadChildren: () => import('./quiz-rating/quiz-rating.routes'),
  },
  {
    path: 'feedback',
    data: { pageTitle: 'quizApp.feedback.home.title' },
    loadChildren: () => import('./feedback/feedback.routes'),
  },
  {
    path: 'quiz-analytics',
    data: { pageTitle: 'quizApp.quizAnalytics.home.title' },
    loadChildren: () => import('./quiz-analytics/quiz-analytics.routes'),
  },
  {
    path: 'quiz-user',
    data: { pageTitle: 'quizApp.quizUser.home.title' },
    loadChildren: () => import('./quiz-user/quiz-user.routes'),
  },
  {
    path: 'user-statistics',
    data: { pageTitle: 'quizApp.userStatistics.home.title' },
    loadChildren: () => import('./user-statistics/user-statistics.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
