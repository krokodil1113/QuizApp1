import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { QuizAnalyticsDetailComponent } from './quiz-analytics-detail.component';

describe('QuizAnalytics Management Detail Component', () => {
  let comp: QuizAnalyticsDetailComponent;
  let fixture: ComponentFixture<QuizAnalyticsDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuizAnalyticsDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: QuizAnalyticsDetailComponent,
              resolve: { quizAnalytics: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(QuizAnalyticsDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(QuizAnalyticsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load quizAnalytics on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', QuizAnalyticsDetailComponent);

      // THEN
      expect(instance.quizAnalytics).toEqual(expect.objectContaining({ id: 123 }));
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
