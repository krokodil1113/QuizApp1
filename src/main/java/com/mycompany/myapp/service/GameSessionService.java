package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.GameSession;
import com.mycompany.myapp.repository.GameSessionRepository;
import com.mycompany.myapp.service.dto.GameSessionDTO;
import com.mycompany.myapp.service.mapper.GameSessionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.GameSession}.
 */
@Service
@Transactional
public class GameSessionService {

    private final Logger log = LoggerFactory.getLogger(GameSessionService.class);

    private final GameSessionRepository gameSessionRepository;

    private final GameSessionMapper gameSessionMapper;

    public GameSessionService(GameSessionRepository gameSessionRepository, GameSessionMapper gameSessionMapper) {
        this.gameSessionRepository = gameSessionRepository;
        this.gameSessionMapper = gameSessionMapper;
    }

    /**
     * Save a gameSession.
     *
     * @param gameSessionDTO the entity to save.
     * @return the persisted entity.
     */
    public GameSessionDTO save(GameSessionDTO gameSessionDTO) {
        log.debug("Request to save GameSession : {}", gameSessionDTO);
        GameSession gameSession = gameSessionMapper.toEntity(gameSessionDTO);
        gameSession = gameSessionRepository.save(gameSession);
        return gameSessionMapper.toDto(gameSession);
    }

    /**
     * Update a gameSession.
     *
     * @param gameSessionDTO the entity to save.
     * @return the persisted entity.
     */
    public GameSessionDTO update(GameSessionDTO gameSessionDTO) {
        log.debug("Request to update GameSession : {}", gameSessionDTO);
        GameSession gameSession = gameSessionMapper.toEntity(gameSessionDTO);
        gameSession = gameSessionRepository.save(gameSession);
        return gameSessionMapper.toDto(gameSession);
    }

    /**
     * Partially update a gameSession.
     *
     * @param gameSessionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GameSessionDTO> partialUpdate(GameSessionDTO gameSessionDTO) {
        log.debug("Request to partially update GameSession : {}", gameSessionDTO);

        return gameSessionRepository
            .findById(gameSessionDTO.getId())
            .map(existingGameSession -> {
                gameSessionMapper.partialUpdate(existingGameSession, gameSessionDTO);

                return existingGameSession;
            })
            .map(gameSessionRepository::save)
            .map(gameSessionMapper::toDto);
    }

    /**
     * Get one gameSession by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GameSessionDTO> findOne(Long id) {
        log.debug("Request to get GameSession : {}", id);
        return gameSessionRepository.findById(id).map(gameSessionMapper::toDto);
    }

    /**
     * Delete the gameSession by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GameSession : {}", id);
        gameSessionRepository.deleteById(id);
    }
}
