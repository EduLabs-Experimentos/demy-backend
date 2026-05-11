package com.nistra.demy.platform.scheduling.interfaces.rest.controllers;

import com.nistra.demy.platform.institution.domain.model.queries.GetTeacherByUserIdQuery;
import com.nistra.demy.platform.institution.domain.services.TeacherQueryService;
import com.nistra.demy.platform.scheduling.application.internal.outboundservices.acl.ExternalEnrollmentService;
import com.nistra.demy.platform.scheduling.domain.model.commands.*;
import com.nistra.demy.platform.scheduling.domain.model.queries.GetAllWeeklySchedulesQuery;
import com.nistra.demy.platform.scheduling.domain.model.queries.GetSchedulesByTeacherIdQuery;
import com.nistra.demy.platform.scheduling.domain.model.queries.GetWeeklyScheduleByIdQuery;
import com.nistra.demy.platform.scheduling.domain.services.WeeklyScheduleCommandService;
import com.nistra.demy.platform.scheduling.domain.services.WeeklyScheduleQueryService;
import com.nistra.demy.platform.scheduling.interfaces.rest.resources.*;
import com.nistra.demy.platform.scheduling.interfaces.rest.transform.*;
import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;
import com.nistra.demy.platform.shared.interfaces.rest.resources.MessageResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller for managing weekly schedules. It provides endpoints for creating,
 * retrieving, updating, deleting weekly schedules, and managing associated schedules.
 * This controller interacts with {@link WeeklyScheduleCommandService} for handling
 * commands related to weekly schedule creation, updating, deletion, and adding/removing
 * schedules, and with {@link WeeklyScheduleQueryService} for handling queries related to
 * retrieving weekly schedules and schedules by teacher.
 */

@RestController
@RequestMapping(value = "/api/v1/schedules", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Schedules", description = "Schedule Management Endpoints")
public class WeeklySchedulesController {

    private final WeeklyScheduleCommandService weeklyScheduleCommandService;
    private final WeeklyScheduleQueryService weeklyScheduleQueryService;
    private final WeeklyScheduleResourceFromEntityAssembler weeklyScheduleResourceFromEntityAssembler;
    private final ScheduleResourceFromEntityAssembler scheduleResourceFromEntityAssembler;
    private final TeacherQueryService teacherQueryService;
    private final ExternalEnrollmentService externalEnrollmentService;

    public WeeklySchedulesController(
            WeeklyScheduleCommandService weeklyScheduleCommandService,
            WeeklyScheduleQueryService weeklyScheduleQueryService,
            WeeklyScheduleResourceFromEntityAssembler weeklyScheduleResourceFromEntityAssembler,
            ScheduleResourceFromEntityAssembler scheduleResourceFromEntityAssembler,
            TeacherQueryService teacherQueryService,
            ExternalEnrollmentService externalEnrollmentService

    ) {
        this.weeklyScheduleCommandService = weeklyScheduleCommandService;
        this.weeklyScheduleQueryService = weeklyScheduleQueryService;
        this.weeklyScheduleResourceFromEntityAssembler = weeklyScheduleResourceFromEntityAssembler;
        this.scheduleResourceFromEntityAssembler = scheduleResourceFromEntityAssembler;
        this.teacherQueryService = teacherQueryService;
        this.externalEnrollmentService = externalEnrollmentService;
    }

    /**
     * Creates a new schedule based on the provided resource. // Texto Actualizado
     *
     * @param resource The resource containing the details of the schedule to be created. // Texto Actualizado
     * @return A {@link ResponseEntity} with the created schedule or an error response. // Texto Actualizado
     */
    @PostMapping
    @Operation(summary = "Create a new schedule", description = "Create a new schedule") // Texto Actualizado
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Schedule created successfully"), // Texto Actualizado
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Schedule not found") // Texto Actualizado
    })
    public ResponseEntity<WeeklyScheduleResource> createSchedule(@RequestBody CreateWeeklyScheduleResource resource) {
        var createWeeklyScheduleCommand = CreateWeeklyScheduleCommandFromResourceAssembler.toCommandFromResource(resource);
        var scheduleId = weeklyScheduleCommandService.handle(createWeeklyScheduleCommand);

        if (scheduleId == null || scheduleId == 0L) {
            return ResponseEntity.badRequest().build();
        }

        var getWeeklyScheduleByIdQuery = new GetWeeklyScheduleByIdQuery(scheduleId);
        var schedule = weeklyScheduleQueryService.handle(getWeeklyScheduleByIdQuery);

        if (schedule.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // MODIFICADO: Uso el bean inyectado
        var scheduleResource = weeklyScheduleResourceFromEntityAssembler.toResourceFromEntity(schedule.get());
        return new ResponseEntity<>(scheduleResource, HttpStatus.CREATED);
    }

    /**
     * Retrieves all schedules. // Texto Actualizado
     *
     * @return A {@link ResponseEntity} containing a list of schedules or an error response. // Texto Actualizado
     */
    @GetMapping
    @Operation(summary = "Get all schedules", description = "Get all schedules") // Texto Actualizado
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedules found"), // Texto Actualizado
            @ApiResponse(responseCode = "404", description = "No schedules found") // Texto Actualizado
    })
    public ResponseEntity<List<WeeklyScheduleResource>> getAllSchedules() {
        var getAllWeeklySchedulesQuery = new GetAllWeeklySchedulesQuery();
        var schedules = weeklyScheduleQueryService.handle(getAllWeeklySchedulesQuery);

        var scheduleResources = schedules.stream()
                // MODIFICADO: Uso el bean inyectado
                .map(weeklyScheduleResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(scheduleResources);
    }

    /**
     * Retrieves a schedule by its ID. // Texto Actualizado
     *
     * @param scheduleId The ID of the schedule to retrieve. // Variable Renombrada
     * @return A {@link ResponseEntity} containing the schedule resource or an error response. // Texto Actualizado
     */
    @GetMapping("/{scheduleId}") // Ruta Actualizada
    @Operation(summary = "Get schedule by ID", description = "Get a schedule by ID") // Texto Actualizado
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule found"), // Texto Actualizado
            @ApiResponse(responseCode = "404", description = "Schedule not found") // Texto Actualizado
    })
    public ResponseEntity<WeeklyScheduleResource> getScheduleById(@PathVariable Long scheduleId) {
        var getWeeklyScheduleByIdQuery = new GetWeeklyScheduleByIdQuery(scheduleId);
        var schedule = weeklyScheduleQueryService.handle(getWeeklyScheduleByIdQuery);

        if (schedule.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // MODIFICADO: Uso el bean inyectado
        var scheduleResource = weeklyScheduleResourceFromEntityAssembler.toResourceFromEntity(schedule.get());
        return ResponseEntity.ok(scheduleResource);
    }

    /**
     * Updates a schedule by its ID. // Texto Actualizado
     *
     * @param scheduleId The ID of the schedule to update. // Variable Renombrada
     * @param resource The resource containing the updated details of the schedule. // Texto Actualizado
     * @return A {@link ResponseEntity} containing the updated schedule or an error response. // Texto Actualizado
     */
    @PutMapping("/{scheduleId}") // Ruta Actualizada
    @Operation(summary = "Update a schedule by ID", description = "Update a schedule by ID") // Texto Actualizado
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule updated successfully"), // Texto Actualizado
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Schedule not found") // Texto Actualizado
    })
    public ResponseEntity<WeeklyScheduleResource> updateSchedule(@PathVariable Long scheduleId, @RequestBody UpdateWeeklyScheduleNameResource resource) {
        var updateWeeklyScheduleCommand = UpdateWeeklyScheduleNameCommandFromResourceAssembler.toCommandFromResource(scheduleId, resource);
        var updatedSchedule = weeklyScheduleCommandService.handle(updateWeeklyScheduleCommand);

        if (updatedSchedule.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var updatedWeeklyScheduleEntity = updatedSchedule.get();
        // MODIFICADO: Uso el bean inyectado
        var updatedScheduleResource = weeklyScheduleResourceFromEntityAssembler.toResourceFromEntity(updatedWeeklyScheduleEntity);
        return ResponseEntity.ok(updatedScheduleResource);
    }

    /**
     * Deletes a schedule by its ID. // Texto Actualizado
     *
     * @param scheduleId The ID of the schedule to delete. // Variable Renombrada
     * @return A {@link ResponseEntity} indicating the deletion status.
     */
    @DeleteMapping("/{scheduleId}") // Ruta Actualizada
    @Operation(summary = "Delete a schedule by ID", description = "Delete a schedule by ID") // Texto Actualizado
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule deleted successfully"), // Texto Actualizado
            @ApiResponse(responseCode = "404", description = "Schedule not found") // Texto Actualizado
    })
    public ResponseEntity<?> deleteSchedule(@PathVariable Long scheduleId) {
        var deleteWeeklyScheduleCommand = new DeleteWeeklyScheduleCommand(scheduleId);
        weeklyScheduleCommandService.handle(deleteWeeklyScheduleCommand);
        return ResponseEntity.ok(new MessageResource("Schedule deleted successfully")); // Texto Actualizado
    }

    /**
     * Adds a class session to an existing schedule. // Texto Actualizado
     *
     * @param scheduleId The ID of the schedule to add the class session to. // Variables Renombradas
     * @param resource The resource containing the class session details to be added. // Texto Actualizado
     * @return A {@link ResponseEntity} containing the updated schedule. // Texto Actualizado
     */
    @PostMapping("/{scheduleId}/class-sessions") // Ruta Actualizada
    @Operation(summary = "Add Class Session to Schedule", description = "Add a new class session to an existing schedule.") // Texto Actualizado
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The class session was added to the schedule."), // Texto Actualizado
            @ApiResponse(responseCode = "400", description = "The class session was not added."), // Texto Actualizado
            @ApiResponse(responseCode = "404", description = "The schedule was not found.") // Texto Actualizado
    })
    public ResponseEntity<WeeklyScheduleResource> addClassSessionToSchedule(@PathVariable Long scheduleId, @RequestBody AddScheduleToWeeklyResource resource) {
        var addScheduleToWeeklyCommand = AddScheduleToWeeklyCommandFromResourceAssembler.toCommandFromResource(scheduleId, resource);
        var updatedSchedule = weeklyScheduleCommandService.handle(addScheduleToWeeklyCommand); // Variable Renombrada

        if (updatedSchedule.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var updatedWeeklyScheduleEntity = updatedSchedule.get();
        var updatedScheduleResource = weeklyScheduleResourceFromEntityAssembler.toResourceFromEntity(updatedWeeklyScheduleEntity); // Corregido: Usa inyectado
        return ResponseEntity.ok(updatedScheduleResource);
    }

    /**
     * Removes a class session from a schedule. // Texto Actualizado
     *
     * @param scheduleId The ID of the schedule. // Variable Renombrada
     * @param classSessionId The ID of the class session to remove. // Variable Renombrada
     * @return A {@link ResponseEntity} containing the updated schedule. // Texto Actualizado
     */
    @DeleteMapping("/{scheduleId}/class-sessions/{classSessionId}") // Rutas Actualizadas
    @Operation(summary = "Remove Class Session from Schedule", description = "Remove a class session from a schedule.") // Texto Actualizado
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The class session was removed from the schedule."), // Texto Actualizado
            @ApiResponse(responseCode = "400", description = "The class session was not removed."), // Texto Actualizado
            @ApiResponse(responseCode = "404", description = "The schedule or class session was not found.") // Texto Actualizado
    })
    public ResponseEntity<WeeklyScheduleResource> removeClassSessionFromSchedule(@PathVariable Long scheduleId, @PathVariable Long classSessionId) {
        var removeScheduleFromWeeklyCommand = new RemoveScheduleFromWeeklyCommand(scheduleId, classSessionId);
        var updatedSchedule = weeklyScheduleCommandService.handle(removeScheduleFromWeeklyCommand); // Variable Renombrada
        if (updatedSchedule.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var updatedWeeklyScheduleEntity = updatedSchedule.get();
        var updatedScheduleResource = weeklyScheduleResourceFromEntityAssembler.toResourceFromEntity(updatedWeeklyScheduleEntity); // Corregido: Usa inyectado
        return ResponseEntity.ok(updatedScheduleResource);
    }

    /**
     * Retrieves all class sessions for a specific teacher by their teacher ID. // Texto Actualizado
     *
     * @param userId The ID of the teacher whose class sessions to retrieve. // Texto Actualizado
     * @return A {@link ResponseEntity} containing the list of class sessions for the teacher. // Texto Actualizado
     */
    @GetMapping("/by-teacher/{userId}")
    @Operation(summary = "Get Class Sessions by userId", description = "Get all class sessions for a given userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Class sessions found"),
            @ApiResponse(responseCode = "404", description = "No class sessions found for teacherId")
    })
    public ResponseEntity<List<ScheduleResource>> getClassSessionsByTeacherId(@PathVariable Long userId) {
        var getTeacherByUserIdQuery = new GetTeacherByUserIdQuery(new UserId(userId));
        var teacher = teacherQueryService.handle(getTeacherByUserIdQuery);

        if (teacher.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var getSchedulesByTeacherIdQuery = new GetSchedulesByTeacherIdQuery(new UserId(teacher.get().getId()));
        var classSessions = weeklyScheduleQueryService.handle(getSchedulesByTeacherIdQuery);

        if (classSessions == null || classSessions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var classSessionResources = classSessions.stream()
                .map(scheduleResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(classSessionResources);
    }

    /**
     * Updates the details of an existing class session. // Texto Actualizado
     *
     * @param classSessionId The ID of the class session to update. // Variable Renombrada
     * @param resource The updated class session details. // Texto Actualizado
     * @return A {@link ResponseEntity} containing the updated class session. // Texto Actualizado
     */
    @PutMapping("/class-sessions/{classSessionId}") // Ruta Actualizada
    @Operation(summary = "Update a Class Session by ID", description = "Updates the classroom, start time, end time and day fields of a Class Session by its ID") // Texto Actualizado
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Class Session updated successfully"), // Texto Actualizado
            @ApiResponse(responseCode = "404", description = "Class Session not found") // Texto Actualizado
    })
    public ResponseEntity<ScheduleResource> updateClassSession(
            @PathVariable Long classSessionId,
            @RequestBody UpdateScheduleResource resource) {
        var command = UpdateScheduleCommandFromResourceAssembler.toCommandFromResource(classSessionId, resource);
        var updatedClassSessionOpt = weeklyScheduleCommandService.handle(command); // Variable Renombrada
        if (updatedClassSessionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var updatedClassSession = updatedClassSessionOpt.get();
        var classSessionResource = scheduleResourceFromEntityAssembler.toResourceFromEntity(updatedClassSession); // Corregido: Usa inyectado
        return ResponseEntity.ok(classSessionResource);
    }

    /**
     * Retrieves the Weekly Schedule associated with an enrolled student via their User ID.
     *
     * @param userId The User ID (from IAM) of the student.
     * @return A ResponseEntity containing the WeeklyScheduleResource or a 404 Not Found.
     */
    @GetMapping("/by-student/{userId}")
    @Operation(summary = "Get Schedule by Student User Id", description = "Retrieves the schedule associated with a student's User ID by consulting the active enrollment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule found"),
            @ApiResponse(responseCode = "404", description = "Student or active Enrollment not found")
    })
    public ResponseEntity<WeeklyScheduleResource> getScheduleByStudentUserId(@PathVariable Long userId) {
        var weeklyScheduleId = externalEnrollmentService.fetchScheduleIdByStudentUserId(userId);

        if (weeklyScheduleId.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var getWeeklyScheduleByIdQuery = new GetWeeklyScheduleByIdQuery(weeklyScheduleId.get());
        var weeklySchedule = weeklyScheduleQueryService.handle(getWeeklyScheduleByIdQuery);

        if (weeklySchedule.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var weeklyScheduleResource = weeklyScheduleResourceFromEntityAssembler.toResourceFromEntity(weeklySchedule.get());
        return ResponseEntity.ok(weeklyScheduleResource);
    }

}
