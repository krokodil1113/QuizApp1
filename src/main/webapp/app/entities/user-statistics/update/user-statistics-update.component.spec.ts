import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IQuizUser } from 'app/entities/quiz-user/quiz-user.model';
import { QuizUserService } from 'app/entities/quiz-user/service/quiz-user.service';
import { UserStatisticsService } from '../service/user-statistics.service';
import { IUserStatistics } from '../user-statistics.model';
import { UserStatisticsFormService } from './user-statistics-form.service';

import { UserStatisticsUpdateComponent } from './user-statistics-update.component';

describe('UserStatistics Management Update Component', () => {
  let comp: UserStatisticsUpdateComponent;
  let fixture: ComponentFixture<UserStatisticsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userStatisticsFormService: UserStatisticsFormService;
  let userStatisticsService: UserStatisticsService;
  let quizUserService: QuizUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), UserStatisticsUpdateComponent],
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
      .overrideTemplate(UserStatisticsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserStatisticsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userStatisticsFormService = TestBed.inject(UserStatisticsFormService);
    userStatisticsService = TestBed.inject(UserStatisticsService);
    quizUserService = TestBed.inject(QuizUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call quizUser query and add missing value', () => {
      const userStatistics: IUserStatistics = { id: 456 };
      const quizUser: IQuizUser = { id: 5047 };
      userStatistics.quizUser = quizUser;

      const quizUserCollection: IQuizUser[] = [{ id: 10787 }];
      jest.spyOn(quizUserService, 'query').mockReturnValue(of(new HttpResponse({ body: quizUserCollection })));
      const expectedCollection: IQuizUser[] = [quizUser, ...quizUserCollection];
      jest.spyOn(quizUserService, 'addQuizUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userStatistics });
      comp.ngOnInit();

      expect(quizUserService.query).toHaveBeenCalled();
      expect(quizUserService.addQuizUserToCollectionIfMissing).toHaveBeenCalledWith(quizUserCollection, quizUser);
      expect(comp.quizUsersCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userStatistics: IUserStatistics = { id: 456 };
      const quizUser: IQuizUser = { id: 9554 };
      userStatistics.quizUser = quizUser;

      activatedRoute.data = of({ userStatistics });
      comp.ngOnInit();

      expect(comp.quizUsersCollection).toContain(quizUser);
      expect(comp.userStatistics).toEqual(userStatistics);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserStatistics>>();
      const userStatistics = { id: 123 };
      jest.spyOn(userStatisticsFormService, 'getUserStatistics').mockReturnValue(userStatistics);
      jest.spyOn(userStatisticsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userStatistics });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userStatistics }));
      saveSubject.complete();

      // THEN
      expect(userStatisticsFormService.getUserStatistics).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userStatisticsService.update).toHaveBeenCalledWith(expect.objectContaining(userStatistics));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserStatistics>>();
      const userStatistics = { id: 123 };
      jest.spyOn(userStatisticsFormService, 'getUserStatistics').mockReturnValue({ id: null });
      jest.spyOn(userStatisticsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userStatistics: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userStatistics }));
      saveSubject.complete();

      // THEN
      expect(userStatisticsFormService.getUserStatistics).toHaveBeenCalled();
      expect(userStatisticsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserStatistics>>();
      const userStatistics = { id: 123 };
      jest.spyOn(userStatisticsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userStatistics });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userStatisticsService.update).toHaveBeenCalled();
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
