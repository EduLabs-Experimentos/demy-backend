package com.nistra.demy.platform.institution.application.acl;

import com.nistra.demy.platform.institution.domain.model.queries.GetTeacherByFullNameQuery;
import com.nistra.demy.platform.institution.domain.services.TeacherQueryService;
import com.nistra.demy.platform.institution.interfaces.acl.InstitutionContextFacade;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PersonName;
import org.springframework.stereotype.Service;

@Service
public class InstitutionContextFacadeImpl implements InstitutionContextFacade {
    private final TeacherQueryService teacherQueryService;

    public InstitutionContextFacadeImpl(TeacherQueryService teacherQueryService) {
        this.teacherQueryService = teacherQueryService;
    }

    @Override
    public Long fetchTeacherIdByFullName(String firstName, String lastName) {
        PersonName personName = new PersonName(firstName, lastName);
        var query = new GetTeacherByFullNameQuery(personName);
        var teacher = teacherQueryService.handle(query);
        return teacher.isEmpty() ? Long.valueOf(0L) : teacher.get().getId();
    }


}
