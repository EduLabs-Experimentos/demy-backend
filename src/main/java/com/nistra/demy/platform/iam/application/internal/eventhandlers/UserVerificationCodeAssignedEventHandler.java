package com.nistra.demy.platform.iam.application.internal.eventhandlers;

import com.nistra.demy.platform.iam.application.internal.outboundservices.email.EmailService;
import com.nistra.demy.platform.iam.domain.model.events.UserVerificationCodeAssignedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
@Service
public class UserVerificationCodeAssignedEventHandler {

    private final EmailService emailService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserVerificationCodeAssignedEventHandler.class);

    public UserVerificationCodeAssignedEventHandler(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @EventListener
    public void handle(UserVerificationCodeAssignedEvent event) {
        LOGGER.info("Handling UserVerificationCodeAssignedEvent for email: {}, code: {}, expiration: {} minutes",
                event.getEmail(), event.getCode(), event.getExpirationMinutes());
        emailService.sendVerificationEmail(
                event.getEmail(),
                event.getCode(),
                event.getExpirationMinutes()
        );
    }
}
