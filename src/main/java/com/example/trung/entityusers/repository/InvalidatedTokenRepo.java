package com.example.trung.entityusers.repository;

import com.example.trung.entityusers.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepo extends JpaRepository<InvalidatedToken, String> {
}
