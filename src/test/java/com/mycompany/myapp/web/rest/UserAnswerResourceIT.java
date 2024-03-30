package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.UserAnswerAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.UserAnswer;
import com.mycompany.myapp.repository.UserAnswerRepository;
import com.mycompany.myapp.service.dto.UserAnswerDTO;
import com.mycompany.myapp.service.mapper.UserAnswerMapper;
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
 * Integration tests for the {@link UserAnswerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserAnswerResourceIT {

    private static final String DEFAULT_CUSTOM_ANSWER_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOM_ANSWER_TEXT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-answers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @Autowired
    private UserAnswerMapper userAnswerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAnswerMockMvc;

    private UserAnswer userAnswer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAnswer createEntity(EntityManager em) {
        UserAnswer userAnswer = new UserAnswer().customAnswerText(DEFAULT_CUSTOM_ANSWER_TEXT);
        return userAnswer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAnswer createUpdatedEntity(EntityManager em) {
        UserAnswer userAnswer = new UserAnswer().customAnswerText(UPDATED_CUSTOM_ANSWER_TEXT);
        return userAnswer;
    }

    @BeforeEach
    public void initTest() {
        userAnswer = createEntity(em);
    }

    @Test
    @Transactional
    void createUserAnswer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserAnswer
        UserAnswerDTO userAnswerDTO = userAnswerMapper.toDto(userAnswer);
        var returnedUserAnswerDTO = om.readValue(
            restUserAnswerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAnswerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserAnswerDTO.class
        );

        // Validate the UserAnswer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserAnswer = userAnswerMapper.toEntity(returnedUserAnswerDTO);
        assertUserAnswerUpdatableFieldsEquals(returnedUserAnswer, getPersistedUserAnswer(returnedUserAnswer));
    }

    @Test
    @Transactional
    void createUserAnswerWithExistingId() throws Exception {
        // Create the UserAnswer with an existing ID
        userAnswer.setId(1L);
        UserAnswerDTO userAnswerDTO = userAnswerMapper.toDto(userAnswer);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAnswerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAnswerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserAnswer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserAnswers() throws Exception {
        // Initialize the database
        userAnswerRepository.saveAndFlush(userAnswer);

        // Get all the userAnswerList
        restUserAnswerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAnswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].customAnswerText").value(hasItem(DEFAULT_CUSTOM_ANSWER_TEXT)));
    }

    @Test
    @Transactional
    void getUserAnswer() throws Exception {
        // Initialize the database
        userAnswerRepository.saveAndFlush(userAnswer);

        // Get the userAnswer
        restUserAnswerMockMvc
            .perform(get(ENTITY_API_URL_ID, userAnswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAnswer.getId().intValue()))
            .andExpect(jsonPath("$.customAnswerText").value(DEFAULT_CUSTOM_ANSWER_TEXT));
    }

    @Test
    @Transactional
    void getNonExistingUserAnswer() throws Exception {
        // Get the userAnswer
        restUserAnswerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserAnswer() throws Exception {
        // Initialize the database
        userAnswerRepository.saveAndFlush(userAnswer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAnswer
        UserAnswer updatedUserAnswer = userAnswerRepository.findById(userAnswer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserAnswer are not directly saved in db
        em.detach(updatedUserAnswer);
        updatedUserAnswer.customAnswerText(UPDATED_CUSTOM_ANSWER_TEXT);
        UserAnswerDTO userAnswerDTO = userAnswerMapper.toDto(updatedUserAnswer);

        restUserAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAnswerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAnswerDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserAnswer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserAnswerToMatchAllProperties(updatedUserAnswer);
    }

    @Test
    @Transactional
    void putNonExistingUserAnswer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAnswer.setId(longCount.incrementAndGet());

        // Create the UserAnswer
        UserAnswerDTO userAnswerDTO = userAnswerMapper.toDto(userAnswer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAnswerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAnswer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserAnswer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAnswer.setId(longCount.incrementAndGet());

        // Create the UserAnswer
        UserAnswerDTO userAnswerDTO = userAnswerMapper.toDto(userAnswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAnswer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserAnswer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAnswer.setId(longCount.incrementAndGet());

        // Create the UserAnswer
        UserAnswerDTO userAnswerDTO = userAnswerMapper.toDto(userAnswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAnswerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAnswerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAnswer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserAnswerWithPatch() throws Exception {
        // Initialize the database
        userAnswerRepository.saveAndFlush(userAnswer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAnswer using partial update
        UserAnswer partialUpdatedUserAnswer = new UserAnswer();
        partialUpdatedUserAnswer.setId(userAnswer.getId());

        restUserAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserAnswer))
            )
            .andExpect(status().isOk());

        // Validate the UserAnswer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAnswerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserAnswer, userAnswer),
            getPersistedUserAnswer(userAnswer)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserAnswerWithPatch() throws Exception {
        // Initialize the database
        userAnswerRepository.saveAndFlush(userAnswer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAnswer using partial update
        UserAnswer partialUpdatedUserAnswer = new UserAnswer();
        partialUpdatedUserAnswer.setId(userAnswer.getId());

        partialUpdatedUserAnswer.customAnswerText(UPDATED_CUSTOM_ANSWER_TEXT);

        restUserAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserAnswer))
            )
            .andExpect(status().isOk());

        // Validate the UserAnswer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAnswerUpdatableFieldsEquals(partialUpdatedUserAnswer, getPersistedUserAnswer(partialUpdatedUserAnswer));
    }

    @Test
    @Transactional
    void patchNonExistingUserAnswer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAnswer.setId(longCount.incrementAndGet());

        // Create the UserAnswer
        UserAnswerDTO userAnswerDTO = userAnswerMapper.toDto(userAnswer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userAnswerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAnswer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserAnswer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAnswer.setId(longCount.incrementAndGet());

        // Create the UserAnswer
        UserAnswerDTO userAnswerDTO = userAnswerMapper.toDto(userAnswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAnswer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserAnswer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAnswer.setId(longCount.incrementAndGet());

        // Create the UserAnswer
        UserAnswerDTO userAnswerDTO = userAnswerMapper.toDto(userAnswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAnswerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userAnswerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAnswer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserAnswer() throws Exception {
        // Initialize the database
        userAnswerRepository.saveAndFlush(userAnswer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userAnswer
        restUserAnswerMockMvc
            .perform(delete(ENTITY_API_URL_ID, userAnswer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userAnswerRepository.count();
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

    protected UserAnswer getPersistedUserAnswer(UserAnswer userAnswer) {
        return userAnswerRepository.findById(userAnswer.getId()).orElseThrow();
    }

    protected void assertPersistedUserAnswerToMatchAllProperties(UserAnswer expectedUserAnswer) {
        assertUserAnswerAllPropertiesEquals(expectedUserAnswer, getPersistedUserAnswer(expectedUserAnswer));
    }

    protected void assertPersistedUserAnswerToMatchUpdatableProperties(UserAnswer expectedUserAnswer) {
        assertUserAnswerAllUpdatablePropertiesEquals(expectedUserAnswer, getPersistedUserAnswer(expectedUserAnswer));
    }
}
