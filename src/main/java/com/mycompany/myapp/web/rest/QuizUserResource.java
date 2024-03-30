package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.QuizUserRepository;
import com.mycompany.myapp.service.QuizUserService;
import com.mycompany.myapp.service.dto.QuizUserDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.QuizUser}.
 */
@RestController
@RequestMapping("/api/quiz-users")
public class QuizUserResource {

    private final Logger log = LoggerFactory.getLogger(QuizUserResource.class);

    private static final String ENTITY_NAME = "quizUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuizUserService quizUserService;

    private final QuizUserRepository quizUserRepository;

    public QuizUserResource(QuizUserService quizUserService, QuizUserRepository quizUserRepository) {
        this.quizUserService = quizUserService;
        this.quizUserRepository = quizUserRepository;
    }

    /**
     * {@code POST  /quiz-users} : Create a new quizUser.
     *
     * @param quizUserDTO the quizUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quizUserDTO, or with status {@code 400 (Bad Request)} if the quizUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuizUserDTO> createQuizUser(@RequestBody QuizUserDTO quizUserDTO) throws URISyntaxException {
        log.debug("REST request to save QuizUser : {}", quizUserDTO);
        if (quizUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new quizUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        quizUserDTO = quizUserService.save(quizUserDTO);
        return ResponseEntity.created(new URI("/api/quiz-users/" + quizUserDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, quizUserDTO.getId().toString()))
            .body(quizUserDTO);
    }

    /**
     * {@code PUT  /quiz-users/:id} : Updates an existing quizUser.
     *
     * @param id the id of the quizUserDTO to save.
     * @param quizUserDTO the quizUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizUserDTO,
     * or with status {@code 400 (Bad Request)} if the quizUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quizUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuizUserDTO> updateQuizUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuizUserDTO quizUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update QuizUser : {}, {}", id, quizUserDTO);
        if (quizUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        quizUserDTO = quizUserService.update(quizUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quizUserDTO.getId().toString()))
            .body(quizUserDTO);
    }

    /**
     * {@code PATCH  /quiz-users/:id} : Partial updates given fields of an existing quizUser, field will ignore if it is null
     *
     * @param id the id of the quizUserDTO to save.
     * @param quizUserDTO the quizUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizUserDTO,
     * or with status {@code 400 (Bad Request)} if the quizUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the quizUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the quizUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuizUserDTO> partialUpdateQuizUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuizUserDTO quizUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update QuizUser partially : {}, {}", id, quizUserDTO);
        if (quizUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuizUserDTO> result = quizUserService.partialUpdate(quizUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quizUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /quiz-users} : get all the quizUsers.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quizUsers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QuizUserDTO>> getAllQuizUsers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("userstatistics-is-null".equals(filter)) {
            log.debug("REST request to get all QuizUsers where userStatistics is null");
            return new ResponseEntity<>(quizUserService.findAllWhereUserStatisticsIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of QuizUsers");
        Page<QuizUserDTO> page = quizUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quiz-users/:id} : get the "id" quizUser.
     *
     * @param id the id of the quizUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quizUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuizUserDTO> getQuizUser(@PathVariable("id") Long id) {
        log.debug("REST request to get QuizUser : {}", id);
        Optional<QuizUserDTO> quizUserDTO = quizUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quizUserDTO);
    }

    /**
     * {@code DELETE  /quiz-users/:id} : delete the "id" quizUser.
     *
     * @param id the id of the quizUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuizUser(@PathVariable("id") Long id) {
        log.debug("REST request to delete QuizUser : {}", id);
        quizUserService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
