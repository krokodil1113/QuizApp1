package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.QuizAnalyticsTestSamples.*;
import static com.mycompany.myapp.domain.QuizTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuizAnalyticsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuizAnalytics.class);
        QuizAnalytics quizAnalytics1 = getQuizAnalyticsSample1();
        QuizAnalytics quizAnalytics2 = new QuizAnalytics();
        assertThat(quizAnalytics1).isNotEqualTo(quizAnalytics2);

        quizAnalytics2.setId(quizAnalytics1.getId());
        assertThat(quizAnalytics1).isEqualTo(quizAnalytics2);

        quizAnalytics2 = getQuizAnalyticsSample2();
        assertThat(quizAnalytics1).isNotEqualTo(quizAnalytics2);
    }

    @Test
    void quizTest() throws Exception {
        QuizAnalytics quizAnalytics = getQuizAnalyticsRandomSampleGenerator();
        Quiz quizBack = getQuizRandomSampleGenerator();

        quizAnalytics.setQuiz(quizBack);
        assertThat(quizAnalytics.getQuiz()).isEqualTo(quizBack);
        assertThat(quizBack.getQuizAnalytics()).isEqualTo(quizAnalytics);

        quizAnalytics.quiz(null);
        assertThat(quizAnalytics.getQuiz()).isNull();
        assertThat(quizBack.getQuizAnalytics()).isNull();
    }
}
