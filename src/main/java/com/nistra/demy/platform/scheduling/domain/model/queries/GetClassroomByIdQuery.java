package com.nistra.demy.platform.scheduling.domain.model.queries;

public record GetClassroomByIdQuery(Long classroomId) {

    public GetClassroomByIdQuery {
        if(classroomId == null || classroomId <= 0) {
            throw new IllegalArgumentException("Classroom ID is required and must be a positive number");
        }
    }
}
