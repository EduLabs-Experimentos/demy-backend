Feature: Registro de Academia
  Para gestionar la información de la academia y sus miembros
  Como administrador
  Quiero registrar una nueva academia en la plataforma

  Scenario Outline: Validacion de las reglas de negocio al registrar una academia
    Given un administrador con id <adminId>
    When intento registrar una academia con nombre "<nombre>", ruc "<ruc>" y correo "<email>"
    Then el registro de la academia debe validarse con
      | academyName  | <nombre>  |
      | ruc          | <ruc>     |
      | emailAddress | <email>   |
    And el resultado de la creacion es "<mensaje>"

    Examples:
      | adminId | nombre         | ruc         | email               | mensaje     |
      | 10      | Nistra Academy | 10456789123 | contacto@nistra.com | Test Passed |
      | 10      |                | 10456789123 | contacto@nistra.com | Error       |
      | 15      | Demy Code      |             | admin@demy.com      | Error       |