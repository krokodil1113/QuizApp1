package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.QuizRating;
import com.mycompany.myapp.repository.QuizRatingRepository;
import com.mycompany.myapp.service.dto.QuizRatingDTO;
import com.mycompany.myapp.service.mapper.QuizRatingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.QuizRating}.
 */
@Service
@Transactional
public class QuizRatingService {

    private final Logger log = LoggerFactory.getLogger(QuizRatingService.class);

    private final QuizRatingRepository quizRatingRepository;

    private final QuizRatingMapper quizRatingMapper;

    public QuizRatingService(QuizRatingRepository quizRatingRepository, QuizRatingMapper quizRatingMapper) {
        this.quizRatingRepository = quizRatingRepository;
        this.quizRatingMapper = quizRatingMapper;
    }

    /**
     * Save a quizRating.
     *
     * @param quizRatingDTO the entity to save.
     * @return the persisted entity.
     */
    public QuizRatingDTO save(QuizRatingDTO quizRatingDTO) {
        log.debug("Request to save QuizRating : {}", quizRatingDTO);
        QuizRating quizRating = quizRatingMapper.toEntity(quizRatingDTO);
        quizRating = quizRatingRepository.save(quizRating);
        return quizRatingMapper.toDto(quizRating);
    }

    /**
     * Update a quizRating.
     *
     * @param quizRatingDTO the entity to save.
     * @return the persisted entity.
     */
    public QuizRatingDTO update(QuizRatingDTO quizRatingDTO) {
        log.debug("Request to update QuizRating : {}", quizRatingDTO);
        QuizRating quizRating = quizRatingMapper.toEntity(quizRatingDTO);
        quizRating = quizRatingRepository.save(quizRating);
        return quizRatingMapper.toDto(quizRating);
    }

    /**
     * Partially update a quizRating.
     *
     * @param quizRatingDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuizRatingDTO> partialUpdate(QuizRatingDTO quizRatingDTO) {
        log.debug("Request to partially update QuizRating : {}", quizRatingDTO);

        return quizRatingRepository
            .findById(quizRatingDTO.getId())
            .map(existingQuizRating -> {
                quizRatingMapper.partialUpdate(existingQuizRating, quizRatingDTO);

                return existingQuizRating;
            })
            .map(quizRatingRepository::save)
            .map(quizRatingMapper::toDto);
    }

    /**
     * Get all the quizRatings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QuizRatingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuizRatings");
        return quizRatingRepository.findAll(pageable).map(quizRatingMapper::toDto);
    }

    /**
     * Get one quizRating by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuizRatingDTO> findOne(Long id) {
        log.debug("Request to get QuizRating : {}", id);
        return quizRatingRepository.findById(id).map(quizRatingMapper::toDto);
    }

    /**
     * Delete the quizRating by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete QuizRating : {}", id);
        quizRatingRepository.deleteById(id);
    }
}
