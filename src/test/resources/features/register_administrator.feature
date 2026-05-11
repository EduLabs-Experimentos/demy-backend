Feature: Registro de Administrador
  Para gestionar la institución y sus miembros
  Como dueño del sistema
  Quiero registrar un administrador

  Scenario Outline: Validar reglas de negocio al registrar un administrador
    When intento registrar un administrador con nombre "<nombre>", apellido "<apellido>", dni "<dni>", celular "<telefono>" y userId <userId>
    Then el registro del administrador debe validarse con
      | firstName | <nombre>   |
      | lastName  | <apellido> |
      | dni       | <dni>      |
      | phone     | <telefono> |
      | userId    | <userId>   |
    And el resultado del registro de admin es "<mensaje>"

    Examples:
      | nombre | apellido | dni      | telefono  | userId | mensaje     |
      | Diego  | Vilca    | 76543210 | 999888777 | 10     | Test Passed |
      |        | Vilca    | 76543210 | 999888777 | 10     | Error       |
      | Salim  | Ramirez  | 123      | 999888777 | 15     | Error       |
      | Paul   | Sulca    | 12345678 | 987654321 | -5     | Error       |