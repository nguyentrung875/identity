package com.example.trung.entityusers.dto.request;

import com.example.trung.entityusers.validators.DobContraints;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserUpdateRequest {
    private String username;

    @Size(min = 8, message = "PASSWORD_INVALID")
    private String password;
    private String firstName;
    private String lastName;

    @DobContraints(min = 18, message = "INVALID_DOB")
    private LocalDate dob;
    List<String> roles;
}
