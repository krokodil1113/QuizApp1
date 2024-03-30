package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuizUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuizUserDTO.class);
        QuizUserDTO quizUserDTO1 = new QuizUserDTO();
        quizUserDTO1.setId(1L);
        QuizUserDTO quizUserDTO2 = new QuizUserDTO();
        assertThat(quizUserDTO1).isNotEqualTo(quizUserDTO2);
        quizUserDTO2.setId(quizUserDTO1.getId());
        assertThat(quizUserDTO1).isEqualTo(quizUserDTO2);
        quizUserDTO2.setId(2L);
        assertThat(quizUserDTO1).isNotEqualTo(quizUserDTO2);
        quizUserDTO1.setId(null);
        assertThat(quizUserDTO1).isNotEqualTo(quizUserDTO2);
    }
}
