package com.nistra.demy.platform.iam.domain.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("User with ID %s not found".formatted(userId));
    }
}
