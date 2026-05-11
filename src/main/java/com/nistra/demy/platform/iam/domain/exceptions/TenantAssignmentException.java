package com.nistra.demy.platform.iam.domain.exceptions;

public class TenantAssignmentException extends RuntimeException {
    public TenantAssignmentException(String event) {
        super("Tenant ID cannot be null for %s event".formatted(event));
    }
}
