package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.QuizAttemptAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.QuizAttempt;
import com.mycompany.myapp.repository.QuizAttemptRepository;
import com.mycompany.myapp.service.dto.QuizAttemptDTO;
import com.mycompany.myapp.service.mapper.QuizAttemptMapper;
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
 * Integration tests for the {@link QuizAttemptResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuizAttemptResourceIT {

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_SCORE = 1;
    private static final Integer UPDATED_SCORE = 2;

    private static final String ENTITY_API_URL = "/api/quiz-attempts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Autowired
    private QuizAttemptMapper quizAttemptMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuizAttemptMockMvc;

    private QuizAttempt quizAttempt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizAttempt createEntity(EntityManager em) {
        QuizAttempt quizAttempt = new QuizAttempt().startTime(DEFAULT_START_TIME).endTime(DEFAULT_END_TIME).score(DEFAULT_SCORE);
        return quizAttempt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizAttempt createUpdatedEntity(EntityManager em) {
        QuizAttempt quizAttempt = new QuizAttempt().startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME).score(UPDATED_SCORE);
        return quizAttempt;
    }

    @BeforeEach
    public void initTest() {
        quizAttempt = createEntity(em);
    }

    @Test
    @Transactional
    void createQuizAttempt() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QuizAttempt
        QuizAttemptDTO quizAttemptDTO = quizAttemptMapper.toDto(quizAttempt);
        var returnedQuizAttemptDTO = om.readValue(
            restQuizAttemptMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quizAttemptDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuizAttemptDTO.class
        );

        // Validate the QuizAttempt in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuizAttempt = quizAttemptMapper.toEntity(returnedQuizAttemptDTO);
        assertQuizAttemptUpdatableFieldsEquals(returnedQuizAttempt, getPersistedQuizAttempt(returnedQuizAttempt));
    }

    @Test
    @Transactional
    void createQuizAttemptWithExistingId() throws Exception {
        // Create the QuizAttempt with an existing ID
        quizAttempt.setId(1L);
        QuizAttemptDTO quizAttemptDTO = quizAttemptMapper.toDto(quizAttempt);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuizAttemptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quizAttemptDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuizAttempt in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuizAttempts() throws Exception {
        // Initialize the database
        quizAttemptRepository.saveAndFlush(quizAttempt);

        // Get all the quizAttemptList
        restQuizAttemptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quizAttempt.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(sameInstant(DEFAULT_END_TIME))))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)));
    }

    @Test
    @Transactional
    void getQuizAttempt() throws Exception {
        // Initialize the database
        quizAttemptRepository.saveAndFlush(quizAttempt);

        // Get the quizAttempt
        restQuizAttemptMockMvc
            .perform(get(ENTITY_API_URL_ID, quizAttempt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quizAttempt.getId().intValue()))
            .andExpect(jsonPath("$.startTime").value(sameInstant(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.endTime").value(sameInstant(DEFAULT_END_TIME)))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE));
    }

    @Test
    @Transactional
    void getNonExistingQuizAttempt() throws Exception {
        // Get the quizAttempt
        restQuizAttemptMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuizAttempt() throws Exception {
        // Initialize the database
        quizAttemptRepository.saveAndFlush(quizAttempt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quizAttempt
        QuizAttempt updatedQuizAttempt = quizAttemptRepository.findById(quizAttempt.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuizAttempt are not directly saved in db
        em.detach(updatedQuizAttempt);
        updatedQuizAttempt.startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME).score(UPDATED_SCORE);
        QuizAttemptDTO quizAttemptDTO = quizAttemptMapper.toDto(updatedQuizAttempt);

        restQuizAttemptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizAttemptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quizAttemptDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuizAttempt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuizAttemptToMatchAllProperties(updatedQuizAttempt);
    }

    @Test
    @Transactional
    void putNonExistingQuizAttempt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizAttempt.setId(longCount.incrementAndGet());

        // Create the QuizAttempt
        QuizAttemptDTO quizAttemptDTO = quizAttemptMapper.toDto(quizAttempt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizAttemptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizAttemptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quizAttemptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizAttempt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuizAttempt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizAttempt.setId(longCount.incrementAndGet());

        // Create the QuizAttempt
        QuizAttemptDTO quizAttemptDTO = quizAttemptMapper.toDto(quizAttempt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizAttemptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quizAttemptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizAttempt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuizAttempt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizAttempt.setId(longCount.incrementAndGet());

        // Create the QuizAttempt
        QuizAttemptDTO quizAttemptDTO = quizAttemptMapper.toDto(quizAttempt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizAttemptMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quizAttemptDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuizAttempt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuizAttemptWithPatch() throws Exception {
        // Initialize the database
        quizAttemptRepository.saveAndFlush(quizAttempt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quizAttempt using partial update
        QuizAttempt partialUpdatedQuizAttempt = new QuizAttempt();
        partialUpdatedQuizAttempt.setId(quizAttempt.getId());

        restQuizAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizAttempt.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuizAttempt))
            )
            .andExpect(status().isOk());

        // Validate the QuizAttempt in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuizAttemptUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuizAttempt, quizAttempt),
            getPersistedQuizAttempt(quizAttempt)
        );
    }

    @Test
    @Transactional
    void fullUpdateQuizAttemptWithPatch() throws Exception {
        // Initialize the database
        quizAttemptRepository.saveAndFlush(quizAttempt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quizAttempt using partial update
        QuizAttempt partialUpdatedQuizAttempt = new QuizAttempt();
        partialUpdatedQuizAttempt.setId(quizAttempt.getId());

        partialUpdatedQuizAttempt.startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME).score(UPDATED_SCORE);

        restQuizAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizAttempt.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuizAttempt))
            )
            .andExpect(status().isOk());

        // Validate the QuizAttempt in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuizAttemptUpdatableFieldsEquals(partialUpdatedQuizAttempt, getPersistedQuizAttempt(partialUpdatedQuizAttempt));
    }

    @Test
    @Transactional
    void patchNonExistingQuizAttempt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizAttempt.setId(longCount.incrementAndGet());

        // Create the QuizAttempt
        QuizAttemptDTO quizAttemptDTO = quizAttemptMapper.toDto(quizAttempt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quizAttemptDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quizAttemptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizAttempt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuizAttempt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizAttempt.setId(longCount.incrementAndGet());

        // Create the QuizAttempt
        QuizAttemptDTO quizAttemptDTO = quizAttemptMapper.toDto(quizAttempt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quizAttemptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizAttempt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuizAttempt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizAttempt.setId(longCount.incrementAndGet());

        // Create the QuizAttempt
        QuizAttemptDTO quizAttemptDTO = quizAttemptMapper.toDto(quizAttempt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizAttemptMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quizAttemptDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuizAttempt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuizAttempt() throws Exception {
        // Initialize the database
        quizAttemptRepository.saveAndFlush(quizAttempt);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the quizAttempt
        restQuizAttemptMockMvc
            .perform(delete(ENTITY_API_URL_ID, quizAttempt.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return quizAttemptRepository.count();
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

    protected QuizAttempt getPersistedQuizAttempt(QuizAttempt quizAttempt) {
        return quizAttemptRepository.findById(quizAttempt.getId()).orElseThrow();
    }

    protected void assertPersistedQuizAttemptToMatchAllProperties(QuizAttempt expectedQuizAttempt) {
        assertQuizAttemptAllPropertiesEquals(expectedQuizAttempt, getPersistedQuizAttempt(expectedQuizAttempt));
    }

    protected void assertPersistedQuizAttemptToMatchUpdatableProperties(QuizAttempt expectedQuizAttempt) {
        assertQuizAttemptAllUpdatablePropertiesEquals(expectedQuizAttempt, getPersistedQuizAttempt(expectedQuizAttempt));
    }
}
