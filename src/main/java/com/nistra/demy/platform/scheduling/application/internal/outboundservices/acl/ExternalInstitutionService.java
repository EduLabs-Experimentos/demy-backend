package com.nistra.demy.platform.scheduling.application.internal.outboundservices.acl;

import com.nistra.demy.platform.iam.interfaces.acl.IamContextFacade;
import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;
import com.nistra.demy.platform.institution.interfaces.acl.InstitutionContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * External IAM Service
 * <p>This class provides a service layer for interacting with the IAM context through the Anti-Corruption Layer (ACL) pattern. It handles teacher identification and validation operations.</p>
 */
@Service
public class ExternalInstitutionService {
    private final InstitutionContextFacade institutionContextFacade;

    public ExternalInstitutionService(InstitutionContextFacade institutionContextFacade) {
        this.institutionContextFacade = institutionContextFacade;
    }

    /**
     * This method is used to fetch a teacher ID by their full name using the IAM ACL.
     * @param firstName The first name of the teacher.
     * @param lastName The last name of the teacher.
     * @return An optional with the teacher's user ID if found and valid, otherwise an empty optional.
     * @see IamContextFacade
     * @see UserId
     */
    public Optional<UserId> fetchTeacherIdByFullName(String firstName, String lastName) {
        var teacherId = institutionContextFacade.fetchTeacherIdByFullName(firstName, lastName);
        if (teacherId == null || teacherId == 0L) return Optional.empty();
        return Optional.of(new UserId(teacherId));
    }

}
