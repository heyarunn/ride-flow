package com.rideflow.auth_service.repository;

import com.rideflow.auth_service.domain.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthRepository extends JpaRepository<Credential, UUID> {

    Boolean existsByEmail(String email);

    Credential findByEmail(String email);

}
