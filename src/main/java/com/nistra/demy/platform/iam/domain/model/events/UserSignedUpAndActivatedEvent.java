package com.nistra.demy.platform.iam.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserSignedUpAndActivatedEvent extends ApplicationEvent {

    private final String email;
    private final String password;

    public UserSignedUpAndActivatedEvent(Object source, String email, String password) {
        super(source);
        this.email = email;
        this.password = password;
    }
}
