package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.QuizUserTestSamples.*;
import static com.mycompany.myapp.domain.UserStatisticsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserStatisticsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserStatistics.class);
        UserStatistics userStatistics1 = getUserStatisticsSample1();
        UserStatistics userStatistics2 = new UserStatistics();
        assertThat(userStatistics1).isNotEqualTo(userStatistics2);

        userStatistics2.setId(userStatistics1.getId());
        assertThat(userStatistics1).isEqualTo(userStatistics2);

        userStatistics2 = getUserStatisticsSample2();
        assertThat(userStatistics1).isNotEqualTo(userStatistics2);
    }

    @Test
    void quizUserTest() throws Exception {
        UserStatistics userStatistics = getUserStatisticsRandomSampleGenerator();
        QuizUser quizUserBack = getQuizUserRandomSampleGenerator();

        userStatistics.setQuizUser(quizUserBack);
        assertThat(userStatistics.getQuizUser()).isEqualTo(quizUserBack);

        userStatistics.quizUser(null);
        assertThat(userStatistics.getQuizUser()).isNull();
    }
}
