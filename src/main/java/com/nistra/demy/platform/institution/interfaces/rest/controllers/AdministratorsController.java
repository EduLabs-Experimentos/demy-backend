package com.nistra.demy.platform.institution.interfaces.rest.controllers;

import com.nistra.demy.platform.institution.domain.model.queries.GetAdministratorEmailAddressByUserIdQuery;
import com.nistra.demy.platform.institution.domain.model.queries.GetCurrentAdministratorQuery;
import com.nistra.demy.platform.institution.domain.services.AdministratorCommandService;
import com.nistra.demy.platform.institution.domain.services.AdministratorQueryService;
import com.nistra.demy.platform.institution.interfaces.rest.resources.AdministratorResource;
import com.nistra.demy.platform.institution.interfaces.rest.resources.CurrentAdministratorResource;
import com.nistra.demy.platform.institution.interfaces.rest.resources.RegisterAdministratorResource;
import com.nistra.demy.platform.institution.interfaces.rest.transform.AdministratorResourceFromEntityAssembler;
import com.nistra.demy.platform.institution.interfaces.rest.transform.CurrentAdministratorResourceFromEntityAssembler;
import com.nistra.demy.platform.institution.interfaces.rest.transform.RegisterAdministratorCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/administrators", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Administrators", description = "Endpoints for managing administrators")
public class AdministratorsController {

    private final AdministratorCommandService administratorCommandService;
    private final AdministratorQueryService administratorQueryService;

    public AdministratorsController(
            AdministratorCommandService administratorCommandService,
            AdministratorQueryService administratorQueryService
    ) {
        this.administratorCommandService = administratorCommandService;
        this.administratorQueryService = administratorQueryService;
    }

    @Operation(
            summary = "Register a new administrator",
            description = "Creates a new administrator in the system using the provided information."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Administrator created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdministratorResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid administrator registration data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AdministratorResource> registerAdministrator(@RequestBody RegisterAdministratorResource resource) {
        var registerAdministratorCommand = RegisterAdministratorCommandFromResourceAssembler.toCommandFromResource(resource);
        var administrator = administratorCommandService.handle(registerAdministratorCommand);
        if (administrator.isEmpty()) return ResponseEntity.badRequest().build();
        var administratorEntity = administrator.get();
        var administratorResource = AdministratorResourceFromEntityAssembler.toResourceFromEntity(administratorEntity);
        return new ResponseEntity<>(administratorResource, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get current administrator",
            description = "Retrieves the administrator entity associated with the currently authenticated user. " +
                    "The response also includes the administrator's email address."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Current administrator retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CurrentAdministratorResource.class))),
            @ApiResponse(responseCode = "404", description = "Administrator or associated email not found", content = @Content)
    })
    @GetMapping("/me")
    public ResponseEntity<CurrentAdministratorResource> getCurrentAdministrator() {
        var getCurrentAdministratorQuery = new GetCurrentAdministratorQuery();
        var administrator = administratorQueryService.handle(getCurrentAdministratorQuery);
        if (administrator.isEmpty()) return ResponseEntity.notFound().build();
        var administratorEntity = administrator.get();
        var getAdministratorEmailAddressByUserIdQuery = new GetAdministratorEmailAddressByUserIdQuery(administratorEntity.getUserId());
        var emailAddress = administratorQueryService.handle(getAdministratorEmailAddressByUserIdQuery);
        if (emailAddress.isEmpty()) return ResponseEntity.notFound().build();
        var emailAddressValueObject = emailAddress.get();
        var currentAdministratorResource = CurrentAdministratorResourceFromEntityAssembler.toResourceFromEntity(administratorEntity, emailAddressValueObject);
        return new ResponseEntity<>(currentAdministratorResource, HttpStatus.OK);
    }
}
