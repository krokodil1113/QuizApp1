import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IQuestion } from 'app/entities/question/question.model';
import { QuestionService } from 'app/entities/question/service/question.service';
import { AnswerService } from '../service/answer.service';
import { IAnswer } from '../answer.model';
import { AnswerFormService } from './answer-form.service';

import { AnswerUpdateComponent } from './answer-update.component';

describe('Answer Management Update Component', () => {
  let comp: AnswerUpdateComponent;
  let fixture: ComponentFixture<AnswerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let answerFormService: AnswerFormService;
  let answerService: AnswerService;
  let questionService: QuestionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AnswerUpdateComponent],
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
      .overrideTemplate(AnswerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AnswerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    answerFormService = TestBed.inject(AnswerFormService);
    answerService = TestBed.inject(AnswerService);
    questionService = TestBed.inject(QuestionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Question query and add missing value', () => {
      const answer: IAnswer = { id: 456 };
      const question: IQuestion = { id: 21029 };
      answer.question = question;

      const questionCollection: IQuestion[] = [{ id: 17923 }];
      jest.spyOn(questionService, 'query').mockReturnValue(of(new HttpResponse({ body: questionCollection })));
      const additionalQuestions = [question];
      const expectedCollection: IQuestion[] = [...additionalQuestions, ...questionCollection];
      jest.spyOn(questionService, 'addQuestionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ answer });
      comp.ngOnInit();

      expect(questionService.query).toHaveBeenCalled();
      expect(questionService.addQuestionToCollectionIfMissing).toHaveBeenCalledWith(
        questionCollection,
        ...additionalQuestions.map(expect.objectContaining),
      );
      expect(comp.questionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const answer: IAnswer = { id: 456 };
      const question: IQuestion = { id: 15162 };
      answer.question = question;

      activatedRoute.data = of({ answer });
      comp.ngOnInit();

      expect(comp.questionsSharedCollection).toContain(question);
      expect(comp.answer).toEqual(answer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnswer>>();
      const answer = { id: 123 };
      jest.spyOn(answerFormService, 'getAnswer').mockReturnValue(answer);
      jest.spyOn(answerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ answer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: answer }));
      saveSubject.complete();

      // THEN
      expect(answerFormService.getAnswer).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(answerService.update).toHaveBeenCalledWith(expect.objectContaining(answer));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnswer>>();
      const answer = { id: 123 };
      jest.spyOn(answerFormService, 'getAnswer').mockReturnValue({ id: null });
      jest.spyOn(answerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ answer: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: answer }));
      saveSubject.complete();

      // THEN
      expect(answerFormService.getAnswer).toHaveBeenCalled();
      expect(answerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnswer>>();
      const answer = { id: 123 };
      jest.spyOn(answerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ answer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(answerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareQuestion', () => {
      it('Should forward to questionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(questionService, 'compareQuestion');
        comp.compareQuestion(entity, entity2);
        expect(questionService.compareQuestion).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
