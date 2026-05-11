package com.nistra.demy.platform.shared.interfaces.rest.resources;

import java.time.LocalDateTime;

/**
 * Error Resource
 * Represents a structured error response with metadata.
 *
 * @since 1.0.0
 */
public record ErrorResource(
        LocalDateTime timestamp,
        int status,
        String error,
        String code,
        String message,
        String path
) {
}
