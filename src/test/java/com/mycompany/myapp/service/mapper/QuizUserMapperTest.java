package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.QuizUserAsserts.*;
import static com.mycompany.myapp.domain.QuizUserTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuizUserMapperTest {

    private QuizUserMapper quizUserMapper;

    @BeforeEach
    void setUp() {
        quizUserMapper = new QuizUserMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getQuizUserSample1();
        var actual = quizUserMapper.toEntity(quizUserMapper.toDto(expected));
        assertQuizUserAllPropertiesEquals(expected, actual);
    }
}
