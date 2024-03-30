package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.FeedbackTestSamples.*;
import static com.mycompany.myapp.domain.QuizUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeedbackTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Feedback.class);
        Feedback feedback1 = getFeedbackSample1();
        Feedback feedback2 = new Feedback();
        assertThat(feedback1).isNotEqualTo(feedback2);

        feedback2.setId(feedback1.getId());
        assertThat(feedback1).isEqualTo(feedback2);

        feedback2 = getFeedbackSample2();
        assertThat(feedback1).isNotEqualTo(feedback2);
    }

    @Test
    void userTest() throws Exception {
        Feedback feedback = getFeedbackRandomSampleGenerator();
        QuizUser quizUserBack = getQuizUserRandomSampleGenerator();

        feedback.setUser(quizUserBack);
        assertThat(feedback.getUser()).isEqualTo(quizUserBack);

        feedback.user(null);
        assertThat(feedback.getUser()).isNull();
    }
}
