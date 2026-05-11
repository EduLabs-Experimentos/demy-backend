# ============================================================
# Feature: Billing Account Management
# Bounded Context: Billing
# ============================================================
@billing
Feature: Manage billing accounts and invoices for enrolled students

  Background:
    Given a billing account exists for student with DNI "12345678" in academy with ID 1

  # ----------------------------------------------------------
  # Scenario 1: Assign invoice successfully
  # ----------------------------------------------------------
  @happy-path
  Scenario: Assign a new invoice to a billing account
    When the admin assigns an invoice of type "STUDENT_MONTHLY_FEE" with amount 150.00 and description "Monthly fee May 2026"
    Then the billing account should have 1 invoice with status "PENDING"

  # ----------------------------------------------------------
  # Scenario 2: Mark invoice as paid
  # ----------------------------------------------------------
  @happy-path
  Scenario: Mark an existing invoice as paid
    Given the billing account has a PENDING invoice with description "Enrollment fee"
    When the admin marks the invoice as paid
    Then the invoice status should be "PAID"

  # ----------------------------------------------------------
  # Scenario 3: Delete a PENDING invoice
  # ----------------------------------------------------------
  @happy-path
  Scenario: Delete a pending invoice from a billing account
    Given the billing account has a PENDING invoice with description "One-time payment"
    When the admin deletes the invoice
    Then the billing account should have 0 invoices

  # ----------------------------------------------------------
  # Scenario 4: Cannot delete a PAID invoice
  # ----------------------------------------------------------
  @error-path
  Scenario: Attempt to delete a paid invoice
    Given the billing account has a PAID invoice with description "Already paid fee"
    When the admin deletes the invoice
    Then an error should be raised indicating the invoice cannot be deleted
