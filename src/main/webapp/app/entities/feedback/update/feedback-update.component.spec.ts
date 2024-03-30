import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IQuizUser } from 'app/entities/quiz-user/quiz-user.model';
import { QuizUserService } from 'app/entities/quiz-user/service/quiz-user.service';
import { FeedbackService } from '../service/feedback.service';
import { IFeedback } from '../feedback.model';
import { FeedbackFormService } from './feedback-form.service';

import { FeedbackUpdateComponent } from './feedback-update.component';

describe('Feedback Management Update Component', () => {
  let comp: FeedbackUpdateComponent;
  let fixture: ComponentFixture<FeedbackUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let feedbackFormService: FeedbackFormService;
  let feedbackService: FeedbackService;
  let quizUserService: QuizUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), FeedbackUpdateComponent],
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
      .overrideTemplate(FeedbackUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FeedbackUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    feedbackFormService = TestBed.inject(FeedbackFormService);
    feedbackService = TestBed.inject(FeedbackService);
    quizUserService = TestBed.inject(QuizUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call QuizUser query and add missing value', () => {
      const feedback: IFeedback = { id: 456 };
      const user: IQuizUser = { id: 22938 };
      feedback.user = user;

      const quizUserCollection: IQuizUser[] = [{ id: 30526 }];
      jest.spyOn(quizUserService, 'query').mockReturnValue(of(new HttpResponse({ body: quizUserCollection })));
      const additionalQuizUsers = [user];
      const expectedCollection: IQuizUser[] = [...additionalQuizUsers, ...quizUserCollection];
      jest.spyOn(quizUserService, 'addQuizUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ feedback });
      comp.ngOnInit();

      expect(quizUserService.query).toHaveBeenCalled();
      expect(quizUserService.addQuizUserToCollectionIfMissing).toHaveBeenCalledWith(
        quizUserCollection,
        ...additionalQuizUsers.map(expect.objectContaining),
      );
      expect(comp.quizUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const feedback: IFeedback = { id: 456 };
      const user: IQuizUser = { id: 30828 };
      feedback.user = user;

      activatedRoute.data = of({ feedback });
      comp.ngOnInit();

      expect(comp.quizUsersSharedCollection).toContain(user);
      expect(comp.feedback).toEqual(feedback);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedback>>();
      const feedback = { id: 123 };
      jest.spyOn(feedbackFormService, 'getFeedback').mockReturnValue(feedback);
      jest.spyOn(feedbackService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedback });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedback }));
      saveSubject.complete();

      // THEN
      expect(feedbackFormService.getFeedback).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(feedbackService.update).toHaveBeenCalledWith(expect.objectContaining(feedback));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedback>>();
      const feedback = { id: 123 };
      jest.spyOn(feedbackFormService, 'getFeedback').mockReturnValue({ id: null });
      jest.spyOn(feedbackService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedback: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedback }));
      saveSubject.complete();

      // THEN
      expect(feedbackFormService.getFeedback).toHaveBeenCalled();
      expect(feedbackService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedback>>();
      const feedback = { id: 123 };
      jest.spyOn(feedbackService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedback });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(feedbackService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
