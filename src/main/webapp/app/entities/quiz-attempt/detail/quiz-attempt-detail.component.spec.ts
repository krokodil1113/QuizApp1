import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { QuizAttemptDetailComponent } from './quiz-attempt-detail.component';

describe('QuizAttempt Management Detail Component', () => {
  let comp: QuizAttemptDetailComponent;
  let fixture: ComponentFixture<QuizAttemptDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuizAttemptDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: QuizAttemptDetailComponent,
              resolve: { quizAttempt: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(QuizAttemptDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(QuizAttemptDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load quizAttempt on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', QuizAttemptDetailComponent);

      // THEN
      expect(instance.quizAttempt).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
