package com.nistra.demy.platform.institution.interfaces.rest.controllers;

import com.nistra.demy.platform.institution.domain.model.queries.ExistsAcademyByIdQuery;
import com.nistra.demy.platform.institution.domain.model.queries.GetCurrentAcademyQuery;
import com.nistra.demy.platform.institution.domain.services.AcademyCommandService;
import com.nistra.demy.platform.institution.domain.services.AcademyQueryService;
import com.nistra.demy.platform.institution.interfaces.rest.resources.AcademyResource;
import com.nistra.demy.platform.institution.interfaces.rest.resources.CurrentAcademyResource;
import com.nistra.demy.platform.institution.interfaces.rest.resources.RegisterAcademyResource;
import com.nistra.demy.platform.institution.interfaces.rest.transform.AcademyResourceFromEntityAssembler;
import com.nistra.demy.platform.institution.interfaces.rest.transform.CurrentAcademyResourceFromEntityAssembler;
import com.nistra.demy.platform.institution.interfaces.rest.transform.RegisterAcademyCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/academies", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Academies", description = "Endpoints for managing academies")
public class AcademiesController {

    private final AcademyCommandService academyCommandService;
    private final AcademyQueryService academyQueryService;

    public AcademiesController(
            AcademyCommandService academyCommandService,
            AcademyQueryService academyQueryService
    ) {
        this.academyCommandService = academyCommandService;
        this.academyQueryService = academyQueryService;
    }

    @Operation(
            summary = "Register a new academy",
            description = "Creates a new academy in the system using the given data.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Academy successfully created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AcademyResource.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @PostMapping
    public ResponseEntity<AcademyResource> registerAcademy(@RequestBody RegisterAcademyResource resource) {
        var registerAcademyCommand = RegisterAcademyCommandFromResourceAssembler.toCommandFromResource(resource);
        var academy = academyCommandService.handle(registerAcademyCommand);
        if (academy.isEmpty()) return ResponseEntity.badRequest().build();
        var academyEntity = academy.get();
        var academyResource = AcademyResourceFromEntityAssembler.toResourceFromEntity(academyEntity);
        return new ResponseEntity<>(academyResource, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get current academy",
            description = "Retrieves the academy linked to the currently authenticated administrator. " +
                    "This endpoint returns the institution context (academy) associated with the user " +
                    "based on the authentication token or session context.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Current academy retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CurrentAcademyResource.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No academy is associated with the current user or could not be found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/current")
    public ResponseEntity<CurrentAcademyResource> getCurrentAcademy() {
        var getCurrentAcademyQuery = new GetCurrentAcademyQuery();
        var academy = academyQueryService.handle(getCurrentAcademyQuery);
        if (academy.isEmpty()) return ResponseEntity.notFound().build();
        var academyEntity = academy.get();
        var currentAcademyResource = CurrentAcademyResourceFromEntityAssembler.toResourceFromEntity(academyEntity);
        return new ResponseEntity<>(currentAcademyResource, HttpStatus.OK);
    }

    @Operation(
            summary = "Check if an academy exists by ID",
            description = "Uses the HEAD method to verify if an academy exists without returning the entity.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Academy exists"),
                    @ApiResponse(responseCode = "404", description = "Academy not found")
            }
    )
    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> checkAcademyExists(@PathVariable Long id) {
        boolean exists = academyQueryService.handle(new ExistsAcademyByIdQuery(id));
        return exists ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Handle CORS preflight requests",
            description = "Exposes allowed HTTP methods and headers for the /academies resource.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "CORS configuration returned successfully")
            }
    )
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Allow", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}
