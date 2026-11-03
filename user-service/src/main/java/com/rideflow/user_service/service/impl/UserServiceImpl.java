package com.rideflow.user_service.service.impl;

import com.rideflow.user_service.domain.User;
import com.rideflow.user_service.domain.dto.UserRequest;
import com.rideflow.user_service.exception.DuplicateUserException;
import com.rideflow.user_service.exception.UserNotFoundException;
import com.rideflow.user_service.repository.UserRepository;
import com.rideflow.user_service.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerUser(UserRequest userRequest) {
        // Check if the same email exists
        if (userRepository.existsByEmail(userRequest.email())) {
            throw new DuplicateUserException("email already registered: "+userRequest.email());
        }
        // Check if the same phone number exists
        if (userRepository.existsByPhoneNumber(userRequest.phoneNumber())) {
            throw new DuplicateUserException("phone number already registered: "+userRequest.phoneNumber());
        }

        User user = new User(userRequest.username(), userRequest.email(), userRequest.phoneNumber(), userRequest.authSubjectId());
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
