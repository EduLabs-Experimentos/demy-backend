package com.nistra.demy.platform.institution.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AdministratorRegisteredEvent extends ApplicationEvent {

    private final Long academyId;
    private final Long userId;

    public AdministratorRegisteredEvent(Object source, Long academyId, Long userId) {
        super(source);
        this.academyId = academyId;
        this.userId = userId;
    }
}
