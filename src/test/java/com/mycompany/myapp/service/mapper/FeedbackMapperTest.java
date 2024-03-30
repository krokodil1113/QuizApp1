package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.FeedbackAsserts.*;
import static com.mycompany.myapp.domain.FeedbackTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FeedbackMapperTest {

    private FeedbackMapper feedbackMapper;

    @BeforeEach
    void setUp() {
        feedbackMapper = new FeedbackMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFeedbackSample1();
        var actual = feedbackMapper.toEntity(feedbackMapper.toDto(expected));
        assertFeedbackAllPropertiesEquals(expected, actual);
    }
}
