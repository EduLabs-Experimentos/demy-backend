package com.nistra.demy.platform.attendance.domain.exceptions;

import com.nistra.demy.platform.attendance.domain.model.valueobjects.ClassSessionId;
import com.nistra.demy.platform.shared.domain.exceptions.DomainException;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;

import java.time.LocalDate;


public class ClassAttendanceAlreadyExistsException extends DomainException {
    public ClassAttendanceAlreadyExistsException(ClassSessionId id, LocalDate date) {
        super("Class attendance already exists for session %s on %s"
                .formatted(id.value(), date));
    }
}

