package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.QuizRatingRepository;
import com.mycompany.myapp.service.QuizRatingService;
import com.mycompany.myapp.service.dto.QuizRatingDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.QuizRating}.
 */
@RestController
@RequestMapping("/api/quiz-ratings")
public class QuizRatingResource {

    private final Logger log = LoggerFactory.getLogger(QuizRatingResource.class);

    private static final String ENTITY_NAME = "quizRating";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuizRatingService quizRatingService;

    private final QuizRatingRepository quizRatingRepository;

    public QuizRatingResource(QuizRatingService quizRatingService, QuizRatingRepository quizRatingRepository) {
        this.quizRatingService = quizRatingService;
        this.quizRatingRepository = quizRatingRepository;
    }

    /**
     * {@code POST  /quiz-ratings} : Create a new quizRating.
     *
     * @param quizRatingDTO the quizRatingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quizRatingDTO, or with status {@code 400 (Bad Request)} if the quizRating has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuizRatingDTO> createQuizRating(@RequestBody QuizRatingDTO quizRatingDTO) throws URISyntaxException {
        log.debug("REST request to save QuizRating : {}", quizRatingDTO);
        if (quizRatingDTO.getId() != null) {
            throw new BadRequestAlertException("A new quizRating cannot already have an ID", ENTITY_NAME, "idexists");
        }
        quizRatingDTO = quizRatingService.save(quizRatingDTO);
        return ResponseEntity.created(new URI("/api/quiz-ratings/" + quizRatingDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, quizRatingDTO.getId().toString()))
            .body(quizRatingDTO);
    }

    /**
     * {@code PUT  /quiz-ratings/:id} : Updates an existing quizRating.
     *
     * @param id the id of the quizRatingDTO to save.
     * @param quizRatingDTO the quizRatingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizRatingDTO,
     * or with status {@code 400 (Bad Request)} if the quizRatingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quizRatingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuizRatingDTO> updateQuizRating(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuizRatingDTO quizRatingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update QuizRating : {}, {}", id, quizRatingDTO);
        if (quizRatingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizRatingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizRatingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        quizRatingDTO = quizRatingService.update(quizRatingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quizRatingDTO.getId().toString()))
            .body(quizRatingDTO);
    }

    /**
     * {@code PATCH  /quiz-ratings/:id} : Partial updates given fields of an existing quizRating, field will ignore if it is null
     *
     * @param id the id of the quizRatingDTO to save.
     * @param quizRatingDTO the quizRatingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizRatingDTO,
     * or with status {@code 400 (Bad Request)} if the quizRatingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the quizRatingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the quizRatingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuizRatingDTO> partialUpdateQuizRating(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuizRatingDTO quizRatingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update QuizRating partially : {}, {}", id, quizRatingDTO);
        if (quizRatingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizRatingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizRatingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuizRatingDTO> result = quizRatingService.partialUpdate(quizRatingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quizRatingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /quiz-ratings} : get all the quizRatings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quizRatings in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QuizRatingDTO>> getAllQuizRatings(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of QuizRatings");
        Page<QuizRatingDTO> page = quizRatingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quiz-ratings/:id} : get the "id" quizRating.
     *
     * @param id the id of the quizRatingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quizRatingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuizRatingDTO> getQuizRating(@PathVariable("id") Long id) {
        log.debug("REST request to get QuizRating : {}", id);
        Optional<QuizRatingDTO> quizRatingDTO = quizRatingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quizRatingDTO);
    }

    /**
     * {@code DELETE  /quiz-ratings/:id} : delete the "id" quizRating.
     *
     * @param id the id of the quizRatingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuizRating(@PathVariable("id") Long id) {
        log.debug("REST request to delete QuizRating : {}", id);
        quizRatingService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
