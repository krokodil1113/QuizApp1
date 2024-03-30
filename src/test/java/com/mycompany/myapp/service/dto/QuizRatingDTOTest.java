package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuizRatingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuizRatingDTO.class);
        QuizRatingDTO quizRatingDTO1 = new QuizRatingDTO();
        quizRatingDTO1.setId(1L);
        QuizRatingDTO quizRatingDTO2 = new QuizRatingDTO();
        assertThat(quizRatingDTO1).isNotEqualTo(quizRatingDTO2);
        quizRatingDTO2.setId(quizRatingDTO1.getId());
        assertThat(quizRatingDTO1).isEqualTo(quizRatingDTO2);
        quizRatingDTO2.setId(2L);
        assertThat(quizRatingDTO1).isNotEqualTo(quizRatingDTO2);
        quizRatingDTO1.setId(null);
        assertThat(quizRatingDTO1).isNotEqualTo(quizRatingDTO2);
    }
}
