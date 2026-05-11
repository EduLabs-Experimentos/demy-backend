package com.nistra.demy.platform.shared.infrastructure.localization.messagesource.services;

import com.nistra.demy.platform.shared.infrastructure.localization.messagesource.MessageSourceLocalizationService;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;

@Component
public class LocalizationServiceImpl implements MessageSourceLocalizationService {

    private final MessageSource messageSource;

    public LocalizationServiceImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String getMessage(String code, Object[] args, Locale locale) {
        try {
            return messageSource.getMessage(code, args, locale);
        } catch (NoSuchMessageException e) {
            return code + (args != null ? Arrays.toString(args) : "");
        }
    }

    @Override
    public String getMessage(String code, Locale locale) {
        return getMessage(code, null, locale);
    }

    @Override
    public String getCodeForDomainException(String domainExceptionCode) {
        var code = domainExceptionCode;
        if (code.endsWith("Exception")) {
            code = code.substring(0, code.length() - "Exception".length());
        }
        return code.replaceAll("([a-z])([A-Z])", "$1.$2").toLowerCase();
    }

    @Override
    public String getCodeForStatus(String statusCode) {
        // Mapea "BAD_REQUEST" -> "bad.request"
        String code = statusCode.toLowerCase().replace("_", ".");
        try {
            messageSource.getMessage(code, null, Locale.getDefault());
            return code;
        } catch (NoSuchMessageException e) {
            return "error.unexpected";
        }
    }
}
