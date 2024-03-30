package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.UserStatistics;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserStatistics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Long> {}
