import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { QuizAnalyticsService } from '../service/quiz-analytics.service';
import { IQuizAnalytics } from '../quiz-analytics.model';
import { QuizAnalyticsFormService } from './quiz-analytics-form.service';

import { QuizAnalyticsUpdateComponent } from './quiz-analytics-update.component';

describe('QuizAnalytics Management Update Component', () => {
  let comp: QuizAnalyticsUpdateComponent;
  let fixture: ComponentFixture<QuizAnalyticsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let quizAnalyticsFormService: QuizAnalyticsFormService;
  let quizAnalyticsService: QuizAnalyticsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), QuizAnalyticsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(QuizAnalyticsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuizAnalyticsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    quizAnalyticsFormService = TestBed.inject(QuizAnalyticsFormService);
    quizAnalyticsService = TestBed.inject(QuizAnalyticsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const quizAnalytics: IQuizAnalytics = { id: 456 };

      activatedRoute.data = of({ quizAnalytics });
      comp.ngOnInit();

      expect(comp.quizAnalytics).toEqual(quizAnalytics);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuizAnalytics>>();
      const quizAnalytics = { id: 123 };
      jest.spyOn(quizAnalyticsFormService, 'getQuizAnalytics').mockReturnValue(quizAnalytics);
      jest.spyOn(quizAnalyticsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quizAnalytics });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quizAnalytics }));
      saveSubject.complete();

      // THEN
      expect(quizAnalyticsFormService.getQuizAnalytics).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(quizAnalyticsService.update).toHaveBeenCalledWith(expect.objectContaining(quizAnalytics));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuizAnalytics>>();
      const quizAnalytics = { id: 123 };
      jest.spyOn(quizAnalyticsFormService, 'getQuizAnalytics').mockReturnValue({ id: null });
      jest.spyOn(quizAnalyticsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quizAnalytics: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quizAnalytics }));
      saveSubject.complete();

      // THEN
      expect(quizAnalyticsFormService.getQuizAnalytics).toHaveBeenCalled();
      expect(quizAnalyticsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuizAnalytics>>();
      const quizAnalytics = { id: 123 };
      jest.spyOn(quizAnalyticsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quizAnalytics });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(quizAnalyticsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
