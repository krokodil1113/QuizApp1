package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.QuizAnalytics;
import com.mycompany.myapp.service.dto.QuizAnalyticsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuizAnalytics} and its DTO {@link QuizAnalyticsDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuizAnalyticsMapper extends EntityMapper<QuizAnalyticsDTO, QuizAnalytics> {}
