package com.nistra.demy.platform.enrollment.domain.model.commands;

import com.nistra.demy.platform.enrollment.domain.model.valueobjects.Sex;
import com.nistra.demy.platform.shared.domain.model.valueobjects.*;
import jakarta.persistence.Column;

import java.time.LocalDate;

public record CreateStudentCommand(
        PersonName personName,
        DniNumber dni,
        EmailAddress emailAddress,
        Sex sex,
        LocalDate birthDate,
        StreetAddress streetAddress,
        PhoneNumber phoneNumber
) {
    public CreateStudentCommand {
        if (personName == null) {
            throw new IllegalArgumentException("Person Name cannot be null");
        }
        if (dni == null) {
            throw new IllegalArgumentException("DNI cannot be null");
        }
        if (emailAddress == null) {
            throw new IllegalArgumentException("Email Address cannot be null");
        }
        if (sex == null) {
            throw new IllegalArgumentException("Sex cannot be null");
        }
        if (birthDate == null || birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("birthDate cannot be null or after today");
        }
        if (streetAddress == null) {
            throw new IllegalArgumentException("Street Address cannot be null");
        }
        if (phoneNumber == null) {
            throw new IllegalArgumentException("PhoneNumber cannot be null");
        }
    }
}
