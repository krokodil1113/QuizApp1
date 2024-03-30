package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.QuizRatingAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.QuizRating;
import com.mycompany.myapp.repository.QuizRatingRepository;
import com.mycompany.myapp.service.dto.QuizRatingDTO;
import com.mycompany.myapp.service.mapper.QuizRatingMapper;
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
 * Integration tests for the {@link QuizRatingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuizRatingResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/quiz-ratings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuizRatingRepository quizRatingRepository;

    @Autowired
    private QuizRatingMapper quizRatingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuizRatingMockMvc;

    private QuizRating quizRating;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizRating createEntity(EntityManager em) {
        QuizRating quizRating = new QuizRating().rating(DEFAULT_RATING).comment(DEFAULT_COMMENT);
        return quizRating;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizRating createUpdatedEntity(EntityManager em) {
        QuizRating quizRating = new QuizRating().rating(UPDATED_RATING).comment(UPDATED_COMMENT);
        return quizRating;
    }

    @BeforeEach
    public void initTest() {
        quizRating = createEntity(em);
    }

    @Test
    @Transactional
    void createQuizRating() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QuizRating
        QuizRatingDTO quizRatingDTO = quizRatingMapper.toDto(quizRating);
        var returnedQuizRatingDTO = om.readValue(
            restQuizRatingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quizRatingDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuizRatingDTO.class
        );

        // Validate the QuizRating in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuizRating = quizRatingMapper.toEntity(returnedQuizRatingDTO);
        assertQuizRatingUpdatableFieldsEquals(returnedQuizRating, getPersistedQuizRating(returnedQuizRating));
    }

    @Test
    @Transactional
    void createQuizRatingWithExistingId() throws Exception {
        // Create the QuizRating with an existing ID
        quizRating.setId(1L);
        QuizRatingDTO quizRatingDTO = quizRatingMapper.toDto(quizRating);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuizRatingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quizRatingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuizRating in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuizRatings() throws Exception {
        // Initialize the database
        quizRatingRepository.saveAndFlush(quizRating);

        // Get all the quizRatingList
        restQuizRatingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quizRating.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }

    @Test
    @Transactional
    void getQuizRating() throws Exception {
        // Initialize the database
        quizRatingRepository.saveAndFlush(quizRating);

        // Get the quizRating
        restQuizRatingMockMvc
            .perform(get(ENTITY_API_URL_ID, quizRating.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quizRating.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingQuizRating() throws Exception {
        // Get the quizRating
        restQuizRatingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuizRating() throws Exception {
        // Initialize the database
        quizRatingRepository.saveAndFlush(quizRating);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quizRating
        QuizRating updatedQuizRating = quizRatingRepository.findById(quizRating.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuizRating are not directly saved in db
        em.detach(updatedQuizRating);
        updatedQuizRating.rating(UPDATED_RATING).comment(UPDATED_COMMENT);
        QuizRatingDTO quizRatingDTO = quizRatingMapper.toDto(updatedQuizRating);

        restQuizRatingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizRatingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quizRatingDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuizRating in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuizRatingToMatchAllProperties(updatedQuizRating);
    }

    @Test
    @Transactional
    void putNonExistingQuizRating() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizRating.setId(longCount.incrementAndGet());

        // Create the QuizRating
        QuizRatingDTO quizRatingDTO = quizRatingMapper.toDto(quizRating);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizRatingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizRatingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quizRatingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizRating in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuizRating() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizRating.setId(longCount.incrementAndGet());

        // Create the QuizRating
        QuizRatingDTO quizRatingDTO = quizRatingMapper.toDto(quizRating);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizRatingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quizRatingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizRating in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuizRating() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizRating.setId(longCount.incrementAndGet());

        // Create the QuizRating
        QuizRatingDTO quizRatingDTO = quizRatingMapper.toDto(quizRating);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizRatingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quizRatingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuizRating in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuizRatingWithPatch() throws Exception {
        // Initialize the database
        quizRatingRepository.saveAndFlush(quizRating);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quizRating using partial update
        QuizRating partialUpdatedQuizRating = new QuizRating();
        partialUpdatedQuizRating.setId(quizRating.getId());

        partialUpdatedQuizRating.comment(UPDATED_COMMENT);

        restQuizRatingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizRating.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuizRating))
            )
            .andExpect(status().isOk());

        // Validate the QuizRating in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuizRatingUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuizRating, quizRating),
            getPersistedQuizRating(quizRating)
        );
    }

    @Test
    @Transactional
    void fullUpdateQuizRatingWithPatch() throws Exception {
        // Initialize the database
        quizRatingRepository.saveAndFlush(quizRating);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quizRating using partial update
        QuizRating partialUpdatedQuizRating = new QuizRating();
        partialUpdatedQuizRating.setId(quizRating.getId());

        partialUpdatedQuizRating.rating(UPDATED_RATING).comment(UPDATED_COMMENT);

        restQuizRatingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizRating.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuizRating))
            )
            .andExpect(status().isOk());

        // Validate the QuizRating in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuizRatingUpdatableFieldsEquals(partialUpdatedQuizRating, getPersistedQuizRating(partialUpdatedQuizRating));
    }

    @Test
    @Transactional
    void patchNonExistingQuizRating() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizRating.setId(longCount.incrementAndGet());

        // Create the QuizRating
        QuizRatingDTO quizRatingDTO = quizRatingMapper.toDto(quizRating);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizRatingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quizRatingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quizRatingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizRating in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuizRating() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizRating.setId(longCount.incrementAndGet());

        // Create the QuizRating
        QuizRatingDTO quizRatingDTO = quizRatingMapper.toDto(quizRating);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizRatingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quizRatingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizRating in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuizRating() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizRating.setId(longCount.incrementAndGet());

        // Create the QuizRating
        QuizRatingDTO quizRatingDTO = quizRatingMapper.toDto(quizRating);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizRatingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quizRatingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuizRating in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuizRating() throws Exception {
        // Initialize the database
        quizRatingRepository.saveAndFlush(quizRating);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the quizRating
        restQuizRatingMockMvc
            .perform(delete(ENTITY_API_URL_ID, quizRating.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return quizRatingRepository.count();
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

    protected QuizRating getPersistedQuizRating(QuizRating quizRating) {
        return quizRatingRepository.findById(quizRating.getId()).orElseThrow();
    }

    protected void assertPersistedQuizRatingToMatchAllProperties(QuizRating expectedQuizRating) {
        assertQuizRatingAllPropertiesEquals(expectedQuizRating, getPersistedQuizRating(expectedQuizRating));
    }

    protected void assertPersistedQuizRatingToMatchUpdatableProperties(QuizRating expectedQuizRating) {
        assertQuizRatingAllUpdatablePropertiesEquals(expectedQuizRating, getPersistedQuizRating(expectedQuizRating));
    }
}
