package com.nistra.demy.platform.scheduling.application.internal.outboundservices.acl;

import com.nistra.demy.platform.enrollment.interfaces.acl.EnrollmentsContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("schedulingExternalEnrollmentService")
public class ExternalEnrollmentService {

    private final EnrollmentsContextFacade enrollmentsContextFacade;

    public ExternalEnrollmentService(EnrollmentsContextFacade enrollmentsContextFacade) {
        this.enrollmentsContextFacade = enrollmentsContextFacade;
    }

    public Optional<Long> fetchScheduleIdByStudentUserId(Long userId) {
        Long scheduleId = enrollmentsContextFacade.fetchScheduleIdByStudentUserId(userId);
        return scheduleId == 0L ? Optional.empty() : Optional.of(scheduleId);
    }
}