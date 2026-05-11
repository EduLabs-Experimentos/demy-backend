package com.nistra.demy.platform.enrollment.domain.services;

import com.nistra.demy.platform.enrollment.domain.model.aggregates.AcademicPeriod;
import com.nistra.demy.platform.enrollment.domain.model.commands.CreateAcademicPeriodCommand;
import com.nistra.demy.platform.enrollment.domain.model.commands.DeleteAcademicPeriodCommand;
import com.nistra.demy.platform.enrollment.domain.model.commands.UpdateAcademicPeriodCommand;

import java.util.Optional;

public interface AcademicPeriodCommandService {
    Long handle(CreateAcademicPeriodCommand command);
    void handle(DeleteAcademicPeriodCommand command);
    Optional<AcademicPeriod> handle(UpdateAcademicPeriodCommand command);
}
