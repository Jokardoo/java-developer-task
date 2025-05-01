package com.example.test.mapper;

import com.example.test.domain.user.User;
import com.example.test.domain.user.UserDto;
import com.example.test.domain.user.UserShortInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserToDtoMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "secondName", source = "secondName"),
            @Mapping(target = "patronymic", source = "patronymic"),
            @Mapping(target = "password", source = "password"),
            @Mapping(target = "birthday", source = "birthday"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "phoneNumber", source = "phoneNumber"),
            @Mapping(target = "role", ignore = true)
    })
    User toModel(UserDto dto);

    @Mappings({
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "secondName", source = "secondName"),
            @Mapping(target = "patronymic", source = "patronymic"),
            @Mapping(target = "password", source = "password"),
            @Mapping(target = "birthday", source = "birthday"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "phoneNumber", source = "phoneNumber")
    })
    UserDto toDto(User user);

    @Mappings({
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "secondName", source = "secondName"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "phoneNumber", source = "phoneNumber")
    })
    UserShortInfo userToShortInfo(User user);
}
