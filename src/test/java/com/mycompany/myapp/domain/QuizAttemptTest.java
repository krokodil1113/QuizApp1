package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.QuizAttemptTestSamples.*;
import static com.mycompany.myapp.domain.QuizTestSamples.*;
import static com.mycompany.myapp.domain.QuizUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuizAttemptTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuizAttempt.class);
        QuizAttempt quizAttempt1 = getQuizAttemptSample1();
        QuizAttempt quizAttempt2 = new QuizAttempt();
        assertThat(quizAttempt1).isNotEqualTo(quizAttempt2);

        quizAttempt2.setId(quizAttempt1.getId());
        assertThat(quizAttempt1).isEqualTo(quizAttempt2);

        quizAttempt2 = getQuizAttemptSample2();
        assertThat(quizAttempt1).isNotEqualTo(quizAttempt2);
    }

    @Test
    void quizTest() throws Exception {
        QuizAttempt quizAttempt = getQuizAttemptRandomSampleGenerator();
        Quiz quizBack = getQuizRandomSampleGenerator();

        quizAttempt.setQuiz(quizBack);
        assertThat(quizAttempt.getQuiz()).isEqualTo(quizBack);

        quizAttempt.quiz(null);
        assertThat(quizAttempt.getQuiz()).isNull();
    }

    @Test
    void userTest() throws Exception {
        QuizAttempt quizAttempt = getQuizAttemptRandomSampleGenerator();
        QuizUser quizUserBack = getQuizUserRandomSampleGenerator();

        quizAttempt.setUser(quizUserBack);
        assertThat(quizAttempt.getUser()).isEqualTo(quizUserBack);

        quizAttempt.user(null);
        assertThat(quizAttempt.getUser()).isNull();
    }
}
