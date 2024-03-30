package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.UserStatisticsRepository;
import com.mycompany.myapp.service.UserStatisticsService;
import com.mycompany.myapp.service.dto.UserStatisticsDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.UserStatistics}.
 */
@RestController
@RequestMapping("/api/user-statistics")
public class UserStatisticsResource {

    private final Logger log = LoggerFactory.getLogger(UserStatisticsResource.class);

    private static final String ENTITY_NAME = "userStatistics";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserStatisticsService userStatisticsService;

    private final UserStatisticsRepository userStatisticsRepository;

    public UserStatisticsResource(UserStatisticsService userStatisticsService, UserStatisticsRepository userStatisticsRepository) {
        this.userStatisticsService = userStatisticsService;
        this.userStatisticsRepository = userStatisticsRepository;
    }

    /**
     * {@code POST  /user-statistics} : Create a new userStatistics.
     *
     * @param userStatisticsDTO the userStatisticsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userStatisticsDTO, or with status {@code 400 (Bad Request)} if the userStatistics has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserStatisticsDTO> createUserStatistics(@RequestBody UserStatisticsDTO userStatisticsDTO)
        throws URISyntaxException {
        log.debug("REST request to save UserStatistics : {}", userStatisticsDTO);
        if (userStatisticsDTO.getId() != null) {
            throw new BadRequestAlertException("A new userStatistics cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userStatisticsDTO = userStatisticsService.save(userStatisticsDTO);
        return ResponseEntity.created(new URI("/api/user-statistics/" + userStatisticsDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userStatisticsDTO.getId().toString()))
            .body(userStatisticsDTO);
    }

    /**
     * {@code PUT  /user-statistics/:id} : Updates an existing userStatistics.
     *
     * @param id the id of the userStatisticsDTO to save.
     * @param userStatisticsDTO the userStatisticsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userStatisticsDTO,
     * or with status {@code 400 (Bad Request)} if the userStatisticsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userStatisticsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserStatisticsDTO> updateUserStatistics(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserStatisticsDTO userStatisticsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserStatistics : {}, {}", id, userStatisticsDTO);
        if (userStatisticsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userStatisticsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userStatisticsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userStatisticsDTO = userStatisticsService.update(userStatisticsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userStatisticsDTO.getId().toString()))
            .body(userStatisticsDTO);
    }

    /**
     * {@code PATCH  /user-statistics/:id} : Partial updates given fields of an existing userStatistics, field will ignore if it is null
     *
     * @param id the id of the userStatisticsDTO to save.
     * @param userStatisticsDTO the userStatisticsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userStatisticsDTO,
     * or with status {@code 400 (Bad Request)} if the userStatisticsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userStatisticsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userStatisticsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserStatisticsDTO> partialUpdateUserStatistics(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserStatisticsDTO userStatisticsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserStatistics partially : {}, {}", id, userStatisticsDTO);
        if (userStatisticsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userStatisticsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userStatisticsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserStatisticsDTO> result = userStatisticsService.partialUpdate(userStatisticsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userStatisticsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-statistics} : get all the userStatistics.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userStatistics in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserStatisticsDTO>> getAllUserStatistics(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of UserStatistics");
        Page<UserStatisticsDTO> page = userStatisticsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-statistics/:id} : get the "id" userStatistics.
     *
     * @param id the id of the userStatisticsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userStatisticsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserStatisticsDTO> getUserStatistics(@PathVariable("id") Long id) {
        log.debug("REST request to get UserStatistics : {}", id);
        Optional<UserStatisticsDTO> userStatisticsDTO = userStatisticsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userStatisticsDTO);
    }

    /**
     * {@code DELETE  /user-statistics/:id} : delete the "id" userStatistics.
     *
     * @param id the id of the userStatisticsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserStatistics(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserStatistics : {}", id);
        userStatisticsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
