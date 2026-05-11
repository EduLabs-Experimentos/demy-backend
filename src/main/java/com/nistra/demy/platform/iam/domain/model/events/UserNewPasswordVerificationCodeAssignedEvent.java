package com.nistra.demy.platform.iam.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserNewPasswordVerificationCodeAssignedEvent extends ApplicationEvent {

    private final String email;
    private final String code;
    private final Integer expirationMinutes;

    public UserNewPasswordVerificationCodeAssignedEvent(Object source, String email, String code, Integer expirationMinutes) {
        super(source);
        this.email = email;
        this.code = code;
        this.expirationMinutes = expirationMinutes;
    }
}
