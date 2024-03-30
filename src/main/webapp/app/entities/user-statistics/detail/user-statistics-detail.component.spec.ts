import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { UserStatisticsDetailComponent } from './user-statistics-detail.component';

describe('UserStatistics Management Detail Component', () => {
  let comp: UserStatisticsDetailComponent;
  let fixture: ComponentFixture<UserStatisticsDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserStatisticsDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: UserStatisticsDetailComponent,
              resolve: { userStatistics: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UserStatisticsDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserStatisticsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load userStatistics on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UserStatisticsDetailComponent);

      // THEN
      expect(instance.userStatistics).toEqual(expect.objectContaining({ id: 123 }));
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
