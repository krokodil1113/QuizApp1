package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.GameSessionAsserts.*;
import static com.mycompany.myapp.domain.GameSessionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameSessionMapperTest {

    private GameSessionMapper gameSessionMapper;

    @BeforeEach
    void setUp() {
        gameSessionMapper = new GameSessionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getGameSessionSample1();
        var actual = gameSessionMapper.toEntity(gameSessionMapper.toDto(expected));
        assertGameSessionAllPropertiesEquals(expected, actual);
    }
}
