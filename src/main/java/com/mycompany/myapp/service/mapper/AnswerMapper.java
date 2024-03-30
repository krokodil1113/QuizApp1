package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Answer;
import com.mycompany.myapp.domain.Question;
import com.mycompany.myapp.service.dto.AnswerDTO;
import com.mycompany.myapp.service.dto.QuestionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Answer} and its DTO {@link AnswerDTO}.
 */
@Mapper(componentModel = "spring")
public interface AnswerMapper extends EntityMapper<AnswerDTO, Answer> {
    @Mapping(target = "question", source = "question", qualifiedByName = "questionId")
    AnswerDTO toDto(Answer s);

    @Named("questionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuestionDTO toDtoQuestionId(Question question);
}
