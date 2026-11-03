package com.rideflow.auth_service.domain.dto;

import com.rideflow.auth_service.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AuthRequest (
        @NotBlank(message = "email is required")
        @Email(message = "email must be valid")
        String email,

        @NotBlank(message = "password is required")
        @Size(min = 8, max = 72, message = "password must be in 8-72 characters")
        String password,

        @NotNull(message = "role is required")
        Role role
) { }
