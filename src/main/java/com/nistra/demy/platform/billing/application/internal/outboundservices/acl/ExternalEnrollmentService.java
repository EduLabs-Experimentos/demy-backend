package com.nistra.demy.platform.billing.application.internal.outboundservices.acl;

import com.nistra.demy.platform.enrollment.interfaces.acl.EnrollmentsContextFacade;
import com.nistra.demy.platform.shared.domain.model.valueobjects.StudentId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExternalEnrollmentService {

    private final EnrollmentsContextFacade enrollmentsContextFacade;

    public ExternalEnrollmentService(EnrollmentsContextFacade enrollmentsContextFacade) {
        this.enrollmentsContextFacade = enrollmentsContextFacade;
    }

    public Optional<StudentId> fetchStudentInvoiceIdByDniNumber(String dniNumber) {
        var studentId = enrollmentsContextFacade.fetchStudentIdByDni(dniNumber);
        return studentId == 0L ? Optional.empty() : Optional.of(new StudentId(studentId));
    }
}
