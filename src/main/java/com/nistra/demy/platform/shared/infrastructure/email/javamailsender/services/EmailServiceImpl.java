package com.nistra.demy.platform.shared.infrastructure.email.javamailsender.services;

import com.nistra.demy.platform.shared.infrastructure.email.javamailsender.TemplatedEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service
public class EmailServiceImpl implements TemplatedEmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.from.address}")
    private String fromAddress;

    @Value("${spring.mail.from.name}")
    private String fromName;

    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            String htmlContent = processTemplate(templateName, variables);
            MimeMessage message = createMimeMessage(to, subject, htmlContent);
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Error sending email to %s: ".formatted(to), e);
        }
    }

    private String processTemplate(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templateName, context);
    }

    private MimeMessage createMimeMessage(String to, String subject, String htmlContent) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(fromAddress, fromName);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(stripHtmlTags(htmlContent), htmlContent);
        message.addHeader("X-Mailer", "Spring Boot Mailer");
        message.addHeader("X-Priority", "1");
        return message;
    }

    private String stripHtmlTags(String html) {
        return html.replaceAll("<[^>]*>", "");
    }
}
