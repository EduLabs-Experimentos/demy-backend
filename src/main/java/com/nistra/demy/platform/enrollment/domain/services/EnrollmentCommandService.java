package com.nistra.demy.platform.enrollment.domain.services;

import com.nistra.demy.platform.enrollment.domain.model.aggregates.Enrollment;
import com.nistra.demy.platform.enrollment.domain.model.commands.*;

import java.util.Optional;

public interface EnrollmentCommandService {
    Long handle(CreateEnrollmentCommand command);
    void handle(DeleteEnrollmentCommand command);
    Optional<Enrollment> handle(UpdateEnrollmentCommand command);
}
