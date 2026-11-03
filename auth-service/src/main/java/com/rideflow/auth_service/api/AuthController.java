package com.rideflow.auth_service.api;

import com.rideflow.auth_service.domain.Credential;
import com.rideflow.auth_service.domain.dto.AuthRequest;
import com.rideflow.auth_service.domain.dto.AuthResponse;
import com.rideflow.auth_service.domain.dto.LoginRequest;
import com.rideflow.auth_service.domain.dto.TokenResponse;
import com.rideflow.auth_service.security.JwtTokenService;
import com.rideflow.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenService jwtTokenService;

    public AuthController(AuthService authService, JwtTokenService jwtTokenService) {
        this.authService = authService;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest authRequest) {
        Credential nwCredential = authService.register(authRequest);
        return new ResponseEntity<>(
                AuthResponse.fromCredential(nwCredential),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        String bearerToken = authService.login(loginRequest);
        return new ResponseEntity<>(
                TokenResponse.bearer(bearerToken, jwtTokenService.getExpiryInSeconds()),
                HttpStatus.OK);
    }

}
