package com.nistra.demy.platform.institution.interfaces.rest.resources;

/**
 * Represents a resource for an Academy in the REST API.
 * <p>
 * This record is used to transfer data related to an Academy.
 *
 * @param id the unique identifier of the academy
 * @param administratorId the unique identifier of the administrator managing the academy
 * @param academyName the name of the academy
 * @param academyDescription a description of the academy
 * @param street the street address of the academy
 * @param district the district where the academy is located
 * @param province the province where the academy is located
 * @param department the department where the academy is located
 * @param emailAddress the email address of the academy
 * @param phoneNumber the phone number of the academy
 * @param ruc the Registro Único de Contribuyentes (RUC) of the academy
 *
 * @author Salim Ramirez
 * @since 1.0.0
 */
public record AcademyResource(
        Long id,
        Long administratorId,
        String academyName,
        String academyDescription,
        String street,
        String district,
        String province,
        String department,
        String emailAddress,
        String phoneNumber,
        String ruc
) {
}
