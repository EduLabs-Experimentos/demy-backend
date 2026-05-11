package com.nistra.demy.platform.institution.interfaces.rest.resources;

public record TeacherResource(
        Long id,
        String firstName,
        String lastName,
        String emailAddress,
        String countryCode,
        String phoneNumber,
        Long academyId
) {
}