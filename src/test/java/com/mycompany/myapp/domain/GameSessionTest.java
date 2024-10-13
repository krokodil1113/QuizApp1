package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.GameSessionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GameSessionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameSession.class);
        GameSession gameSession1 = getGameSessionSample1();
        GameSession gameSession2 = new GameSession();
        assertThat(gameSession1).isNotEqualTo(gameSession2);

        gameSession2.setId(gameSession1.getId());
        assertThat(gameSession1).isEqualTo(gameSession2);

        gameSession2 = getGameSessionSample2();
        assertThat(gameSession1).isNotEqualTo(gameSession2);
    }
}
