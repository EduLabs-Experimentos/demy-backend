package com.nistra.demy.platform.enrollment.interfaces.rest.transform;

import com.nistra.demy.platform.enrollment.domain.model.aggregates.Enrollment;
import com.nistra.demy.platform.enrollment.interfaces.rest.resources.EnrollmentResource;

public class EnrollmentResourceFromEntityAssembler {
    public static EnrollmentResource toResourceFromEntity(Enrollment entity) {
        return new EnrollmentResource(
                entity.getId(),
                entity.getStudentId().studentId(),
                entity.getPeriodId().periodId(),
                entity.getScheduleId().scheduleId(),
                entity.getAcademyId().academyId(),
                entity.getMoney().amount(),
                entity.getMoney().currency(),
                entity.getEnrollmentStatus().toString(),
                entity.getPaymentStatus().toString()
        );
    }
}
