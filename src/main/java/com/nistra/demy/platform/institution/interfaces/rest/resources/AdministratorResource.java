package com.nistra.demy.platform.institution.interfaces.rest.resources;

/**
 * Represents a resource for an Administrator in the REST API.
 * <p>
 * This record is used to transfer data related to an Administrator.
 *
 * @param id the unique identifier of the administrator
 * @param firstName the first name of the administrator
 * @param lastName the last name of the administrator
 * @param phoneNumber the phone number of the administrator
 * @param dniNumber the DNI number of the administrator
 * @param academyId the academy ID associated with the administrator
 *
 * @author Salim Ramirez
 * @since 1.0.0
 */
public record AdministratorResource(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber,
        String dniNumber,
        Long academyId,
        Long userId
) {
}
