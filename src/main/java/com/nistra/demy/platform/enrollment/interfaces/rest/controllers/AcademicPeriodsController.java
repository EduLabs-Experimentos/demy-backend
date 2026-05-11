package com.nistra.demy.platform.enrollment.interfaces.rest.controllers;

import com.nistra.demy.platform.enrollment.domain.model.commands.DeleteAcademicPeriodCommand;
import com.nistra.demy.platform.enrollment.domain.model.queries.GetAcademicPeriodByIdQuery;
import com.nistra.demy.platform.enrollment.domain.model.queries.GetAllAcademicPeriodsQuery;
import com.nistra.demy.platform.enrollment.domain.services.AcademicPeriodCommandService;
import com.nistra.demy.platform.enrollment.domain.services.AcademicPeriodQueryService;
import com.nistra.demy.platform.enrollment.interfaces.rest.resources.AcademicPeriodResource;
import com.nistra.demy.platform.enrollment.interfaces.rest.resources.CreateAcademicPeriodResource;
import com.nistra.demy.platform.enrollment.interfaces.rest.resources.UpdateAcademicPeriodResource;
import com.nistra.demy.platform.enrollment.interfaces.rest.transform.AcademicPeriodResourceFromEntityAssembler;
import com.nistra.demy.platform.enrollment.interfaces.rest.transform.CreateAcademicPeriodCommandFromResourceAssembler;
import com.nistra.demy.platform.enrollment.interfaces.rest.transform.UpdateAcademicPeriodCommandFromResourceAssembler;
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
 * REST Controller for managing academic periods.
 * Provides endpoints to create, retrieve, update, and delete academic periods.
 */
@RestController
@RequestMapping(value = "/api/v1/academic-periods", produces = APPLICATION_JSON_VALUE)
@Tag(name = "AcademicPeriods", description = "Available Academic Periods Endpoints")
public class AcademicPeriodsController {

    private final AcademicPeriodCommandService academicPeriodCommandService;
    private final AcademicPeriodQueryService academicPeriodQueryService;

    /**
     * Creates the controller with the required services injected.
     *
     * @param academicPeriodCommandService service for handling commands (create, update, delete)
     * @param academicPeriodQueryService service for handling queries (get)
     */
    public AcademicPeriodsController(
            AcademicPeriodCommandService academicPeriodCommandService,
            AcademicPeriodQueryService academicPeriodQueryService
    ) {
        this.academicPeriodCommandService = academicPeriodCommandService;
        this.academicPeriodQueryService = academicPeriodQueryService;
    }

    /**
     * Creates a new academic period.
     *
     * @param resource the request body containing data to create the academic period
     * @return ResponseEntity with the created academic period or an error status if invalid
     */
    @PostMapping
    @Operation(summary = "Create a new academic period", description = "Create a new academic period")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Academic period created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Academic period not found")
    })
    public ResponseEntity<AcademicPeriodResource> createAcademicPeriod(@RequestBody CreateAcademicPeriodResource resource) {
        var createAcademicPeriodCommand = CreateAcademicPeriodCommandFromResourceAssembler.toCommandFromResource(resource);
        var academicPeriodId = academicPeriodCommandService.handle(createAcademicPeriodCommand);
        if (academicPeriodId == null || academicPeriodId == 0L) return ResponseEntity.badRequest().build();

        var getAcademicPeriodById = new GetAcademicPeriodByIdQuery(academicPeriodId);
        var academicPeriod = academicPeriodQueryService.handle(getAcademicPeriodById);
        if (academicPeriod.isEmpty()) return ResponseEntity.notFound().build();

        var academicPeriodEntity = academicPeriod.get();
        var academicPeriodResource = AcademicPeriodResourceFromEntityAssembler.toResourceFromEntity(academicPeriodEntity);
        return new ResponseEntity<>(academicPeriodResource, HttpStatus.CREATED);
    }

    /**
     * Retrieves all registered academic periods.
     *
     * @return a list of academic period resources
     */
    @GetMapping
    @Operation(summary = "Get all academic periods", description = "Get all academic periods")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Academic periods found"),
            @ApiResponse(responseCode = "404", description = "No academic periods found")
    })
    public ResponseEntity<List<AcademicPeriodResource>> getAllAcademicPeriods() {
        var academicPeriods = academicPeriodQueryService.handle(new GetAllAcademicPeriodsQuery());
        // if (academicPeriods.isEmpty()) return ResponseEntity.notFound().build();

        var academicPeriodResources = academicPeriods.stream()
                .map(AcademicPeriodResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(academicPeriodResources);
    }

    /**
     * Updates an existing academic period by its ID.
     *
     * @param academicPeriodId the ID of the academic period to update
     * @param resource the request body containing updated data
     * @return ResponseEntity with the updated academic period or not found status if it does not exist
     */
    @PutMapping("/{academicPeriodId}")
    @Operation(summary = "Update academic period", description = "Update academic period")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Academic period updated"),
            @ApiResponse(responseCode = "404", description = "Academic period not found")
    })
    public ResponseEntity<AcademicPeriodResource> updateAcademicPeriod(
            @PathVariable Long academicPeriodId,
            @RequestBody UpdateAcademicPeriodResource resource
    ) {
        var updateAcademicPeriodCommand = UpdateAcademicPeriodCommandFromResourceAssembler.toCommandFromResource(academicPeriodId, resource);
        var updatedAcademicPeriod = academicPeriodCommandService.handle(updateAcademicPeriodCommand);

        if (updatedAcademicPeriod.isEmpty()) return ResponseEntity.notFound().build();

        var updatedAcademicPeriodEntity = updatedAcademicPeriod.get();
        var updatedAcademicPeriodResource = AcademicPeriodResourceFromEntityAssembler.toResourceFromEntity(updatedAcademicPeriodEntity);
        return ResponseEntity.ok(updatedAcademicPeriodResource);
    }

    /**
     * Deletes an academic period by its ID.
     *
     * @param academicPeriodId the ID of the academic period to delete
     * @return a message indicating the academic period was deleted
     */
    @DeleteMapping("/{academicPeriodId}")
    @Operation(summary = "Delete academic period", description = "Delete academic period")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Academic period deleted"),
            @ApiResponse(responseCode = "404", description = "Academic period not found")
    })
    public ResponseEntity<?> deleteAcademicPeriod(@PathVariable Long academicPeriodId) {
        var deleteAcademicPeriodCommand = new DeleteAcademicPeriodCommand(academicPeriodId);
        academicPeriodCommandService.handle(deleteAcademicPeriodCommand);
        return ResponseEntity.ok(new MessageResource("Academic period deleted successfully"));
    }
}
