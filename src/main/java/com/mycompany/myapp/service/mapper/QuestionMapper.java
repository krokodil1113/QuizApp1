package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Question;
import com.mycompany.myapp.domain.Quiz;
import com.mycompany.myapp.service.dto.QuestionDTO;
import com.mycompany.myapp.service.dto.QuizDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Question} and its DTO {@link QuestionDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {
    @Mapping(target = "quiz", source = "quiz", qualifiedByName = "quizId")
    QuestionDTO toDto(Question s);

    @Named("quizId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuizDTO toDtoQuizId(Quiz quiz);
}
