    # ============================================================
    # Feature: Mark Student Attendance
    # Bounded Context: Attendance
    # ============================================================
    @attendance
    Feature: Mark student attendance in a class session

      Background:
        Given an academy with ID 1 and a class session with ID 1 exists for today's date
        And the following students are enrolled in the session:
          | dni      | initialStatus |
          | 12345678 | ABSENT        |
          | 87654321 | ABSENT        |

      # ----------------------------------------------------------
      # Scenario 1: Successful status update (ABSENT → PRESENT)
      # ----------------------------------------------------------
      @happy-path
      Scenario: Update a student's attendance from ABSENT to PRESENT
        Given the student with DNI "12345678" has status "ABSENT"
        When the teacher marks the student with DNI "12345678" as "PRESENT"
        Then the attendance record for DNI "12345678" should have status "PRESENT"

      # ----------------------------------------------------------
      # Scenario 2: Update to EXCUSED (ABSENT → EXCUSED)
      # ----------------------------------------------------------
      @happy-path
      Scenario: Update a student's attendance from ABSENT to EXCUSED
        Given the student with DNI "87654321" has status "ABSENT"
        When the teacher marks the student with DNI "87654321" as "EXCUSED"
        Then the attendance record for DNI "87654321" should have status "EXCUSED"

      # ----------------------------------------------------------
      # Scenario 3: Attempt to update attendance for a non-enrolled DNI
      # ----------------------------------------------------------
      @error-path
      Scenario: Attempt to update attendance for a non-enrolled DNI
        Given the student with DNI "00000001" is not enrolled in the session
        When the teacher marks the student with DNI "00000001" as "PRESENT"
        Then an error should be raised indicating that the DNI was not found
