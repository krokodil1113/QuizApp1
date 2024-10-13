package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Answer;
import com.mycompany.myapp.domain.Question;
import com.mycompany.myapp.service.dto.AnswerDTO;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Answer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestionId(Long questionId);
}
