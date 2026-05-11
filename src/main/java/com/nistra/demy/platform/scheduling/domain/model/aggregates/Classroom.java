package com.nistra.demy.platform.scheduling.domain.model.aggregates;

import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;
import com.nistra.demy.platform.scheduling.domain.model.commands.CreateClassroomCommand;
import com.nistra.demy.platform.scheduling.domain.model.commands.UpdateClassroomCommand;
import com.nistra.demy.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;

/**
 * Represents a classroom entity with code, capacity, and campus information.
 * Provides methods to create and update classroom details.
 */
@Getter
@Entity
public class Classroom extends AuditableAbstractAggregateRoot<Classroom> {

    private String code;
    private Integer capacity;
    private String campus;
    @Embedded
    private AcademyId academyId;


    /**
     * Default constructor.
     * Initializes code and campus as empty strings and capacity as zero.
     */
    public Classroom() {
        this.code = Strings.EMPTY;
        this.capacity = Integer.valueOf(0);
        this.campus = Strings.EMPTY;
        this.academyId = new AcademyId();
    }

    /**
     * Constructs a Classroom with the specified code, capacity, and campus.
     * @param code the classroom code
     * @param capacity the classroom capacity
     * @param campus the campus name
     */
    public Classroom(String code, Integer capacity, String campus, AcademyId academyId) {
        this.code = code;
        this.capacity = capacity;
        this.campus = campus;
        this.academyId = academyId;
    }

    /**
     * Constructs a Classroom using a CreateClassroomCommand.
     * @param command the command containing classroom creation data
     */
    public Classroom(CreateClassroomCommand command, AcademyId academyId) {
        this.code = command.code();
        this.capacity = command.capacity();
        this.campus = command.campus();
        this.academyId = academyId;
    }

    /**
     * Updates the classroom with the specified code, capacity, and campus.
     * @param code the new classroom code
     * @param capacity the new classroom capacity
     * @param campus the new campus name
     * @return the updated Classroom instance
     */
    public Classroom updateClassroom(String code, Integer capacity, String campus) {
        this.code = code;
        this.capacity = capacity;
        this.campus = campus;
        return this;
    }

    /**
     * Updates the classroom using an UpdateClassroomCommand.
     * @param command the command containing updated classroom data
     * @return the updated Classroom instance
     */
    public Classroom updateClassroom(UpdateClassroomCommand command) {
        this.code = command.code();
        this.capacity = command.capacity();
        this.campus = command.campus();
        return this;
    }

}