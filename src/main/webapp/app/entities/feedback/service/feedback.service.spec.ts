import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFeedback } from '../feedback.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../feedback.test-samples';

import { FeedbackService, RestFeedback } from './feedback.service';

const requireRestSample: RestFeedback = {
  ...sampleWithRequiredData,
  createDate: sampleWithRequiredData.createDate?.format(DATE_FORMAT),
};

describe('Feedback Service', () => {
  let service: FeedbackService;
  let httpMock: HttpTestingController;
  let expectedResult: IFeedback | IFeedback[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FeedbackService);
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

    it('should create a Feedback', () => {
      const feedback = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(feedback).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Feedback', () => {
      const feedback = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(feedback).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Feedback', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Feedback', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Feedback', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFeedbackToCollectionIfMissing', () => {
      it('should add a Feedback to an empty array', () => {
        const feedback: IFeedback = sampleWithRequiredData;
        expectedResult = service.addFeedbackToCollectionIfMissing([], feedback);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feedback);
      });

      it('should not add a Feedback to an array that contains it', () => {
        const feedback: IFeedback = sampleWithRequiredData;
        const feedbackCollection: IFeedback[] = [
          {
            ...feedback,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFeedbackToCollectionIfMissing(feedbackCollection, feedback);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Feedback to an array that doesn't contain it", () => {
        const feedback: IFeedback = sampleWithRequiredData;
        const feedbackCollection: IFeedback[] = [sampleWithPartialData];
        expectedResult = service.addFeedbackToCollectionIfMissing(feedbackCollection, feedback);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feedback);
      });

      it('should add only unique Feedback to an array', () => {
        const feedbackArray: IFeedback[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const feedbackCollection: IFeedback[] = [sampleWithRequiredData];
        expectedResult = service.addFeedbackToCollectionIfMissing(feedbackCollection, ...feedbackArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const feedback: IFeedback = sampleWithRequiredData;
        const feedback2: IFeedback = sampleWithPartialData;
        expectedResult = service.addFeedbackToCollectionIfMissing([], feedback, feedback2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feedback);
        expect(expectedResult).toContain(feedback2);
      });

      it('should accept null and undefined values', () => {
        const feedback: IFeedback = sampleWithRequiredData;
        expectedResult = service.addFeedbackToCollectionIfMissing([], null, feedback, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feedback);
      });

      it('should return initial array if no Feedback is added', () => {
        const feedbackCollection: IFeedback[] = [sampleWithRequiredData];
        expectedResult = service.addFeedbackToCollectionIfMissing(feedbackCollection, undefined, null);
        expect(expectedResult).toEqual(feedbackCollection);
      });
    });

    describe('compareFeedback', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFeedback(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFeedback(entity1, entity2);
        const compareResult2 = service.compareFeedback(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFeedback(entity1, entity2);
        const compareResult2 = service.compareFeedback(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFeedback(entity1, entity2);
        const compareResult2 = service.compareFeedback(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
