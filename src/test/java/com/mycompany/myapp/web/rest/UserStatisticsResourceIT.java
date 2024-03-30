package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.UserStatisticsAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.UserStatistics;
import com.mycompany.myapp.repository.UserStatisticsRepository;
import com.mycompany.myapp.service.dto.UserStatisticsDTO;
import com.mycompany.myapp.service.mapper.UserStatisticsMapper;
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
 * Integration tests for the {@link UserStatisticsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserStatisticsResourceIT {

    private static final Integer DEFAULT_TOTAL_QUIZZES_TAKEN = 1;
    private static final Integer UPDATED_TOTAL_QUIZZES_TAKEN = 2;

    private static final Integer DEFAULT_TOTAL_SCORE = 1;
    private static final Integer UPDATED_TOTAL_SCORE = 2;

    private static final Double DEFAULT_AVERAGE_SCORE = 1D;
    private static final Double UPDATED_AVERAGE_SCORE = 2D;

    private static final String ENTITY_API_URL = "/api/user-statistics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserStatisticsRepository userStatisticsRepository;

    @Autowired
    private UserStatisticsMapper userStatisticsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserStatisticsMockMvc;

    private UserStatistics userStatistics;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserStatistics createEntity(EntityManager em) {
        UserStatistics userStatistics = new UserStatistics()
            .totalQuizzesTaken(DEFAULT_TOTAL_QUIZZES_TAKEN)
            .totalScore(DEFAULT_TOTAL_SCORE)
            .averageScore(DEFAULT_AVERAGE_SCORE);
        return userStatistics;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserStatistics createUpdatedEntity(EntityManager em) {
        UserStatistics userStatistics = new UserStatistics()
            .totalQuizzesTaken(UPDATED_TOTAL_QUIZZES_TAKEN)
            .totalScore(UPDATED_TOTAL_SCORE)
            .averageScore(UPDATED_AVERAGE_SCORE);
        return userStatistics;
    }

    @BeforeEach
    public void initTest() {
        userStatistics = createEntity(em);
    }

    @Test
    @Transactional
    void createUserStatistics() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserStatistics
        UserStatisticsDTO userStatisticsDTO = userStatisticsMapper.toDto(userStatistics);
        var returnedUserStatisticsDTO = om.readValue(
            restUserStatisticsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userStatisticsDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserStatisticsDTO.class
        );

        // Validate the UserStatistics in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserStatistics = userStatisticsMapper.toEntity(returnedUserStatisticsDTO);
        assertUserStatisticsUpdatableFieldsEquals(returnedUserStatistics, getPersistedUserStatistics(returnedUserStatistics));
    }

    @Test
    @Transactional
    void createUserStatisticsWithExistingId() throws Exception {
        // Create the UserStatistics with an existing ID
        userStatistics.setId(1L);
        UserStatisticsDTO userStatisticsDTO = userStatisticsMapper.toDto(userStatistics);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserStatisticsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userStatisticsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserStatistics() throws Exception {
        // Initialize the database
        userStatisticsRepository.saveAndFlush(userStatistics);

        // Get all the userStatisticsList
        restUserStatisticsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userStatistics.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalQuizzesTaken").value(hasItem(DEFAULT_TOTAL_QUIZZES_TAKEN)))
            .andExpect(jsonPath("$.[*].totalScore").value(hasItem(DEFAULT_TOTAL_SCORE)))
            .andExpect(jsonPath("$.[*].averageScore").value(hasItem(DEFAULT_AVERAGE_SCORE.doubleValue())));
    }

    @Test
    @Transactional
    void getUserStatistics() throws Exception {
        // Initialize the database
        userStatisticsRepository.saveAndFlush(userStatistics);

        // Get the userStatistics
        restUserStatisticsMockMvc
            .perform(get(ENTITY_API_URL_ID, userStatistics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userStatistics.getId().intValue()))
            .andExpect(jsonPath("$.totalQuizzesTaken").value(DEFAULT_TOTAL_QUIZZES_TAKEN))
            .andExpect(jsonPath("$.totalScore").value(DEFAULT_TOTAL_SCORE))
            .andExpect(jsonPath("$.averageScore").value(DEFAULT_AVERAGE_SCORE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserStatistics() throws Exception {
        // Get the userStatistics
        restUserStatisticsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserStatistics() throws Exception {
        // Initialize the database
        userStatisticsRepository.saveAndFlush(userStatistics);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userStatistics
        UserStatistics updatedUserStatistics = userStatisticsRepository.findById(userStatistics.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserStatistics are not directly saved in db
        em.detach(updatedUserStatistics);
        updatedUserStatistics
            .totalQuizzesTaken(UPDATED_TOTAL_QUIZZES_TAKEN)
            .totalScore(UPDATED_TOTAL_SCORE)
            .averageScore(UPDATED_AVERAGE_SCORE);
        UserStatisticsDTO userStatisticsDTO = userStatisticsMapper.toDto(updatedUserStatistics);

        restUserStatisticsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userStatisticsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userStatisticsDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserStatisticsToMatchAllProperties(updatedUserStatistics);
    }

    @Test
    @Transactional
    void putNonExistingUserStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userStatistics.setId(longCount.incrementAndGet());

        // Create the UserStatistics
        UserStatisticsDTO userStatisticsDTO = userStatisticsMapper.toDto(userStatistics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserStatisticsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userStatisticsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userStatisticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userStatistics.setId(longCount.incrementAndGet());

        // Create the UserStatistics
        UserStatisticsDTO userStatisticsDTO = userStatisticsMapper.toDto(userStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserStatisticsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userStatisticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userStatistics.setId(longCount.incrementAndGet());

        // Create the UserStatistics
        UserStatisticsDTO userStatisticsDTO = userStatisticsMapper.toDto(userStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserStatisticsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userStatisticsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserStatisticsWithPatch() throws Exception {
        // Initialize the database
        userStatisticsRepository.saveAndFlush(userStatistics);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userStatistics using partial update
        UserStatistics partialUpdatedUserStatistics = new UserStatistics();
        partialUpdatedUserStatistics.setId(userStatistics.getId());

        partialUpdatedUserStatistics.totalQuizzesTaken(UPDATED_TOTAL_QUIZZES_TAKEN).totalScore(UPDATED_TOTAL_SCORE);

        restUserStatisticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserStatistics.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserStatistics))
            )
            .andExpect(status().isOk());

        // Validate the UserStatistics in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserStatisticsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserStatistics, userStatistics),
            getPersistedUserStatistics(userStatistics)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserStatisticsWithPatch() throws Exception {
        // Initialize the database
        userStatisticsRepository.saveAndFlush(userStatistics);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userStatistics using partial update
        UserStatistics partialUpdatedUserStatistics = new UserStatistics();
        partialUpdatedUserStatistics.setId(userStatistics.getId());

        partialUpdatedUserStatistics
            .totalQuizzesTaken(UPDATED_TOTAL_QUIZZES_TAKEN)
            .totalScore(UPDATED_TOTAL_SCORE)
            .averageScore(UPDATED_AVERAGE_SCORE);

        restUserStatisticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserStatistics.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserStatistics))
            )
            .andExpect(status().isOk());

        // Validate the UserStatistics in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserStatisticsUpdatableFieldsEquals(partialUpdatedUserStatistics, getPersistedUserStatistics(partialUpdatedUserStatistics));
    }

    @Test
    @Transactional
    void patchNonExistingUserStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userStatistics.setId(longCount.incrementAndGet());

        // Create the UserStatistics
        UserStatisticsDTO userStatisticsDTO = userStatisticsMapper.toDto(userStatistics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserStatisticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userStatisticsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userStatisticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userStatistics.setId(longCount.incrementAndGet());

        // Create the UserStatistics
        UserStatisticsDTO userStatisticsDTO = userStatisticsMapper.toDto(userStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserStatisticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userStatisticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userStatistics.setId(longCount.incrementAndGet());

        // Create the UserStatistics
        UserStatisticsDTO userStatisticsDTO = userStatisticsMapper.toDto(userStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserStatisticsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userStatisticsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserStatistics() throws Exception {
        // Initialize the database
        userStatisticsRepository.saveAndFlush(userStatistics);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userStatistics
        restUserStatisticsMockMvc
            .perform(delete(ENTITY_API_URL_ID, userStatistics.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userStatisticsRepository.count();
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

    protected UserStatistics getPersistedUserStatistics(UserStatistics userStatistics) {
        return userStatisticsRepository.findById(userStatistics.getId()).orElseThrow();
    }

    protected void assertPersistedUserStatisticsToMatchAllProperties(UserStatistics expectedUserStatistics) {
        assertUserStatisticsAllPropertiesEquals(expectedUserStatistics, getPersistedUserStatistics(expectedUserStatistics));
    }

    protected void assertPersistedUserStatisticsToMatchUpdatableProperties(UserStatistics expectedUserStatistics) {
        assertUserStatisticsAllUpdatablePropertiesEquals(expectedUserStatistics, getPersistedUserStatistics(expectedUserStatistics));
    }
}
