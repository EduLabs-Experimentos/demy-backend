package com.nistra.demy.platform.accountingfinance.interfaces.rest.controllers;

import com.nistra.demy.platform.accountingfinance.domain.model.commands.DeleteTransactionCommand;
import com.nistra.demy.platform.accountingfinance.domain.model.queries.GetAllTransactionsQuery;
import com.nistra.demy.platform.accountingfinance.domain.model.queries.GetTransactionByIdQuery;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionCategory;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionMethod;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionType;
import com.nistra.demy.platform.accountingfinance.domain.services.TransactionCommandService;
import com.nistra.demy.platform.accountingfinance.domain.services.TransactionQueryService;
import com.nistra.demy.platform.accountingfinance.interfaces.rest.resources.*;
import com.nistra.demy.platform.accountingfinance.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping(value = "/api/v1/transactions", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Transactions", description = "Endpoints for managing financial transactions")
public class TransactionsController {

    private final TransactionCommandService transactionCommandService;
    private final TransactionQueryService transactionQueryService;

    public TransactionsController(
            TransactionCommandService transactionCommandService,
            TransactionQueryService transactionQueryService
    ) {
        this.transactionCommandService = transactionCommandService;
        this.transactionQueryService = transactionQueryService;
    }

    @PostMapping
    @Operation(
            summary = "Register a Transaction",
            description = "Creates a new financial transaction in the system. The transaction must include type, category, method, amount, description, and date."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid transaction data provided")
    })
    public ResponseEntity<TransactionResource> registerTransaction(@RequestBody RegisterTransactionResource resource) {
        var registerTransactionCommand = RegisterTransactionCommandFromResourceAssembler.toCommandFromResource(resource);
        var transaction = transactionCommandService.handle(registerTransactionCommand);
        if (transaction.isEmpty()) return ResponseEntity.badRequest().build();
        var transactionEntity = transaction.get();
        var transactionResource = TransactionResourceFromEntityAssembler.toResourceFromEntity(transactionEntity);
        return new ResponseEntity<>(transactionResource, HttpStatus.CREATED);
    }

    @PutMapping("/{transactionId}")
    @Operation(
            summary = "Update a Transaction",
            description = "Updates an existing transaction by its ID. Only transactions belonging to the current academy can be updated."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid transaction data provided"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    public ResponseEntity<TransactionResource> updateTransaction(
            @Parameter(description = "ID of the transaction to update")
            @PathVariable Long transactionId,
            @RequestBody UpdateTransactionResource resource
    ) {
        var updateTransactionCommand = UpdateTransactionCommandFromResourceAssembler.toCommandFromResource(transactionId, resource);
        var transaction = transactionCommandService.handle(updateTransactionCommand);
        if (transaction.isEmpty()) return ResponseEntity.badRequest().build();
        var transactionEntity = transaction.get();
        var transactionResource = TransactionResourceFromEntityAssembler.toResourceFromEntity(transactionEntity);
        return ResponseEntity.ok(transactionResource);
    }

    @GetMapping("/{transactionId}")
    @Operation(
            summary = "Get Transaction by ID",
            description = "Retrieves a specific transaction by its ID. Only transactions belonging to the current academy can be accessed."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction found and returned"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    public ResponseEntity<TransactionResource> getTransactionById(
            @Parameter(description = "ID of the transaction to retrieve")
            @PathVariable Long transactionId
    ) {
        var getTransactionByIdQuery = new GetTransactionByIdQuery(transactionId);
        var transaction = transactionQueryService.handle(getTransactionByIdQuery);
        if (transaction.isEmpty()) return ResponseEntity.notFound().build();
        var transactionResource = TransactionResourceFromEntityAssembler.toResourceFromEntity(transaction.get());
        return ResponseEntity.ok(transactionResource);
    }

    @GetMapping
    @Operation(
            summary = "Get All Transactions",
            description = "Retrieves all transactions registered in the system. " +
                    "Optional query parameters can be used to filter the results by category, method, or type."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of transactions returned successfully")
    })
    public ResponseEntity<List<TransactionResource>> getAllTransactions(
            @Parameter(
                    description = "Filter by transaction category (e.g., STUDENT_ENROLLMENT, TEACHER_SALARY)",
                    schema = @Schema(implementation = TransactionCategory.class)
            )
            @RequestParam(required = false) String category,
            @Parameter(
                    description = "Filter by transaction method (e.g., CASH, CREDIT_CARD)",
                    schema = @Schema(implementation = TransactionMethod.class)
            )
            @RequestParam(required = false) String method,
            @Parameter(
                    description = "Filter by transaction type (e.g., INCOME, EXPENSE)",
                    schema = @Schema(implementation = TransactionType.class)
            )
            @RequestParam(required = false) String type
    ) {
        var getAllTransactionsQuery = new GetAllTransactionsQuery(
                TransactionCategoryFromResourceAssembler.toValueObjectFromString(category),
                TransactionMethodResourceAssembler.toValueObjectFromString(method),
                TransactionTypeFromResourceAssembler.toValueObjectFromString(type)
        );
        var transactions = transactionQueryService.handle(getAllTransactionsQuery);
        if (transactions.isEmpty()) return ResponseEntity.ok().body(List.of());
        var transactionResources = TransactionResourceFromEntityAssembler.toResourcesFromEntities(transactions);
        return new ResponseEntity<>(transactionResources, HttpStatus.OK);
    }

    @DeleteMapping("/{transactionId}")
    @Operation(
            summary = "Delete a Transaction",
            description = "Deletes an existing transaction by its ID. Only transactions belonging to the current academy can be deleted."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transaction successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    public ResponseEntity<Void> deleteTransaction(
            @Parameter(description = "ID of the transaction to delete")
            @PathVariable Long transactionId
    ) {
        var deleteTransactionCommand = new DeleteTransactionCommand(transactionId);
        transactionCommandService.handle(deleteTransactionCommand);
        return ResponseEntity.noContent().build();
    }
}

