import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IQuiz } from 'app/entities/quiz/quiz.model';
import { QuizService } from 'app/entities/quiz/service/quiz.service';
import { IQuizUser } from 'app/entities/quiz-user/quiz-user.model';
import { QuizUserService } from 'app/entities/quiz-user/service/quiz-user.service';
import { IQuizRating } from '../quiz-rating.model';
import { QuizRatingService } from '../service/quiz-rating.service';
import { QuizRatingFormService } from './quiz-rating-form.service';

import { QuizRatingUpdateComponent } from './quiz-rating-update.component';

describe('QuizRating Management Update Component', () => {
  let comp: QuizRatingUpdateComponent;
  let fixture: ComponentFixture<QuizRatingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let quizRatingFormService: QuizRatingFormService;
  let quizRatingService: QuizRatingService;
  let quizService: QuizService;
  let quizUserService: QuizUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), QuizRatingUpdateComponent],
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
      .overrideTemplate(QuizRatingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuizRatingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    quizRatingFormService = TestBed.inject(QuizRatingFormService);
    quizRatingService = TestBed.inject(QuizRatingService);
    quizService = TestBed.inject(QuizService);
    quizUserService = TestBed.inject(QuizUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Quiz query and add missing value', () => {
      const quizRating: IQuizRating = { id: 456 };
      const quiz: IQuiz = { id: 1892 };
      quizRating.quiz = quiz;

      const quizCollection: IQuiz[] = [{ id: 1008 }];
      jest.spyOn(quizService, 'query').mockReturnValue(of(new HttpResponse({ body: quizCollection })));
      const additionalQuizzes = [quiz];
      const expectedCollection: IQuiz[] = [...additionalQuizzes, ...quizCollection];
      jest.spyOn(quizService, 'addQuizToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quizRating });
      comp.ngOnInit();

      expect(quizService.query).toHaveBeenCalled();
      expect(quizService.addQuizToCollectionIfMissing).toHaveBeenCalledWith(
        quizCollection,
        ...additionalQuizzes.map(expect.objectContaining),
      );
      expect(comp.quizzesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call QuizUser query and add missing value', () => {
      const quizRating: IQuizRating = { id: 456 };
      const user: IQuizUser = { id: 14179 };
      quizRating.user = user;

      const quizUserCollection: IQuizUser[] = [{ id: 24004 }];
      jest.spyOn(quizUserService, 'query').mockReturnValue(of(new HttpResponse({ body: quizUserCollection })));
      const additionalQuizUsers = [user];
      const expectedCollection: IQuizUser[] = [...additionalQuizUsers, ...quizUserCollection];
      jest.spyOn(quizUserService, 'addQuizUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quizRating });
      comp.ngOnInit();

      expect(quizUserService.query).toHaveBeenCalled();
      expect(quizUserService.addQuizUserToCollectionIfMissing).toHaveBeenCalledWith(
        quizUserCollection,
        ...additionalQuizUsers.map(expect.objectContaining),
      );
      expect(comp.quizUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const quizRating: IQuizRating = { id: 456 };
      const quiz: IQuiz = { id: 31242 };
      quizRating.quiz = quiz;
      const user: IQuizUser = { id: 8111 };
      quizRating.user = user;

      activatedRoute.data = of({ quizRating });
      comp.ngOnInit();

      expect(comp.quizzesSharedCollection).toContain(quiz);
      expect(comp.quizUsersSharedCollection).toContain(user);
      expect(comp.quizRating).toEqual(quizRating);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuizRating>>();
      const quizRating = { id: 123 };
      jest.spyOn(quizRatingFormService, 'getQuizRating').mockReturnValue(quizRating);
      jest.spyOn(quizRatingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quizRating });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quizRating }));
      saveSubject.complete();

      // THEN
      expect(quizRatingFormService.getQuizRating).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(quizRatingService.update).toHaveBeenCalledWith(expect.objectContaining(quizRating));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuizRating>>();
      const quizRating = { id: 123 };
      jest.spyOn(quizRatingFormService, 'getQuizRating').mockReturnValue({ id: null });
      jest.spyOn(quizRatingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quizRating: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quizRating }));
      saveSubject.complete();

      // THEN
      expect(quizRatingFormService.getQuizRating).toHaveBeenCalled();
      expect(quizRatingService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuizRating>>();
      const quizRating = { id: 123 };
      jest.spyOn(quizRatingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quizRating });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(quizRatingService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareQuiz', () => {
      it('Should forward to quizService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(quizService, 'compareQuiz');
        comp.compareQuiz(entity, entity2);
        expect(quizService.compareQuiz).toHaveBeenCalledWith(entity, entity2);
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
  });
});
