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
import { IQuizAttempt } from '../quiz-attempt.model';
import { QuizAttemptService } from '../service/quiz-attempt.service';
import { QuizAttemptFormService } from './quiz-attempt-form.service';

import { QuizAttemptUpdateComponent } from './quiz-attempt-update.component';

describe('QuizAttempt Management Update Component', () => {
  let comp: QuizAttemptUpdateComponent;
  let fixture: ComponentFixture<QuizAttemptUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let quizAttemptFormService: QuizAttemptFormService;
  let quizAttemptService: QuizAttemptService;
  let quizService: QuizService;
  let quizUserService: QuizUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), QuizAttemptUpdateComponent],
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
      .overrideTemplate(QuizAttemptUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuizAttemptUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    quizAttemptFormService = TestBed.inject(QuizAttemptFormService);
    quizAttemptService = TestBed.inject(QuizAttemptService);
    quizService = TestBed.inject(QuizService);
    quizUserService = TestBed.inject(QuizUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Quiz query and add missing value', () => {
      const quizAttempt: IQuizAttempt = { id: 456 };
      const quiz: IQuiz = { id: 26123 };
      quizAttempt.quiz = quiz;

      const quizCollection: IQuiz[] = [{ id: 12660 }];
      jest.spyOn(quizService, 'query').mockReturnValue(of(new HttpResponse({ body: quizCollection })));
      const additionalQuizzes = [quiz];
      const expectedCollection: IQuiz[] = [...additionalQuizzes, ...quizCollection];
      jest.spyOn(quizService, 'addQuizToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quizAttempt });
      comp.ngOnInit();

      expect(quizService.query).toHaveBeenCalled();
      expect(quizService.addQuizToCollectionIfMissing).toHaveBeenCalledWith(
        quizCollection,
        ...additionalQuizzes.map(expect.objectContaining),
      );
      expect(comp.quizzesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call QuizUser query and add missing value', () => {
      const quizAttempt: IQuizAttempt = { id: 456 };
      const user: IQuizUser = { id: 11590 };
      quizAttempt.user = user;

      const quizUserCollection: IQuizUser[] = [{ id: 7247 }];
      jest.spyOn(quizUserService, 'query').mockReturnValue(of(new HttpResponse({ body: quizUserCollection })));
      const additionalQuizUsers = [user];
      const expectedCollection: IQuizUser[] = [...additionalQuizUsers, ...quizUserCollection];
      jest.spyOn(quizUserService, 'addQuizUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quizAttempt });
      comp.ngOnInit();

      expect(quizUserService.query).toHaveBeenCalled();
      expect(quizUserService.addQuizUserToCollectionIfMissing).toHaveBeenCalledWith(
        quizUserCollection,
        ...additionalQuizUsers.map(expect.objectContaining),
      );
      expect(comp.quizUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const quizAttempt: IQuizAttempt = { id: 456 };
      const quiz: IQuiz = { id: 27857 };
      quizAttempt.quiz = quiz;
      const user: IQuizUser = { id: 32050 };
      quizAttempt.user = user;

      activatedRoute.data = of({ quizAttempt });
      comp.ngOnInit();

      expect(comp.quizzesSharedCollection).toContain(quiz);
      expect(comp.quizUsersSharedCollection).toContain(user);
      expect(comp.quizAttempt).toEqual(quizAttempt);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuizAttempt>>();
      const quizAttempt = { id: 123 };
      jest.spyOn(quizAttemptFormService, 'getQuizAttempt').mockReturnValue(quizAttempt);
      jest.spyOn(quizAttemptService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quizAttempt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quizAttempt }));
      saveSubject.complete();

      // THEN
      expect(quizAttemptFormService.getQuizAttempt).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(quizAttemptService.update).toHaveBeenCalledWith(expect.objectContaining(quizAttempt));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuizAttempt>>();
      const quizAttempt = { id: 123 };
      jest.spyOn(quizAttemptFormService, 'getQuizAttempt').mockReturnValue({ id: null });
      jest.spyOn(quizAttemptService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quizAttempt: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quizAttempt }));
      saveSubject.complete();

      // THEN
      expect(quizAttemptFormService.getQuizAttempt).toHaveBeenCalled();
      expect(quizAttemptService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuizAttempt>>();
      const quizAttempt = { id: 123 };
      jest.spyOn(quizAttemptService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quizAttempt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(quizAttemptService.update).toHaveBeenCalled();
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
