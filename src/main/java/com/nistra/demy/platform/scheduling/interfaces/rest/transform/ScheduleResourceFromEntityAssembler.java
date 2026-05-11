package com.nistra.demy.platform.scheduling.interfaces.rest.transform;
// Imports de Servicios y Queries
import com.nistra.demy.platform.institution.domain.model.queries.GetTeacherByIdQuery;
import com.nistra.demy.platform.institution.domain.model.queries.GetTeacherEmailAddressByUserIdQuery;
import com.nistra.demy.platform.institution.domain.services.TeacherQueryService;
import com.nistra.demy.platform.scheduling.domain.model.entities.Schedule;
import com.nistra.demy.platform.scheduling.domain.model.queries.GetClassroomByIdQuery;
import com.nistra.demy.platform.scheduling.domain.model.queries.GetCourseByIdQuery;
import com.nistra.demy.platform.scheduling.domain.services.ClassroomQueryService;
import com.nistra.demy.platform.scheduling.domain.services.CourseQueryService;
import com.nistra.demy.platform.scheduling.interfaces.rest.resources.ScheduleResource;
// Imports de Ensambladores Anidados
import com.nistra.demy.platform.institution.interfaces.rest.transform.TeacherResourceFromEntityAssembler;
// Eliminados: jakarta.annotation.PostConstruct, se usa inyección directa
import org.springframework.stereotype.Component;

/**
 * Ensamblador para convertir una entidad Schedule a un ScheduleResource detallado.
 * Es un componente inyectable que coordina la obtención de datos de otros contextos
 * (Course, Classroom, Teacher) para construir el recurso anidado.
 */
@Component // MODIFICADO: Es un componente de Spring
public class ScheduleResourceFromEntityAssembler {

    private final CourseQueryService courseQueryService;
    private final ClassroomQueryService classroomQueryService;
    private final TeacherQueryService teacherQueryService;

    // Inyectar ensambladores de recursos anidados (para delegar el mapeo del sub-recurso)
    private final CourseResourceFromEntityAssembler courseResourceAssembler;
    private final ClassroomResourceFromEntityAssembler classroomResourceAssembler;
    private final TeacherResourceFromEntityAssembler teacherResourceAssembler;

    // Constructor con inyección de todas las dependencias
    public ScheduleResourceFromEntityAssembler(
            CourseQueryService courseQueryService,
            ClassroomQueryService classroomQueryService,
            TeacherQueryService teacherQueryService,
            CourseResourceFromEntityAssembler courseResourceAssembler,
            ClassroomResourceFromEntityAssembler classroomResourceAssembler,
            TeacherResourceFromEntityAssembler teacherResourceAssembler) {
        this.courseQueryService = courseQueryService;
        this.classroomQueryService = classroomQueryService;
        this.teacherQueryService = teacherQueryService;
        this.courseResourceAssembler = courseResourceAssembler;
        this.classroomResourceAssembler = classroomResourceAssembler;
        this.teacherResourceAssembler = teacherResourceAssembler;
    }

    /**
     * Convierte una entidad Schedule en un ScheduleResource con recursos anidados.
     */
    public ScheduleResource toResourceFromEntity(Schedule entity) { // MODIFICADO: Ya no es static
        Long courseId = entity.getCourseId().id();
        Long classroomId = entity.getClassroomId().id();
        Long teacherId = entity.getTeacherId().userId();

        // 1. Obtener y ensamblar CourseResource
        var courseEntity = courseQueryService.handle(new GetCourseByIdQuery(courseId))
                .orElseThrow(() -> new IllegalStateException("Course not found for ID: " + courseId));
        var courseResource = courseResourceAssembler.toResourceFromEntity(courseEntity);

        // 2. Obtener y ensamblar ClassroomResource
        var classroomEntity = classroomQueryService.handle(new GetClassroomByIdQuery(classroomId))
                .orElseThrow(() -> new IllegalStateException("Classroom not found for ID: " + classroomId));
        var classroomResource = classroomResourceAssembler.toResourceFromEntity(classroomEntity);

        // 3. Obtener el email y ensamblar TeacherResource
        var teacherEntity = teacherQueryService.handle(new GetTeacherByIdQuery(teacherId))
                .orElseThrow(() -> new IllegalStateException("Teacher UserAccount not found for ID: " + teacherId));
        var emailAddress = teacherQueryService.handle(new GetTeacherEmailAddressByUserIdQuery(teacherEntity.getUserId()))
                .orElseThrow(() -> new IllegalStateException("Email de Teacher no encontrado para User ID: " + teacherEntity.getUserId().userId()));
        var teacherResource = teacherResourceAssembler.toResourceFromEntity(teacherEntity, emailAddress);

        return new ScheduleResource(
                entity.getId(),
                entity.getTimeRange().startTime().toString(),
                entity.getTimeRange().endTime().toString(),
                entity.getDayOfWeek().name(),
                courseResource,
                classroomResource,
                teacherResource
        );
    }
}