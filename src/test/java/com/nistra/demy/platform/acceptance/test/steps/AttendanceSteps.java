package com.nistra.demy.platform.acceptance.test.steps;


import com.nistra.demy.platform.attendance.domain.model.aggregates.ClassAttendance;
import com.nistra.demy.platform.attendance.domain.model.commands.CreateClassAttendanceCommand;
import com.nistra.demy.platform.attendance.domain.model.entities.AttendanceRecord;
import com.nistra.demy.platform.attendance.domain.model.valueobjects.AttendanceInput;
import com.nistra.demy.platform.attendance.domain.model.valueobjects.AttendanceStatus;
import com.nistra.demy.platform.attendance.domain.model.valueobjects.ClassSessionId;
import com.nistra.demy.platform.attendance.infrastructure.persistence.jpa.repositories.ClassAttendanceRepository;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Cucumber Step Definitions for the "Mark Student Attendance" feature.
 *
 * <p>Uses {@code @SpringBootTest} so the real JPA repository (H2 in test
 * profile) is injected, giving full integration coverage through BDD
 * scenarios.  All domain objects use the actual Value Objects
 * ({@link DniNumber}, {@link AcademyId}, {@link ClassSessionId}) instead
 * of raw primitives.</p>
 */
@SpringBootTest
public class AttendanceSteps {

    // ── Injected dependencies ──────────────────────────────────────────────

    @Autowired
    private ClassAttendanceRepository classAttendanceRepository;

    // ── Per-scenario mutable state ─────────────────────────────────────────

    /** The aggregate under test for the current scenario. */
    private ClassAttendance currentAttendance;

    /** Exception captured in a failing When step, asserted in Then steps. */
    private Exception capturedException;

    // ── Lifecycle ──────────────────────────────────────────────────────────

    @Before
    public void resetState() {
        capturedException = null;
        currentAttendance = null;
        classAttendanceRepository.deleteAll();
    }

    // ── Background steps ───────────────────────────────────────────────────

    @Given("an academy with ID {long} and a class session with ID {long} exists for today's date")
    public void anAcademyWithIdAndAClassSessionWithIdExistsForTodaysDate(long academyId,
                                                                         long sessionId) {
        // Stored temporarily; the aggregate is built once the enrollment table arrives.
        // We keep this as a two-step Background to stay close to the Gherkin narrative.
        // The actual save happens in the next step once we have at least one record
        // (CreateClassAttendanceCommand rejects an empty list).
        currentAttendance = null; // reset; will be built in the And step below
        // Stash IDs so the And step can use them
        this.pendingAcademyId  = new AcademyId(academyId);
        this.pendingSessionId  = new ClassSessionId(sessionId);
    }

    // Transient state used between the two Background steps
    private AcademyId      pendingAcademyId;
    private ClassSessionId pendingSessionId;

    @And("the following students are enrolled in the session:")
    public void theFollowingStudentsAreEnrolledInTheSession(DataTable dataTable) {
        // Arrange: map each DataTable row to an AttendanceInput Value Object
        List<AttendanceInput> inputs = dataTable.asMaps().stream()
                .map(row -> new AttendanceInput(
                        new DniNumber(row.get("dni")),
                        AttendanceStatus.valueOf(row.get("initialStatus"))
                ))
                .toList();

        CreateClassAttendanceCommand command = new CreateClassAttendanceCommand(
                pendingSessionId,
                LocalDate.now(),
                inputs
        );

        currentAttendance = classAttendanceRepository.save(
                new ClassAttendance(pendingAcademyId, command)
        );
    }

    // ── Given steps ────────────────────────────────────────────────────────

    @Given("the student with DNI {string} has status {string}")
    public void theStudentWithDniHasStatus(String dniStr, String status) {
        // Assert pre-condition: record exists with the expected status
        DniNumber dni = new DniNumber(dniStr);
        AttendanceRecord record = currentAttendance.getRecordByDniOrThrow(dni);
        assertThat(record.getStatus()).isEqualTo(AttendanceStatus.valueOf(status));
    }

    @Given("the student with DNI {string} is not enrolled in the session")
    public void theStudentWithDniIsNotEnrolledInTheSession(String dniStr) {
        // Assert pre-condition: NO record with this DNI exists in the aggregate
        DniNumber dni = new DniNumber(dniStr);
        boolean enrolled = currentAttendance.getAttendance().stream()
                .anyMatch(r -> r.getDni().equals(dni));
        assertThat(enrolled)
                .as("Student with DNI %s should NOT be enrolled", dniStr)
                .isFalse();
    }

    // ── When steps ─────────────────────────────────────────────────────────

    @When("the teacher marks the student with DNI {string} as {string}")
    public void theTeacherMarksTheStudentWithDniAs(String dniStr, String status) {
        try {
            DniNumber dni = new DniNumber(dniStr);

            // Act: domain method under test
            currentAttendance.updateRecordStatus(dni, AttendanceStatus.valueOf(status));
            currentAttendance = classAttendanceRepository.save(currentAttendance);

        } catch (Exception e) {
            // Capture for error-path Then assertions (avoids try/catch crossing step boundaries)
            capturedException = e;
        }
    }

    // ── Then steps ─────────────────────────────────────────────────────────

    @Then("the attendance record for DNI {string} should have status {string}")
    public void theAttendanceRecordForDniShouldHaveStatus(String dniStr, String expectedStatus) {
        // Reload from DB to confirm persistence (not just in-memory state)
        ClassAttendance reloaded = classAttendanceRepository
                .findByIdAndAcademyId(currentAttendance.getId(), pendingAcademyId)
                .orElseThrow(() -> new AssertionError("ClassAttendance not found in DB"));

        DniNumber dni = new DniNumber(dniStr);
        AttendanceStatus actual = reloaded.getRecordByDniOrThrow(dni).getStatus();
        assertThat(actual).isEqualTo(AttendanceStatus.valueOf(expectedStatus));
    }

    @Then("an error should be raised indicating that the DNI was not found")
    public void anErrorShouldBeRaisedIndicatingThatTheDniWasNotFound() {
        // Assert: an IllegalArgumentException was captured in the When step
        assertThat(capturedException)
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(capturedException.getMessage())
                .containsIgnoringCase("does not exist");
    }
}