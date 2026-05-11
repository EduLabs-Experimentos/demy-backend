package com.nistra.demy.platform.billing.application.internal.eventhandlers;

import com.nistra.demy.platform.billing.domain.model.commands.CreateBillingAccountCommand;
import com.nistra.demy.platform.billing.domain.services.BillingAccountCommandService;
import com.nistra.demy.platform.enrollment.domain.model.events.StudentEnrolledEvent;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.StudentId;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class StudentEnrolledEventHandler {

    private final BillingAccountCommandService billingAccountCommandService;

    public StudentEnrolledEventHandler(BillingAccountCommandService billingAccountCommandService) {
        this.billingAccountCommandService = billingAccountCommandService;
    }

    @EventListener
    public void on(StudentEnrolledEvent event) {
        var createBillingAccountCommand = new CreateBillingAccountCommand(
                new StudentId(event.getStudentId()),
                new DniNumber(event.getDniNumber())
        );
        try {
            billingAccountCommandService.handle(createBillingAccountCommand);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create billing account for enrolled student: %s".formatted(e.getMessage()));
        }
    }
}
