package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Quiz;
import com.mycompany.myapp.domain.QuizRating;
import com.mycompany.myapp.domain.QuizUser;
import com.mycompany.myapp.service.dto.QuizDTO;
import com.mycompany.myapp.service.dto.QuizRatingDTO;
import com.mycompany.myapp.service.dto.QuizUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuizRating} and its DTO {@link QuizRatingDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuizRatingMapper extends EntityMapper<QuizRatingDTO, QuizRating> {
    @Mapping(target = "quiz", source = "quiz", qualifiedByName = "quizId")
    @Mapping(target = "user", source = "user", qualifiedByName = "quizUserId")
    QuizRatingDTO toDto(QuizRating s);

    @Named("quizId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuizDTO toDtoQuizId(Quiz quiz);

    @Named("quizUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuizUserDTO toDtoQuizUserId(QuizUser quizUser);
}
