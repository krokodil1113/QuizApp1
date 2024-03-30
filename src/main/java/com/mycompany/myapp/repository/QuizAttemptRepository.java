package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.QuizAttempt;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QuizAttempt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {}
