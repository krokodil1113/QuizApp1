package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.QuizRatingTestSamples.*;
import static com.mycompany.myapp.domain.QuizTestSamples.*;
import static com.mycompany.myapp.domain.QuizUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuizRatingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuizRating.class);
        QuizRating quizRating1 = getQuizRatingSample1();
        QuizRating quizRating2 = new QuizRating();
        assertThat(quizRating1).isNotEqualTo(quizRating2);

        quizRating2.setId(quizRating1.getId());
        assertThat(quizRating1).isEqualTo(quizRating2);

        quizRating2 = getQuizRatingSample2();
        assertThat(quizRating1).isNotEqualTo(quizRating2);
    }

    @Test
    void quizTest() throws Exception {
        QuizRating quizRating = getQuizRatingRandomSampleGenerator();
        Quiz quizBack = getQuizRandomSampleGenerator();

        quizRating.setQuiz(quizBack);
        assertThat(quizRating.getQuiz()).isEqualTo(quizBack);

        quizRating.quiz(null);
        assertThat(quizRating.getQuiz()).isNull();
    }

    @Test
    void userTest() throws Exception {
        QuizRating quizRating = getQuizRatingRandomSampleGenerator();
        QuizUser quizUserBack = getQuizUserRandomSampleGenerator();

        quizRating.setUser(quizUserBack);
        assertThat(quizRating.getUser()).isEqualTo(quizUserBack);

        quizRating.user(null);
        assertThat(quizRating.getUser()).isNull();
    }
}
