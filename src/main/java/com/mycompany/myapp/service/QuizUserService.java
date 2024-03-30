package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.QuizUser;
import com.mycompany.myapp.repository.QuizUserRepository;
import com.mycompany.myapp.service.dto.QuizUserDTO;
import com.mycompany.myapp.service.mapper.QuizUserMapper;
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
 * Service Implementation for managing {@link com.mycompany.myapp.domain.QuizUser}.
 */
@Service
@Transactional
public class QuizUserService {

    private final Logger log = LoggerFactory.getLogger(QuizUserService.class);

    private final QuizUserRepository quizUserRepository;

    private final QuizUserMapper quizUserMapper;

    public QuizUserService(QuizUserRepository quizUserRepository, QuizUserMapper quizUserMapper) {
        this.quizUserRepository = quizUserRepository;
        this.quizUserMapper = quizUserMapper;
    }

    /**
     * Save a quizUser.
     *
     * @param quizUserDTO the entity to save.
     * @return the persisted entity.
     */
    public QuizUserDTO save(QuizUserDTO quizUserDTO) {
        log.debug("Request to save QuizUser : {}", quizUserDTO);
        QuizUser quizUser = quizUserMapper.toEntity(quizUserDTO);
        quizUser = quizUserRepository.save(quizUser);
        return quizUserMapper.toDto(quizUser);
    }

    /**
     * Update a quizUser.
     *
     * @param quizUserDTO the entity to save.
     * @return the persisted entity.
     */
    public QuizUserDTO update(QuizUserDTO quizUserDTO) {
        log.debug("Request to update QuizUser : {}", quizUserDTO);
        QuizUser quizUser = quizUserMapper.toEntity(quizUserDTO);
        quizUser = quizUserRepository.save(quizUser);
        return quizUserMapper.toDto(quizUser);
    }

    /**
     * Partially update a quizUser.
     *
     * @param quizUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuizUserDTO> partialUpdate(QuizUserDTO quizUserDTO) {
        log.debug("Request to partially update QuizUser : {}", quizUserDTO);

        return quizUserRepository
            .findById(quizUserDTO.getId())
            .map(existingQuizUser -> {
                quizUserMapper.partialUpdate(existingQuizUser, quizUserDTO);

                return existingQuizUser;
            })
            .map(quizUserRepository::save)
            .map(quizUserMapper::toDto);
    }

    /**
     * Get all the quizUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QuizUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuizUsers");
        return quizUserRepository.findAll(pageable).map(quizUserMapper::toDto);
    }

    /**
     *  Get all the quizUsers where UserStatistics is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<QuizUserDTO> findAllWhereUserStatisticsIsNull() {
        log.debug("Request to get all quizUsers where UserStatistics is null");
        return StreamSupport.stream(quizUserRepository.findAll().spliterator(), false)
            .filter(quizUser -> quizUser.getUserStatistics() == null)
            .map(quizUserMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one quizUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuizUserDTO> findOne(Long id) {
        log.debug("Request to get QuizUser : {}", id);
        return quizUserRepository.findById(id).map(quizUserMapper::toDto);
    }

    /**
     * Delete the quizUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete QuizUser : {}", id);
        quizUserRepository.deleteById(id);
    }
}
