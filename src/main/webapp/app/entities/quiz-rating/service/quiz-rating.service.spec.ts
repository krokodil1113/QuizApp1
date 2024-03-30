import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQuizRating } from '../quiz-rating.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../quiz-rating.test-samples';

import { QuizRatingService } from './quiz-rating.service';

const requireRestSample: IQuizRating = {
  ...sampleWithRequiredData,
};

describe('QuizRating Service', () => {
  let service: QuizRatingService;
  let httpMock: HttpTestingController;
  let expectedResult: IQuizRating | IQuizRating[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(QuizRatingService);
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

    it('should create a QuizRating', () => {
      const quizRating = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(quizRating).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a QuizRating', () => {
      const quizRating = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(quizRating).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a QuizRating', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of QuizRating', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a QuizRating', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addQuizRatingToCollectionIfMissing', () => {
      it('should add a QuizRating to an empty array', () => {
        const quizRating: IQuizRating = sampleWithRequiredData;
        expectedResult = service.addQuizRatingToCollectionIfMissing([], quizRating);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(quizRating);
      });

      it('should not add a QuizRating to an array that contains it', () => {
        const quizRating: IQuizRating = sampleWithRequiredData;
        const quizRatingCollection: IQuizRating[] = [
          {
            ...quizRating,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addQuizRatingToCollectionIfMissing(quizRatingCollection, quizRating);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a QuizRating to an array that doesn't contain it", () => {
        const quizRating: IQuizRating = sampleWithRequiredData;
        const quizRatingCollection: IQuizRating[] = [sampleWithPartialData];
        expectedResult = service.addQuizRatingToCollectionIfMissing(quizRatingCollection, quizRating);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(quizRating);
      });

      it('should add only unique QuizRating to an array', () => {
        const quizRatingArray: IQuizRating[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const quizRatingCollection: IQuizRating[] = [sampleWithRequiredData];
        expectedResult = service.addQuizRatingToCollectionIfMissing(quizRatingCollection, ...quizRatingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const quizRating: IQuizRating = sampleWithRequiredData;
        const quizRating2: IQuizRating = sampleWithPartialData;
        expectedResult = service.addQuizRatingToCollectionIfMissing([], quizRating, quizRating2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(quizRating);
        expect(expectedResult).toContain(quizRating2);
      });

      it('should accept null and undefined values', () => {
        const quizRating: IQuizRating = sampleWithRequiredData;
        expectedResult = service.addQuizRatingToCollectionIfMissing([], null, quizRating, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(quizRating);
      });

      it('should return initial array if no QuizRating is added', () => {
        const quizRatingCollection: IQuizRating[] = [sampleWithRequiredData];
        expectedResult = service.addQuizRatingToCollectionIfMissing(quizRatingCollection, undefined, null);
        expect(expectedResult).toEqual(quizRatingCollection);
      });
    });

    describe('compareQuizRating', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareQuizRating(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareQuizRating(entity1, entity2);
        const compareResult2 = service.compareQuizRating(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareQuizRating(entity1, entity2);
        const compareResult2 = service.compareQuizRating(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareQuizRating(entity1, entity2);
        const compareResult2 = service.compareQuizRating(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
