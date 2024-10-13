package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.GameSessionAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.GameSession;
import com.mycompany.myapp.repository.GameSessionRepository;
import com.mycompany.myapp.service.dto.GameSessionDTO;
import com.mycompany.myapp.service.mapper.GameSessionMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GameSessionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GameSessionResourceIT {

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Integer DEFAULT_CURRENT_QUESTION_INDEX = 1;
    private static final Integer UPDATED_CURRENT_QUESTION_INDEX = 2;
    private static final Integer SMALLER_CURRENT_QUESTION_INDEX = 1 - 1;

    private static final String ENTITY_API_URL = "/api/game-sessions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private GameSessionMapper gameSessionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGameSessionMockMvc;

    private GameSession gameSession;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameSession createEntity(EntityManager em) {
        GameSession gameSession = new GameSession()
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .status(DEFAULT_STATUS)
            .currentQuestionIndex(DEFAULT_CURRENT_QUESTION_INDEX);
        return gameSession;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameSession createUpdatedEntity(EntityManager em) {
        GameSession gameSession = new GameSession()
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .status(UPDATED_STATUS)
            .currentQuestionIndex(UPDATED_CURRENT_QUESTION_INDEX);
        return gameSession;
    }

    @BeforeEach
    public void initTest() {
        gameSession = createEntity(em);
    }

    @Test
    @Transactional
    void createGameSession() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the GameSession
        GameSessionDTO gameSessionDTO = gameSessionMapper.toDto(gameSession);
        var returnedGameSessionDTO = om.readValue(
            restGameSessionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gameSessionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            GameSessionDTO.class
        );

        // Validate the GameSession in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedGameSession = gameSessionMapper.toEntity(returnedGameSessionDTO);
        assertGameSessionUpdatableFieldsEquals(returnedGameSession, getPersistedGameSession(returnedGameSession));
    }

    @Test
    @Transactional
    void createGameSessionWithExistingId() throws Exception {
        // Create the GameSession with an existing ID
        gameSession.setId(1L);
        GameSessionDTO gameSessionDTO = gameSessionMapper.toDto(gameSession);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gameSessionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GameSession in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGameSessions() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList
        restGameSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gameSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(sameInstant(DEFAULT_END_TIME))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].currentQuestionIndex").value(hasItem(DEFAULT_CURRENT_QUESTION_INDEX)));
    }

    @Test
    @Transactional
    void getGameSession() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get the gameSession
        restGameSessionMockMvc
            .perform(get(ENTITY_API_URL_ID, gameSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gameSession.getId().intValue()))
            .andExpect(jsonPath("$.startTime").value(sameInstant(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.endTime").value(sameInstant(DEFAULT_END_TIME)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.currentQuestionIndex").value(DEFAULT_CURRENT_QUESTION_INDEX));
    }

    @Test
    @Transactional
    void getGameSessionsByIdFiltering() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        Long id = gameSession.getId();

        defaultGameSessionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultGameSessionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultGameSessionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGameSessionsByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where startTime equals to
        defaultGameSessionFiltering("startTime.equals=" + DEFAULT_START_TIME, "startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllGameSessionsByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where startTime in
        defaultGameSessionFiltering("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME, "startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllGameSessionsByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where startTime is not null
        defaultGameSessionFiltering("startTime.specified=true", "startTime.specified=false");
    }

    @Test
    @Transactional
    void getAllGameSessionsByStartTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where startTime is greater than or equal to
        defaultGameSessionFiltering(
            "startTime.greaterThanOrEqual=" + DEFAULT_START_TIME,
            "startTime.greaterThanOrEqual=" + UPDATED_START_TIME
        );
    }

    @Test
    @Transactional
    void getAllGameSessionsByStartTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where startTime is less than or equal to
        defaultGameSessionFiltering("startTime.lessThanOrEqual=" + DEFAULT_START_TIME, "startTime.lessThanOrEqual=" + SMALLER_START_TIME);
    }

    @Test
    @Transactional
    void getAllGameSessionsByStartTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where startTime is less than
        defaultGameSessionFiltering("startTime.lessThan=" + UPDATED_START_TIME, "startTime.lessThan=" + DEFAULT_START_TIME);
    }

    @Test
    @Transactional
    void getAllGameSessionsByStartTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where startTime is greater than
        defaultGameSessionFiltering("startTime.greaterThan=" + SMALLER_START_TIME, "startTime.greaterThan=" + DEFAULT_START_TIME);
    }

    @Test
    @Transactional
    void getAllGameSessionsByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where endTime equals to
        defaultGameSessionFiltering("endTime.equals=" + DEFAULT_END_TIME, "endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllGameSessionsByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where endTime in
        defaultGameSessionFiltering("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME, "endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllGameSessionsByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where endTime is not null
        defaultGameSessionFiltering("endTime.specified=true", "endTime.specified=false");
    }

    @Test
    @Transactional
    void getAllGameSessionsByEndTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where endTime is greater than or equal to
        defaultGameSessionFiltering("endTime.greaterThanOrEqual=" + DEFAULT_END_TIME, "endTime.greaterThanOrEqual=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllGameSessionsByEndTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where endTime is less than or equal to
        defaultGameSessionFiltering("endTime.lessThanOrEqual=" + DEFAULT_END_TIME, "endTime.lessThanOrEqual=" + SMALLER_END_TIME);
    }

    @Test
    @Transactional
    void getAllGameSessionsByEndTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where endTime is less than
        defaultGameSessionFiltering("endTime.lessThan=" + UPDATED_END_TIME, "endTime.lessThan=" + DEFAULT_END_TIME);
    }

    @Test
    @Transactional
    void getAllGameSessionsByEndTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where endTime is greater than
        defaultGameSessionFiltering("endTime.greaterThan=" + SMALLER_END_TIME, "endTime.greaterThan=" + DEFAULT_END_TIME);
    }

    @Test
    @Transactional
    void getAllGameSessionsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where status equals to
        defaultGameSessionFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGameSessionsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where status in
        defaultGameSessionFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGameSessionsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where status is not null
        defaultGameSessionFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllGameSessionsByStatusContainsSomething() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where status contains
        defaultGameSessionFiltering("status.contains=" + DEFAULT_STATUS, "status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGameSessionsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where status does not contain
        defaultGameSessionFiltering("status.doesNotContain=" + UPDATED_STATUS, "status.doesNotContain=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllGameSessionsByCurrentQuestionIndexIsEqualToSomething() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where currentQuestionIndex equals to
        defaultGameSessionFiltering(
            "currentQuestionIndex.equals=" + DEFAULT_CURRENT_QUESTION_INDEX,
            "currentQuestionIndex.equals=" + UPDATED_CURRENT_QUESTION_INDEX
        );
    }

    @Test
    @Transactional
    void getAllGameSessionsByCurrentQuestionIndexIsInShouldWork() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where currentQuestionIndex in
        defaultGameSessionFiltering(
            "currentQuestionIndex.in=" + DEFAULT_CURRENT_QUESTION_INDEX + "," + UPDATED_CURRENT_QUESTION_INDEX,
            "currentQuestionIndex.in=" + UPDATED_CURRENT_QUESTION_INDEX
        );
    }

    @Test
    @Transactional
    void getAllGameSessionsByCurrentQuestionIndexIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where currentQuestionIndex is not null
        defaultGameSessionFiltering("currentQuestionIndex.specified=true", "currentQuestionIndex.specified=false");
    }

    @Test
    @Transactional
    void getAllGameSessionsByCurrentQuestionIndexIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where currentQuestionIndex is greater than or equal to
        defaultGameSessionFiltering(
            "currentQuestionIndex.greaterThanOrEqual=" + DEFAULT_CURRENT_QUESTION_INDEX,
            "currentQuestionIndex.greaterThanOrEqual=" + UPDATED_CURRENT_QUESTION_INDEX
        );
    }

    @Test
    @Transactional
    void getAllGameSessionsByCurrentQuestionIndexIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where currentQuestionIndex is less than or equal to
        defaultGameSessionFiltering(
            "currentQuestionIndex.lessThanOrEqual=" + DEFAULT_CURRENT_QUESTION_INDEX,
            "currentQuestionIndex.lessThanOrEqual=" + SMALLER_CURRENT_QUESTION_INDEX
        );
    }

    @Test
    @Transactional
    void getAllGameSessionsByCurrentQuestionIndexIsLessThanSomething() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where currentQuestionIndex is less than
        defaultGameSessionFiltering(
            "currentQuestionIndex.lessThan=" + UPDATED_CURRENT_QUESTION_INDEX,
            "currentQuestionIndex.lessThan=" + DEFAULT_CURRENT_QUESTION_INDEX
        );
    }

    @Test
    @Transactional
    void getAllGameSessionsByCurrentQuestionIndexIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList where currentQuestionIndex is greater than
        defaultGameSessionFiltering(
            "currentQuestionIndex.greaterThan=" + SMALLER_CURRENT_QUESTION_INDEX,
            "currentQuestionIndex.greaterThan=" + DEFAULT_CURRENT_QUESTION_INDEX
        );
    }

    private void defaultGameSessionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultGameSessionShouldBeFound(shouldBeFound);
        defaultGameSessionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGameSessionShouldBeFound(String filter) throws Exception {
        restGameSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gameSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(sameInstant(DEFAULT_END_TIME))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].currentQuestionIndex").value(hasItem(DEFAULT_CURRENT_QUESTION_INDEX)));

        // Check, that the count call also returns 1
        restGameSessionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGameSessionShouldNotBeFound(String filter) throws Exception {
        restGameSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGameSessionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGameSession() throws Exception {
        // Get the gameSession
        restGameSessionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGameSession() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the gameSession
        GameSession updatedGameSession = gameSessionRepository.findById(gameSession.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGameSession are not directly saved in db
        em.detach(updatedGameSession);
        updatedGameSession
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .status(UPDATED_STATUS)
            .currentQuestionIndex(UPDATED_CURRENT_QUESTION_INDEX);
        GameSessionDTO gameSessionDTO = gameSessionMapper.toDto(updatedGameSession);

        restGameSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameSessionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(gameSessionDTO))
            )
            .andExpect(status().isOk());

        // Validate the GameSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGameSessionToMatchAllProperties(updatedGameSession);
    }

    @Test
    @Transactional
    void putNonExistingGameSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gameSession.setId(longCount.incrementAndGet());

        // Create the GameSession
        GameSessionDTO gameSessionDTO = gameSessionMapper.toDto(gameSession);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameSessionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(gameSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGameSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gameSession.setId(longCount.incrementAndGet());

        // Create the GameSession
        GameSessionDTO gameSessionDTO = gameSessionMapper.toDto(gameSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(gameSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGameSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gameSession.setId(longCount.incrementAndGet());

        // Create the GameSession
        GameSessionDTO gameSessionDTO = gameSessionMapper.toDto(gameSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameSessionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gameSessionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGameSessionWithPatch() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the gameSession using partial update
        GameSession partialUpdatedGameSession = new GameSession();
        partialUpdatedGameSession.setId(gameSession.getId());

        partialUpdatedGameSession.status(UPDATED_STATUS);

        restGameSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGameSession))
            )
            .andExpect(status().isOk());

        // Validate the GameSession in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGameSessionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedGameSession, gameSession),
            getPersistedGameSession(gameSession)
        );
    }

    @Test
    @Transactional
    void fullUpdateGameSessionWithPatch() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the gameSession using partial update
        GameSession partialUpdatedGameSession = new GameSession();
        partialUpdatedGameSession.setId(gameSession.getId());

        partialUpdatedGameSession
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .status(UPDATED_STATUS)
            .currentQuestionIndex(UPDATED_CURRENT_QUESTION_INDEX);

        restGameSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGameSession))
            )
            .andExpect(status().isOk());

        // Validate the GameSession in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGameSessionUpdatableFieldsEquals(partialUpdatedGameSession, getPersistedGameSession(partialUpdatedGameSession));
    }

    @Test
    @Transactional
    void patchNonExistingGameSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gameSession.setId(longCount.incrementAndGet());

        // Create the GameSession
        GameSessionDTO gameSessionDTO = gameSessionMapper.toDto(gameSession);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gameSessionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(gameSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGameSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gameSession.setId(longCount.incrementAndGet());

        // Create the GameSession
        GameSessionDTO gameSessionDTO = gameSessionMapper.toDto(gameSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(gameSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGameSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gameSession.setId(longCount.incrementAndGet());

        // Create the GameSession
        GameSessionDTO gameSessionDTO = gameSessionMapper.toDto(gameSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameSessionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(gameSessionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGameSession() throws Exception {
        // Initialize the database
        gameSessionRepository.saveAndFlush(gameSession);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the gameSession
        restGameSessionMockMvc
            .perform(delete(ENTITY_API_URL_ID, gameSession.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return gameSessionRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected GameSession getPersistedGameSession(GameSession gameSession) {
        return gameSessionRepository.findById(gameSession.getId()).orElseThrow();
    }

    protected void assertPersistedGameSessionToMatchAllProperties(GameSession expectedGameSession) {
        assertGameSessionAllPropertiesEquals(expectedGameSession, getPersistedGameSession(expectedGameSession));
    }

    protected void assertPersistedGameSessionToMatchUpdatableProperties(GameSession expectedGameSession) {
        assertGameSessionAllUpdatablePropertiesEquals(expectedGameSession, getPersistedGameSession(expectedGameSession));
    }
}
