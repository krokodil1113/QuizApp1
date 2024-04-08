package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Quiz;
import com.mycompany.myapp.domain.QuizAttempt;
import com.mycompany.myapp.domain.QuizUser;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.UserAnswer;
import com.mycompany.myapp.repository.QuizAttemptRepository;
import com.mycompany.myapp.repository.QuizRepository;
import com.mycompany.myapp.repository.QuizUserRepository;
import com.mycompany.myapp.repository.UserAnswerRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.dto.QuizAttemptDTO;
import com.mycompany.myapp.service.dto.QuizUserDTO;
import com.mycompany.myapp.service.mapper.QuizAttemptMapper;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
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

    private final UserAnswerRepository userAnswerRepository;

    public QuizAttemptService(
        QuizAttemptRepository quizAttemptRepository,
        QuizRepository quizRepository,
        QuizUserRepository quizUserRepository,
        QuizAttemptMapper quizAttemptMapper,
        UserAnswerRepository userAnswerRepository
    ) {
        this.quizAttemptRepository = quizAttemptRepository;
        this.quizAttemptMapper = quizAttemptMapper;
        this.quizRepository = quizRepository;
        this.quizUserRepository = quizUserRepository;
        this.userAnswerRepository = userAnswerRepository;
    }

    public QuizAttemptDTO getQuizAttemptDetails(Long attemptId) {
        QuizAttempt quizAttempt = quizAttemptRepository
            .findById(attemptId)
            .orElseThrow(() -> new EntityNotFoundException("QuizAttempt not found with id " + attemptId));

        // Fetch user answers for this attempt
        List<UserAnswer> userAnswers = userAnswerRepository.findByAttemptId(attemptId);

        int score = 0;
        for (UserAnswer userAnswer : userAnswers) {
            // Assume each correct answer adds 1 point. Adapt this logic based on your scoring rules.
            if (userAnswer.getSelectedAnswer().getIsCorrect()) { // isCorrect() needs to be implemented based on your application logic
                score++;
            }
            System.out.println("This is user answer " + userAnswer.getSelectedAnswer());
        }
        System.out.println("This is SCORE " + score);
        // Here, you would set the score in the QuizAttempt and save it if needed
        quizAttempt.setScore(score); // Assuming there's a setScore method
        quizAttemptRepository.save(quizAttempt);

        QuizAttemptDTO quizAttemptDTO = quizAttemptMapper.toDto(quizAttempt);
        return quizAttemptDTO;
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
