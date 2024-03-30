import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQuizAttempt } from '../quiz-attempt.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../quiz-attempt.test-samples';

import { QuizAttemptService, RestQuizAttempt } from './quiz-attempt.service';

const requireRestSample: RestQuizAttempt = {
  ...sampleWithRequiredData,
  startTime: sampleWithRequiredData.startTime?.toJSON(),
  endTime: sampleWithRequiredData.endTime?.toJSON(),
};

describe('QuizAttempt Service', () => {
  let service: QuizAttemptService;
  let httpMock: HttpTestingController;
  let expectedResult: IQuizAttempt | IQuizAttempt[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(QuizAttemptService);
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

    it('should create a QuizAttempt', () => {
      const quizAttempt = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(quizAttempt).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a QuizAttempt', () => {
      const quizAttempt = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(quizAttempt).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a QuizAttempt', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of QuizAttempt', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a QuizAttempt', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addQuizAttemptToCollectionIfMissing', () => {
      it('should add a QuizAttempt to an empty array', () => {
        const quizAttempt: IQuizAttempt = sampleWithRequiredData;
        expectedResult = service.addQuizAttemptToCollectionIfMissing([], quizAttempt);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(quizAttempt);
      });

      it('should not add a QuizAttempt to an array that contains it', () => {
        const quizAttempt: IQuizAttempt = sampleWithRequiredData;
        const quizAttemptCollection: IQuizAttempt[] = [
          {
            ...quizAttempt,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addQuizAttemptToCollectionIfMissing(quizAttemptCollection, quizAttempt);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a QuizAttempt to an array that doesn't contain it", () => {
        const quizAttempt: IQuizAttempt = sampleWithRequiredData;
        const quizAttemptCollection: IQuizAttempt[] = [sampleWithPartialData];
        expectedResult = service.addQuizAttemptToCollectionIfMissing(quizAttemptCollection, quizAttempt);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(quizAttempt);
      });

      it('should add only unique QuizAttempt to an array', () => {
        const quizAttemptArray: IQuizAttempt[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const quizAttemptCollection: IQuizAttempt[] = [sampleWithRequiredData];
        expectedResult = service.addQuizAttemptToCollectionIfMissing(quizAttemptCollection, ...quizAttemptArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const quizAttempt: IQuizAttempt = sampleWithRequiredData;
        const quizAttempt2: IQuizAttempt = sampleWithPartialData;
        expectedResult = service.addQuizAttemptToCollectionIfMissing([], quizAttempt, quizAttempt2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(quizAttempt);
        expect(expectedResult).toContain(quizAttempt2);
      });

      it('should accept null and undefined values', () => {
        const quizAttempt: IQuizAttempt = sampleWithRequiredData;
        expectedResult = service.addQuizAttemptToCollectionIfMissing([], null, quizAttempt, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(quizAttempt);
      });

      it('should return initial array if no QuizAttempt is added', () => {
        const quizAttemptCollection: IQuizAttempt[] = [sampleWithRequiredData];
        expectedResult = service.addQuizAttemptToCollectionIfMissing(quizAttemptCollection, undefined, null);
        expect(expectedResult).toEqual(quizAttemptCollection);
      });
    });

    describe('compareQuizAttempt', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareQuizAttempt(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareQuizAttempt(entity1, entity2);
        const compareResult2 = service.compareQuizAttempt(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareQuizAttempt(entity1, entity2);
        const compareResult2 = service.compareQuizAttempt(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareQuizAttempt(entity1, entity2);
        const compareResult2 = service.compareQuizAttempt(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
