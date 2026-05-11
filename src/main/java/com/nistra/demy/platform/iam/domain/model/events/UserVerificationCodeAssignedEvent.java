package com.nistra.demy.platform.iam.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserVerificationCodeAssignedEvent extends ApplicationEvent {

    private final String email;
    private final String code;
    private final Integer expirationMinutes;

    public UserVerificationCodeAssignedEvent(Object source, String email, String code, Integer expirationMinutes) {
        super(source);
        this.email = email;
        this.code = code;
        this.expirationMinutes = expirationMinutes;
    }
}
