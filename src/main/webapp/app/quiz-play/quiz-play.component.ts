import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { QuizService } from '../entities/quiz/service/quiz.service';
import { IQuestion } from 'app/entities/question/question.model';
import { IAnswer } from 'app/entities/answer/answer.model';
import { QuestionService } from 'app/entities/question/service/question.service';
import { AnswerService } from 'app/entities/answer/service/answer.service';

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

  constructor(
    private route: ActivatedRoute,
    private questionService: QuestionService,
    private answerService: AnswerService,
  ) {}

  ngOnInit(): void {
    const quizId = +this.route.snapshot.paramMap.get('quizId')!;
    this.loadQuizQuestions(quizId);
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
