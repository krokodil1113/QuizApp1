package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.QuizRatingAsserts.*;
import static com.mycompany.myapp.domain.QuizRatingTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuizRatingMapperTest {

    private QuizRatingMapper quizRatingMapper;

    @BeforeEach
    void setUp() {
        quizRatingMapper = new QuizRatingMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getQuizRatingSample1();
        var actual = quizRatingMapper.toEntity(quizRatingMapper.toDto(expected));
        assertQuizRatingAllPropertiesEquals(expected, actual);
    }
}
