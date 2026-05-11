package com.nistra.demy.platform.enrollment.interfaces.rest.resources;

import java.time.LocalDate;

public record StudentResource(
        Long id,
        String firstName,
        String lastName,
        String dni,
        String email,
        String sex,
        LocalDate birthDate,
        String street,
        String district,
        String province,
        String department,
        String countryCode,
        String phone,
        Long academyId,
        Long userId
) {
}
