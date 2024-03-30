package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.QuizUser;
import com.mycompany.myapp.domain.UserStatistics;
import com.mycompany.myapp.service.dto.QuizUserDTO;
import com.mycompany.myapp.service.dto.UserStatisticsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserStatistics} and its DTO {@link UserStatisticsDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserStatisticsMapper extends EntityMapper<UserStatisticsDTO, UserStatistics> {
    @Mapping(target = "quizUser", source = "quizUser", qualifiedByName = "quizUserId")
    UserStatisticsDTO toDto(UserStatistics s);

    @Named("quizUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuizUserDTO toDtoQuizUserId(QuizUser quizUser);
}
