import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGameSession } from '../game-session.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../game-session.test-samples';

import { GameSessionService, RestGameSession } from './game-session.service';

const requireRestSample: RestGameSession = {
  ...sampleWithRequiredData,
  startTime: sampleWithRequiredData.startTime?.toJSON(),
  endTime: sampleWithRequiredData.endTime?.toJSON(),
};

describe('GameSession Service', () => {
  let service: GameSessionService;
  let httpMock: HttpTestingController;
  let expectedResult: IGameSession | IGameSession[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GameSessionService);
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

    it('should create a GameSession', () => {
      const gameSession = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(gameSession).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GameSession', () => {
      const gameSession = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(gameSession).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GameSession', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GameSession', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a GameSession', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addGameSessionToCollectionIfMissing', () => {
      it('should add a GameSession to an empty array', () => {
        const gameSession: IGameSession = sampleWithRequiredData;
        expectedResult = service.addGameSessionToCollectionIfMissing([], gameSession);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gameSession);
      });

      it('should not add a GameSession to an array that contains it', () => {
        const gameSession: IGameSession = sampleWithRequiredData;
        const gameSessionCollection: IGameSession[] = [
          {
            ...gameSession,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGameSessionToCollectionIfMissing(gameSessionCollection, gameSession);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GameSession to an array that doesn't contain it", () => {
        const gameSession: IGameSession = sampleWithRequiredData;
        const gameSessionCollection: IGameSession[] = [sampleWithPartialData];
        expectedResult = service.addGameSessionToCollectionIfMissing(gameSessionCollection, gameSession);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gameSession);
      });

      it('should add only unique GameSession to an array', () => {
        const gameSessionArray: IGameSession[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const gameSessionCollection: IGameSession[] = [sampleWithRequiredData];
        expectedResult = service.addGameSessionToCollectionIfMissing(gameSessionCollection, ...gameSessionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const gameSession: IGameSession = sampleWithRequiredData;
        const gameSession2: IGameSession = sampleWithPartialData;
        expectedResult = service.addGameSessionToCollectionIfMissing([], gameSession, gameSession2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gameSession);
        expect(expectedResult).toContain(gameSession2);
      });

      it('should accept null and undefined values', () => {
        const gameSession: IGameSession = sampleWithRequiredData;
        expectedResult = service.addGameSessionToCollectionIfMissing([], null, gameSession, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gameSession);
      });

      it('should return initial array if no GameSession is added', () => {
        const gameSessionCollection: IGameSession[] = [sampleWithRequiredData];
        expectedResult = service.addGameSessionToCollectionIfMissing(gameSessionCollection, undefined, null);
        expect(expectedResult).toEqual(gameSessionCollection);
      });
    });

    describe('compareGameSession', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGameSession(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareGameSession(entity1, entity2);
        const compareResult2 = service.compareGameSession(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareGameSession(entity1, entity2);
        const compareResult2 = service.compareGameSession(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareGameSession(entity1, entity2);
        const compareResult2 = service.compareGameSession(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
