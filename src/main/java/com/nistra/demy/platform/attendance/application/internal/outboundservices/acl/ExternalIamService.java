package com.nistra.demy.platform.attendance.application.internal.outboundservices.acl;

import com.nistra.demy.platform.iam.interfaces.acl.IamContextFacade;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * ACL for the current AcademyId
 */
@Service("attendanceExternalIamService")
public class ExternalIamService {

    private final IamContextFacade iamContextFacade;

    public ExternalIamService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    public Optional<AcademyId> fetchCurrentAcademyId() {
        var academyId = iamContextFacade.fetchAuthenticatedUserTenantId();
        return academyId == 0L ? Optional.empty() : Optional.of(new AcademyId(academyId));
    }
}
