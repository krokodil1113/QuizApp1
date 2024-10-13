package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GameSessionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameSessionDTO.class);
        GameSessionDTO gameSessionDTO1 = new GameSessionDTO();
        gameSessionDTO1.setId(1L);
        GameSessionDTO gameSessionDTO2 = new GameSessionDTO();
        assertThat(gameSessionDTO1).isNotEqualTo(gameSessionDTO2);
        gameSessionDTO2.setId(gameSessionDTO1.getId());
        assertThat(gameSessionDTO1).isEqualTo(gameSessionDTO2);
        gameSessionDTO2.setId(2L);
        assertThat(gameSessionDTO1).isNotEqualTo(gameSessionDTO2);
        gameSessionDTO1.setId(null);
        assertThat(gameSessionDTO1).isNotEqualTo(gameSessionDTO2);
    }
}
