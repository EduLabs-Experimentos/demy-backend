package com.nistra.demy.platform.shared.application.internal.outboundservices.localization;

import java.util.Locale;

/**
 * Abstraction for resolving localized messages.
 *
 * @since 1.0.0
 */
public interface LocalizationService {
    String getMessage(String code, Object[] args, Locale locale);

    String getMessage(String code, Locale locale);

    String getCodeForDomainException(String domainExceptionCode);

    String getCodeForStatus(String statusCode);
}
