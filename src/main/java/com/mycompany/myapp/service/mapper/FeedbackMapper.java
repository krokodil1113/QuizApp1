package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Feedback;
import com.mycompany.myapp.domain.QuizUser;
import com.mycompany.myapp.service.dto.FeedbackDTO;
import com.mycompany.myapp.service.dto.QuizUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Feedback} and its DTO {@link FeedbackDTO}.
 */
@Mapper(componentModel = "spring")
public interface FeedbackMapper extends EntityMapper<FeedbackDTO, Feedback> {
    @Mapping(target = "user", source = "user", qualifiedByName = "quizUserId")
    FeedbackDTO toDto(Feedback s);

    @Named("quizUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuizUserDTO toDtoQuizUserId(QuizUser quizUser);
}
