package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.QuizUserTestSamples.*;
import static com.mycompany.myapp.domain.UserStatisticsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuizUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuizUser.class);
        QuizUser quizUser1 = getQuizUserSample1();
        QuizUser quizUser2 = new QuizUser();
        assertThat(quizUser1).isNotEqualTo(quizUser2);

        quizUser2.setId(quizUser1.getId());
        assertThat(quizUser1).isEqualTo(quizUser2);

        quizUser2 = getQuizUserSample2();
        assertThat(quizUser1).isNotEqualTo(quizUser2);
    }

    @Test
    void userStatisticsTest() throws Exception {
        QuizUser quizUser = getQuizUserRandomSampleGenerator();
        UserStatistics userStatisticsBack = getUserStatisticsRandomSampleGenerator();

        quizUser.setUserStatistics(userStatisticsBack);
        assertThat(quizUser.getUserStatistics()).isEqualTo(userStatisticsBack);
        assertThat(userStatisticsBack.getQuizUser()).isEqualTo(quizUser);

        quizUser.userStatistics(null);
        assertThat(quizUser.getUserStatistics()).isNull();
        assertThat(userStatisticsBack.getQuizUser()).isNull();
    }
}
