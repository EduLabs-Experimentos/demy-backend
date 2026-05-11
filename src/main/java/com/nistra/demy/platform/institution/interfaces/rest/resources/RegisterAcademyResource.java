package com.nistra.demy.platform.institution.interfaces.rest.resources;

/**
 * Represents a resource for registering a new Academy in the REST API.
 * <p>
 * This record is used to transfer data required to register a new Academy.
 *
 * @param academyName       the name of the academy
 * @param academyDescription a description of the academy
 * @param street            the street address of the academy
 * @param district          the district where the academy is located
 * @param province          the province where the academy is located
 * @param department        the department where the academy is located
 * @param emailAddress     the email address of the academy
 * @param countryCode       the country code of the academy's location
 * @param phone             the phone number of the academy
 * @param ruc              the Registro Único de Contribuyentes (RUC) of the academy
 *
 * @author Salim Ramirez
 * @see IllegalArgumentException
 * @since 1.0.0
 */
public record RegisterAcademyResource(
        String academyName,
        String academyDescription,
        String street,
        String district,
        String province,
        String department,
        String emailAddress,
        String countryCode,
        String phone,
        String ruc,
        Long administratorId
) {
    /**
     * Validates the fields of the RegisterAcademyResource.
     *
     * @throws IllegalArgumentException if any required field is null or invalid
     */
    public RegisterAcademyResource {
        if (academyName == null || academyName.isBlank())
            throw new IllegalArgumentException("Academy name is required");
        if (academyDescription == null || academyDescription.isBlank())
            throw new IllegalArgumentException("Academy description is required");
        if (street == null || street.isBlank())
            throw new IllegalArgumentException("Street address is required");
        if (district == null || district.isBlank())
            throw new IllegalArgumentException("District is required");
        if (province == null || province.isBlank())
            throw new IllegalArgumentException("Province is required");
        if (department == null || department.isBlank())
            throw new IllegalArgumentException("Department is required");
        if (emailAddress == null || emailAddress.isBlank())
            throw new IllegalArgumentException("Email address is required");
        if (countryCode == null || countryCode.isBlank())
            throw new IllegalArgumentException("Country code is required");
        if (phone == null || phone.isBlank())
            throw new IllegalArgumentException("Phone number is required");
        if (ruc == null || ruc.isBlank())
            throw new IllegalArgumentException("RUC is required");
        if (administratorId == null || administratorId <= 0)
            throw new IllegalArgumentException("Administrator ID must be a positive number");
    }
}
