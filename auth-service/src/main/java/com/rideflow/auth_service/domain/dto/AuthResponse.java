package com.rideflow.auth_service.domain.dto;

import com.rideflow.auth_service.domain.Credential;
import com.rideflow.auth_service.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AuthResponse (
        UUID subjectId,
        String email,
        Role role
) {

    public static AuthResponse fromCredential(Credential credential) {
        return new AuthResponse(
                credential.getSubjectId(),
                credential.getEmail(),
                credential.getRole()
        );
    }
}
