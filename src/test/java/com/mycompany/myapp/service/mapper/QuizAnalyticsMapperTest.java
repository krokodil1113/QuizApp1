package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.QuizAnalyticsAsserts.*;
import static com.mycompany.myapp.domain.QuizAnalyticsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuizAnalyticsMapperTest {

    private QuizAnalyticsMapper quizAnalyticsMapper;

    @BeforeEach
    void setUp() {
        quizAnalyticsMapper = new QuizAnalyticsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getQuizAnalyticsSample1();
        var actual = quizAnalyticsMapper.toEntity(quizAnalyticsMapper.toDto(expected));
        assertQuizAnalyticsAllPropertiesEquals(expected, actual);
    }
}
