package com.nistra.demy.platform.enrollment.application.internal.outboundservices.acl;

import com.nistra.demy.platform.iam.interfaces.acl.IamContextFacade;
import com.nistra.demy.platform.institution.domain.model.valueobjects.InstitutionRoles;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.UserId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("enrollmentExternalIamService")
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

    public Optional<UserId> registerStudent(String email) {
        var userId = iamContextFacade.signUpVerifiedUser(email, List.of(InstitutionRoles.ROLE_STUDENT.name()));
        return userId == 0L ? Optional.empty() : Optional.of(new UserId(userId));
    }

    public Optional<EmailAddress> fetchStudentEmailByUserId(Long userId) {
        var email = iamContextFacade.fetchUserEmailAddressByUserId(userId);
        return email == null ? Optional.empty() : Optional.of(new EmailAddress(email));
    }
}
