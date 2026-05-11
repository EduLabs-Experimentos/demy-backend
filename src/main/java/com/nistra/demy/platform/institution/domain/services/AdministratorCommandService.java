package com.nistra.demy.platform.institution.domain.services;

import com.nistra.demy.platform.institution.domain.model.aggregates.Administrator;
import com.nistra.demy.platform.institution.domain.model.commands.RegisterAdministratorCommand;

import java.util.Optional;

public interface AdministratorCommandService {

    Optional<Administrator> handle(RegisterAdministratorCommand command);
}
