import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUserStatistics } from '../user-statistics.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../user-statistics.test-samples';

import { UserStatisticsService } from './user-statistics.service';

const requireRestSample: IUserStatistics = {
  ...sampleWithRequiredData,
};

describe('UserStatistics Service', () => {
  let service: UserStatisticsService;
  let httpMock: HttpTestingController;
  let expectedResult: IUserStatistics | IUserStatistics[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UserStatisticsService);
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

    it('should create a UserStatistics', () => {
      const userStatistics = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(userStatistics).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserStatistics', () => {
      const userStatistics = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(userStatistics).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserStatistics', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserStatistics', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UserStatistics', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUserStatisticsToCollectionIfMissing', () => {
      it('should add a UserStatistics to an empty array', () => {
        const userStatistics: IUserStatistics = sampleWithRequiredData;
        expectedResult = service.addUserStatisticsToCollectionIfMissing([], userStatistics);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userStatistics);
      });

      it('should not add a UserStatistics to an array that contains it', () => {
        const userStatistics: IUserStatistics = sampleWithRequiredData;
        const userStatisticsCollection: IUserStatistics[] = [
          {
            ...userStatistics,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUserStatisticsToCollectionIfMissing(userStatisticsCollection, userStatistics);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserStatistics to an array that doesn't contain it", () => {
        const userStatistics: IUserStatistics = sampleWithRequiredData;
        const userStatisticsCollection: IUserStatistics[] = [sampleWithPartialData];
        expectedResult = service.addUserStatisticsToCollectionIfMissing(userStatisticsCollection, userStatistics);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userStatistics);
      });

      it('should add only unique UserStatistics to an array', () => {
        const userStatisticsArray: IUserStatistics[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const userStatisticsCollection: IUserStatistics[] = [sampleWithRequiredData];
        expectedResult = service.addUserStatisticsToCollectionIfMissing(userStatisticsCollection, ...userStatisticsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userStatistics: IUserStatistics = sampleWithRequiredData;
        const userStatistics2: IUserStatistics = sampleWithPartialData;
        expectedResult = service.addUserStatisticsToCollectionIfMissing([], userStatistics, userStatistics2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userStatistics);
        expect(expectedResult).toContain(userStatistics2);
      });

      it('should accept null and undefined values', () => {
        const userStatistics: IUserStatistics = sampleWithRequiredData;
        expectedResult = service.addUserStatisticsToCollectionIfMissing([], null, userStatistics, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userStatistics);
      });

      it('should return initial array if no UserStatistics is added', () => {
        const userStatisticsCollection: IUserStatistics[] = [sampleWithRequiredData];
        expectedResult = service.addUserStatisticsToCollectionIfMissing(userStatisticsCollection, undefined, null);
        expect(expectedResult).toEqual(userStatisticsCollection);
      });
    });

    describe('compareUserStatistics', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUserStatistics(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUserStatistics(entity1, entity2);
        const compareResult2 = service.compareUserStatistics(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUserStatistics(entity1, entity2);
        const compareResult2 = service.compareUserStatistics(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUserStatistics(entity1, entity2);
        const compareResult2 = service.compareUserStatistics(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
