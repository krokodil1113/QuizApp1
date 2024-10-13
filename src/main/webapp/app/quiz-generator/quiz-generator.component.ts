import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { QuizService } from 'app/entities/quiz/service/quiz.service';

@Component({
  selector: 'jhi-quiz-generator',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './quiz-generator.component.html',
  styleUrl: './quiz-generator.component.scss',
})
export class QuizGeneratorComponent {
  topic: string = '';
  quiz: any; // Define a more specific type based on your data structure
  loading: boolean = false;
  error: string = '';
  allowTextInput: boolean = true;

  constructor(private quizService: QuizService) {}

  generateQuiz(): void {
    this.loading = true;
    this.error = '';
    this.quizService.generateQuiz(this.topic).subscribe({
      next: (data: any) => {
        this.quiz = data;
        this.loading = false;
      },
      error: (error: any) => {
        this.error = 'Failed to generate quiz';
        this.loading = false;
      },
    });
  }
}
