package com.nistra.demy.platform.iam.application.internal.outboundservices.verification;

import java.time.LocalDateTime;

public interface VerificationService {

    String generateCode();

    String generateCode(int length);

    String generateRandomPassword(int length);

    String generateRandomPassword();

    Integer generateExpirationMinutes();

    boolean verifyCode(String code, String expectedCode, LocalDateTime expirationTime);
}
