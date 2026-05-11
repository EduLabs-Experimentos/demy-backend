package com.nistra.demy.platform.institution.domain.model.aggregates;

import com.nistra.demy.platform.institution.domain.model.commands.RegisterAcademyCommand;
import com.nistra.demy.platform.institution.domain.model.valueobjects.AcademyDescription;
import com.nistra.demy.platform.institution.domain.model.valueobjects.AcademyName;
import com.nistra.demy.platform.institution.domain.model.valueobjects.AdministratorId;
import com.nistra.demy.platform.institution.domain.model.valueobjects.Ruc;
import com.nistra.demy.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PhoneNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.StreetAddress;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

/**
 * Represents an Academy entity in the domain model.
 * <p>
 * This class extends {@link AuditableAbstractAggregateRoot} to provide auditing capabilities.
 * It contains the administrator ID and RUC (Registro Único de Contribuyentes) as embedded value objects.
 *
 * @author Salim Ramirez
 * @see AuditableAbstractAggregateRoot
 * @see AdministratorId
 * @see Ruc
 * @since 1.0.0
 */
@Entity
public class Academy extends AuditableAbstractAggregateRoot<Academy> {

    @Embedded
    @Getter
    private AdministratorId administratorId;

    @Embedded
    @Getter
    private AcademyName academyName;

    @Embedded
    @Getter
    private AcademyDescription academyDescription;

    @Embedded
    @Getter
    private StreetAddress streetAddress;

    @Embedded
    @Getter
    private EmailAddress emailAddress;

    @Embedded
    @Getter
    private PhoneNumber phoneNumber;

    @Embedded
    @Getter
    private Ruc ruc;

    /**
     * Default constructor for JPA
     */
    protected Academy() {}

    /**
     * Constructs an Academy instance with the specified administrator ID and RUC.
     *
     * @param academyName the name of the academy
     * @param academyDescription the description of the academy
     * @param streetAddress the street address of the academy
     * @param emailAddress the email address of the academy
     * @param phoneNumber the phone number of the academy
     * @param ruc the RUC of the academy
     * @see AdministratorId
     * @see Ruc
     */
    public Academy(AcademyName academyName,
                   AcademyDescription academyDescription,
                   StreetAddress streetAddress,
                   EmailAddress emailAddress,
                   PhoneNumber phoneNumber,
                   Ruc ruc
    ) {
        this.academyName = academyName;
        this.academyDescription = academyDescription;
        this.streetAddress = streetAddress;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.ruc = ruc;
    }

    /**
     * Constructs an Academy instance from a RegisterAcademyCommand.
     *
     * @param command the command containing the necessary data to register an academy
     * @see RegisterAcademyCommand
     */
    public Academy(RegisterAcademyCommand command) {
        this(
                command.academyName(),
                command.academyDescription(),
                command.streetAddress(),
                command.emailAddress(),
                command.phoneNumber(),
                command.ruc()
        );
    }

    public void assignAdministrator(AdministratorId administratorId) {
        if (this.administratorId != null && this.administratorId.isAssigned()) {
            throw new IllegalStateException("Administrator is already assigned to this academy");
        }
        this.administratorId = administratorId;
    }
}
