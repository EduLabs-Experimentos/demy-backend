package com.nistra.demy.platform.iam.application.internal.eventhandlers;

import com.nistra.demy.platform.iam.application.internal.outboundservices.email.EmailService;
import com.nistra.demy.platform.iam.domain.model.events.UserSignedUpAndActivatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class UserSignedUpAndActivatedEventHandler {

    private final EmailService emailService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserSignedUpAndActivatedEventHandler.class);

    public UserSignedUpAndActivatedEventHandler(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @EventListener
    public void on(UserSignedUpAndActivatedEvent event) {
        LOGGER.info("Handling UserSignedUpAndActivatedEvent for email: {}", event.getEmail());
        emailService.sendTemporaryPasswordEmail(event.getEmail(), event.getPassword());
    }
}
