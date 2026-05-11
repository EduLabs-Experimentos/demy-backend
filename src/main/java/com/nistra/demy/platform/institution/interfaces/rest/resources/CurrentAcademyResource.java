package com.nistra.demy.platform.institution.interfaces.rest.resources;

public record CurrentAcademyResource(
        String academyName,
        String street,
        String district,
        String province,
        String department,
        String phoneNumber,
        String emailAddress,
        String ruc
) {
}
