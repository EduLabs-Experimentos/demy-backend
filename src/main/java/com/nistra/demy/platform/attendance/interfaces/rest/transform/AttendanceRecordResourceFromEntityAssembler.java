package com.nistra.demy.platform.attendance.interfaces.rest.transform;

import com.nistra.demy.platform.attendance.domain.model.entities.AttendanceRecord;
import com.nistra.demy.platform.attendance.interfaces.rest.resources.AttendanceRecordResource;

/**
 * Assembler to create a AttendanceRecordResource from AttendanceRecord entity.
 */
public class AttendanceRecordResourceFromEntityAssembler {

    /**
     * Converts a AttendanceRecord entity to a AttendanceRecordResource.
     * @param entity
     * @return
     */
    public static AttendanceRecordResource toResourceFromEntity(AttendanceRecord entity) {
        return new AttendanceRecordResource (
                entity.getDni().dniNumber(),
                entity.getStatus().name()

        );
    }
}
