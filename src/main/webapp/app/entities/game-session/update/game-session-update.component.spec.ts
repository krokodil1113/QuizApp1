import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GameSessionService } from '../service/game-session.service';
import { IGameSession } from '../game-session.model';
import { GameSessionFormService } from './game-session-form.service';

import { GameSessionUpdateComponent } from './game-session-update.component';

describe('GameSession Management Update Component', () => {
  let comp: GameSessionUpdateComponent;
  let fixture: ComponentFixture<GameSessionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gameSessionFormService: GameSessionFormService;
  let gameSessionService: GameSessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), GameSessionUpdateComponent],
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
      .overrideTemplate(GameSessionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GameSessionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gameSessionFormService = TestBed.inject(GameSessionFormService);
    gameSessionService = TestBed.inject(GameSessionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const gameSession: IGameSession = { id: 456 };

      activatedRoute.data = of({ gameSession });
      comp.ngOnInit();

      expect(comp.gameSession).toEqual(gameSession);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameSession>>();
      const gameSession = { id: 123 };
      jest.spyOn(gameSessionFormService, 'getGameSession').mockReturnValue(gameSession);
      jest.spyOn(gameSessionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameSession });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gameSession }));
      saveSubject.complete();

      // THEN
      expect(gameSessionFormService.getGameSession).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(gameSessionService.update).toHaveBeenCalledWith(expect.objectContaining(gameSession));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameSession>>();
      const gameSession = { id: 123 };
      jest.spyOn(gameSessionFormService, 'getGameSession').mockReturnValue({ id: null });
      jest.spyOn(gameSessionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameSession: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gameSession }));
      saveSubject.complete();

      // THEN
      expect(gameSessionFormService.getGameSession).toHaveBeenCalled();
      expect(gameSessionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameSession>>();
      const gameSession = { id: 123 };
      jest.spyOn(gameSessionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameSession });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gameSessionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
