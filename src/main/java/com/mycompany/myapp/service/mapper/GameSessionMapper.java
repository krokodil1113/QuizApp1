package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.GameSession;
import com.mycompany.myapp.service.dto.GameSessionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GameSession} and its DTO {@link GameSessionDTO}.
 */
@Mapper(componentModel = "spring")
public interface GameSessionMapper extends EntityMapper<GameSessionDTO, GameSession> {}
