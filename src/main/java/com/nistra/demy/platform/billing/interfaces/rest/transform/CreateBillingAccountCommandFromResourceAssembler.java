package com.nistra.demy.platform.billing.interfaces.rest.transform;

import com.nistra.demy.platform.billing.domain.model.commands.CreateBillingAccountCommand;
import com.nistra.demy.platform.billing.interfaces.rest.resources.CreateBillingAccountResource;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.StudentId;

public class CreateBillingAccountCommandFromResourceAssembler {
    public static CreateBillingAccountCommand toCommandFromResource(CreateBillingAccountResource resource) {
        return new CreateBillingAccountCommand(
                new StudentId(resource.studentId()),
                new DniNumber(resource.dniNumber())
        );
    }
}
