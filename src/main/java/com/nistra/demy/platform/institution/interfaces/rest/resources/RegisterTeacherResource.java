package com.nistra.demy.platform.institution.interfaces.rest.resources;

public record RegisterTeacherResource(
        String firstName,
        String lastName,
        String emailAddress,
        String countryCode,
        String phone
) {
    public RegisterTeacherResource {
        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name is required");
        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name is required");
        if (emailAddress == null || emailAddress.isBlank())
            throw new IllegalArgumentException("Email address is required");
        if (countryCode == null || countryCode.isBlank())
            throw new IllegalArgumentException("Country code is required");
        if (phone == null || phone.isBlank())
            throw new IllegalArgumentException("Phone number is required");
    }
}
