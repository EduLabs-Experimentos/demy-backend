package com.nistra.demy.platform.attendance.domain.model.queries;


/**
 * @summary
 * This class represents the query to get a favorite source by AcademyId and class attendance id.
 */
public record GetClassAttendanceByIdQuery(Long id) {
    public GetClassAttendanceByIdQuery {
        if (id == null || id <= 0) throw new IllegalArgumentException("id must be a positive number");
    }
}
