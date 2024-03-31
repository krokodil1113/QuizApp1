import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { QuizService } from '../entities/quiz/service/quiz.service';
import { CategoryService } from '../entities/category/service/category.service';

import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { IQuiz } from 'app/entities/quiz/quiz.model';
import { ICategory } from 'app/entities/category/category.model';

import { Router } from '@angular/router';

@Component({
  selector: 'jhi-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent {
  quizzes: IQuiz[] = [];
  categories: ICategory[] = [];
  selectedCategoryId = 1; // Example category ID

  constructor(
    private quizService: QuizService,
    private categoryService: CategoryService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.loadCategories();
    // this.loadQuizzesByCategory(this.selectedCategoryId);
  }

  startQuiz(quizId: number): void {
    this.router.navigate(['/quiz-play', quizId]);
  }

  loadCategories(): void {
    this.categoryService.query().subscribe({
      next: response => {
        if (response.body) {
          this.categories = response.body;
        } else {
          console.warn('No categories returned from server');
        }
      },
      error: error => console.error('Error fetching categories:', error),
    });
  }

  loadQuizzesByCategory(categoryId: number): void {
    console.log('AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa', categoryId);
    if (!categoryId) {
      this.quizzes = [];
      return;
    }
    this.selectedCategoryId = categoryId;
    this.quizService
      .getQuizzesByCategory(categoryId)
      .pipe(
        catchError(error => {
          console.error('Error fetching quizzes by category:', error);
          return of([]); // Providing a fallback value in case of an error
        }),
      )
      .subscribe(data => (this.quizzes = data));
  }

  /* loadQuizzesByCategory(categoryId: number): void {
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
  } */
}
