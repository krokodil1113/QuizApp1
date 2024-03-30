import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQuizUser } from '../quiz-user.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../quiz-user.test-samples';

import { QuizUserService } from './quiz-user.service';

const requireRestSample: IQuizUser = {
  ...sampleWithRequiredData,
};

describe('QuizUser Service', () => {
  let service: QuizUserService;
  let httpMock: HttpTestingController;
  let expectedResult: IQuizUser | IQuizUser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(QuizUserService);
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

    it('should create a QuizUser', () => {
      const quizUser = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(quizUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a QuizUser', () => {
      const quizUser = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(quizUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a QuizUser', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of QuizUser', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a QuizUser', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addQuizUserToCollectionIfMissing', () => {
      it('should add a QuizUser to an empty array', () => {
        const quizUser: IQuizUser = sampleWithRequiredData;
        expectedResult = service.addQuizUserToCollectionIfMissing([], quizUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(quizUser);
      });

      it('should not add a QuizUser to an array that contains it', () => {
        const quizUser: IQuizUser = sampleWithRequiredData;
        const quizUserCollection: IQuizUser[] = [
          {
            ...quizUser,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addQuizUserToCollectionIfMissing(quizUserCollection, quizUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a QuizUser to an array that doesn't contain it", () => {
        const quizUser: IQuizUser = sampleWithRequiredData;
        const quizUserCollection: IQuizUser[] = [sampleWithPartialData];
        expectedResult = service.addQuizUserToCollectionIfMissing(quizUserCollection, quizUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(quizUser);
      });

      it('should add only unique QuizUser to an array', () => {
        const quizUserArray: IQuizUser[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const quizUserCollection: IQuizUser[] = [sampleWithRequiredData];
        expectedResult = service.addQuizUserToCollectionIfMissing(quizUserCollection, ...quizUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const quizUser: IQuizUser = sampleWithRequiredData;
        const quizUser2: IQuizUser = sampleWithPartialData;
        expectedResult = service.addQuizUserToCollectionIfMissing([], quizUser, quizUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(quizUser);
        expect(expectedResult).toContain(quizUser2);
      });

      it('should accept null and undefined values', () => {
        const quizUser: IQuizUser = sampleWithRequiredData;
        expectedResult = service.addQuizUserToCollectionIfMissing([], null, quizUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(quizUser);
      });

      it('should return initial array if no QuizUser is added', () => {
        const quizUserCollection: IQuizUser[] = [sampleWithRequiredData];
        expectedResult = service.addQuizUserToCollectionIfMissing(quizUserCollection, undefined, null);
        expect(expectedResult).toEqual(quizUserCollection);
      });
    });

    describe('compareQuizUser', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareQuizUser(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareQuizUser(entity1, entity2);
        const compareResult2 = service.compareQuizUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareQuizUser(entity1, entity2);
        const compareResult2 = service.compareQuizUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareQuizUser(entity1, entity2);
        const compareResult2 = service.compareQuizUser(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
