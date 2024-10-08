package com.example.trung.entityusers.repository;

import com.example.trung.entityusers.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepo extends JpaRepository<Permission, String> {
}
