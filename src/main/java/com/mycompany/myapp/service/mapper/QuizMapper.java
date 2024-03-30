package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Category;
import com.mycompany.myapp.domain.Quiz;
import com.mycompany.myapp.domain.QuizAnalytics;
import com.mycompany.myapp.domain.QuizUser;
import com.mycompany.myapp.service.dto.CategoryDTO;
import com.mycompany.myapp.service.dto.QuizAnalyticsDTO;
import com.mycompany.myapp.service.dto.QuizDTO;
import com.mycompany.myapp.service.dto.QuizUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Quiz} and its DTO {@link QuizDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuizMapper extends EntityMapper<QuizDTO, Quiz> {
    @Mapping(target = "quizAnalytics", source = "quizAnalytics", qualifiedByName = "quizAnalyticsId")
    @Mapping(target = "creator", source = "creator", qualifiedByName = "quizUserId")
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryId")
    QuizDTO toDto(Quiz s);

    @Named("quizAnalyticsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuizAnalyticsDTO toDtoQuizAnalyticsId(QuizAnalytics quizAnalytics);

    @Named("quizUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuizUserDTO toDtoQuizUserId(QuizUser quizUser);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);
}
