package com.nistra.demy.platform.institution.domain.services;

import com.nistra.demy.platform.institution.domain.model.aggregates.Administrator;
import com.nistra.demy.platform.institution.domain.model.queries.GetAdministratorByDniNumberQuery;
import com.nistra.demy.platform.institution.domain.model.queries.GetAdministratorEmailAddressByUserIdQuery;
import com.nistra.demy.platform.institution.domain.model.queries.GetCurrentAdministratorQuery;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;

import java.util.Optional;

public interface AdministratorQueryService {

    Optional<Administrator> handle(GetAdministratorByDniNumberQuery query);

    Optional<Administrator> handle(GetCurrentAdministratorQuery query);

    Optional<EmailAddress> handle(GetAdministratorEmailAddressByUserIdQuery query);
}
