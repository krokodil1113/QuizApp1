package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Answer;
import com.mycompany.myapp.domain.Question;
import com.mycompany.myapp.domain.QuizAttempt;
import com.mycompany.myapp.domain.UserAnswer;
import com.mycompany.myapp.service.dto.AnswerDTO;
import com.mycompany.myapp.service.dto.QuestionDTO;
import com.mycompany.myapp.service.dto.QuizAttemptDTO;
import com.mycompany.myapp.service.dto.UserAnswerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAnswer} and its DTO {@link UserAnswerDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserAnswerMapper extends EntityMapper<UserAnswerDTO, UserAnswer> {
    @Mapping(target = "attempt", source = "attempt", qualifiedByName = "quizAttemptId")
    @Mapping(target = "question", source = "question", qualifiedByName = "questionId")
    @Mapping(target = "selectedAnswer", source = "selectedAnswer", qualifiedByName = "answerId")
    UserAnswerDTO toDto(UserAnswer s);

    @Named("quizAttemptId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuizAttemptDTO toDtoQuizAttemptId(QuizAttempt quizAttempt);

    @Named("questionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuestionDTO toDtoQuestionId(Question question);

    @Named("answerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AnswerDTO toDtoAnswerId(Answer answer);
}
