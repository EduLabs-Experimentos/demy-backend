package com.nistra.demy.platform.institution.interfaces.acl;

public interface InstitutionContextFacade {
    Long fetchTeacherIdByFullName(String firstName, String lastName);
}
