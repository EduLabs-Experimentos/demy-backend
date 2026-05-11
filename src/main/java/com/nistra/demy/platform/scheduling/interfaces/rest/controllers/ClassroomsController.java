package com.nistra.demy.platform.scheduling.interfaces.rest.controllers;

import com.nistra.demy.platform.scheduling.domain.model.commands.DeleteClassroomCommand;
import com.nistra.demy.platform.scheduling.domain.model.queries.GetAllClassroomsQuery;
import com.nistra.demy.platform.scheduling.domain.model.queries.GetClassroomByIdQuery;
import com.nistra.demy.platform.scheduling.domain.services.ClassroomCommandService;
import com.nistra.demy.platform.scheduling.domain.services.ClassroomQueryService;
import com.nistra.demy.platform.scheduling.interfaces.rest.resources.ClassroomResource;
import com.nistra.demy.platform.scheduling.interfaces.rest.resources.CreateClassroomResource;
import com.nistra.demy.platform.scheduling.interfaces.rest.resources.UpdateClassroomResource;
import com.nistra.demy.platform.scheduling.interfaces.rest.transform.ClassroomResourceFromEntityAssembler;
import com.nistra.demy.platform.scheduling.interfaces.rest.transform.CreateClassroomCommandFromResourceAssembler;
import com.nistra.demy.platform.scheduling.interfaces.rest.transform.UpdateClassroomCommandFromResourceAssembler;
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
 * REST controller for managing classrooms. It provides endpoints for creating,
 * retrieving, updating, and deleting classrooms.
 * This controller interacts with the {@link ClassroomCommandService} for handling
 * commands related to classroom creation, updating, and deletion, and the
 * {@link ClassroomQueryService} for handling queries related to retrieving
 * classrooms.
 */
@RestController
@RequestMapping(value = "/api/v1/classrooms", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Classrooms", description = "Classroom Management Endpoints")
public class ClassroomsController {

    private final ClassroomCommandService classroomCommandService;
    private final ClassroomQueryService classroomQueryService;

    public ClassroomsController(ClassroomCommandService classroomCommandService, ClassroomQueryService classroomQueryService) {
        this.classroomCommandService = classroomCommandService;
        this.classroomQueryService = classroomQueryService;
    }

    /**
     * Creates a new classroom based on the provided resource.
     *
     * @param resource The resource containing the details of the classroom to be created.
     * @return A {@link ResponseEntity} with the created classroom or an error response.
     */
    @PostMapping
    @Operation(summary = "Create a new classroom", description = "Create a new classroom")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Classroom created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Classroom not found")
    })
    public ResponseEntity<ClassroomResource> createClassroom(@RequestBody CreateClassroomResource resource) {
        var createClassroomCommand = CreateClassroomCommandFromResourceAssembler.toCommandFromResource(resource);
        var classroomId = classroomCommandService.handle(createClassroomCommand);
        if (classroomId == null || classroomId == 0L ) {
            return ResponseEntity.badRequest().build();
        }
        var getClassroomByIdQuery = new GetClassroomByIdQuery(classroomId);
        var classroom = classroomQueryService.handle(getClassroomByIdQuery);

        if (classroom.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var classroomEntity = classroom.get();
        var classroomResource = ClassroomResourceFromEntityAssembler.toResourceFromEntity(classroomEntity);
        return new ResponseEntity<>(classroomResource, HttpStatus.CREATED);
    }

    /**
     * Retrieves all classrooms.
     *
     * @return A {@link ResponseEntity} containing a list of classrooms or an error response.
     */
    @GetMapping
    @Operation(summary = "Get all classrooms", description = "Get all classrooms")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Classrooms found"),
            @ApiResponse(responseCode = "404", description = "No classrooms found")
    })
    public ResponseEntity<List<ClassroomResource>> getAllClassrooms() {
        var getAllClassroomsQuery = new GetAllClassroomsQuery();
        var classrooms = classroomQueryService.handle(getAllClassroomsQuery);
//        if (classrooms.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
        var classroomResources = classrooms.stream()
                .map(ClassroomResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(classroomResources);
    }

    /**
     * Retrieves a classroom by its ID.
     *
     * @param classroomId The ID of the classroom to retrieve.
     * @return A {@link ResponseEntity} containing the classroom resource or an error response.
     */
    @GetMapping("/{classroomId}")
    @Operation(summary = "Get classroom by ID", description = "Get a classroom by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Classroom found"),
            @ApiResponse(responseCode = "404", description = "Classroom not found")
    })
    public ResponseEntity<ClassroomResource> getClassroomById(@PathVariable Long classroomId) {
        var getClassroomByIdQuery = new GetClassroomByIdQuery(classroomId);
        var classroom = classroomQueryService.handle(getClassroomByIdQuery);
        if (classroom.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var classroomResource = ClassroomResourceFromEntityAssembler.toResourceFromEntity(classroom.get());
        return ResponseEntity.ok(classroomResource);
    }

    /**
     * Updates a classroom by its ID.
     *
     * @param classroomId The ID of the classroom to update.
     * @param resource The resource containing the updated classroom details.
     * @return A {@link ResponseEntity} containing the updated classroom or an error response.
     */
    @PutMapping("/{classroomId}")
    @Operation(summary = "Update a classroom by ID", description = "Update a classroom by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Classroom updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Classroom not found")
    })
    public ResponseEntity<ClassroomResource> updateClassroom(@PathVariable Long classroomId, @RequestBody UpdateClassroomResource resource) {
        var updateClassroomCommand = UpdateClassroomCommandFromResourceAssembler.toCommandFromResource(classroomId, resource);
        var updatedClassroom = classroomCommandService.handle(updateClassroomCommand);
        if (updatedClassroom.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var updatedClassroomEntity = updatedClassroom.get();
        var classroomResource = ClassroomResourceFromEntityAssembler.toResourceFromEntity(updatedClassroomEntity);
        return ResponseEntity.ok(classroomResource);
    }

    /**
     * Deletes a classroom by its ID.
     *
     * @param classroomId The ID of the classroom to delete.
     * @return A {@link ResponseEntity} indicating the deletion status.
     */
    @DeleteMapping("/{classroomId}")
    @Operation(summary = "Delete a classroom by ID", description = "Delete a classroom by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Classroom deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Classroom not found")
    })
    public ResponseEntity<?> deleteClassroom(@PathVariable Long classroomId) {
        var deleteClassroomCommand = new DeleteClassroomCommand(classroomId);
        classroomCommandService.handle(deleteClassroomCommand);
        return ResponseEntity.ok(new MessageResource("Classroom deleted successfully"));
    }
}

