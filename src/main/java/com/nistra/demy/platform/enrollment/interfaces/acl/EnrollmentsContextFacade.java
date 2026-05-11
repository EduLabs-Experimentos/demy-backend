package com.nistra.demy.platform.enrollment.interfaces.acl;

public interface EnrollmentsContextFacade {

    String fetchStudentFullNameByDni(String dni);

    Long fetchStudentIdByDni(String dni);

    Long fetchScheduleIdByStudentUserId(Long userId);
}
