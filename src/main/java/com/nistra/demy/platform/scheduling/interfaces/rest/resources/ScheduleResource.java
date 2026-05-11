package com.nistra.demy.platform.scheduling.interfaces.rest.resources;


import com.nistra.demy.platform.institution.interfaces.rest.resources.TeacherResource;

/**
 * Recurso detallado para una sesión de horario, con información anidada de
 * Curso, Salón y Profesor para la visualización.
 */
public record ScheduleResource(
        Long id,
        String startTime,
        String endTime,
        String dayOfWeek,

        // Recursos anidados
        CourseResource course, // MODIFICADO
        ClassroomResource classroom, // MODIFICADO
        TeacherResource teacher // MODIFICADO (ahora usa el recurso completo de Teacher)
) {
}