import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { QuizService } from 'app/entities/quiz/service/quiz.service';
import { IQuizAttempt } from 'app/entities/quiz-attempt/quiz-attempt.model';
import { QuizAttemptService } from 'app/entities/quiz-attempt/service/quiz-attempt.service';

@Component({
  selector: 'jhi-quiz-result',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './quiz-result.component.html',
  styleUrl: './quiz-result.component.scss',
})
export class QuizResultComponent {
  score: number | undefined;
  totalQuestions: number | undefined;
  questions: any[] | undefined; // This would be structured based on how your API returns question and answer details

  constructor(
    private quizAttemptService: QuizAttemptService,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    const attemptIdStr = this.route.snapshot.paramMap.get('attemptId');
    if (attemptIdStr !== null) {
      const attemptId = Number(attemptIdStr);
      if (!isNaN(attemptId)) {
        // Fetch quiz attempt details including score
        this.quizAttemptService.getQuizAttemptDetails(attemptId).subscribe({
          next: (attemptDetails: IQuizAttempt) => {
            console.log('score is:************************', attemptDetails.score);
            this.score = attemptDetails.score !== null ? attemptDetails.score : undefined;
          },
          error: error => {
            console.error('Error fetching quiz attempt details:', error);
            // Handle the error appropriately
          },
        });
      } else {
        console.error('Invalid attemptId:', attemptIdStr);
        // Handle the error (e.g., redirect to a "Not Found" page or back to the dashboard)
      }
    } else {
      console.error('No attemptId provided in the route.');
      // Handle the lack of an attemptId (e.g., redirect to the dashboard)
    }
  }

  retakeQuiz() {
    // Implementation for retaking the quiz
  }
}
