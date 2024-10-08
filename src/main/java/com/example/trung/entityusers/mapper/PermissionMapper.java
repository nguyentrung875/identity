package com.example.trung.entityusers.mapper;

import com.example.trung.entityusers.dto.request.PermissionRequest;
import com.example.trung.entityusers.dto.request.UserRequest;
import com.example.trung.entityusers.dto.request.UserUpdateRequest;
import com.example.trung.entityusers.dto.response.PermissionResponse;
import com.example.trung.entityusers.dto.response.UserResponse;
import com.example.trung.entityusers.entity.Permission;
import com.example.trung.entityusers.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
