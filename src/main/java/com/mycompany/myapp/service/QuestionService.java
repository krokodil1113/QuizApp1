package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Question;
import com.mycompany.myapp.repository.QuestionRepository;
import com.mycompany.myapp.service.dto.QuestionDTO;
import com.mycompany.myapp.service.mapper.QuestionMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Question}.
 */
@Service
@Transactional
public class QuestionService {

    private final Logger log = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;

    private final QuestionMapper questionMapper;

    public QuestionService(QuestionRepository questionRepository, QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
    }

    public List<QuestionDTO> findByQuizId(Long quizId) {
        // Assuming you have a method in your repository to find questions by quiz ID.
        // Convert entities to DTOs as necessary.
        return questionRepository
            .findByQuizId(quizId)
            .stream()
            .map(questionMapper::toDto) // Assuming you're using MapStruct or similar
            .collect(Collectors.toList());
    }

    /**
     * Save a question.
     *
     * @param questionDTO the entity to save.
     * @return the persisted entity.
     */
    public QuestionDTO save(QuestionDTO questionDTO) {
        log.debug("Request to save Question : {}", questionDTO);
        Question question = questionMapper.toEntity(questionDTO);
        question = questionRepository.save(question);
        return questionMapper.toDto(question);
    }

    /**
     * Update a question.
     *
     * @param questionDTO the entity to save.
     * @return the persisted entity.
     */
    public QuestionDTO update(QuestionDTO questionDTO) {
        log.debug("Request to update Question : {}", questionDTO);
        Question question = questionMapper.toEntity(questionDTO);
        question = questionRepository.save(question);
        return questionMapper.toDto(question);
    }

    /**
     * Partially update a question.
     *
     * @param questionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuestionDTO> partialUpdate(QuestionDTO questionDTO) {
        log.debug("Request to partially update Question : {}", questionDTO);

        return questionRepository
            .findById(questionDTO.getId())
            .map(existingQuestion -> {
                questionMapper.partialUpdate(existingQuestion, questionDTO);

                return existingQuestion;
            })
            .map(questionRepository::save)
            .map(questionMapper::toDto);
    }

    /**
     * Get all the questions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Questions");
        return questionRepository.findAll(pageable).map(questionMapper::toDto);
    }

    /**
     * Get one question by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuestionDTO> findOne(Long id) {
        log.debug("Request to get Question : {}", id);
        return questionRepository.findById(id).map(questionMapper::toDto);
    }

    /**
     * Delete the question by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Question : {}", id);
        questionRepository.deleteById(id);
    }
}
