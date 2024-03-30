package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Quiz;
import com.mycompany.myapp.domain.QuizAttempt;
import com.mycompany.myapp.domain.QuizUser;
import com.mycompany.myapp.service.dto.QuizAttemptDTO;
import com.mycompany.myapp.service.dto.QuizDTO;
import com.mycompany.myapp.service.dto.QuizUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuizAttempt} and its DTO {@link QuizAttemptDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuizAttemptMapper extends EntityMapper<QuizAttemptDTO, QuizAttempt> {
    @Mapping(target = "quiz", source = "quiz", qualifiedByName = "quizId")
    @Mapping(target = "user", source = "user", qualifiedByName = "quizUserId")
    QuizAttemptDTO toDto(QuizAttempt s);

    @Named("quizId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuizDTO toDtoQuizId(Quiz quiz);

    @Named("quizUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuizUserDTO toDtoQuizUserId(QuizUser quizUser);
}
