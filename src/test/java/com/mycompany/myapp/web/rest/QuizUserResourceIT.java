package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.QuizUserAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.QuizUser;
import com.mycompany.myapp.repository.QuizUserRepository;
import com.mycompany.myapp.service.dto.QuizUserDTO;
import com.mycompany.myapp.service.mapper.QuizUserMapper;
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
 * Integration tests for the {@link QuizUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuizUserResourceIT {

    private static final String DEFAULT_NICKNAME = "AAAAAAAAAA";
    private static final String UPDATED_NICKNAME = "BBBBBBBBBB";

    private static final String DEFAULT_BIO = "AAAAAAAAAA";
    private static final String UPDATED_BIO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/quiz-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuizUserRepository quizUserRepository;

    @Autowired
    private QuizUserMapper quizUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuizUserMockMvc;

    private QuizUser quizUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizUser createEntity(EntityManager em) {
        QuizUser quizUser = new QuizUser().nickname(DEFAULT_NICKNAME).bio(DEFAULT_BIO);
        return quizUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizUser createUpdatedEntity(EntityManager em) {
        QuizUser quizUser = new QuizUser().nickname(UPDATED_NICKNAME).bio(UPDATED_BIO);
        return quizUser;
    }

    @BeforeEach
    public void initTest() {
        quizUser = createEntity(em);
    }

    @Test
    @Transactional
    void createQuizUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QuizUser
        QuizUserDTO quizUserDTO = quizUserMapper.toDto(quizUser);
        var returnedQuizUserDTO = om.readValue(
            restQuizUserMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quizUserDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuizUserDTO.class
        );

        // Validate the QuizUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuizUser = quizUserMapper.toEntity(returnedQuizUserDTO);
        assertQuizUserUpdatableFieldsEquals(returnedQuizUser, getPersistedQuizUser(returnedQuizUser));
    }

    @Test
    @Transactional
    void createQuizUserWithExistingId() throws Exception {
        // Create the QuizUser with an existing ID
        quizUser.setId(1L);
        QuizUserDTO quizUserDTO = quizUserMapper.toDto(quizUser);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuizUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quizUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuizUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuizUsers() throws Exception {
        // Initialize the database
        quizUserRepository.saveAndFlush(quizUser);

        // Get all the quizUserList
        restQuizUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quizUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME)))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO.toString())));
    }

    @Test
    @Transactional
    void getQuizUser() throws Exception {
        // Initialize the database
        quizUserRepository.saveAndFlush(quizUser);

        // Get the quizUser
        restQuizUserMockMvc
            .perform(get(ENTITY_API_URL_ID, quizUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quizUser.getId().intValue()))
            .andExpect(jsonPath("$.nickname").value(DEFAULT_NICKNAME))
            .andExpect(jsonPath("$.bio").value(DEFAULT_BIO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingQuizUser() throws Exception {
        // Get the quizUser
        restQuizUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuizUser() throws Exception {
        // Initialize the database
        quizUserRepository.saveAndFlush(quizUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quizUser
        QuizUser updatedQuizUser = quizUserRepository.findById(quizUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuizUser are not directly saved in db
        em.detach(updatedQuizUser);
        updatedQuizUser.nickname(UPDATED_NICKNAME).bio(UPDATED_BIO);
        QuizUserDTO quizUserDTO = quizUserMapper.toDto(updatedQuizUser);

        restQuizUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quizUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuizUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuizUserToMatchAllProperties(updatedQuizUser);
    }

    @Test
    @Transactional
    void putNonExistingQuizUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizUser.setId(longCount.incrementAndGet());

        // Create the QuizUser
        QuizUserDTO quizUserDTO = quizUserMapper.toDto(quizUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quizUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuizUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizUser.setId(longCount.incrementAndGet());

        // Create the QuizUser
        QuizUserDTO quizUserDTO = quizUserMapper.toDto(quizUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quizUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuizUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizUser.setId(longCount.incrementAndGet());

        // Create the QuizUser
        QuizUserDTO quizUserDTO = quizUserMapper.toDto(quizUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quizUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuizUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuizUserWithPatch() throws Exception {
        // Initialize the database
        quizUserRepository.saveAndFlush(quizUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quizUser using partial update
        QuizUser partialUpdatedQuizUser = new QuizUser();
        partialUpdatedQuizUser.setId(quizUser.getId());

        restQuizUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuizUser))
            )
            .andExpect(status().isOk());

        // Validate the QuizUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuizUserUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedQuizUser, quizUser), getPersistedQuizUser(quizUser));
    }

    @Test
    @Transactional
    void fullUpdateQuizUserWithPatch() throws Exception {
        // Initialize the database
        quizUserRepository.saveAndFlush(quizUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quizUser using partial update
        QuizUser partialUpdatedQuizUser = new QuizUser();
        partialUpdatedQuizUser.setId(quizUser.getId());

        partialUpdatedQuizUser.nickname(UPDATED_NICKNAME).bio(UPDATED_BIO);

        restQuizUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuizUser))
            )
            .andExpect(status().isOk());

        // Validate the QuizUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuizUserUpdatableFieldsEquals(partialUpdatedQuizUser, getPersistedQuizUser(partialUpdatedQuizUser));
    }

    @Test
    @Transactional
    void patchNonExistingQuizUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizUser.setId(longCount.incrementAndGet());

        // Create the QuizUser
        QuizUserDTO quizUserDTO = quizUserMapper.toDto(quizUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quizUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quizUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuizUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizUser.setId(longCount.incrementAndGet());

        // Create the QuizUser
        QuizUserDTO quizUserDTO = quizUserMapper.toDto(quizUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quizUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuizUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizUser.setId(longCount.incrementAndGet());

        // Create the QuizUser
        QuizUserDTO quizUserDTO = quizUserMapper.toDto(quizUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quizUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuizUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuizUser() throws Exception {
        // Initialize the database
        quizUserRepository.saveAndFlush(quizUser);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the quizUser
        restQuizUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, quizUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return quizUserRepository.count();
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

    protected QuizUser getPersistedQuizUser(QuizUser quizUser) {
        return quizUserRepository.findById(quizUser.getId()).orElseThrow();
    }

    protected void assertPersistedQuizUserToMatchAllProperties(QuizUser expectedQuizUser) {
        assertQuizUserAllPropertiesEquals(expectedQuizUser, getPersistedQuizUser(expectedQuizUser));
    }

    protected void assertPersistedQuizUserToMatchUpdatableProperties(QuizUser expectedQuizUser) {
        assertQuizUserAllUpdatablePropertiesEquals(expectedQuizUser, getPersistedQuizUser(expectedQuizUser));
    }
}
