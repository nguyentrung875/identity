package com.example.trung.entityusers.service;

import com.example.trung.entityusers.dto.request.PermissionRequest;
import com.example.trung.entityusers.dto.response.PermissionResponse;
import com.example.trung.entityusers.entity.Permission;
import com.example.trung.entityusers.exception.CustomException;
import com.example.trung.entityusers.exception.ErrorStatus;
import com.example.trung.entityusers.mapper.PermissionMapper;
import com.example.trung.entityusers.repository.PermissionRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class PermissionService {
    private PermissionRepo permissionRepo;
    private PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepo.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll(){
        var permissions = permissionRepo.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permission) {
        if (permissionRepo.existsById(permission)){
            permissionRepo.deleteById(permission);
        } else {
            throw new CustomException(ErrorStatus.NOT_FOUND);
        }
    }
}
