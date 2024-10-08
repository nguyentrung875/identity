package com.example.trung.entityusers.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleRequest {
    private String name;
    private String description;
    private Set<String> permissions;
}
