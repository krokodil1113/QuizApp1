package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CategoryTestSamples.*;
import static com.mycompany.myapp.domain.QuizAnalyticsTestSamples.*;
import static com.mycompany.myapp.domain.QuizTestSamples.*;
import static com.mycompany.myapp.domain.QuizUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuizTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Quiz.class);
        Quiz quiz1 = getQuizSample1();
        Quiz quiz2 = new Quiz();
        assertThat(quiz1).isNotEqualTo(quiz2);

        quiz2.setId(quiz1.getId());
        assertThat(quiz1).isEqualTo(quiz2);

        quiz2 = getQuizSample2();
        assertThat(quiz1).isNotEqualTo(quiz2);
    }

    @Test
    void quizAnalyticsTest() throws Exception {
        Quiz quiz = getQuizRandomSampleGenerator();
        QuizAnalytics quizAnalyticsBack = getQuizAnalyticsRandomSampleGenerator();

        quiz.setQuizAnalytics(quizAnalyticsBack);
        assertThat(quiz.getQuizAnalytics()).isEqualTo(quizAnalyticsBack);

        quiz.quizAnalytics(null);
        assertThat(quiz.getQuizAnalytics()).isNull();
    }

    @Test
    void creatorTest() throws Exception {
        Quiz quiz = getQuizRandomSampleGenerator();
        QuizUser quizUserBack = getQuizUserRandomSampleGenerator();

        quiz.setCreator(quizUserBack);
        assertThat(quiz.getCreator()).isEqualTo(quizUserBack);

        quiz.creator(null);
        assertThat(quiz.getCreator()).isNull();
    }

    @Test
    void categoryTest() throws Exception {
        Quiz quiz = getQuizRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        quiz.setCategory(categoryBack);
        assertThat(quiz.getCategory()).isEqualTo(categoryBack);

        quiz.category(null);
        assertThat(quiz.getCategory()).isNull();
    }
}
