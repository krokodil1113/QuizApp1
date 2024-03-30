package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.QuizAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Quiz;
import com.mycompany.myapp.repository.QuizRepository;
import com.mycompany.myapp.service.dto.QuizDTO;
import com.mycompany.myapp.service.mapper.QuizMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link QuizResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuizResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_DIFFICULTY_LEVEL = 1;
    private static final Integer UPDATED_DIFFICULTY_LEVEL = 2;

    private static final Boolean DEFAULT_IS_PUBLISHED = false;
    private static final Boolean UPDATED_IS_PUBLISHED = true;

    private static final LocalDate DEFAULT_PUBLISH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PUBLISH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/quizzes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizMapper quizMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuizMockMvc;

    private Quiz quiz;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quiz createEntity(EntityManager em) {
        Quiz quiz = new Quiz()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .difficultyLevel(DEFAULT_DIFFICULTY_LEVEL)
            .isPublished(DEFAULT_IS_PUBLISHED)
            .publishDate(DEFAULT_PUBLISH_DATE);
        return quiz;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quiz createUpdatedEntity(EntityManager em) {
        Quiz quiz = new Quiz()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .difficultyLevel(UPDATED_DIFFICULTY_LEVEL)
            .isPublished(UPDATED_IS_PUBLISHED)
            .publishDate(UPDATED_PUBLISH_DATE);
        return quiz;
    }

    @BeforeEach
    public void initTest() {
        quiz = createEntity(em);
    }

    @Test
    @Transactional
    void createQuiz() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Quiz
        QuizDTO quizDTO = quizMapper.toDto(quiz);
        var returnedQuizDTO = om.readValue(
            restQuizMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quizDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuizDTO.class
        );

        // Validate the Quiz in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuiz = quizMapper.toEntity(returnedQuizDTO);
        assertQuizUpdatableFieldsEquals(returnedQuiz, getPersistedQuiz(returnedQuiz));
    }

    @Test
    @Transactional
    void createQuizWithExistingId() throws Exception {
        // Create the Quiz with an existing ID
        quiz.setId(1L);
        QuizDTO quizDTO = quizMapper.toDto(quiz);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuizMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quizDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Quiz in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quiz.setTitle(null);

        // Create the Quiz, which fails.
        QuizDTO quizDTO = quizMapper.toDto(quiz);

        restQuizMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quizDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsPublishedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quiz.setIsPublished(null);

        // Create the Quiz, which fails.
        QuizDTO quizDTO = quizMapper.toDto(quiz);

        restQuizMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quizDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuizzes() throws Exception {
        // Initialize the database
        quizRepository.saveAndFlush(quiz);

        // Get all the quizList
        restQuizMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quiz.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].difficultyLevel").value(hasItem(DEFAULT_DIFFICULTY_LEVEL)))
            .andExpect(jsonPath("$.[*].isPublished").value(hasItem(DEFAULT_IS_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].publishDate").value(hasItem(DEFAULT_PUBLISH_DATE.toString())));
    }

    @Test
    @Transactional
    void getQuiz() throws Exception {
        // Initialize the database
        quizRepository.saveAndFlush(quiz);

        // Get the quiz
        restQuizMockMvc
            .perform(get(ENTITY_API_URL_ID, quiz.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quiz.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.difficultyLevel").value(DEFAULT_DIFFICULTY_LEVEL))
            .andExpect(jsonPath("$.isPublished").value(DEFAULT_IS_PUBLISHED.booleanValue()))
            .andExpect(jsonPath("$.publishDate").value(DEFAULT_PUBLISH_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingQuiz() throws Exception {
        // Get the quiz
        restQuizMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuiz() throws Exception {
        // Initialize the database
        quizRepository.saveAndFlush(quiz);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quiz
        Quiz updatedQuiz = quizRepository.findById(quiz.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuiz are not directly saved in db
        em.detach(updatedQuiz);
        updatedQuiz
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .difficultyLevel(UPDATED_DIFFICULTY_LEVEL)
            .isPublished(UPDATED_IS_PUBLISHED)
            .publishDate(UPDATED_PUBLISH_DATE);
        QuizDTO quizDTO = quizMapper.toDto(updatedQuiz);

        restQuizMockMvc
            .perform(put(ENTITY_API_URL_ID, quizDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quizDTO)))
            .andExpect(status().isOk());

        // Validate the Quiz in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuizToMatchAllProperties(updatedQuiz);
    }

    @Test
    @Transactional
    void putNonExistingQuiz() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quiz.setId(longCount.incrementAndGet());

        // Create the Quiz
        QuizDTO quizDTO = quizMapper.toDto(quiz);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizMockMvc
            .perform(put(ENTITY_API_URL_ID, quizDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quizDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Quiz in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuiz() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quiz.setId(longCount.incrementAndGet());

        // Create the Quiz
        QuizDTO quizDTO = quizMapper.toDto(quiz);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quizDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quiz in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuiz() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quiz.setId(longCount.incrementAndGet());

        // Create the Quiz
        QuizDTO quizDTO = quizMapper.toDto(quiz);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quizDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quiz in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuizWithPatch() throws Exception {
        // Initialize the database
        quizRepository.saveAndFlush(quiz);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quiz using partial update
        Quiz partialUpdatedQuiz = new Quiz();
        partialUpdatedQuiz.setId(quiz.getId());

        partialUpdatedQuiz.description(UPDATED_DESCRIPTION).publishDate(UPDATED_PUBLISH_DATE);

        restQuizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuiz.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuiz))
            )
            .andExpect(status().isOk());

        // Validate the Quiz in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuizUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedQuiz, quiz), getPersistedQuiz(quiz));
    }

    @Test
    @Transactional
    void fullUpdateQuizWithPatch() throws Exception {
        // Initialize the database
        quizRepository.saveAndFlush(quiz);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quiz using partial update
        Quiz partialUpdatedQuiz = new Quiz();
        partialUpdatedQuiz.setId(quiz.getId());

        partialUpdatedQuiz
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .difficultyLevel(UPDATED_DIFFICULTY_LEVEL)
            .isPublished(UPDATED_IS_PUBLISHED)
            .publishDate(UPDATED_PUBLISH_DATE);

        restQuizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuiz.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuiz))
            )
            .andExpect(status().isOk());

        // Validate the Quiz in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuizUpdatableFieldsEquals(partialUpdatedQuiz, getPersistedQuiz(partialUpdatedQuiz));
    }

    @Test
    @Transactional
    void patchNonExistingQuiz() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quiz.setId(longCount.incrementAndGet());

        // Create the Quiz
        QuizDTO quizDTO = quizMapper.toDto(quiz);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quizDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quizDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quiz in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuiz() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quiz.setId(longCount.incrementAndGet());

        // Create the Quiz
        QuizDTO quizDTO = quizMapper.toDto(quiz);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quizDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quiz in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuiz() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quiz.setId(longCount.incrementAndGet());

        // Create the Quiz
        QuizDTO quizDTO = quizMapper.toDto(quiz);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quizDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quiz in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuiz() throws Exception {
        // Initialize the database
        quizRepository.saveAndFlush(quiz);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the quiz
        restQuizMockMvc
            .perform(delete(ENTITY_API_URL_ID, quiz.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return quizRepository.count();
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

    protected Quiz getPersistedQuiz(Quiz quiz) {
        return quizRepository.findById(quiz.getId()).orElseThrow();
    }

    protected void assertPersistedQuizToMatchAllProperties(Quiz expectedQuiz) {
        assertQuizAllPropertiesEquals(expectedQuiz, getPersistedQuiz(expectedQuiz));
    }

    protected void assertPersistedQuizToMatchUpdatableProperties(Quiz expectedQuiz) {
        assertQuizAllUpdatablePropertiesEquals(expectedQuiz, getPersistedQuiz(expectedQuiz));
    }
}
