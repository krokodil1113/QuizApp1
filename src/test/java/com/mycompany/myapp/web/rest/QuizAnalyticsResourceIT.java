package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.QuizAnalyticsAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.QuizAnalytics;
import com.mycompany.myapp.repository.QuizAnalyticsRepository;
import com.mycompany.myapp.service.dto.QuizAnalyticsDTO;
import com.mycompany.myapp.service.mapper.QuizAnalyticsMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link QuizAnalyticsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuizAnalyticsResourceIT {

    private static final Integer DEFAULT_TOTAL_ATTEMPTS = 1;
    private static final Integer UPDATED_TOTAL_ATTEMPTS = 2;

    private static final Double DEFAULT_AVERAGE_SCORE = 1D;
    private static final Double UPDATED_AVERAGE_SCORE = 2D;

    private static final Double DEFAULT_COMPLETION_RATE = 1D;
    private static final Double UPDATED_COMPLETION_RATE = 2D;

    private static final String ENTITY_API_URL = "/api/quiz-analytics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuizAnalyticsRepository quizAnalyticsRepository;

    @Autowired
    private QuizAnalyticsMapper quizAnalyticsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuizAnalyticsMockMvc;

    private QuizAnalytics quizAnalytics;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizAnalytics createEntity(EntityManager em) {
        QuizAnalytics quizAnalytics = new QuizAnalytics()
            .totalAttempts(DEFAULT_TOTAL_ATTEMPTS)
            .averageScore(DEFAULT_AVERAGE_SCORE)
            .completionRate(DEFAULT_COMPLETION_RATE);
        return quizAnalytics;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizAnalytics createUpdatedEntity(EntityManager em) {
        QuizAnalytics quizAnalytics = new QuizAnalytics()
            .totalAttempts(UPDATED_TOTAL_ATTEMPTS)
            .averageScore(UPDATED_AVERAGE_SCORE)
            .completionRate(UPDATED_COMPLETION_RATE);
        return quizAnalytics;
    }

    @BeforeEach
    public void initTest() {
        quizAnalytics = createEntity(em);
    }

    @Test
    @Transactional
    void createQuizAnalytics() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QuizAnalytics
        QuizAnalyticsDTO quizAnalyticsDTO = quizAnalyticsMapper.toDto(quizAnalytics);
        var returnedQuizAnalyticsDTO = om.readValue(
            restQuizAnalyticsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quizAnalyticsDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuizAnalyticsDTO.class
        );

        // Validate the QuizAnalytics in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuizAnalytics = quizAnalyticsMapper.toEntity(returnedQuizAnalyticsDTO);
        assertQuizAnalyticsUpdatableFieldsEquals(returnedQuizAnalytics, getPersistedQuizAnalytics(returnedQuizAnalytics));
    }

    @Test
    @Transactional
    void createQuizAnalyticsWithExistingId() throws Exception {
        // Create the QuizAnalytics with an existing ID
        quizAnalytics.setId(1L);
        QuizAnalyticsDTO quizAnalyticsDTO = quizAnalyticsMapper.toDto(quizAnalytics);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuizAnalyticsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quizAnalyticsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuizAnalytics in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuizAnalytics() throws Exception {
        // Initialize the database
        quizAnalyticsRepository.saveAndFlush(quizAnalytics);

        // Get all the quizAnalyticsList
        restQuizAnalyticsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quizAnalytics.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalAttempts").value(hasItem(DEFAULT_TOTAL_ATTEMPTS)))
            .andExpect(jsonPath("$.[*].averageScore").value(hasItem(DEFAULT_AVERAGE_SCORE.doubleValue())))
            .andExpect(jsonPath("$.[*].completionRate").value(hasItem(DEFAULT_COMPLETION_RATE.doubleValue())));
    }

    @Test
    @Transactional
    void getQuizAnalytics() throws Exception {
        // Initialize the database
        quizAnalyticsRepository.saveAndFlush(quizAnalytics);

        // Get the quizAnalytics
        restQuizAnalyticsMockMvc
            .perform(get(ENTITY_API_URL_ID, quizAnalytics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quizAnalytics.getId().intValue()))
            .andExpect(jsonPath("$.totalAttempts").value(DEFAULT_TOTAL_ATTEMPTS))
            .andExpect(jsonPath("$.averageScore").value(DEFAULT_AVERAGE_SCORE.doubleValue()))
            .andExpect(jsonPath("$.completionRate").value(DEFAULT_COMPLETION_RATE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingQuizAnalytics() throws Exception {
        // Get the quizAnalytics
        restQuizAnalyticsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuizAnalytics() throws Exception {
        // Initialize the database
        quizAnalyticsRepository.saveAndFlush(quizAnalytics);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quizAnalytics
        QuizAnalytics updatedQuizAnalytics = quizAnalyticsRepository.findById(quizAnalytics.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuizAnalytics are not directly saved in db
        em.detach(updatedQuizAnalytics);
        updatedQuizAnalytics
            .totalAttempts(UPDATED_TOTAL_ATTEMPTS)
            .averageScore(UPDATED_AVERAGE_SCORE)
            .completionRate(UPDATED_COMPLETION_RATE);
        QuizAnalyticsDTO quizAnalyticsDTO = quizAnalyticsMapper.toDto(updatedQuizAnalytics);

        restQuizAnalyticsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizAnalyticsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quizAnalyticsDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuizAnalytics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuizAnalyticsToMatchAllProperties(updatedQuizAnalytics);
    }

    @Test
    @Transactional
    void putNonExistingQuizAnalytics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizAnalytics.setId(longCount.incrementAndGet());

        // Create the QuizAnalytics
        QuizAnalyticsDTO quizAnalyticsDTO = quizAnalyticsMapper.toDto(quizAnalytics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizAnalyticsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizAnalyticsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quizAnalyticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizAnalytics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuizAnalytics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizAnalytics.setId(longCount.incrementAndGet());

        // Create the QuizAnalytics
        QuizAnalyticsDTO quizAnalyticsDTO = quizAnalyticsMapper.toDto(quizAnalytics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizAnalyticsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quizAnalyticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizAnalytics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuizAnalytics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizAnalytics.setId(longCount.incrementAndGet());

        // Create the QuizAnalytics
        QuizAnalyticsDTO quizAnalyticsDTO = quizAnalyticsMapper.toDto(quizAnalytics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizAnalyticsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quizAnalyticsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuizAnalytics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuizAnalyticsWithPatch() throws Exception {
        // Initialize the database
        quizAnalyticsRepository.saveAndFlush(quizAnalytics);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quizAnalytics using partial update
        QuizAnalytics partialUpdatedQuizAnalytics = new QuizAnalytics();
        partialUpdatedQuizAnalytics.setId(quizAnalytics.getId());

        restQuizAnalyticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizAnalytics.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuizAnalytics))
            )
            .andExpect(status().isOk());

        // Validate the QuizAnalytics in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuizAnalyticsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuizAnalytics, quizAnalytics),
            getPersistedQuizAnalytics(quizAnalytics)
        );
    }

    @Test
    @Transactional
    void fullUpdateQuizAnalyticsWithPatch() throws Exception {
        // Initialize the database
        quizAnalyticsRepository.saveAndFlush(quizAnalytics);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quizAnalytics using partial update
        QuizAnalytics partialUpdatedQuizAnalytics = new QuizAnalytics();
        partialUpdatedQuizAnalytics.setId(quizAnalytics.getId());

        partialUpdatedQuizAnalytics
            .totalAttempts(UPDATED_TOTAL_ATTEMPTS)
            .averageScore(UPDATED_AVERAGE_SCORE)
            .completionRate(UPDATED_COMPLETION_RATE);

        restQuizAnalyticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizAnalytics.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuizAnalytics))
            )
            .andExpect(status().isOk());

        // Validate the QuizAnalytics in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuizAnalyticsUpdatableFieldsEquals(partialUpdatedQuizAnalytics, getPersistedQuizAnalytics(partialUpdatedQuizAnalytics));
    }

    @Test
    @Transactional
    void patchNonExistingQuizAnalytics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizAnalytics.setId(longCount.incrementAndGet());

        // Create the QuizAnalytics
        QuizAnalyticsDTO quizAnalyticsDTO = quizAnalyticsMapper.toDto(quizAnalytics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizAnalyticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quizAnalyticsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quizAnalyticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizAnalytics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuizAnalytics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizAnalytics.setId(longCount.incrementAndGet());

        // Create the QuizAnalytics
        QuizAnalyticsDTO quizAnalyticsDTO = quizAnalyticsMapper.toDto(quizAnalytics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizAnalyticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quizAnalyticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizAnalytics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuizAnalytics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizAnalytics.setId(longCount.incrementAndGet());

        // Create the QuizAnalytics
        QuizAnalyticsDTO quizAnalyticsDTO = quizAnalyticsMapper.toDto(quizAnalytics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizAnalyticsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quizAnalyticsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuizAnalytics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuizAnalytics() throws Exception {
        // Initialize the database
        quizAnalyticsRepository.saveAndFlush(quizAnalytics);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the quizAnalytics
        restQuizAnalyticsMockMvc
            .perform(delete(ENTITY_API_URL_ID, quizAnalytics.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return quizAnalyticsRepository.count();
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

    protected QuizAnalytics getPersistedQuizAnalytics(QuizAnalytics quizAnalytics) {
        return quizAnalyticsRepository.findById(quizAnalytics.getId()).orElseThrow();
    }

    protected void assertPersistedQuizAnalyticsToMatchAllProperties(QuizAnalytics expectedQuizAnalytics) {
        assertQuizAnalyticsAllPropertiesEquals(expectedQuizAnalytics, getPersistedQuizAnalytics(expectedQuizAnalytics));
    }

    protected void assertPersistedQuizAnalyticsToMatchUpdatableProperties(QuizAnalytics expectedQuizAnalytics) {
        assertQuizAnalyticsAllUpdatablePropertiesEquals(expectedQuizAnalytics, getPersistedQuizAnalytics(expectedQuizAnalytics));
    }
}
