package com.nistra.demy.platform.shared.interfaces.rest.transform;

import com.nistra.demy.platform.shared.interfaces.rest.resources.ErrorResource;

import java.time.LocalDateTime;

public class ErrorResourceFromExceptionAssembler {
    public static ErrorResource toResourceFromException(
            String exception,
            int status,
            String error,
            String message,
            String path
    ) {
        return new ErrorResource(
                LocalDateTime.now(),
                status,
                error,
                exception.toUpperCase().replace('.', '_'),
                message,
                path
        );
    }
}
