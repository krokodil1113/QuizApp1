package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuizAnalyticsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuizAnalyticsDTO.class);
        QuizAnalyticsDTO quizAnalyticsDTO1 = new QuizAnalyticsDTO();
        quizAnalyticsDTO1.setId(1L);
        QuizAnalyticsDTO quizAnalyticsDTO2 = new QuizAnalyticsDTO();
        assertThat(quizAnalyticsDTO1).isNotEqualTo(quizAnalyticsDTO2);
        quizAnalyticsDTO2.setId(quizAnalyticsDTO1.getId());
        assertThat(quizAnalyticsDTO1).isEqualTo(quizAnalyticsDTO2);
        quizAnalyticsDTO2.setId(2L);
        assertThat(quizAnalyticsDTO1).isNotEqualTo(quizAnalyticsDTO2);
        quizAnalyticsDTO1.setId(null);
        assertThat(quizAnalyticsDTO1).isNotEqualTo(quizAnalyticsDTO2);
    }
}
