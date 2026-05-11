package com.nistra.demy.platform.institution.interfaces.rest.resources;

public record RegisterAdministratorResource(
        String firstName,
        String lastName,
        String countryCode,
        String phone,
        String dniNumber,
        Long userId
) {
    public RegisterAdministratorResource {
        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name is required");
        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Email address is required");
        if (countryCode == null || countryCode.isBlank())
            throw new IllegalArgumentException("Country code is required");
        if (phone == null || phone.isBlank())
            throw new IllegalArgumentException("Phone number is required");
        if (dniNumber == null || dniNumber.isBlank())
            throw new IllegalArgumentException("DNI number is required");
        if (userId == null || userId <= 0)
            throw new IllegalArgumentException("User ID is required and must be a positive number");
    }
}
