package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Quiz;
import com.mycompany.myapp.domain.QuizAttempt;
import com.mycompany.myapp.domain.QuizUser;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.QuizAttemptRepository;
import com.mycompany.myapp.repository.QuizRepository;
import com.mycompany.myapp.repository.QuizUserRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.dto.QuizAttemptDTO;
import com.mycompany.myapp.service.dto.QuizUserDTO;
import com.mycompany.myapp.service.mapper.QuizAttemptMapper;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.QuizAttempt}.
 */
@Service
@Transactional
public class QuizAttemptService {

    private final Logger log = LoggerFactory.getLogger(QuizAttemptService.class);

    private final QuizAttemptRepository quizAttemptRepository;

    private final QuizRepository quizRepository;

    private final QuizUserRepository quizUserRepository;

    private final QuizAttemptMapper quizAttemptMapper;

    public QuizAttemptService(
        QuizAttemptRepository quizAttemptRepository,
        QuizRepository quizRepository,
        QuizUserRepository quizUserRepository,
        QuizAttemptMapper quizAttemptMapper
    ) {
        this.quizAttemptRepository = quizAttemptRepository;
        this.quizAttemptMapper = quizAttemptMapper;
        this.quizRepository = quizRepository;
        this.quizUserRepository = quizUserRepository;
    }

    /**
     * Save a quizAttempt.
     *
     * @param quizAttemptDTO the entity to save.
     * @return the persisted entity.
     */
    /*  public QuizAttemptDTO save(QuizAttemptDTO quizAttemptDTO) {
        log.debug("Request to save QuizAttempt : {}", quizAttemptDTO);
        QuizAttempt quizAttempt = quizAttemptMapper.toEntity(quizAttemptDTO);
        quizAttempt = quizAttemptRepository.save(quizAttempt);
        return quizAttemptMapper.toDto(quizAttempt);
    } */
    public QuizAttemptDTO save(QuizAttemptDTO quizAttemptDTO) {
        Quiz quiz = quizRepository.findById(quizAttemptDTO.getQuizId()).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
        QuizUser user = quizUserRepository
            .findById(quizAttemptDTO.getUserId())
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        QuizAttempt quizAttempt = quizAttemptMapper.toEntity(quizAttemptDTO);
        quizAttempt.setQuiz(quiz);
        quizAttempt.setUser(user);
        QuizAttempt quizAttempt1 = quizAttemptRepository.save(quizAttempt);
        return quizAttemptMapper.toDto(quizAttempt1);
    }

    /**
     * Update a quizAttempt.
     *
     * @param quizAttemptDTO the entity to save.
     * @return the persisted entity.
     */
    public QuizAttemptDTO update(QuizAttemptDTO quizAttemptDTO) {
        log.debug("Request to update QuizAttempt : {}", quizAttemptDTO);
        QuizAttempt quizAttempt = quizAttemptMapper.toEntity(quizAttemptDTO);
        quizAttempt = quizAttemptRepository.save(quizAttempt);
        return quizAttemptMapper.toDto(quizAttempt);
    }

    /**
     * Partially update a quizAttempt.
     *
     * @param quizAttemptDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuizAttemptDTO> partialUpdate(QuizAttemptDTO quizAttemptDTO) {
        log.debug("Request to partially update QuizAttempt : {}", quizAttemptDTO);

        return quizAttemptRepository
            .findById(quizAttemptDTO.getId())
            .map(existingQuizAttempt -> {
                quizAttemptMapper.partialUpdate(existingQuizAttempt, quizAttemptDTO);

                return existingQuizAttempt;
            })
            .map(quizAttemptRepository::save)
            .map(quizAttemptMapper::toDto);
    }

    /**
     * Get all the quizAttempts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QuizAttemptDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuizAttempts");
        return quizAttemptRepository.findAll(pageable).map(quizAttemptMapper::toDto);
    }

    /**
     * Get one quizAttempt by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuizAttemptDTO> findOne(Long id) {
        log.debug("Request to get QuizAttempt : {}", id);
        return quizAttemptRepository.findById(id).map(quizAttemptMapper::toDto);
    }

    /**
     * Delete the quizAttempt by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete QuizAttempt : {}", id);
        quizAttemptRepository.deleteById(id);
    }
}
