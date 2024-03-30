import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IQuizAttempt } from 'app/entities/quiz-attempt/quiz-attempt.model';
import { QuizAttemptService } from 'app/entities/quiz-attempt/service/quiz-attempt.service';
import { IQuestion } from 'app/entities/question/question.model';
import { QuestionService } from 'app/entities/question/service/question.service';
import { IAnswer } from 'app/entities/answer/answer.model';
import { AnswerService } from 'app/entities/answer/service/answer.service';
import { IUserAnswer } from '../user-answer.model';
import { UserAnswerService } from '../service/user-answer.service';
import { UserAnswerFormService } from './user-answer-form.service';

import { UserAnswerUpdateComponent } from './user-answer-update.component';

describe('UserAnswer Management Update Component', () => {
  let comp: UserAnswerUpdateComponent;
  let fixture: ComponentFixture<UserAnswerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userAnswerFormService: UserAnswerFormService;
  let userAnswerService: UserAnswerService;
  let quizAttemptService: QuizAttemptService;
  let questionService: QuestionService;
  let answerService: AnswerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), UserAnswerUpdateComponent],
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
      .overrideTemplate(UserAnswerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserAnswerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userAnswerFormService = TestBed.inject(UserAnswerFormService);
    userAnswerService = TestBed.inject(UserAnswerService);
    quizAttemptService = TestBed.inject(QuizAttemptService);
    questionService = TestBed.inject(QuestionService);
    answerService = TestBed.inject(AnswerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call QuizAttempt query and add missing value', () => {
      const userAnswer: IUserAnswer = { id: 456 };
      const attempt: IQuizAttempt = { id: 4862 };
      userAnswer.attempt = attempt;

      const quizAttemptCollection: IQuizAttempt[] = [{ id: 31567 }];
      jest.spyOn(quizAttemptService, 'query').mockReturnValue(of(new HttpResponse({ body: quizAttemptCollection })));
      const additionalQuizAttempts = [attempt];
      const expectedCollection: IQuizAttempt[] = [...additionalQuizAttempts, ...quizAttemptCollection];
      jest.spyOn(quizAttemptService, 'addQuizAttemptToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userAnswer });
      comp.ngOnInit();

      expect(quizAttemptService.query).toHaveBeenCalled();
      expect(quizAttemptService.addQuizAttemptToCollectionIfMissing).toHaveBeenCalledWith(
        quizAttemptCollection,
        ...additionalQuizAttempts.map(expect.objectContaining),
      );
      expect(comp.quizAttemptsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Question query and add missing value', () => {
      const userAnswer: IUserAnswer = { id: 456 };
      const question: IQuestion = { id: 1702 };
      userAnswer.question = question;

      const questionCollection: IQuestion[] = [{ id: 24968 }];
      jest.spyOn(questionService, 'query').mockReturnValue(of(new HttpResponse({ body: questionCollection })));
      const additionalQuestions = [question];
      const expectedCollection: IQuestion[] = [...additionalQuestions, ...questionCollection];
      jest.spyOn(questionService, 'addQuestionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userAnswer });
      comp.ngOnInit();

      expect(questionService.query).toHaveBeenCalled();
      expect(questionService.addQuestionToCollectionIfMissing).toHaveBeenCalledWith(
        questionCollection,
        ...additionalQuestions.map(expect.objectContaining),
      );
      expect(comp.questionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Answer query and add missing value', () => {
      const userAnswer: IUserAnswer = { id: 456 };
      const selectedAnswer: IAnswer = { id: 13475 };
      userAnswer.selectedAnswer = selectedAnswer;

      const answerCollection: IAnswer[] = [{ id: 22113 }];
      jest.spyOn(answerService, 'query').mockReturnValue(of(new HttpResponse({ body: answerCollection })));
      const additionalAnswers = [selectedAnswer];
      const expectedCollection: IAnswer[] = [...additionalAnswers, ...answerCollection];
      jest.spyOn(answerService, 'addAnswerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userAnswer });
      comp.ngOnInit();

      expect(answerService.query).toHaveBeenCalled();
      expect(answerService.addAnswerToCollectionIfMissing).toHaveBeenCalledWith(
        answerCollection,
        ...additionalAnswers.map(expect.objectContaining),
      );
      expect(comp.answersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userAnswer: IUserAnswer = { id: 456 };
      const attempt: IQuizAttempt = { id: 18793 };
      userAnswer.attempt = attempt;
      const question: IQuestion = { id: 17269 };
      userAnswer.question = question;
      const selectedAnswer: IAnswer = { id: 15808 };
      userAnswer.selectedAnswer = selectedAnswer;

      activatedRoute.data = of({ userAnswer });
      comp.ngOnInit();

      expect(comp.quizAttemptsSharedCollection).toContain(attempt);
      expect(comp.questionsSharedCollection).toContain(question);
      expect(comp.answersSharedCollection).toContain(selectedAnswer);
      expect(comp.userAnswer).toEqual(userAnswer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserAnswer>>();
      const userAnswer = { id: 123 };
      jest.spyOn(userAnswerFormService, 'getUserAnswer').mockReturnValue(userAnswer);
      jest.spyOn(userAnswerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAnswer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userAnswer }));
      saveSubject.complete();

      // THEN
      expect(userAnswerFormService.getUserAnswer).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userAnswerService.update).toHaveBeenCalledWith(expect.objectContaining(userAnswer));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserAnswer>>();
      const userAnswer = { id: 123 };
      jest.spyOn(userAnswerFormService, 'getUserAnswer').mockReturnValue({ id: null });
      jest.spyOn(userAnswerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAnswer: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userAnswer }));
      saveSubject.complete();

      // THEN
      expect(userAnswerFormService.getUserAnswer).toHaveBeenCalled();
      expect(userAnswerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserAnswer>>();
      const userAnswer = { id: 123 };
      jest.spyOn(userAnswerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAnswer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userAnswerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareQuizAttempt', () => {
      it('Should forward to quizAttemptService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(quizAttemptService, 'compareQuizAttempt');
        comp.compareQuizAttempt(entity, entity2);
        expect(quizAttemptService.compareQuizAttempt).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareQuestion', () => {
      it('Should forward to questionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(questionService, 'compareQuestion');
        comp.compareQuestion(entity, entity2);
        expect(questionService.compareQuestion).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAnswer', () => {
      it('Should forward to answerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(answerService, 'compareAnswer');
        comp.compareAnswer(entity, entity2);
        expect(answerService.compareAnswer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
