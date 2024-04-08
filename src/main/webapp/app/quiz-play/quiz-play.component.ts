import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { QuizService } from '../entities/quiz/service/quiz.service';
import { IQuestion } from 'app/entities/question/question.model';
import { IAnswer } from 'app/entities/answer/answer.model';
import { QuestionService } from 'app/entities/question/service/question.service';
import { AnswerService } from 'app/entities/answer/service/answer.service';
import { UserAnswerService } from 'app/entities/user-answer/service/user-answer.service';

@Component({
  selector: 'jhi-quiz-play',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './quiz-play.component.html',
  styleUrl: './quiz-play.component.scss',
})
export class QuizPlayComponent {
  questions: IQuestion[] = []; // Assume Question is a model for your questions
  currentQuestionIndex: number = 0;
  currentQuestion?: IQuestion;
  selectedAnswerId?: number;
  answers: IAnswer[] = [];
  currentQuizAttemptId?: number | undefined;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private questionService: QuestionService,
    private answerService: AnswerService,
    private userAnswerService: UserAnswerService,
  ) {}

  ngOnInit(): void {
    const quizId = +this.route.snapshot.paramMap.get('quizId')!;
    this.loadQuizQuestions(quizId);

    let attemptId = this.router.getCurrentNavigation()?.extras.state?.attemptId;
    if (!attemptId) {
      attemptId = localStorage.getItem('currentAttemptId');
    }

    console.log('Router State:', attemptId);

    if (attemptId) {
      this.currentQuizAttemptId = +attemptId;
      console.log('QuizPlayComponent -- currentQuizAttemptId is:', this.currentQuizAttemptId);
    } else {
      console.warn('QuizPlayComponent -- Quiz attempt ID is missing');
      console.log('QuizPlayComponent -- Quiz attempt ID is missing');
      // Optional: Navigate away if necessary
      // this.router.navigate(['/dashboard']);
    }
  }

  loadQuizQuestions(quizId: number): void {
    this.questionService.getQuestionsByQuizId(quizId).subscribe((questions: IQuestion[]) => {
      this.questions = questions;
      console.log('aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', this.questions);
      this.currentQuestion = questions[0];
      this.loadAnswersForQuestion(questions[0].id); // Load answers for the first question
    });
  }

  loadAnswersForQuestion(questionId: number): void {
    this.answerService.getAnswersByQuestionId(questionId).subscribe((answers: IAnswer[]) => {
      this.answers = answers;
      console.log('bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb', this.answers);
    });
  }

  selectAnswer(answerId: number): void {
    this.selectedAnswerId = answerId;
  }

  submitAnswer(): void {
    if (this.selectedAnswerId != null) {
      // Assuming you have a service method to save the user's answer
      this.userAnswerService.saveUserAnswer(this.currentQuizAttemptId!, this.currentQuestion!.id, this.selectedAnswerId).subscribe({
        next: (_response: any) => {
          console.log('Answer submitted successfully');
          // Move to the next question if not the last question
          if (!this.isLastQuestion()) {
            this.goToNextQuestion();
          } else {
            // Handle the end of the quiz, such as navigating to a results page or showing a summary
            this.router.navigate(['/quiz-result', this.currentQuizAttemptId]);
          }
        },
        error: (error: any) => {
          console.error('Error submitting answer:', error);
          // Handle error (e.g., show error message to the user)
        },
      });
    } else {
      console.log('No answer selected');
      // Prompt the user to select an answer before proceeding
    }
  }

  isLastQuestion(): boolean {
    return this.currentQuestionIndex === this.questions.length - 1;
  }

  // Update goToNextQuestion to reset selectedAnswerId when moving to the next question
  goToNextQuestion(): void {
    if (!this.isLastQuestion()) {
      this.currentQuestionIndex++;
      this.currentQuestion = this.questions[this.currentQuestionIndex];
      this.loadAnswersForQuestion(this.currentQuestion.id);
      this.selectedAnswerId = undefined; // Reset selected answer
    } else {
      // Handle quiz completion here
    }
  }
}
