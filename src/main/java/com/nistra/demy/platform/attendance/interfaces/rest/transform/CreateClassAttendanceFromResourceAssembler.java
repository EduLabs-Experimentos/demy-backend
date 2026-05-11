package com.nistra.demy.platform.attendance.interfaces.rest.transform;

import com.nistra.demy.platform.attendance.domain.model.aggregates.ClassAttendance;
import com.nistra.demy.platform.attendance.domain.model.commands.CreateClassAttendanceCommand;
import com.nistra.demy.platform.attendance.domain.model.valueobjects.AttendanceInput;
import com.nistra.demy.platform.attendance.domain.model.valueobjects.AttendanceStatus;
import com.nistra.demy.platform.attendance.domain.model.valueobjects.ClassSessionId;
import com.nistra.demy.platform.attendance.interfaces.rest.resources.CreateClassAttendanceResource;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;

/**
 * Assembler to create a CreateClassAttendanceCommand from a CreateClassAttendanceResource.
 */
public class CreateClassAttendanceFromResourceAssembler {

    /**
     * Converts a CreateClassAttendanceResource from a CreateClassAttendanceCommand.
     * @param resource
     * @return
     */
    public static CreateClassAttendanceCommand toCommandFromResource(CreateClassAttendanceResource resource)
    {
        return new CreateClassAttendanceCommand(
                new ClassSessionId(resource.classSessionId()),
                resource.date(),
                resource.attendance().stream()
                        .map( a -> new AttendanceInput(
                                new DniNumber(a.dni()),
                                AttendanceStatus.valueOf(a.status().toUpperCase())
                        )).toList()
        );
    }
}
