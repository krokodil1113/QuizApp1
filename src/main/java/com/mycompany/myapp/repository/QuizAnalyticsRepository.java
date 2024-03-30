package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.QuizAnalytics;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QuizAnalytics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuizAnalyticsRepository extends JpaRepository<QuizAnalytics, Long> {}
