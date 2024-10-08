package com.example.trung.entityusers.dto.response;

import com.example.trung.entityusers.entity.Permission;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class RoleResponse {
    private String name;
    private String description;
    private Set<PermissionResponse> permissions;
}
