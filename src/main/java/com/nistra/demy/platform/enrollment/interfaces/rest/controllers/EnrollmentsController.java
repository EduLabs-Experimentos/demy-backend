package com.nistra.demy.platform.enrollment.interfaces.rest.controllers;

import com.nistra.demy.platform.enrollment.domain.model.commands.DeleteEnrollmentCommand;
import com.nistra.demy.platform.enrollment.domain.model.queries.GetAllEnrollmentsByStudentDniQuery;
import com.nistra.demy.platform.enrollment.domain.model.queries.GetAllEnrollmentsByStudentIdQuery;
import com.nistra.demy.platform.enrollment.domain.model.queries.GetAllEnrollmentsQuery;
import com.nistra.demy.platform.enrollment.domain.model.queries.GetEnrollmentByIdQuery;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.StudentId;
import com.nistra.demy.platform.enrollment.domain.services.EnrollmentCommandService;
import com.nistra.demy.platform.enrollment.domain.services.EnrollmentQueryService;
import com.nistra.demy.platform.enrollment.interfaces.rest.resources.CreateEnrollmentResource;
import com.nistra.demy.platform.enrollment.interfaces.rest.resources.EnrollmentResource;
import com.nistra.demy.platform.enrollment.interfaces.rest.resources.UpdateEnrollmentResource;
import com.nistra.demy.platform.enrollment.interfaces.rest.transform.CreateEnrollmentCommandFromResourceAssembler;
import com.nistra.demy.platform.enrollment.interfaces.rest.transform.EnrollmentResourceFromEntityAssembler;
import com.nistra.demy.platform.enrollment.interfaces.rest.transform.UpdateEnrollmentCommandFromResourceAssembler;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/enrollments", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Enrollments", description = "Available Enrollment Endpoints")
public class EnrollmentsController {

    private final EnrollmentCommandService enrollmentCommandService;
    private final EnrollmentQueryService enrollmentQueryService;

    public EnrollmentsController(
            EnrollmentCommandService enrollmentCommandService,
            EnrollmentQueryService enrollmentQueryService
    ) {
        this.enrollmentCommandService = enrollmentCommandService;
        this.enrollmentQueryService = enrollmentQueryService;
    }

    /**
     * Creates a new enrollment.
     *
     * @param resource the request body containing enrollment data
     * @return ResponseEntity with the created enrollment or error status
     */
    @PostMapping
    @Operation(summary = "Create a new enrollment", description = "Registers a new enrollment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Enrollment created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Duplicate enrollment")
    })
    public ResponseEntity<EnrollmentResource> createEnrollment(@RequestBody CreateEnrollmentResource resource) {
        var createCommand = CreateEnrollmentCommandFromResourceAssembler.toCommandFromResource(resource);
        var enrollmentId = enrollmentCommandService.handle(createCommand);
        if (enrollmentId == null || enrollmentId == 0L) return ResponseEntity.badRequest().build();

        var enrollment = enrollmentQueryService.handle(new GetEnrollmentByIdQuery(enrollmentId));
        if (enrollment.isEmpty()) return ResponseEntity.notFound().build();

        var enrollmentEntity = enrollment.get();
        var enrollmentResource = EnrollmentResourceFromEntityAssembler.toResourceFromEntity(enrollmentEntity);
        return new ResponseEntity<>(enrollmentResource, HttpStatus.CREATED);
    }

    /**
     * Retrieves all enrollments.
     *
     * @return list of enrollment resources
     */
    @GetMapping
    @Operation(summary = "Get all enrollments", description = "Retrieves all enrollments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollments found"),
            @ApiResponse(responseCode = "404", description = "No enrollments found")
    })
    public ResponseEntity<List<EnrollmentResource>> getAllEnrollments() {
        var enrollments = enrollmentQueryService.handle(new GetAllEnrollmentsQuery());
        // if (enrollments.isEmpty()) return ResponseEntity.notFound().build();

        var enrollmentsResources = enrollments.stream()
                .map(EnrollmentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(enrollmentsResources);
    }

    /**
     * Retrieves a specific enrollment by its ID.
     *
     * @param enrollmentId the ID of the enrollment
     * @return the enrollment resource or not found
     */
    @GetMapping("/{enrollmentId}")
    @Operation(summary = "Get enrollment by ID", description = "Retrieve a specific enrollment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment found"),
            @ApiResponse(responseCode = "404", description = "Enrollment not found")
    })
    public ResponseEntity<EnrollmentResource> getEnrollmentById(@PathVariable Long enrollmentId) {
        var enrollment = enrollmentQueryService.handle(new GetEnrollmentByIdQuery(enrollmentId));
        if (enrollment.isEmpty()) return ResponseEntity.notFound().build();

        var resource = EnrollmentResourceFromEntityAssembler.toResourceFromEntity(enrollment.get());
        return ResponseEntity.ok(resource);
    }

    /**
     * Retrieves all enrollments by a specific student ID.
     *
     * @param studentId the ID of the student
     * @return list of enrollments for the given student
     */
    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get all enrollments by student ID", description = "Returns enrollments for a specific student ID")
    public ResponseEntity<List<EnrollmentResource>> getEnrollmentsByStudentId(@PathVariable Long studentId) {
        var enrollments = enrollmentQueryService.handle(new GetAllEnrollmentsByStudentIdQuery(new StudentId(studentId)));
        if (enrollments.isEmpty()) return ResponseEntity.notFound().build();

        var resources = enrollments.stream()
                .map(EnrollmentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    /**
     * Retrieves all enrollments by a student's DNI.
     *
     * @param dni the student's DNI
     * @return list of enrollments for the given DNI
     */
    @GetMapping("/student/dni/{dni}")
    @Operation(summary = "Get all enrollments by student DNI", description = "Returns enrollments using the student's DNI")
    public ResponseEntity<List<EnrollmentResource>> getEnrollmentsByStudentDni(@PathVariable String dni) {
        try {
            var enrollments = enrollmentQueryService.handle(new GetAllEnrollmentsByStudentDniQuery(new DniNumber(dni)));
            if (enrollments.isEmpty()) return ResponseEntity.notFound().build();

            var resources = enrollments.stream()
                    .map(EnrollmentResourceFromEntityAssembler::toResourceFromEntity)
                    .toList();
            return ResponseEntity.ok(resources);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Updates an existing enrollment by ID.
     *
     * @param enrollmentId the ID of the enrollment to update
     * @param resource the request body with updated enrollment data
     * @return the updated enrollment resource or not found
     */
    @PutMapping("/{enrollmentId}")
    @Operation(summary = "Update enrollment", description = "Update an existing enrollment")
    public ResponseEntity<EnrollmentResource> updateEnrollment(@PathVariable Long enrollmentId, @RequestBody UpdateEnrollmentResource resource) {
        var command = UpdateEnrollmentCommandFromResourceAssembler.toCommandFromResource(enrollmentId, resource);
        var updated = enrollmentCommandService.handle(command);
        if (updated.isEmpty()) return ResponseEntity.notFound().build();

        var resourceResponse = EnrollmentResourceFromEntityAssembler.toResourceFromEntity(updated.get());
        return ResponseEntity.ok(resourceResponse);
    }

    /**
     * Deletes an enrollment by its ID.
     *
     * @param enrollmentId the ID of the enrollment to delete
     * @return a message confirming deletion
     */
    @DeleteMapping("/{enrollmentId}")
    @Operation(summary = "Delete enrollment", description = "Deletes an enrollment by ID")
    public ResponseEntity<String> deleteEnrollment(@PathVariable Long enrollmentId) {
        enrollmentCommandService.handle(new DeleteEnrollmentCommand(enrollmentId));
        return ResponseEntity.ok("Enrollment successfully deleted");
    }
}
