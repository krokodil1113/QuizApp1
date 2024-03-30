package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.QuizUser;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.dto.QuizUserDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuizUser} and its DTO {@link QuizUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuizUserMapper extends EntityMapper<QuizUserDTO, QuizUser> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    QuizUserDTO toDto(QuizUser s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
