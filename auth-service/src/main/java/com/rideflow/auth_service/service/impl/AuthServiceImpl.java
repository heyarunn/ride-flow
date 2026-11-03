package com.rideflow.auth_service.service.impl;

import com.rideflow.auth_service.domain.Credential;
import com.rideflow.auth_service.domain.dto.AuthRequest;
import com.rideflow.auth_service.domain.dto.LoginRequest;
import com.rideflow.auth_service.exception.DuplicateCredentialException;
import com.rideflow.auth_service.exception.InvalidCredentialsException;
import com.rideflow.auth_service.repository.AuthRepository;
import com.rideflow.auth_service.security.JwtTokenService;
import com.rideflow.auth_service.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    public AuthServiceImpl(AuthRepository authRepository, PasswordEncoder passwordEncoder, JwtTokenService jwtTokenService) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public Credential register(AuthRequest authRequest) {
        if (authRepository.existsByEmail(authRequest.email()))
            throw new DuplicateCredentialException("credential already found with email: "+authRequest.email());
        String passwordHash = passwordEncoder.encode(authRequest.password());
        Credential nwCredential = new Credential(authRequest.email(), passwordHash, authRequest.role());
        return authRepository.save(nwCredential);
    }

    @Override
    public String login(LoginRequest loginRequest) {
        Credential userCredential = authRepository.findByEmail(loginRequest.email());
        String rawPassword = loginRequest.password();

        if (userCredential == null || !passwordEncoder.matches(rawPassword, userCredential.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        return jwtTokenService.issueToken(userCredential);
    }
}
