package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.GameSessionRepository;
import com.mycompany.myapp.service.GameSessionQueryService;
import com.mycompany.myapp.service.GameSessionService;
import com.mycompany.myapp.service.criteria.GameSessionCriteria;
import com.mycompany.myapp.service.dto.GameSessionDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.GameSession}.
 */
@RestController
@RequestMapping("/api/game-sessions")
public class GameSessionResource {

    private final Logger log = LoggerFactory.getLogger(GameSessionResource.class);

    private static final String ENTITY_NAME = "gameSession";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GameSessionService gameSessionService;

    private final GameSessionRepository gameSessionRepository;

    private final GameSessionQueryService gameSessionQueryService;

    public GameSessionResource(
        GameSessionService gameSessionService,
        GameSessionRepository gameSessionRepository,
        GameSessionQueryService gameSessionQueryService
    ) {
        this.gameSessionService = gameSessionService;
        this.gameSessionRepository = gameSessionRepository;
        this.gameSessionQueryService = gameSessionQueryService;
    }

    /**
     * {@code POST  /game-sessions} : Create a new gameSession.
     *
     * @param gameSessionDTO the gameSessionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gameSessionDTO, or with status {@code 400 (Bad Request)} if the gameSession has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<GameSessionDTO> createGameSession(@RequestBody GameSessionDTO gameSessionDTO) throws URISyntaxException {
        log.debug("REST request to save GameSession : {}", gameSessionDTO);
        if (gameSessionDTO.getId() != null) {
            throw new BadRequestAlertException("A new gameSession cannot already have an ID", ENTITY_NAME, "idexists");
        }
        gameSessionDTO = gameSessionService.save(gameSessionDTO);
        return ResponseEntity.created(new URI("/api/game-sessions/" + gameSessionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, gameSessionDTO.getId().toString()))
            .body(gameSessionDTO);
    }

    /**
     * {@code PUT  /game-sessions/:id} : Updates an existing gameSession.
     *
     * @param id the id of the gameSessionDTO to save.
     * @param gameSessionDTO the gameSessionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameSessionDTO,
     * or with status {@code 400 (Bad Request)} if the gameSessionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gameSessionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GameSessionDTO> updateGameSession(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GameSessionDTO gameSessionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GameSession : {}, {}", id, gameSessionDTO);
        if (gameSessionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameSessionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        gameSessionDTO = gameSessionService.update(gameSessionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gameSessionDTO.getId().toString()))
            .body(gameSessionDTO);
    }

    /**
     * {@code PATCH  /game-sessions/:id} : Partial updates given fields of an existing gameSession, field will ignore if it is null
     *
     * @param id the id of the gameSessionDTO to save.
     * @param gameSessionDTO the gameSessionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameSessionDTO,
     * or with status {@code 400 (Bad Request)} if the gameSessionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the gameSessionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the gameSessionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GameSessionDTO> partialUpdateGameSession(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GameSessionDTO gameSessionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GameSession partially : {}, {}", id, gameSessionDTO);
        if (gameSessionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameSessionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GameSessionDTO> result = gameSessionService.partialUpdate(gameSessionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gameSessionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /game-sessions} : get all the gameSessions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gameSessions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<GameSessionDTO>> getAllGameSessions(
        GameSessionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get GameSessions by criteria: {}", criteria);

        Page<GameSessionDTO> page = gameSessionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /game-sessions/count} : count all the gameSessions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countGameSessions(GameSessionCriteria criteria) {
        log.debug("REST request to count GameSessions by criteria: {}", criteria);
        return ResponseEntity.ok().body(gameSessionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /game-sessions/:id} : get the "id" gameSession.
     *
     * @param id the id of the gameSessionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gameSessionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GameSessionDTO> getGameSession(@PathVariable("id") Long id) {
        log.debug("REST request to get GameSession : {}", id);
        Optional<GameSessionDTO> gameSessionDTO = gameSessionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gameSessionDTO);
    }

    /**
     * {@code DELETE  /game-sessions/:id} : delete the "id" gameSession.
     *
     * @param id the id of the gameSessionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGameSession(@PathVariable("id") Long id) {
        log.debug("REST request to delete GameSession : {}", id);
        gameSessionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
