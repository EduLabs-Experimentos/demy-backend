package com.nistra.demy.platform.enrollment.application.internal.outboundservices.acl;


import com.nistra.demy.platform.enrollment.domain.model.valueobjects.ScheduleId;
import com.nistra.demy.platform.scheduling.interfaces.acl.WeeklySchedulesContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExternalSchedulingService {
    private final WeeklySchedulesContextFacade schedulingContextFacade;

    public ExternalSchedulingService(WeeklySchedulesContextFacade schedulingContextFacade) {
        this.schedulingContextFacade = schedulingContextFacade;
    }

    public Optional<ScheduleId> fetchScheduleById(Long scheduleId) {
        boolean exists = schedulingContextFacade.existsScheduleById(scheduleId);
        return exists ? Optional.of(new ScheduleId(scheduleId)) : Optional.empty();
    }
}
