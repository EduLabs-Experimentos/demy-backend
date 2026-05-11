package com.nistra.demy.platform.attendance.interfaces.rest.controllers;


import com.nistra.demy.platform.attendance.domain.model.aggregates.ClassAttendance;
import com.nistra.demy.platform.attendance.domain.model.commands.DeleteClassAttendanceCommand;
import com.nistra.demy.platform.attendance.domain.model.commands.UpdateAttendanceRecordStatusCommand;
import com.nistra.demy.platform.attendance.domain.model.queries.GetAllClassAttendancesByAcademyQuery;
import com.nistra.demy.platform.attendance.domain.model.queries.GetClassAttendanceByIdQuery;
import com.nistra.demy.platform.attendance.domain.model.valueobjects.AttendanceStatus;
import com.nistra.demy.platform.attendance.domain.services.ClassAttendanceCommandService;
import com.nistra.demy.platform.attendance.domain.services.ClassAttendanceQueryService;
import com.nistra.demy.platform.attendance.interfaces.rest.resources.ClassAttendanceResource;
import com.nistra.demy.platform.attendance.interfaces.rest.resources.CreateClassAttendanceResource;
import com.nistra.demy.platform.attendance.interfaces.rest.transform.ClassAttendanceResourceFromEntityAssembler;
import com.nistra.demy.platform.attendance.interfaces.rest.transform.CreateClassAttendanceFromResourceAssembler;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


/**
 * REST controller for class attendance sources.
 * @summary
 * This class provides REST endpoints for class attendances.
 */
@RestController
@RequestMapping(value="/api/v1/class-attendances", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Class Attendances", description = "Endpoints for Attendance Bounded Context")
public class ClassAttendanceController {
    private final ClassAttendanceCommandService classAttendanceCommandService;
    private final ClassAttendanceQueryService classAttendanceQueryService;


    public ClassAttendanceController(ClassAttendanceCommandService classAttendanceCommandService,
                                     ClassAttendanceQueryService classAttendanceQueryService) {
        this.classAttendanceCommandService = classAttendanceCommandService;
        this.classAttendanceQueryService = classAttendanceQueryService;
    }

    @Operation(
            summary = "Creates a class attendance",
            description="Creates a class attendance with the list of attendance records, classSessionId and date"
    )
    @ApiResponses( value={
            @ApiResponse(responseCode = "201", description = "Class attendance created"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping
    public ResponseEntity<ClassAttendanceResource> createClassAttendance(@RequestBody CreateClassAttendanceResource resource){
        Optional<ClassAttendance> classAttendance = classAttendanceCommandService.
                handle(CreateClassAttendanceFromResourceAssembler.toCommandFromResource(resource));
        return classAttendance.map( source ->
                new ResponseEntity<>(ClassAttendanceResourceFromEntityAssembler.toResourceFromEntity(source), CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Update attendance status for a student (by DNI)",
            description = "Update a student’s attendance status (PRESENT, ABSENT, EXCUSED) within a ClassAttendance. " +
                    "Scoped by academy (tenant)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attendance status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request (invalid status or malformed input)"),
            @ApiResponse(responseCode = "404", description = "ClassAttendance or DNI record not found")
    })

    @PatchMapping("/{id}/attendance/{dni}/status")
    public ResponseEntity<?> patchStatus(@PathVariable Long id,
                                         @PathVariable String dni,
                                         @RequestBody UpdateAttendanceStatusResource body) {
        var command = new UpdateAttendanceRecordStatusCommand(
                id, new DniNumber(dni), body.status()
        );
        var updated = classAttendanceCommandService.handle(command);

        return updated
                .map(agg -> ResponseEntity.ok(
                        ClassAttendanceResourceFromEntityAssembler.toResourceFromEntity(agg)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public record UpdateAttendanceStatusResource(AttendanceStatus status) {}


    @Operation(
            summary = "List all class attendances (academy-scoped)",
            description = "Lists all class attendances for the current academy (tenant)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List retrieved successfully")
    })

    @GetMapping("/all")
    public ResponseEntity<List<ClassAttendanceResource>> listAllByAcademy() {
        var list = classAttendanceQueryService.handle(new GetAllClassAttendancesByAcademyQuery());
        var resources = list.stream()
                .map(ClassAttendanceResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(
            summary = "Get a class attendance by ID (academy-scoped)",
            description = "Retrieves a ClassAttendance by its ID within the scope of the current academy (tenant)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })

    @GetMapping("/{id}")
    public ResponseEntity<ClassAttendanceResource> getById(@PathVariable Long id) {
        var maybe = classAttendanceQueryService.handle(new GetClassAttendanceByIdQuery(id));
        return maybe
                .map(agg -> ResponseEntity.ok(ClassAttendanceResourceFromEntityAssembler.toResourceFromEntity(agg)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete a class attendance by ID (academy-scoped)",
            description = "Deletes a ClassAttendance by its ID within the scope of the current academy (tenant)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        var deleted = classAttendanceCommandService.handle(new DeleteClassAttendanceCommand(id));
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}

