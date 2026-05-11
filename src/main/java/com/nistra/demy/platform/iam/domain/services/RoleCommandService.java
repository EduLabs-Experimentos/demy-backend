package com.nistra.demy.platform.iam.domain.services;

import com.nistra.demy.platform.iam.domain.model.commands.SeedRolesCommand;

public interface RoleCommandService {

    void handle(SeedRolesCommand command);
}
