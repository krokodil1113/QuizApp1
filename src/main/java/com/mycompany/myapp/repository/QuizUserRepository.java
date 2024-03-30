package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.QuizUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QuizUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuizUserRepository extends JpaRepository<QuizUser, Long> {}
