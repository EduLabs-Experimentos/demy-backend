// BillingAccountSteps.java
package com.nistra.demy.platform.acceptance.test.steps;

import com.nistra.demy.platform.billing.domain.model.aggregates.BillingAccount;
import com.nistra.demy.platform.billing.domain.model.commands.AssignInvoiceToBillingAccountCommand;
import com.nistra.demy.platform.billing.domain.model.commands.CreateBillingAccountCommand;
import com.nistra.demy.platform.billing.domain.model.entities.Invoice;
import com.nistra.demy.platform.billing.domain.model.valueobjects.InvoiceStatus;
import com.nistra.demy.platform.billing.domain.model.valueobjects.InvoiceType;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;
import com.nistra.demy.platform.shared.domain.model.valueobjects.StudentId;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BillingAccountSteps {

    @Autowired
    private BillingTestService billingTestService;

    private BillingAccount currentAccount;
    private Long           lastInvoiceId;
    private Exception      capturedException;

    @Before
    public void resetState() {
        currentAccount    = null;
        lastInvoiceId     = null;
        capturedException = null;
        billingTestService.deleteAll();
    }

    @Given("a billing account exists for student with DNI {string} in academy with ID {long}")
    public void aBillingAccountExistsForStudentWithDniInAcademy(String dniStr, long academyId) {
        CreateBillingAccountCommand cmd = new CreateBillingAccountCommand(
                new StudentId(1L), new DniNumber(dniStr)
        );
        currentAccount = billingTestService.createAccount(cmd, new AcademyId(academyId));
    }

    @Given("the billing account has a PENDING invoice with description {string}")
    public void theBillingAccountHasAPendingInvoiceWithDescription(String description) {
        currentAccount = billingTestService.assignPendingInvoice(
                currentAccount.getId(),
                buildCmd(InvoiceStatus.PENDING, description, currentAccount.getId())
        );
        lastInvoiceId = currentAccount.getInvoices().get(0).getId();
    }

    @Given("the billing account has a PAID invoice with description {string}")
    public void theBillingAccountHasAPaidInvoiceWithDescription(String description) {
        // First assign as PENDING
        currentAccount = billingTestService.assignPendingInvoice(
                currentAccount.getId(),
                buildCmd(InvoiceStatus.PENDING, description, currentAccount.getId())
        );
        lastInvoiceId = currentAccount.getInvoices().get(0).getId();

        // Then set PAID directly via JPQL (bypasses InvoicePaidEventHandler)
        currentAccount = billingTestService.markInvoiceAsPaidDirectly(
                currentAccount.getId(), lastInvoiceId
        );
    }

    @When("the admin assigns an invoice of type {string} with amount {double} and description {string}")
    public void theAdminAssignsAnInvoiceOfTypeWithAmountAndDescription(
            String type, double amount, String description) {
        AssignInvoiceToBillingAccountCommand cmd = buildCmd(
                InvoiceStatus.PENDING, description, currentAccount.getId(),
                InvoiceType.valueOf(type),
                new Money(BigDecimal.valueOf(amount), java.util.Currency.getInstance("PEN"))
        );
        currentAccount = billingTestService.assignInvoice(currentAccount.getId(), cmd);
        lastInvoiceId  = currentAccount.getInvoices().get(0).getId();
    }

    @When("the admin marks the invoice as paid")
    public void theAdminMarksTheInvoiceAsPaid() {
        try {
            currentAccount = billingTestService.markInvoiceAsPaidDirectly(
                    currentAccount.getId(), lastInvoiceId
            );
        } catch (Exception e) {
            capturedException = e;
        }
    }

    @When("the admin deletes the invoice")
    public void theAdminDeletesTheInvoice() {
        try {
            currentAccount = billingTestService.deleteInvoice(
                    currentAccount.getId(), lastInvoiceId
            );
        } catch (Exception e) {
            capturedException = e;
        }
    }

    @Then("the billing account should have {int} invoice with status {string}")
    public void theBillingAccountShouldHaveInvoiceWithStatus(int count, String expectedStatus) {
        BillingAccount reloaded = billingTestService.reloadWithInvoices(currentAccount.getId());
        List<Invoice> invoices = reloaded.getInvoices();
        assertThat(invoices).hasSize(count);
        assertThat(invoices.get(0).getStatus())
                .isEqualTo(InvoiceStatus.valueOf(expectedStatus));
    }

    @Then("the billing account should have {int} invoices")
    public void theBillingAccountShouldHaveInvoices(int count) {
        BillingAccount reloaded = billingTestService.reloadWithInvoices(currentAccount.getId());
        assertThat(reloaded.getInvoices()).hasSize(count);
    }

    @Then("the invoice status should be {string}")
    public void theInvoiceStatusShouldBe(String expectedStatus) {
        BillingAccount reloaded = billingTestService.reloadWithInvoices(currentAccount.getId());
        Invoice invoice = reloaded.findInvoiceById(lastInvoiceId);
        assertThat(invoice.getStatus()).isEqualTo(InvoiceStatus.valueOf(expectedStatus));
    }

    @Then("an error should be raised indicating the invoice cannot be deleted")
    public void anErrorShouldBeRaisedIndicatingTheInvoiceCannotBeDeleted() {
        assertThat(capturedException)
                .isNotNull()
                .isInstanceOf(IllegalStateException.class);
        assertThat(capturedException.getMessage())
                .containsIgnoringCase("Cannot delete a paid invoice");
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private AssignInvoiceToBillingAccountCommand buildCmd(
            InvoiceStatus status, String description, Long accountId) {
        return buildCmd(status, description, accountId,
                InvoiceType.STUDENT_MONTHLY_FEE,
                new Money(new BigDecimal("150.00"), java.util.Currency.getInstance("PEN")));
    }

    private AssignInvoiceToBillingAccountCommand buildCmd(
            InvoiceStatus status, String description, Long accountId,
            InvoiceType type, Money amount) {
        return new AssignInvoiceToBillingAccountCommand(
                type, amount, description,
                LocalDate.now(), LocalDate.now().plusDays(30),
                status, accountId
        );
    }
}