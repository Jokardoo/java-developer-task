package com.example.test.mapper;

import com.example.test.domain.user.User;
import com.example.test.domain.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserToEntityMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "secondName", source = "secondName"),
            @Mapping(target = "patronymic", source = "patronymic"),
            @Mapping(target = "password", source = "password"),
            @Mapping(target = "birthday", source = "birthday"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "phoneNumber", source = "phoneNumber"),
            @Mapping(target = "role", source = "role")
    })
    User toModel(UserEntity entity);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "secondName", source = "secondName"),
            @Mapping(target = "patronymic", source = "patronymic"),
            @Mapping(target = "password", source = "password"),
            @Mapping(target = "birthday", source = "birthday"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "phoneNumber", source = "phoneNumber"),
            @Mapping(target = "role", source = "role")
    })
    UserEntity toEntity(User user);
}
