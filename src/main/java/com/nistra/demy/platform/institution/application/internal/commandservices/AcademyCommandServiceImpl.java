package com.nistra.demy.platform.institution.application.internal.commandservices;

import com.nistra.demy.platform.institution.domain.model.aggregates.Academy;
import com.nistra.demy.platform.institution.domain.model.commands.AssignAdministratorToAcademyCommand;
import com.nistra.demy.platform.institution.domain.model.commands.RegisterAcademyCommand;
import com.nistra.demy.platform.institution.domain.model.valueobjects.AdministratorId;
import com.nistra.demy.platform.institution.domain.services.AcademyCommandService;
import com.nistra.demy.platform.institution.infrastructure.persistence.jpa.repositories.AcademyRepository;
import com.nistra.demy.platform.institution.infrastructure.persistence.jpa.repositories.AdministratorRepository;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of the {@link AcademyCommandService} interface for handling academy registration commands.
 * <p>
 * This service is responsible for processing commands related to academy registration,
 * including validation and persistence of the academy entity.
 *
 * @author Salim Ramirez
 * @see AcademyCommandService
 * @see RegisterAcademyCommand
 * @since 1.0.0
 */
@Service
public class AcademyCommandServiceImpl implements AcademyCommandService {

    private final AcademyRepository academyRepository;
    private final AdministratorRepository administratorRepository;

    /**
     * Constructs an instance of the class.
     *
     * @param academyRepository the repository used for academy persistence operations
     */
    public AcademyCommandServiceImpl(
            AcademyRepository academyRepository,
            AdministratorRepository administratorRepository
    ) {
        this.academyRepository = academyRepository;
        this.administratorRepository = administratorRepository;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method processes the provided command to create a new academy
     *
     * @param command the command containing the necessary data to register an academy
     * @return the identifier of the newly registered academy
     * @see RegisterAcademyCommand
     */
    @Override
    @Transactional
    public Optional<Academy> handle(RegisterAcademyCommand command) {
        if (academyRepository.existsByEmailAddress(command.emailAddress()))
            throw new IllegalArgumentException("An academy with email %s already exists".formatted(command.emailAddress().email()));
        if (academyRepository.existsByRuc(command.ruc()))
            throw new IllegalArgumentException("An academy with RUC %s already exists".formatted(command.ruc().ruc()));
        var academy = new Academy(command);
        try {
            academyRepository.save(academy);
            var administrator = administratorRepository.findById(command.administratorId().administratorId())
                            .orElseThrow(() -> new IllegalArgumentException("No administrator found with id " + command.administratorId().administratorId()));
            administrator.registerAdministrator(academy.getId(), administrator.getUserId().userId());
            administratorRepository.save(administrator);
            System.out.println("Antes de assign: " + academy.getAdministratorId());
            academy.assignAdministrator(new AdministratorId(administrator.getId()));
            administrator.associateAcademy(new AcademyId(academy.getId()));
            academyRepository.save(academy);
            return Optional.of(academy);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register academy: %s".formatted(e.getMessage()));
        }
    }

    @Override
    public void handle(AssignAdministratorToAcademyCommand command) {
        academyRepository.findById(command.academyId().academyId()).map(academy -> {
            academy.assignAdministrator(command.administratorId());
            academyRepository.save(academy);
            return academy;
        }).orElseThrow(() -> new IllegalArgumentException("No academy found with id " + command.academyId().academyId()));
    }
}
