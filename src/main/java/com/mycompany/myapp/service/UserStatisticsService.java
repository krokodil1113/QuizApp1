package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.UserStatistics;
import com.mycompany.myapp.repository.UserStatisticsRepository;
import com.mycompany.myapp.service.dto.UserStatisticsDTO;
import com.mycompany.myapp.service.mapper.UserStatisticsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.UserStatistics}.
 */
@Service
@Transactional
public class UserStatisticsService {

    private final Logger log = LoggerFactory.getLogger(UserStatisticsService.class);

    private final UserStatisticsRepository userStatisticsRepository;

    private final UserStatisticsMapper userStatisticsMapper;

    public UserStatisticsService(UserStatisticsRepository userStatisticsRepository, UserStatisticsMapper userStatisticsMapper) {
        this.userStatisticsRepository = userStatisticsRepository;
        this.userStatisticsMapper = userStatisticsMapper;
    }

    /**
     * Save a userStatistics.
     *
     * @param userStatisticsDTO the entity to save.
     * @return the persisted entity.
     */
    public UserStatisticsDTO save(UserStatisticsDTO userStatisticsDTO) {
        log.debug("Request to save UserStatistics : {}", userStatisticsDTO);
        UserStatistics userStatistics = userStatisticsMapper.toEntity(userStatisticsDTO);
        userStatistics = userStatisticsRepository.save(userStatistics);
        return userStatisticsMapper.toDto(userStatistics);
    }

    /**
     * Update a userStatistics.
     *
     * @param userStatisticsDTO the entity to save.
     * @return the persisted entity.
     */
    public UserStatisticsDTO update(UserStatisticsDTO userStatisticsDTO) {
        log.debug("Request to update UserStatistics : {}", userStatisticsDTO);
        UserStatistics userStatistics = userStatisticsMapper.toEntity(userStatisticsDTO);
        userStatistics = userStatisticsRepository.save(userStatistics);
        return userStatisticsMapper.toDto(userStatistics);
    }

    /**
     * Partially update a userStatistics.
     *
     * @param userStatisticsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserStatisticsDTO> partialUpdate(UserStatisticsDTO userStatisticsDTO) {
        log.debug("Request to partially update UserStatistics : {}", userStatisticsDTO);

        return userStatisticsRepository
            .findById(userStatisticsDTO.getId())
            .map(existingUserStatistics -> {
                userStatisticsMapper.partialUpdate(existingUserStatistics, userStatisticsDTO);

                return existingUserStatistics;
            })
            .map(userStatisticsRepository::save)
            .map(userStatisticsMapper::toDto);
    }

    /**
     * Get all the userStatistics.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserStatisticsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserStatistics");
        return userStatisticsRepository.findAll(pageable).map(userStatisticsMapper::toDto);
    }

    /**
     * Get one userStatistics by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserStatisticsDTO> findOne(Long id) {
        log.debug("Request to get UserStatistics : {}", id);
        return userStatisticsRepository.findById(id).map(userStatisticsMapper::toDto);
    }

    /**
     * Delete the userStatistics by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserStatistics : {}", id);
        userStatisticsRepository.deleteById(id);
    }
}
