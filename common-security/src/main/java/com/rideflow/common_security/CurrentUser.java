package com.rideflow.common_security;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

// Helper class to read authenticated identity from security context
public class CurrentUser {

    public static Optional<UUID> subjectId() {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication==null || authentication.getPrincipal()==null) {
            return Optional.empty();
        }
        try {
            return Optional.of(UUID.fromString(authentication.getPrincipal().toString()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Boolean hasRole(String role) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication==null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch((auth) -> Objects.equals(auth.getAuthority(), "ROLE_" + role));
    }
}
