package com.rideflow.user_service.service;

import com.rideflow.user_service.domain.User;
import com.rideflow.user_service.domain.dto.UserRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User registerUser(UserRequest userRequest);

    User getUserById(UUID id);

    List<User> getAllUsers();
}
