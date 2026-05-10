package com.nistra.demy.platform.accountingfinance.interfaces.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;
import com.nistra.demy.platform.accountingfinance.domain.model.commands.DeleteTransactionCommand;
import com.nistra.demy.platform.accountingfinance.domain.model.commands.RegisterTransactionCommand;
import com.nistra.demy.platform.accountingfinance.domain.model.commands.UpdateTransactionCommand;
import com.nistra.demy.platform.accountingfinance.domain.model.queries.GetAllTransactionsQuery;
import com.nistra.demy.platform.accountingfinance.domain.model.queries.GetTransactionByIdQuery;
import com.nistra.demy.platform.accountingfinance.domain.services.TransactionCommandService;
import com.nistra.demy.platform.accountingfinance.domain.services.TransactionQueryService;
import com.nistra.demy.platform.accountingfinance.interfaces.rest.resources.RegisterTransactionResource;
import com.nistra.demy.platform.accountingfinance.interfaces.rest.resources.UpdateTransactionResource;
import com.nistra.demy.platform.shared.application.internal.outboundservices.localization.LocalizationService;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TransactionsController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
                org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration.class
        })
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class TransactionsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TransactionCommandService transactionCommandService;

    @MockitoBean
    private TransactionQueryService transactionQueryService;

    @MockitoBean
    private LocalizationService localizationService;

    @MockitoBean
    private org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMetamodelMappingContext;

    private static final Long TRANSACTION_ID = 1L;
    private static final Long ACADEMY_ID = 5L;

    private Transaction sampleTransaction;

    @BeforeEach
    void setUp() {
        RegisterTransactionCommand command = new RegisterTransactionCommand(
                "income",
                "student enrollment",
                "cash",
                new Money(new BigDecimal("150.00"), Currency.getInstance("PEN")),
                "Pago de matricula",
                LocalDate.of(2026, 5, 9)
        );
        sampleTransaction = new Transaction(command, new AcademyId(ACADEMY_ID));
    }

    @Test
    @DisplayName("TS050 - POST /transactions con datos validos retorna 201 Created")
    void registerTransaction_ValidRequest_Returns201() throws Exception {
        // Arrange
        RegisterTransactionResource resource = new RegisterTransactionResource(
                "income",
                "student enrollment",
                "cash",
                new BigDecimal("150.00"),
                "PEN",
                "Pago de matricula",
                LocalDate.of(2026, 5, 9)
        );
        when(transactionCommandService.handle(any(RegisterTransactionCommand.class)))
                .thenReturn(Optional.of(sampleTransaction));

        // Act & Assert
        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionType").value("INCOME"))
                .andExpect(jsonPath("$.transactionCategory").value("STUDENT_ENROLLMENT"))
                .andExpect(jsonPath("$.transactionMethod").value("CASH"))
                .andExpect(jsonPath("$.amount").value(150.00))
                .andExpect(jsonPath("$.currency").value("PEN"));

        verify(transactionCommandService, times(1)).handle(any(RegisterTransactionCommand.class));
    }

    @Test
    @DisplayName("TS054 - GET /transactions/{id} con ID existente retorna 200 OK")
    void getTransactionById_ExistingId_Returns200() throws Exception {
        // Arrange
        when(transactionQueryService.handle(any(GetTransactionByIdQuery.class)))
                .thenReturn(Optional.of(sampleTransaction));

        // Act & Assert
        mockMvc.perform(get("/api/v1/transactions/{id}", TRANSACTION_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionType").value("INCOME"))
                .andExpect(jsonPath("$.transactionCategory").value("STUDENT_ENROLLMENT"))
                .andExpect(jsonPath("$.currency").value("PEN"));
    }

    @Test
    @DisplayName("TS054 - GET /transactions/{id} con ID inexistente retorna 404 Not Found")
    void getTransactionById_NonExistingId_Returns404() throws Exception {
        // Arrange
        when(transactionQueryService.handle(any(GetTransactionByIdQuery.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/transactions/{id}", 9999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("TS053 - GET /transactions retorna lista de movimientos contables")
    void getAllTransactions_ReturnsTransactionList() throws Exception {
        // Arrange
        when(transactionQueryService.handle(any(GetAllTransactionsQuery.class)))
                .thenReturn(List.of(sampleTransaction));

        // Act & Assert
        mockMvc.perform(get("/api/v1/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].transactionType").value("INCOME"));
    }

    @Test
    @DisplayName("TS051 - PUT /transactions/{id} con datos validos retorna 200 OK")
    void updateTransaction_ValidRequest_Returns200() throws Exception {
        // Arrange
        UpdateTransactionResource resource = new UpdateTransactionResource(
                "expense",
                "office supplies",
                "debit card",
                new BigDecimal("80.00"),
                "PEN",
                "Compra de utiles de oficina",
                LocalDate.of(2026, 5, 10)
        );
        sampleTransaction.updateTransaction(new UpdateTransactionCommand(
                TRANSACTION_ID,
                "expense",
                "office supplies",
                "debit card",
                new Money(new BigDecimal("80.00"), Currency.getInstance("PEN")),
                "Compra de utiles de oficina",
                LocalDate.of(2026, 5, 10)
        ));
        when(transactionCommandService.handle(any(UpdateTransactionCommand.class)))
                .thenReturn(Optional.of(sampleTransaction));

        // Act & Assert
        mockMvc.perform(put("/api/v1/transactions/{id}", TRANSACTION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionType").value("EXPENSE"))
                .andExpect(jsonPath("$.transactionCategory").value("OFFICE_SUPPLIES"))
                .andExpect(jsonPath("$.transactionMethod").value("DEBIT_CARD"))
                .andExpect(jsonPath("$.amount").value(80.00));
    }
}