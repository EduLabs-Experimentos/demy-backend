package com.nistra.demy.platform.enrollment.application.acl;

import com.nistra.demy.platform.enrollment.domain.model.queries.GetAllEnrollmentsByStudentIdQuery;
import com.nistra.demy.platform.enrollment.domain.model.queries.GetStudentByDniQuery;
import com.nistra.demy.platform.enrollment.domain.model.queries.GetStudentByUserIdQuery;
import com.nistra.demy.platform.enrollment.domain.model.queries.GetStudentIdByDniQuery;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.StudentId;
import com.nistra.demy.platform.enrollment.domain.services.EnrollmentQueryService;
import com.nistra.demy.platform.enrollment.domain.services.StudentQueryService;
import com.nistra.demy.platform.enrollment.interfaces.acl.EnrollmentsContextFacade;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.UserId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentsContextFacadeImpl implements EnrollmentsContextFacade {
    private final StudentQueryService studentQueryService;
    private final EnrollmentQueryService enrollmentQueryService;

    public EnrollmentsContextFacadeImpl(
            StudentQueryService studentQueryService,
            EnrollmentQueryService enrollmentQueryService) {
        this.studentQueryService = studentQueryService;
        this.enrollmentQueryService = enrollmentQueryService;
    }

    @Override
    public String fetchStudentFullNameByDni(String dni) {
        var getStudentByDniQuery = new GetStudentByDniQuery(new DniNumber(dni));
        var student = studentQueryService.handle(getStudentByDniQuery);
        return String.valueOf(student.map(value -> value.getPersonName().firstName()).orElse(""));
    }

    @Override
    public Long fetchStudentIdByDni(String dni) {
        var getStudentIdByDniQuery = new GetStudentIdByDniQuery(new DniNumber(dni));
        var studentId = studentQueryService.handle(getStudentIdByDniQuery);
        return studentId.isEmpty() ? 0L : studentId.get().studentId();
    }

    @Override
    public Long fetchScheduleIdByStudentUserId(Long userId) {
        try {
            var studentQuery = new GetStudentByUserIdQuery(new UserId(userId));
            var student = studentQueryService.handle(studentQuery);

            if (student.isEmpty()) {
                return 0L;
            }
            var studentIdValueObject = new StudentId(student.get().getId());
            var enrollments = enrollmentQueryService.handle(new GetAllEnrollmentsByStudentIdQuery(studentIdValueObject));
            var activeEnrollment = enrollments.stream()
                    .filter(e -> e.getEnrollmentStatus().name().equals("ACTIVE"))
                    .findFirst();
            return activeEnrollment.map(e -> e.getScheduleId().scheduleId()).orElse(0L);
        } catch (Exception e) {
            return 0L;
        }
    }
}
