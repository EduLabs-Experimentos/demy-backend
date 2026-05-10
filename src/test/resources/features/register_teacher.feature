Feature: Registro de Profesor
  Para asignar clases y gestionar la parte académica
  Como administrador
  Quiero registrar un nuevo profesor en mi academia

  Scenario Outline: Validar reglas de negocio al registrar un profesor
    Given un userId <userId> asignado por el sistema de usuarios y un academyId <academyId>
    When intento registrar un profesor con nombre "<nombre>", apellido "<apellido>", correo "<correo>" y celular "<telefono>"
    Then el registro del profesor debe validarse con
      | firstName | <nombre>   |
      | lastName  | <apellido> |
      | phone     | <telefono> |
      | userId    | <userId>   |
      | academyId | <academyId>|
    And el resultado del registro de profesor es "<mensaje>"

    Examples:
      | userId | academyId | nombre | apellido | correo               | telefono  | mensaje     |
      | 20     | 5         | Lucia  | Vargas   | lucia@academy.com    | 911222333 | Test Passed |
      | 21     | 5         |        | Vargas   | lucia@academy.com    | 911222333 | Error       |
      | -1     | 5         | Mario  | Lopez    | mario@academy.com    | 988777666 | Error       |