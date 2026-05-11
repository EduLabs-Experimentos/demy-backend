package com.nistra.demy.platform.iam.infrastructure.email.javamailsender.services;

import com.nistra.demy.platform.iam.infrastructure.email.javamailsender.UserNotificationEmailService;
import com.nistra.demy.platform.shared.infrastructure.email.javamailsender.TemplatedEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationEmailServiceImpl implements UserNotificationEmailService {

    private final TemplatedEmailService templatedEmailService;
    private static final Logger log = LoggerFactory.getLogger(NotificationEmailServiceImpl.class);

    public NotificationEmailServiceImpl(TemplatedEmailService templatedEmailService) {
        this.templatedEmailService = templatedEmailService;
    }

    @Override
    public void sendVerificationEmail(String to, String code, int expirationMinutes) {
        sendTemplatedEmail(to, "Confirma tu cuenta de Demy", "es/email/verification-email",
                Map.of("code", code, "expirationMinutes", expirationMinutes));
    }

    @Override
    public void sendPasswordResetEmail(String to, String code, int expirationMinutes) {
        sendTemplatedEmail(to, "Recupera tu contraseña", "es/email/reset-password-email",
                Map.of("code", code, "expirationMinutes", expirationMinutes));
    }

    @Override
    public void sendTemporaryPasswordEmail(String to, String temporaryPassword) {
        sendTemplatedEmail(to, "Bienvenido a Demy", "es/email/temporary-password-email",
                Map.of("temporaryPassword", temporaryPassword));
    }

    private void sendTemplatedEmail(String to, String subject, String template, Map<String, Object> variables) {
        log.info("Sending email '{}' to {} using template {}", subject, to, template);
        templatedEmailService.sendEmail(to, subject, template, variables);
    }
}
