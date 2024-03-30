package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.QuizRating;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QuizRating entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuizRatingRepository extends JpaRepository<QuizRating, Long> {}
