package com.nistra.demy.platform.enrollment.domain.exceptions;

import com.nistra.demy.platform.shared.domain.exceptions.DomainException;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;

public class StudentAlreadyExistsException extends DomainException {
    public StudentAlreadyExistsException(DniNumber dni) {
        super("Student with DNI %s already exists in the current academy context.".formatted(dni.dniNumber()), dni.dniNumber());
    }
}
