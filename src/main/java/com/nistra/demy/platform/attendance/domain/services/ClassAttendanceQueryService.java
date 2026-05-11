package com.nistra.demy.platform.attendance.domain.services;

import com.nistra.demy.platform.attendance.domain.model.aggregates.ClassAttendance;
import com.nistra.demy.platform.attendance.domain.model.queries.GetAllClassAttendancesByAcademyQuery;
import com.nistra.demy.platform.attendance.domain.model.queries.GetClassAttendanceByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * @name ClassAttendanceQueryService
 *
 * @summary
 * This interface represents the service to handle class attendance queries.
 * @since 1.0.0
 */
public interface ClassAttendanceQueryService {

    /**
     * Handles the get all class attendances by academy id query.
     * @param query The get all class attendances by academy id query.
     * @return The list class attendances if academyId exists.
     * @throws IllegalArgumentException If academyId is null or empty
     * @see GetAllClassAttendancesByAcademyQuery
     */
    List<ClassAttendance> handle(GetAllClassAttendancesByAcademyQuery query);

    /**
     * Handles the get all class attendances by academy id query and class attendance id.
     * @param query The get all class attendances by academy id query and class attendance id.
     * @return The list class attendances if academyId and id exists.
     * @throws IllegalArgumentException If academyId or id is null or empty
     * @see GetAllClassAttendancesByAcademyQuery
     */

    Optional<ClassAttendance> handle(GetClassAttendanceByIdQuery query);
}