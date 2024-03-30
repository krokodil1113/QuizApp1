import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { QuizService } from '../entities/quiz/service/quiz.service';

import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { IQuiz } from 'app/entities/quiz/quiz.model';

@Component({
  selector: 'jhi-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent {
  quizzes: IQuiz[] = [];
  selectedCategoryId = 1; // Example category ID

  constructor(private quizService: QuizService) {}

  ngOnInit(): void {
    this.loadQuizzesByCategory(this.selectedCategoryId);
  }

  loadQuizzesByCategory(categoryId: number): void {
    this.quizService
      .getQuizzesByCategory(categoryId)
      .pipe(
        catchError(error => {
          console.error(error);
          // Return an observable with an empty or fallback value so the stream completes successfully
          return of([]); // Assuming an empty array as a fallback
        }),
      )
      .subscribe(data => {
        this.quizzes = data;
      });
  }
}
