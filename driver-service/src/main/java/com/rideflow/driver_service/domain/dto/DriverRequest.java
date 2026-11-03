package com.rideflow.driver_service.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DriverRequest (

        @NotBlank(message = "username is required")
        String username,

        @NotBlank(message = "email is required")
        @Email(message = "email must be valid")
        String email,

        @NotBlank(message = "phone number is required")
        @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "phone number must be valid")
        String phoneNumber,

        @NotBlank(message = "vehicle number is required")
        String vehicleNumber
) {

}
