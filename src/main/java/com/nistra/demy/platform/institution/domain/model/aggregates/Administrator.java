package com.nistra.demy.platform.institution.domain.model.aggregates;

import com.nistra.demy.platform.institution.domain.model.commands.RegisterAdministratorCommand;
import com.nistra.demy.platform.institution.domain.model.events.AdministratorRegisteredEvent;
import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;
import com.nistra.demy.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.nistra.demy.platform.shared.domain.model.valueobjects.*;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
public class Administrator extends AuditableAbstractAggregateRoot<Administrator> {

    @Embedded
    @Getter
    private PersonName personName;

    @Embedded
    @Getter
    private PhoneNumber phoneNumber;

    @Embedded
    @Getter
    private DniNumber dniNumber;

    @Embedded
    @Getter
    private AcademyId academyId;

    @Embedded
    @Getter
    private UserId userId;

    /**
     * Default constructor for JPA
     */
    protected Administrator() {}

    public Administrator(
            PersonName personName,
            PhoneNumber phoneNumber,
            DniNumber dniNumber,
            UserId userId
    ) {
        this.personName = personName;
        this.phoneNumber = phoneNumber;
        this.dniNumber = dniNumber;
        this.academyId = new AcademyId();
        this.userId = userId;
    }

    public Administrator(RegisterAdministratorCommand command) {
        this(
                command.personName(),
                command.phoneNumber(),
                command.dniNumber(),
                command.userId()
        );
    }

    public void registerAdministrator(Long academyId, Long userId) {
        this.addDomainEvent(new AdministratorRegisteredEvent(this, academyId, userId));
    }

    public void associateAcademy(AcademyId academyId) {
        if (this.academyId == null || this.academyId.academyId() == null) {
            this.academyId = academyId;
        } else {
            throw new IllegalStateException("Administrator is already associated with an academy.");
        }
    }

    public void disassociateAcademy(AcademyId academyId) {
        if (this.academyId != null && this.academyId.equals(academyId)) {
            this.academyId = new AcademyId();
        } else {
            throw new IllegalStateException("Administrator is not associated with the specified academy.");
        }
    }
}
