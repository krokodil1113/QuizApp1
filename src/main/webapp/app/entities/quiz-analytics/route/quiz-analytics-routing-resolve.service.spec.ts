import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IQuizAnalytics } from '../quiz-analytics.model';
import { QuizAnalyticsService } from '../service/quiz-analytics.service';

import quizAnalyticsResolve from './quiz-analytics-routing-resolve.service';

describe('QuizAnalytics routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: QuizAnalyticsService;
  let resultQuizAnalytics: IQuizAnalytics | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(QuizAnalyticsService);
    resultQuizAnalytics = undefined;
  });

  describe('resolve', () => {
    it('should return IQuizAnalytics returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        quizAnalyticsResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultQuizAnalytics = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultQuizAnalytics).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        quizAnalyticsResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultQuizAnalytics = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultQuizAnalytics).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IQuizAnalytics>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        quizAnalyticsResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultQuizAnalytics = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultQuizAnalytics).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
