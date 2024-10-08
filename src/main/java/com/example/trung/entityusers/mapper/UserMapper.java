package com.example.trung.entityusers.mapper;

import com.example.trung.entityusers.dto.request.UserRequest;
import com.example.trung.entityusers.dto.request.UserUpdateRequest;
import com.example.trung.entityusers.dto.response.UserResponse;
import com.example.trung.entityusers.entity.User;
import lombok.Data;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);

    User toUser(UserRequest userRequest);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
