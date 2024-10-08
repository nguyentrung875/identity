package com.example.trung.entityusers.entity;

import com.example.trung.entityusers.validators.DobContraints;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private LocalDate dob;

    @ManyToMany
    private Set<Role> roles;
}
