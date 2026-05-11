package com.nistra.demy.platform.institution.domain.services;

import com.nistra.demy.platform.institution.domain.model.aggregates.Academy;
import com.nistra.demy.platform.institution.domain.model.commands.AssignAdministratorToAcademyCommand;
import com.nistra.demy.platform.institution.domain.model.commands.RegisterAcademyCommand;

import java.util.Optional;

/**
 * Service interface for handling commands related to the Academy aggregate.
 * <p>
 * Defines operations to process academy-related commands such as registering a new academy.
 *
 * @author Salim Ramirez
 * @since 1.0.0
 */
public interface AcademyCommandService {

    /**
     * Handles the registration of a new academy.
     *
     * @param command the command containing the necessary data to register an academy
     * @return the identifier of the newly registered academy
     */
    Optional<Academy> handle(RegisterAcademyCommand command);

    void handle(AssignAdministratorToAcademyCommand command);
}
