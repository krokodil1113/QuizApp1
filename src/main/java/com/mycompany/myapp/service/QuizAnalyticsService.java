package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.QuizAnalytics;
import com.mycompany.myapp.repository.QuizAnalyticsRepository;
import com.mycompany.myapp.service.dto.QuizAnalyticsDTO;
import com.mycompany.myapp.service.mapper.QuizAnalyticsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.QuizAnalytics}.
 */
@Service
@Transactional
public class QuizAnalyticsService {

    private final Logger log = LoggerFactory.getLogger(QuizAnalyticsService.class);

    private final QuizAnalyticsRepository quizAnalyticsRepository;

    private final QuizAnalyticsMapper quizAnalyticsMapper;

    public QuizAnalyticsService(QuizAnalyticsRepository quizAnalyticsRepository, QuizAnalyticsMapper quizAnalyticsMapper) {
        this.quizAnalyticsRepository = quizAnalyticsRepository;
        this.quizAnalyticsMapper = quizAnalyticsMapper;
    }

    /**
     * Save a quizAnalytics.
     *
     * @param quizAnalyticsDTO the entity to save.
     * @return the persisted entity.
     */
    public QuizAnalyticsDTO save(QuizAnalyticsDTO quizAnalyticsDTO) {
        log.debug("Request to save QuizAnalytics : {}", quizAnalyticsDTO);
        QuizAnalytics quizAnalytics = quizAnalyticsMapper.toEntity(quizAnalyticsDTO);
        quizAnalytics = quizAnalyticsRepository.save(quizAnalytics);
        return quizAnalyticsMapper.toDto(quizAnalytics);
    }

    /**
     * Update a quizAnalytics.
     *
     * @param quizAnalyticsDTO the entity to save.
     * @return the persisted entity.
     */
    public QuizAnalyticsDTO update(QuizAnalyticsDTO quizAnalyticsDTO) {
        log.debug("Request to update QuizAnalytics : {}", quizAnalyticsDTO);
        QuizAnalytics quizAnalytics = quizAnalyticsMapper.toEntity(quizAnalyticsDTO);
        quizAnalytics = quizAnalyticsRepository.save(quizAnalytics);
        return quizAnalyticsMapper.toDto(quizAnalytics);
    }

    /**
     * Partially update a quizAnalytics.
     *
     * @param quizAnalyticsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuizAnalyticsDTO> partialUpdate(QuizAnalyticsDTO quizAnalyticsDTO) {
        log.debug("Request to partially update QuizAnalytics : {}", quizAnalyticsDTO);

        return quizAnalyticsRepository
            .findById(quizAnalyticsDTO.getId())
            .map(existingQuizAnalytics -> {
                quizAnalyticsMapper.partialUpdate(existingQuizAnalytics, quizAnalyticsDTO);

                return existingQuizAnalytics;
            })
            .map(quizAnalyticsRepository::save)
            .map(quizAnalyticsMapper::toDto);
    }

    /**
     * Get all the quizAnalytics.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QuizAnalyticsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuizAnalytics");
        return quizAnalyticsRepository.findAll(pageable).map(quizAnalyticsMapper::toDto);
    }

    /**
     *  Get all the quizAnalytics where Quiz is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<QuizAnalyticsDTO> findAllWhereQuizIsNull() {
        log.debug("Request to get all quizAnalytics where Quiz is null");
        return StreamSupport.stream(quizAnalyticsRepository.findAll().spliterator(), false)
            .filter(quizAnalytics -> quizAnalytics.getQuiz() == null)
            .map(quizAnalyticsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one quizAnalytics by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuizAnalyticsDTO> findOne(Long id) {
        log.debug("Request to get QuizAnalytics : {}", id);
        return quizAnalyticsRepository.findById(id).map(quizAnalyticsMapper::toDto);
    }

    /**
     * Delete the quizAnalytics by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete QuizAnalytics : {}", id);
        quizAnalyticsRepository.deleteById(id);
    }
}
