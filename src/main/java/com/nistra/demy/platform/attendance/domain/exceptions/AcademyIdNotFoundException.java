package com.nistra.demy.platform.attendance.domain.exceptions;

import com.nistra.demy.platform.shared.domain.exceptions.DomainException;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;

public class AcademyIdNotFoundException extends DomainException {
    public AcademyIdNotFoundException() {
        super("Academy Id not found for AcademyId");
    }
}
