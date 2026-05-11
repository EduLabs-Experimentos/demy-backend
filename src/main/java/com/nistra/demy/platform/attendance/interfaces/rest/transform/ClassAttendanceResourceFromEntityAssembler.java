package com.nistra.demy.platform.attendance.interfaces.rest.transform;

import com.nistra.demy.platform.attendance.domain.model.aggregates.ClassAttendance;
import com.nistra.demy.platform.attendance.domain.model.entities.AttendanceRecord;
import com.nistra.demy.platform.attendance.interfaces.rest.resources.AttendanceRecordResource;
import com.nistra.demy.platform.attendance.interfaces.rest.resources.ClassAttendanceResource;

import java.util.List;

/**
 * Assembler to create a ClassAttendanceResource from a ClassAttendance entity.
 *
 */
public class ClassAttendanceResourceFromEntityAssembler {

    /**
     * Converts a ClassAttendanceSource entity  to a ClassAttendanceResource.
     * @param entity
     * @return
     */
    public static ClassAttendanceResource toResourceFromEntity(ClassAttendance entity) {
        return new ClassAttendanceResource(
                entity.getId(),
                entity.getClassSessionId().classSessionId(),
                entity.getDate(),
                entity.getAttendance().stream()
                        .map(record -> new AttendanceRecordResource(
                                record.getDni().dniNumber(),
                                record.getStatus().name()
                        )).toList()
        );
    }
}
