package com.rideflow.ride_service.api;

import com.rideflow.ride_service.exception.DriverNotAvailableException;
import com.rideflow.ride_service.exception.IllegalStatusChangeException;
import com.rideflow.ride_service.exception.RideNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStatusChangeException.class)
    public ProblemDetail handleIllegalStatusChangeException(IllegalStatusChangeException ex) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
    }

    @ExceptionHandler(DriverNotAvailableException.class)
    public ProblemDetail handleDriverNotAvailableException(DriverNotAvailableException ex) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE,
                ex.getMessage()
        );
    }

    @ExceptionHandler(RideNotFoundException.class)
    public ProblemDetail handleRideNotFoundException(RideNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }

}
