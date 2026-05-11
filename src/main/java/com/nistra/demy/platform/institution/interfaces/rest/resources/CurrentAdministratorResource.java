package com.nistra.demy.platform.institution.interfaces.rest.resources;

public record CurrentAdministratorResource(
        String firstName,
        String lastName,
        String emailAddress,
        String phoneNumber,
        String dniNumber
) {
}
