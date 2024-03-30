package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.UserAnswerAsserts.*;
import static com.mycompany.myapp.domain.UserAnswerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserAnswerMapperTest {

    private UserAnswerMapper userAnswerMapper;

    @BeforeEach
    void setUp() {
        userAnswerMapper = new UserAnswerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserAnswerSample1();
        var actual = userAnswerMapper.toEntity(userAnswerMapper.toDto(expected));
        assertUserAnswerAllPropertiesEquals(expected, actual);
    }
}
