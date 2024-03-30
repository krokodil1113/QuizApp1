import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { QuizUserService } from '../service/quiz-user.service';
import { IQuizUser } from '../quiz-user.model';
import { QuizUserFormService } from './quiz-user-form.service';

import { QuizUserUpdateComponent } from './quiz-user-update.component';

describe('QuizUser Management Update Component', () => {
  let comp: QuizUserUpdateComponent;
  let fixture: ComponentFixture<QuizUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let quizUserFormService: QuizUserFormService;
  let quizUserService: QuizUserService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), QuizUserUpdateComponent],
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
      .overrideTemplate(QuizUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuizUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    quizUserFormService = TestBed.inject(QuizUserFormService);
    quizUserService = TestBed.inject(QuizUserService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const quizUser: IQuizUser = { id: 456 };
      const user: IUser = { id: 22655 };
      quizUser.user = user;

      const userCollection: IUser[] = [{ id: 11798 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quizUser });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const quizUser: IQuizUser = { id: 456 };
      const user: IUser = { id: 29133 };
      quizUser.user = user;

      activatedRoute.data = of({ quizUser });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.quizUser).toEqual(quizUser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuizUser>>();
      const quizUser = { id: 123 };
      jest.spyOn(quizUserFormService, 'getQuizUser').mockReturnValue(quizUser);
      jest.spyOn(quizUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quizUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quizUser }));
      saveSubject.complete();

      // THEN
      expect(quizUserFormService.getQuizUser).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(quizUserService.update).toHaveBeenCalledWith(expect.objectContaining(quizUser));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuizUser>>();
      const quizUser = { id: 123 };
      jest.spyOn(quizUserFormService, 'getQuizUser').mockReturnValue({ id: null });
      jest.spyOn(quizUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quizUser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quizUser }));
      saveSubject.complete();

      // THEN
      expect(quizUserFormService.getQuizUser).toHaveBeenCalled();
      expect(quizUserService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuizUser>>();
      const quizUser = { id: 123 };
      jest.spyOn(quizUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quizUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(quizUserService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
