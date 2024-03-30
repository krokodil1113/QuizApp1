package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.QuizAnalyticsRepository;
import com.mycompany.myapp.service.QuizAnalyticsService;
import com.mycompany.myapp.service.dto.QuizAnalyticsDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.QuizAnalytics}.
 */
@RestController
@RequestMapping("/api/quiz-analytics")
public class QuizAnalyticsResource {

    private final Logger log = LoggerFactory.getLogger(QuizAnalyticsResource.class);

    private static final String ENTITY_NAME = "quizAnalytics";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuizAnalyticsService quizAnalyticsService;

    private final QuizAnalyticsRepository quizAnalyticsRepository;

    public QuizAnalyticsResource(QuizAnalyticsService quizAnalyticsService, QuizAnalyticsRepository quizAnalyticsRepository) {
        this.quizAnalyticsService = quizAnalyticsService;
        this.quizAnalyticsRepository = quizAnalyticsRepository;
    }

    /**
     * {@code POST  /quiz-analytics} : Create a new quizAnalytics.
     *
     * @param quizAnalyticsDTO the quizAnalyticsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quizAnalyticsDTO, or with status {@code 400 (Bad Request)} if the quizAnalytics has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuizAnalyticsDTO> createQuizAnalytics(@RequestBody QuizAnalyticsDTO quizAnalyticsDTO) throws URISyntaxException {
        log.debug("REST request to save QuizAnalytics : {}", quizAnalyticsDTO);
        if (quizAnalyticsDTO.getId() != null) {
            throw new BadRequestAlertException("A new quizAnalytics cannot already have an ID", ENTITY_NAME, "idexists");
        }
        quizAnalyticsDTO = quizAnalyticsService.save(quizAnalyticsDTO);
        return ResponseEntity.created(new URI("/api/quiz-analytics/" + quizAnalyticsDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, quizAnalyticsDTO.getId().toString()))
            .body(quizAnalyticsDTO);
    }

    /**
     * {@code PUT  /quiz-analytics/:id} : Updates an existing quizAnalytics.
     *
     * @param id the id of the quizAnalyticsDTO to save.
     * @param quizAnalyticsDTO the quizAnalyticsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizAnalyticsDTO,
     * or with status {@code 400 (Bad Request)} if the quizAnalyticsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quizAnalyticsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuizAnalyticsDTO> updateQuizAnalytics(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuizAnalyticsDTO quizAnalyticsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update QuizAnalytics : {}, {}", id, quizAnalyticsDTO);
        if (quizAnalyticsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizAnalyticsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizAnalyticsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        quizAnalyticsDTO = quizAnalyticsService.update(quizAnalyticsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quizAnalyticsDTO.getId().toString()))
            .body(quizAnalyticsDTO);
    }

    /**
     * {@code PATCH  /quiz-analytics/:id} : Partial updates given fields of an existing quizAnalytics, field will ignore if it is null
     *
     * @param id the id of the quizAnalyticsDTO to save.
     * @param quizAnalyticsDTO the quizAnalyticsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizAnalyticsDTO,
     * or with status {@code 400 (Bad Request)} if the quizAnalyticsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the quizAnalyticsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the quizAnalyticsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuizAnalyticsDTO> partialUpdateQuizAnalytics(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuizAnalyticsDTO quizAnalyticsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update QuizAnalytics partially : {}, {}", id, quizAnalyticsDTO);
        if (quizAnalyticsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizAnalyticsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizAnalyticsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuizAnalyticsDTO> result = quizAnalyticsService.partialUpdate(quizAnalyticsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quizAnalyticsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /quiz-analytics} : get all the quizAnalytics.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quizAnalytics in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QuizAnalyticsDTO>> getAllQuizAnalytics(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("quiz-is-null".equals(filter)) {
            log.debug("REST request to get all QuizAnalyticss where quiz is null");
            return new ResponseEntity<>(quizAnalyticsService.findAllWhereQuizIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of QuizAnalytics");
        Page<QuizAnalyticsDTO> page = quizAnalyticsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quiz-analytics/:id} : get the "id" quizAnalytics.
     *
     * @param id the id of the quizAnalyticsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quizAnalyticsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuizAnalyticsDTO> getQuizAnalytics(@PathVariable("id") Long id) {
        log.debug("REST request to get QuizAnalytics : {}", id);
        Optional<QuizAnalyticsDTO> quizAnalyticsDTO = quizAnalyticsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quizAnalyticsDTO);
    }

    /**
     * {@code DELETE  /quiz-analytics/:id} : delete the "id" quizAnalytics.
     *
     * @param id the id of the quizAnalyticsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuizAnalytics(@PathVariable("id") Long id) {
        log.debug("REST request to delete QuizAnalytics : {}", id);
        quizAnalyticsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
