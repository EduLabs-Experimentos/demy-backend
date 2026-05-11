package com.nistra.demy.platform.billing.interfaces.rest.resources;

public record CreateBillingAccountResource(
        Long studentId,
        String dniNumber
) {
    public CreateBillingAccountResource {
        if (studentId == null || studentId <= 0)
            throw new IllegalArgumentException("Student ID must be a positive non-null value");
        if (dniNumber == null || dniNumber.isBlank())
            throw new IllegalArgumentException("DNI number must be a non-null, non-blank value");
    }
}
