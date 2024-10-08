package com.example.trung.entityusers.service;

import com.example.trung.entityusers.dto.request.RoleRequest;
import com.example.trung.entityusers.dto.response.RoleResponse;
import com.example.trung.entityusers.exception.CustomException;
import com.example.trung.entityusers.exception.ErrorStatus;
import com.example.trung.entityusers.mapper.RoleMapper;
import com.example.trung.entityusers.repository.PermissionRepo;
import com.example.trung.entityusers.repository.RoleRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class RoleService {
    private PermissionRepo permissionRepo;
    private RoleRepo roleRepo;
    private RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request){
        var role = roleMapper.toRole(request);
        var permissions = permissionRepo.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepo.save(role);
        return roleMapper.toroleResponse(role);
    }

    public List<RoleResponse> getAll(){
        var roles = roleRepo.findAll();
        return roles.stream().map(roleMapper::toroleResponse).toList();
    }

    public void delete(String role) {
        if (roleRepo.existsById(role)){
            roleRepo.deleteById(role);
        } else {
            throw new CustomException(ErrorStatus.NOT_FOUND);
        }
    }
}
