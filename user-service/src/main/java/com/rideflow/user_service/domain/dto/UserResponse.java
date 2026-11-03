package com.rideflow.user_service.domain.dto;

import com.rideflow.user_service.domain.User;
import com.rideflow.user_service.domain.UserStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse (
        UUID id,
        String username,
        String email,
        String phoneNumber,
        UserStatus status,
        LocalDateTime createdAt
) {

    public static UserResponse fromUser(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }
}
