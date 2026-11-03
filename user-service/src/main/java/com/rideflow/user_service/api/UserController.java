package com.rideflow.user_service.api;

import com.rideflow.user_service.domain.User;
import com.rideflow.user_service.domain.dto.UserRequest;
import com.rideflow.user_service.domain.dto.UserResponse;
import com.rideflow.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest userRequest) {
        User user = userService.registerUser(userRequest);
        return new ResponseEntity<>(
                UserResponse.fromUser(user),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        User currUser = userService.getUserById(id);
        return new ResponseEntity<>(
                UserResponse.fromUser(currUser),
                HttpStatus.FOUND
        );
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers().stream().map(UserResponse::fromUser).toList();
    }
}
