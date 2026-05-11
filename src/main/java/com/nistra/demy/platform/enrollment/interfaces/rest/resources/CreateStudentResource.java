package com.nistra.demy.platform.enrollment.interfaces.rest.resources;

import java.time.LocalDate;

public record CreateStudentResource(
        String firstName,
        String lastName,
        String dni,
        String emailAddress,
        String sex,
        LocalDate birthDate,
        String street,
        String district,
        String province,
        String department,
        String countryCode,
        String phone
) {
}
