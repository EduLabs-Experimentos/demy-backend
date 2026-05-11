package com.nistra.demy.platform.enrollment.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class StudentEnrolledEvent extends ApplicationEvent {

    Long studentId;

    String dniNumber;

    public StudentEnrolledEvent(Object source, Long studentId, String dniNumber) {
        super(source);
        this.studentId = studentId;
        this.dniNumber = dniNumber;
    }
}
