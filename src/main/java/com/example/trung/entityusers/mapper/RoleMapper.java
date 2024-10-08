package com.example.trung.entityusers.mapper;

import com.example.trung.entityusers.dto.request.PermissionRequest;
import com.example.trung.entityusers.dto.request.RoleRequest;
import com.example.trung.entityusers.dto.response.PermissionResponse;
import com.example.trung.entityusers.dto.response.RoleResponse;
import com.example.trung.entityusers.entity.Permission;
import com.example.trung.entityusers.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toroleResponse(Role role);
}
