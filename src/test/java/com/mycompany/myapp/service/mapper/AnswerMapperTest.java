package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.AnswerAsserts.*;
import static com.mycompany.myapp.domain.AnswerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnswerMapperTest {

    private AnswerMapper answerMapper;

    @BeforeEach
    void setUp() {
        answerMapper = new AnswerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAnswerSample1();
        var actual = answerMapper.toEntity(answerMapper.toDto(expected));
        assertAnswerAllPropertiesEquals(expected, actual);
    }
}
