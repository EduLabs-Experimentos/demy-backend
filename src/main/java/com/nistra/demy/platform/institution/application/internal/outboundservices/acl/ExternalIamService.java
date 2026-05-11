package com.nistra.demy.platform.institution.application.internal.outboundservices.acl;

import com.nistra.demy.platform.iam.interfaces.acl.IamContextFacade;
import com.nistra.demy.platform.institution.domain.model.valueobjects.InstitutionRoles;
import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("institutionExternalIamService")
public class ExternalIamService {

    private final IamContextFacade iamContextFacade;

    public ExternalIamService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    public Optional<UserId> fetchCurrentUserId() {
        var userId = iamContextFacade.fetchAuthenticatedUserId();
        return userId == 0L ? Optional.empty() : Optional.of(new UserId(userId));
    }

    public Optional<AcademyId> fetchCurrentAcademyId() {
        var academyId = iamContextFacade.fetchAuthenticatedUserTenantId();
        return academyId == 0L ? Optional.empty() : Optional.of(new AcademyId(academyId));
    }

    public Optional<UserId> registerTeacher(String email) {
        var userId = iamContextFacade.signUpVerifiedUser(email, List.of(InstitutionRoles.ROLE_TEACHER.name()));
        return userId == 0L ? Optional.empty() : Optional.of(new UserId(userId));
    }

    public Optional<EmailAddress> fetchEmailAddressByUserId(UserId userId) {
        var email = iamContextFacade.fetchUserEmailAddressByUserId(userId.userId());
        return email == null ? Optional.empty() : Optional.of(new EmailAddress(email));
    }
}
