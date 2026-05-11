package com.nistra.demy.platform.institution.domain.model.aggregates;

import com.nistra.demy.platform.institution.domain.model.commands.RegisterTeacherCommand;
import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;
import com.nistra.demy.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PersonName;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PhoneNumber;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
public class Teacher extends AuditableAbstractAggregateRoot<Teacher> {

    @Embedded
    @Getter
    private PersonName personName;

    @Embedded
    @Getter
    private PhoneNumber phoneNumber;

    @Embedded
    @Getter
    private AcademyId academyId;

    @Embedded
    @Getter
    private UserId userId;

    /**
     * Default constructor for JPA
     */
    protected Teacher() {}

    public Teacher(RegisterTeacherCommand command, UserId userId, AcademyId academyId) {
        this.personName = command.personName();
        this.phoneNumber = command.phoneNumber();
        this.userId = userId;
        this.academyId = academyId;
    }
}
