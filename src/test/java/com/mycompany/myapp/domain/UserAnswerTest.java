package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AnswerTestSamples.*;
import static com.mycompany.myapp.domain.QuestionTestSamples.*;
import static com.mycompany.myapp.domain.QuizAttemptTestSamples.*;
import static com.mycompany.myapp.domain.UserAnswerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAnswerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAnswer.class);
        UserAnswer userAnswer1 = getUserAnswerSample1();
        UserAnswer userAnswer2 = new UserAnswer();
        assertThat(userAnswer1).isNotEqualTo(userAnswer2);

        userAnswer2.setId(userAnswer1.getId());
        assertThat(userAnswer1).isEqualTo(userAnswer2);

        userAnswer2 = getUserAnswerSample2();
        assertThat(userAnswer1).isNotEqualTo(userAnswer2);
    }

    @Test
    void attemptTest() throws Exception {
        UserAnswer userAnswer = getUserAnswerRandomSampleGenerator();
        QuizAttempt quizAttemptBack = getQuizAttemptRandomSampleGenerator();

        userAnswer.setAttempt(quizAttemptBack);
        assertThat(userAnswer.getAttempt()).isEqualTo(quizAttemptBack);

        userAnswer.attempt(null);
        assertThat(userAnswer.getAttempt()).isNull();
    }

    @Test
    void questionTest() throws Exception {
        UserAnswer userAnswer = getUserAnswerRandomSampleGenerator();
        Question questionBack = getQuestionRandomSampleGenerator();

        userAnswer.setQuestion(questionBack);
        assertThat(userAnswer.getQuestion()).isEqualTo(questionBack);

        userAnswer.question(null);
        assertThat(userAnswer.getQuestion()).isNull();
    }

    @Test
    void selectedAnswerTest() throws Exception {
        UserAnswer userAnswer = getUserAnswerRandomSampleGenerator();
        Answer answerBack = getAnswerRandomSampleGenerator();

        userAnswer.setSelectedAnswer(answerBack);
        assertThat(userAnswer.getSelectedAnswer()).isEqualTo(answerBack);

        userAnswer.selectedAnswer(null);
        assertThat(userAnswer.getSelectedAnswer()).isNull();
    }
}
