Feature: Gestión de administradores y academias
  Como administrativo de una academia
  Quiero poder registrar administradores y academias
  Para gestionar la estructura institucional

  # Escenario 1: Registro exitoso de administrador
  Scenario: Registrar administrador con datos válidos
    Given no existe administrador con DNI "87654321" en el sistema
    When registro un administrador con nombre "Juan", apellido "Pérez", país "+51", teléfono "999111222", DNI "87654321" y userId 50
    Then el administrador queda registrado exitosamente
    And el código de estado HTTP del administrador es 201
    And se devuelve el recurso del administrador creado

  # Escenario 2: Registro de academia con email duplicado
  Scenario: No permite registrar academia con email ya existente
    Given ya existe una academia con email "academia@test.com" en el sistema
    When registro una nueva academia con nombre "Mi Academia", email "academia@test.com", teléfono "+51 999888777", RUC "12345678901" y administrador ID 5
    Then la operación falla con error de "email duplicado"
    And el código de estado HTTP de la academia es 400
    And se devuelve mensaje de error

  # Escenario 3: Asociación de administrador con academia
  Scenario: Asociar administrador a academia exitosamente
    Given existe un administrador "Carlos" "Admin" sin asociación a academia
    And existe una academia "Mi Academia" sin administrador asignado
    When asociar el administrador a la academia
    Then el administrador queda asociado a la academia
    And la academia tiene el administrador asignado
    And el código de estado HTTP de la academia es 200