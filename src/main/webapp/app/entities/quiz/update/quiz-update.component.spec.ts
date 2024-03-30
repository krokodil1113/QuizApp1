import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IQuizAnalytics } from 'app/entities/quiz-analytics/quiz-analytics.model';
import { QuizAnalyticsService } from 'app/entities/quiz-analytics/service/quiz-analytics.service';
import { IQuizUser } from 'app/entities/quiz-user/quiz-user.model';
import { QuizUserService } from 'app/entities/quiz-user/service/quiz-user.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { IQuiz } from '../quiz.model';
import { QuizService } from '../service/quiz.service';
import { QuizFormService } from './quiz-form.service';

import { QuizUpdateComponent } from './quiz-update.component';

describe('Quiz Management Update Component', () => {
  let comp: QuizUpdateComponent;
  let fixture: ComponentFixture<QuizUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let quizFormService: QuizFormService;
  let quizService: QuizService;
  let quizAnalyticsService: QuizAnalyticsService;
  let quizUserService: QuizUserService;
  let categoryService: CategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), QuizUpdateComponent],
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
      .overrideTemplate(QuizUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuizUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    quizFormService = TestBed.inject(QuizFormService);
    quizService = TestBed.inject(QuizService);
    quizAnalyticsService = TestBed.inject(QuizAnalyticsService);
    quizUserService = TestBed.inject(QuizUserService);
    categoryService = TestBed.inject(CategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call quizAnalytics query and add missing value', () => {
      const quiz: IQuiz = { id: 456 };
      const quizAnalytics: IQuizAnalytics = { id: 6557 };
      quiz.quizAnalytics = quizAnalytics;

      const quizAnalyticsCollection: IQuizAnalytics[] = [{ id: 9568 }];
      jest.spyOn(quizAnalyticsService, 'query').mockReturnValue(of(new HttpResponse({ body: quizAnalyticsCollection })));
      const expectedCollection: IQuizAnalytics[] = [quizAnalytics, ...quizAnalyticsCollection];
      jest.spyOn(quizAnalyticsService, 'addQuizAnalyticsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quiz });
      comp.ngOnInit();

      expect(quizAnalyticsService.query).toHaveBeenCalled();
      expect(quizAnalyticsService.addQuizAnalyticsToCollectionIfMissing).toHaveBeenCalledWith(quizAnalyticsCollection, quizAnalytics);
      expect(comp.quizAnalyticsCollection).toEqual(expectedCollection);
    });

    it('Should call QuizUser query and add missing value', () => {
      const quiz: IQuiz = { id: 456 };
      const creator: IQuizUser = { id: 21250 };
      quiz.creator = creator;

      const quizUserCollection: IQuizUser[] = [{ id: 12927 }];
      jest.spyOn(quizUserService, 'query').mockReturnValue(of(new HttpResponse({ body: quizUserCollection })));
      const additionalQuizUsers = [creator];
      const expectedCollection: IQuizUser[] = [...additionalQuizUsers, ...quizUserCollection];
      jest.spyOn(quizUserService, 'addQuizUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quiz });
      comp.ngOnInit();

      expect(quizUserService.query).toHaveBeenCalled();
      expect(quizUserService.addQuizUserToCollectionIfMissing).toHaveBeenCalledWith(
        quizUserCollection,
        ...additionalQuizUsers.map(expect.objectContaining),
      );
      expect(comp.quizUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Category query and add missing value', () => {
      const quiz: IQuiz = { id: 456 };
      const category: ICategory = { id: 25670 };
      quiz.category = category;

      const categoryCollection: ICategory[] = [{ id: 21310 }];
      jest.spyOn(categoryService, 'query').mockReturnValue(of(new HttpResponse({ body: categoryCollection })));
      const additionalCategories = [category];
      const expectedCollection: ICategory[] = [...additionalCategories, ...categoryCollection];
      jest.spyOn(categoryService, 'addCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quiz });
      comp.ngOnInit();

      expect(categoryService.query).toHaveBeenCalled();
      expect(categoryService.addCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        categoryCollection,
        ...additionalCategories.map(expect.objectContaining),
      );
      expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const quiz: IQuiz = { id: 456 };
      const quizAnalytics: IQuizAnalytics = { id: 31036 };
      quiz.quizAnalytics = quizAnalytics;
      const creator: IQuizUser = { id: 1786 };
      quiz.creator = creator;
      const category: ICategory = { id: 30133 };
      quiz.category = category;

      activatedRoute.data = of({ quiz });
      comp.ngOnInit();

      expect(comp.quizAnalyticsCollection).toContain(quizAnalytics);
      expect(comp.quizUsersSharedCollection).toContain(creator);
      expect(comp.categoriesSharedCollection).toContain(category);
      expect(comp.quiz).toEqual(quiz);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuiz>>();
      const quiz = { id: 123 };
      jest.spyOn(quizFormService, 'getQuiz').mockReturnValue(quiz);
      jest.spyOn(quizService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quiz });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quiz }));
      saveSubject.complete();

      // THEN
      expect(quizFormService.getQuiz).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(quizService.update).toHaveBeenCalledWith(expect.objectContaining(quiz));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuiz>>();
      const quiz = { id: 123 };
      jest.spyOn(quizFormService, 'getQuiz').mockReturnValue({ id: null });
      jest.spyOn(quizService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quiz: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quiz }));
      saveSubject.complete();

      // THEN
      expect(quizFormService.getQuiz).toHaveBeenCalled();
      expect(quizService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuiz>>();
      const quiz = { id: 123 };
      jest.spyOn(quizService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quiz });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(quizService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareQuizAnalytics', () => {
      it('Should forward to quizAnalyticsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(quizAnalyticsService, 'compareQuizAnalytics');
        comp.compareQuizAnalytics(entity, entity2);
        expect(quizAnalyticsService.compareQuizAnalytics).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareQuizUser', () => {
      it('Should forward to quizUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(quizUserService, 'compareQuizUser');
        comp.compareQuizUser(entity, entity2);
        expect(quizUserService.compareQuizUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCategory', () => {
      it('Should forward to categoryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(categoryService, 'compareCategory');
        comp.compareCategory(entity, entity2);
        expect(categoryService.compareCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
