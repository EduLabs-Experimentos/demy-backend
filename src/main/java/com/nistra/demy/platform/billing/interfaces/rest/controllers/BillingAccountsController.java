package com.nistra.demy.platform.billing.interfaces.rest.controllers;

import com.nistra.demy.platform.billing.domain.model.commands.DeleteInvoiceCommand;
import com.nistra.demy.platform.billing.domain.model.commands.MarkInvoiceAsPaidCommand;
import com.nistra.demy.platform.billing.domain.model.queries.*;
import com.nistra.demy.platform.billing.domain.services.BillingAccountCommandService;
import com.nistra.demy.platform.billing.domain.services.BillingAccountQueryService;
import com.nistra.demy.platform.billing.interfaces.rest.resources.*;
import com.nistra.demy.platform.billing.interfaces.rest.transform.*;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.StudentId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/billing-accounts", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Billing Accounts", description = "Endpoints for billing accounts")
public class BillingAccountsController {

    private final BillingAccountCommandService billingAccountCommandService;
    private final BillingAccountQueryService billingAccountQueryService;

    public  BillingAccountsController(
            BillingAccountCommandService billingAccountCommandService,
            BillingAccountQueryService billingAccountQueryService
    ) {
        this.billingAccountCommandService = billingAccountCommandService;
        this.billingAccountQueryService = billingAccountQueryService;
    }

    @Operation(
            summary = "Create a new billing account",
            description = "Registers a new billing account for a specific student and academy."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Billing account created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BillingAccountResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid billing account data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<BillingAccountResource> createBillingAccount(@RequestBody CreateBillingAccountResource resource) {
        var createBillingAccountCommand = CreateBillingAccountCommandFromResourceAssembler.toCommandFromResource(resource);
        var billingAccount = billingAccountCommandService.handle(createBillingAccountCommand);
        if (billingAccount.isEmpty()) return ResponseEntity.badRequest().build();
        var billingAccountEntity = billingAccount.get();
        var billingAccountResource = BillingAccountResourceFromEntityAssembler.toResourceFromEntity(billingAccountEntity);
        return new ResponseEntity<>(billingAccountResource, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Assign a new invoice to a billing account",
            description = "Adds an invoice to an existing billing account. Returns the updated billing account resource."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Invoice assigned successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BillingAccountResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid invoice data or billing account not found", content = @Content)
    })
    @PostMapping("/{billingAccountId}/invoices")
    public ResponseEntity<BillingAccountResource> assignInvoiceToBillingAccount(
            @Parameter(description = "Billing account identifier", example = "1")
            @PathVariable Long billingAccountId,
            @RequestBody AssignInvoiceResource resource
    ) {
        var assignInvoiceCommand = AssignInvoiceCommandFromResourceAssembler.toCommandFromResource(billingAccountId, resource);
        var billingAccount = billingAccountCommandService.handle(assignInvoiceCommand);
        if (billingAccount.isEmpty()) return ResponseEntity.badRequest().build();
        var billingAccountEntity = billingAccount.get();
        var billingAccountResource = BillingAccountResourceFromEntityAssembler.toResourceFromEntity(billingAccountEntity);
        return new ResponseEntity<>(billingAccountResource, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Mark an invoice as paid",
            description = "Marks a specific invoice as paid within the given billing account."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invoice marked as paid successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InvoiceResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid billing account or invoice ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "Invoice or billing account not found", content = @Content)
    })
    @PostMapping("/{billingAccountId}/invoices/{invoiceId}/mark-as-paid")
    public ResponseEntity<InvoiceResource> markInvoiceAsPaid(
            @Parameter(description = "Billing account identifier", example = "1")
            @PathVariable Long billingAccountId,
            @Parameter(description = "Invoice identifier", example = "5")
            @PathVariable Long invoiceId
    ) {
        var markInvoiceAsPaidCommand = new MarkInvoiceAsPaidCommand(billingAccountId, invoiceId);
        var invoice = billingAccountCommandService.handle(markInvoiceAsPaidCommand);
        if (invoice.isEmpty()) return ResponseEntity.badRequest().build();
        var invoiceEntity = invoice.get();
        var invoiceResource = InvoiceResourceFromEntityAssembler.toResourceFromEntity(invoiceEntity);
        return new ResponseEntity<>(invoiceResource, HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve billing account by ID",
            description = "Fetches a specific billing account using its identifier."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Billing account retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BillingAccountResource.class))),
            @ApiResponse(responseCode = "404", description = "Billing account not found", content = @Content)
    })
    @GetMapping("/{billingAccountId}")
    public ResponseEntity<BillingAccountResource> getBillingAccountById(
            @Parameter(description = "Billing account identifier", example = "1")
            @PathVariable Long billingAccountId
    ) {
        var getBillingAccountByIdQuery = new GetBillingAccountByIdQuery(billingAccountId);
        var billingAccount = billingAccountQueryService.handle(getBillingAccountByIdQuery);
        if (billingAccount.isEmpty()) return ResponseEntity.notFound().build();
        var billingAccountEntity = billingAccount.get();
        var billingAccountResource = BillingAccountResourceFromEntityAssembler.toResourceFromEntity(billingAccountEntity);
        return new ResponseEntity<>(billingAccountResource, HttpStatus.OK);
    }

    @Operation(
            summary = "List all billing accounts",
            description = "Retrieves all billing accounts registered in the system."
    )
    @ApiResponse(responseCode = "200", description = "List of billing accounts",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BillingAccountResource.class)))
    @GetMapping
    public ResponseEntity<List<BillingAccountResource>> getAllBillingAccounts() {
        var getAllBillingAccountsQuery = new GetAllBillingAccountsQuery();
        var billingAccounts = billingAccountQueryService.handle(getAllBillingAccountsQuery);
        if (billingAccounts.isEmpty()) return ResponseEntity.ok(List.of());
        var billingAccountResources = BillingAccountResourceFromEntityAssembler.toResourcesFromEntities(billingAccounts);
        return new ResponseEntity<>(billingAccountResources, HttpStatus.OK);
    }

    @Operation(
            summary = "List all invoices by billing account ID",
            description = "Retrieves all invoices associated with a specific billing account."
    )
    @ApiResponse(responseCode = "200", description = "List of invoices for the billing account",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = InvoiceResource.class)))
    @GetMapping("/{billingAccountId}/invoices")
    public ResponseEntity<List<InvoiceResource>> getAllInvoicesByAccountId(
            @Parameter(description = "Billing account identifier", example = "1")
            @PathVariable Long billingAccountId
    ) {
        var qetAllInvoicesByBillingAccountIdQuery = new GetAllInvoicesByBillingAccountIdQuery(billingAccountId);
        var invoices = billingAccountQueryService.handle(qetAllInvoicesByBillingAccountIdQuery);
        if (invoices.isEmpty()) return ResponseEntity.ok(List.of());
        var invoiceResources = InvoiceResourceFromEntityAssembler.toResourcesFromEntities(invoices);
        return new ResponseEntity<>(invoiceResources, HttpStatus.OK);
    }

    @Operation(
            summary = "List all invoices by student ID",
            description = "Retrieves all invoices across all billing accounts for a given student."
    )
    @ApiResponse(responseCode = "200", description = "List of invoices for the student",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = InvoiceResource.class)))
    @GetMapping("/invoices/by-student-id/{studentId}")
    public ResponseEntity<List<InvoiceResource>> getAllInvoicesByStudentId(
            @Parameter(description = "Student identifier", example = "10")
            @PathVariable Long studentId
    ) {
        var getAllInvoicesByStudentIdQuery = new GetAllInvoicesByStudentIdQuery(new StudentId(studentId));
        var invoices = billingAccountQueryService.handle(getAllInvoicesByStudentIdQuery);
        if (invoices.isEmpty()) return ResponseEntity.ok(List.of());
        var invoiceResources = InvoiceResourceFromEntityAssembler.toResourcesFromEntities(invoices);
        return new ResponseEntity<>(invoiceResources, HttpStatus.OK);
    }

    @Operation(
            summary = "List all invoices by student DNI",
            description = "Retrieves all invoices across all billing accounts for a given student."
    )
    @ApiResponse(responseCode = "200", description = "List of invoices for the student",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = InvoiceResource.class)))
    @GetMapping("/invoices/by-student-dni/{dniNumber}")
    public ResponseEntity<List<InvoiceResource>> getAllInvoicesByStudentDniNumber(
            @Parameter(description = "Student DNI", example = "70123456")
            @PathVariable String dniNumber
    ) {
        var getAllInvoicesByStudentDniNumberQuery = new GetAllInvoicesByStudentDniNumberQuery(new DniNumber(dniNumber));
        var invoices = billingAccountQueryService.handle(getAllInvoicesByStudentDniNumberQuery);
        if (invoices.isEmpty()) return ResponseEntity.ok(List.of());
        var invoiceResources = InvoiceResourceFromEntityAssembler.toResourcesFromEntities(invoices);
        return new ResponseEntity<>(invoiceResources, HttpStatus.OK);
    }

    @DeleteMapping("/{billingAccountId}/invoices/{invoiceId}")
    @Operation(
            summary = "Delete an invoice by ID",
            description = "Deletes an invoice from a billing account if it is not paid.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Invoice successfully deleted"),
                    @ApiResponse(responseCode = "400", description = "Cannot delete a paid invoice", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Invoice or account not found", content = @Content)
            }
    )
    public ResponseEntity<Void> deleteInvoice(
            @PathVariable Long billingAccountId,
            @PathVariable Long invoiceId) {

        billingAccountCommandService.handle(new DeleteInvoiceCommand(billingAccountId, invoiceId));
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update invoice details",
            description = "Updates non-date fields (type, amount, description, status) of a specific invoice.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Invoice updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = InvoiceResource.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid data or business rule violation (e.g., trying to update a paid invoice)", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Invoice or billing account not found", content = @Content)
            }
    )

    @PutMapping("/{billingAccountId}/invoices/{invoiceId}")
    public ResponseEntity<InvoiceResource> updateInvoice(
            @Parameter(description = "Billing account identifier", example = "1")
            @PathVariable Long billingAccountId,
            @Parameter(description = "Invoice identifier", example = "5")
            @PathVariable Long invoiceId,
            @RequestBody UpdateInvoiceResource resource
    ) {
        var command = UpdateInvoiceCommandFromResourceAssembler.toCommand(
                billingAccountId,
                invoiceId,
                resource
        );
        var updatedInvoice = billingAccountCommandService.handle(command)
                .orElseThrow(() -> new RuntimeException("Update failed: Invoice could not be retrieved."));
        var responseResource = InvoiceResourceFromEntityAssembler.toResourceFromEntity(updatedInvoice);
        return ResponseEntity.ok(responseResource);
    }
}