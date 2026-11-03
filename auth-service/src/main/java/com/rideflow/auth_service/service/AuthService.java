package com.rideflow.auth_service.service;

import com.rideflow.auth_service.domain.Credential;
import com.rideflow.auth_service.domain.dto.AuthRequest;
import com.rideflow.auth_service.domain.dto.LoginRequest;

public interface AuthService {

    Credential register(AuthRequest authRequest);

    String login(LoginRequest loginRequest);

}
