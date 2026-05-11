package com.nistra.demy.platform.institution.application.internal.commandservices;

import com.nistra.demy.platform.institution.domain.exceptions.AdministratorAlreadyExistsException;
import com.nistra.demy.platform.institution.domain.model.aggregates.Administrator;
import com.nistra.demy.platform.institution.domain.model.commands.RegisterAdministratorCommand;
import com.nistra.demy.platform.institution.domain.services.AdministratorCommandService;
import com.nistra.demy.platform.institution.infrastructure.persistence.jpa.repositories.AdministratorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AdministratorCommandServiceImpl implements AdministratorCommandService {

    private final AdministratorRepository administratorRepository;

    public AdministratorCommandServiceImpl(AdministratorRepository administratorRepository) {

        this.administratorRepository = administratorRepository;
    }

    @Override
    @Transactional
    public Optional<Administrator> handle(RegisterAdministratorCommand command) {
        if (administratorRepository.existsByDniNumber(command.dniNumber()))
            throw new AdministratorAlreadyExistsException(command.dniNumber());
        try {
            var administrator = new Administrator(command);
            administratorRepository.save(administrator);
            return Optional.of(administrator);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register administrator: %s".formatted(e.getMessage()));
        }
    }
}