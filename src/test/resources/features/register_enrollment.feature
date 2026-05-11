Feature: Registrar matrícula de un estudiante
  Para que se almacenen sus datos y se acceda a funcionalidades adicionales
  Como administrativo
  Quiero registrar alumnos en la aplicación web

  Scenario Outline: Registro de matrícula
    Given existe un Student con id <studentId>
    And existe un AcademicPeriod con id <academicPeriodId>
    And existe un WeeklySchedule con id <weeklyScheduleId>
    And existe un Academy con id <academyId>
    When intento registrar la matrícula con amount <amount> y currency <currency>
    Then debe crearse una Enrollment con
      | studentId        | <studentId>        |
      | academicPeriodId | <academicPeriodId> |
      | weeklyScheduleId | <weeklyScheduleId> |
      | academyId        | <academyId>        |
      | amount           | <amount>           |
      | currency         | <currency>         |
      | status           | <status>           |

    And el mensaje final es "<message>"

    Examples:
      | studentId | academicPeriodId | academyId | weeklyScheduleId | amount  | currency | status | message     |
      | 5         | 7                | 2         | 1                | 1500.00 | PEN      | ACTIVE | Test Passed |
      | 6         | 8                | 2         | 2                | -500.00 | PEN      | ACTIVE | Error       |
      | 7         | 9                | 3         | 1                | 1200.00 | PEN      | ACTIVE | Test Passed |
