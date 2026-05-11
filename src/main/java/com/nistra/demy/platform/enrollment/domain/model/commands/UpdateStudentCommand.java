package com.nistra.demy.platform.enrollment.domain.model.commands;

import com.nistra.demy.platform.enrollment.domain.model.valueobjects.Sex;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PersonName;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PhoneNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.StreetAddress;

import java.time.LocalDate;

public record UpdateStudentCommand(
        Long studentId,
        PersonName personName,
        DniNumber dniNumber,
        Sex sex,
        LocalDate birthDate,
        StreetAddress streetAddress,
        PhoneNumber phoneNumber ) {

    public UpdateStudentCommand {
        if (studentId == null || studentId < 1) {
            throw new IllegalArgumentException("studentId must be greater than 0");
        }
        if (personName == null) {
            throw new IllegalArgumentException("Person Name cannot be null");
        }
        if (dniNumber == null) {
            throw new IllegalArgumentException("DniNumber cannot be null");
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
