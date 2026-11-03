package com.rideflow.auth_service.exception;

public class DuplicateCredentialException extends RuntimeException {
    public DuplicateCredentialException(String messsage) {
        super(messsage);
    }
}
