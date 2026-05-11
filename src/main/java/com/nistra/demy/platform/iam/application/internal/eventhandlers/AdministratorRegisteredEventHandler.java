package com.nistra.demy.platform.iam.application.internal.eventhandlers;

import com.nistra.demy.platform.iam.domain.exceptions.TenantAssignmentException;
import com.nistra.demy.platform.iam.domain.model.commands.AssignUserTenantId;
import com.nistra.demy.platform.iam.domain.services.UserCommandService;
import com.nistra.demy.platform.institution.domain.model.events.AdministratorRegisteredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class AdministratorRegisteredEventHandler {

    private final UserCommandService userCommandService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AdministratorRegisteredEventHandler.class);

    public AdministratorRegisteredEventHandler(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @EventListener
    public void on(AdministratorRegisteredEvent event) {
        var tenantId = event.getAcademyId();
        if (tenantId == null) throw new TenantAssignmentException(event.toString());
        LOGGER.info("Handling AdministratorRegisteredEvent for User ID: {}, Academy ID: {}", event.getUserId(), event.getAcademyId());
        var assignUserTenantIdCommand = new AssignUserTenantId(event.getUserId(), tenantId);
        LOGGER.info("Assigning Tenant ID: {} to User ID: {}", event.getAcademyId(), event.getUserId());
        try {
            userCommandService.handle(assignUserTenantIdCommand);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error assigning tenant ID to user: %s".formatted(e.getMessage()));
        }
    }
}
