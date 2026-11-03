package com.rideflow.auth_service.domain.dto;

public record TokenResponse (
        String accessToken,
        String tokenType,
        long expiryInSeconds
) {

    public static TokenResponse bearer(String accessToken, long expiryInSeconds) {
        return new TokenResponse(accessToken, "Bearer", expiryInSeconds);
    }
}
