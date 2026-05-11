package com.nistra.demy.platform.institution.interfaces.rest.controllers;

import com.nistra.demy.platform.institution.domain.model.queries.GetAllTeachersQuery;
import com.nistra.demy.platform.institution.domain.model.queries.GetCurrentTeacherQuery;
import com.nistra.demy.platform.institution.domain.model.queries.GetTeacherEmailAddressByUserIdQuery;
import com.nistra.demy.platform.institution.domain.services.TeacherCommandService;
import com.nistra.demy.platform.institution.domain.services.TeacherQueryService;
import com.nistra.demy.platform.institution.interfaces.rest.resources.CurrentTeacherResource;
import com.nistra.demy.platform.institution.interfaces.rest.resources.RegisterTeacherResource;
import com.nistra.demy.platform.institution.interfaces.rest.resources.TeacherResource;
import com.nistra.demy.platform.institution.interfaces.rest.transform.CurrentTeacherResourceFromEntityAssembler;
import com.nistra.demy.platform.institution.interfaces.rest.transform.RegisterTeacherCommandFromResourceAssembler;
import com.nistra.demy.platform.institution.interfaces.rest.transform.TeacherResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/teachers", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Teachers", description = "Endpoints for managing teachers")
public class TeachersController {

    private final TeacherCommandService teacherCommandService;
    private final TeacherQueryService teacherQueryService;

    public TeachersController(TeacherCommandService teacherCommandService, TeacherQueryService teacherQueryService) {
        this.teacherCommandService = teacherCommandService;
        this.teacherQueryService = teacherQueryService;
    }

    @Operation(
            summary = "Register a new teacher",
            description = "Creates a new teacher in the system with the provided information."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Teacher successfully registered",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data or teacher could not be created", content = @Content)
    })
    @PostMapping
    public ResponseEntity<TeacherResource> registerTeacher(@RequestBody RegisterTeacherResource resource) {
        var registerTeacherCommand = RegisterTeacherCommandFromResourceAssembler.toCommandFromResource(resource);
        var teacher = teacherCommandService.handle(registerTeacherCommand);
        if (teacher.isEmpty()) return ResponseEntity.badRequest().build();
        var teacherEntity = teacher.get();
        var emailAddress = teacherQueryService.handle(new GetTeacherEmailAddressByUserIdQuery(teacherEntity.getUserId()));
        if (emailAddress.isEmpty()) return ResponseEntity.badRequest().build();
        var teacherResource = TeacherResourceFromEntityAssembler.toResourceFromEntity(teacherEntity, emailAddress.get());
        return new ResponseEntity<>(teacherResource, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all teachers",
            description = "Retrieves a list of all registered teachers, including their email addresses."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of teachers retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherResource.class)))
    })
    @GetMapping
    public ResponseEntity<List<TeacherResource>> getAllTeachers() {
        var getAllTeachersQuery = new GetAllTeachersQuery();
        var teachers = teacherQueryService.handle(getAllTeachersQuery);
        var teacherResources = teachers.stream().map(teacher -> {
            var emailAddress = teacherQueryService.handle(new GetTeacherEmailAddressByUserIdQuery(teacher.getUserId()));
            return emailAddress.map(email -> TeacherResourceFromEntityAssembler.toResourceFromEntity(teacher, email)).orElse(null);
        }).filter(Objects::nonNull).toList();
        return ResponseEntity.ok(teacherResources);
    }

    @Operation(
            summary = "Get current teacher",
            description = "Retrieves the teacher entity associated with the currently authenticated user. " +
                    "The response also includes the teacher's email address."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Current teacher retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CurrentTeacherResource.class))),
            @ApiResponse(responseCode = "404", description = "Teacher or associated email not found", content = @Content)
    })
    @GetMapping("/me")
    public ResponseEntity<CurrentTeacherResource> getCurrentTeacher() {
        var getCurrentTeacherQuery = new GetCurrentTeacherQuery();
        var teacher = teacherQueryService.handle(getCurrentTeacherQuery);
        if (teacher.isEmpty()) return ResponseEntity.notFound().build();
        var teacherEntity = teacher.get();
        var getTeacherEmailAddressByUserIdQuery = new GetTeacherEmailAddressByUserIdQuery(teacherEntity.getUserId());
        var emailAddress = teacherQueryService.handle(getTeacherEmailAddressByUserIdQuery);
        if (emailAddress.isEmpty()) return ResponseEntity.notFound().build();
        var emailAddressValueObject = emailAddress.get();
        var currentTeacherResource = CurrentTeacherResourceFromEntityAssembler.toResourceFromEntity(teacherEntity, emailAddressValueObject);
        return new ResponseEntity<>(currentTeacherResource, HttpStatus.OK);
    }
}
