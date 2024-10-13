package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.GameSession;
import com.mycompany.myapp.repository.GameSessionRepository;
import com.mycompany.myapp.service.criteria.GameSessionCriteria;
import com.mycompany.myapp.service.dto.GameSessionDTO;
import com.mycompany.myapp.service.mapper.GameSessionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link GameSession} entities in the database.
 * The main input is a {@link GameSessionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link GameSessionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GameSessionQueryService extends QueryService<GameSession> {

    private final Logger log = LoggerFactory.getLogger(GameSessionQueryService.class);

    private final GameSessionRepository gameSessionRepository;

    private final GameSessionMapper gameSessionMapper;

    public GameSessionQueryService(GameSessionRepository gameSessionRepository, GameSessionMapper gameSessionMapper) {
        this.gameSessionRepository = gameSessionRepository;
        this.gameSessionMapper = gameSessionMapper;
    }

    /**
     * Return a {@link Page} of {@link GameSessionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GameSessionDTO> findByCriteria(GameSessionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GameSession> specification = createSpecification(criteria);
        return gameSessionRepository.findAll(specification, page).map(gameSessionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GameSessionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GameSession> specification = createSpecification(criteria);
        return gameSessionRepository.count(specification);
    }

    /**
     * Function to convert {@link GameSessionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GameSession> createSpecification(GameSessionCriteria criteria) {
        Specification<GameSession> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GameSession_.id));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), GameSession_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTime(), GameSession_.endTime));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), GameSession_.status));
            }
            if (criteria.getCurrentQuestionIndex() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getCurrentQuestionIndex(), GameSession_.currentQuestionIndex)
                );
            }
        }
        return specification;
    }
}
