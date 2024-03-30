import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQuizAnalytics } from '../quiz-analytics.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../quiz-analytics.test-samples';

import { QuizAnalyticsService } from './quiz-analytics.service';

const requireRestSample: IQuizAnalytics = {
  ...sampleWithRequiredData,
};

describe('QuizAnalytics Service', () => {
  let service: QuizAnalyticsService;
  let httpMock: HttpTestingController;
  let expectedResult: IQuizAnalytics | IQuizAnalytics[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(QuizAnalyticsService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a QuizAnalytics', () => {
      const quizAnalytics = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(quizAnalytics).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a QuizAnalytics', () => {
      const quizAnalytics = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(quizAnalytics).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a QuizAnalytics', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of QuizAnalytics', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a QuizAnalytics', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addQuizAnalyticsToCollectionIfMissing', () => {
      it('should add a QuizAnalytics to an empty array', () => {
        const quizAnalytics: IQuizAnalytics = sampleWithRequiredData;
        expectedResult = service.addQuizAnalyticsToCollectionIfMissing([], quizAnalytics);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(quizAnalytics);
      });

      it('should not add a QuizAnalytics to an array that contains it', () => {
        const quizAnalytics: IQuizAnalytics = sampleWithRequiredData;
        const quizAnalyticsCollection: IQuizAnalytics[] = [
          {
            ...quizAnalytics,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addQuizAnalyticsToCollectionIfMissing(quizAnalyticsCollection, quizAnalytics);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a QuizAnalytics to an array that doesn't contain it", () => {
        const quizAnalytics: IQuizAnalytics = sampleWithRequiredData;
        const quizAnalyticsCollection: IQuizAnalytics[] = [sampleWithPartialData];
        expectedResult = service.addQuizAnalyticsToCollectionIfMissing(quizAnalyticsCollection, quizAnalytics);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(quizAnalytics);
      });

      it('should add only unique QuizAnalytics to an array', () => {
        const quizAnalyticsArray: IQuizAnalytics[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const quizAnalyticsCollection: IQuizAnalytics[] = [sampleWithRequiredData];
        expectedResult = service.addQuizAnalyticsToCollectionIfMissing(quizAnalyticsCollection, ...quizAnalyticsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const quizAnalytics: IQuizAnalytics = sampleWithRequiredData;
        const quizAnalytics2: IQuizAnalytics = sampleWithPartialData;
        expectedResult = service.addQuizAnalyticsToCollectionIfMissing([], quizAnalytics, quizAnalytics2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(quizAnalytics);
        expect(expectedResult).toContain(quizAnalytics2);
      });

      it('should accept null and undefined values', () => {
        const quizAnalytics: IQuizAnalytics = sampleWithRequiredData;
        expectedResult = service.addQuizAnalyticsToCollectionIfMissing([], null, quizAnalytics, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(quizAnalytics);
      });

      it('should return initial array if no QuizAnalytics is added', () => {
        const quizAnalyticsCollection: IQuizAnalytics[] = [sampleWithRequiredData];
        expectedResult = service.addQuizAnalyticsToCollectionIfMissing(quizAnalyticsCollection, undefined, null);
        expect(expectedResult).toEqual(quizAnalyticsCollection);
      });
    });

    describe('compareQuizAnalytics', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareQuizAnalytics(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareQuizAnalytics(entity1, entity2);
        const compareResult2 = service.compareQuizAnalytics(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareQuizAnalytics(entity1, entity2);
        const compareResult2 = service.compareQuizAnalytics(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareQuizAnalytics(entity1, entity2);
        const compareResult2 = service.compareQuizAnalytics(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
