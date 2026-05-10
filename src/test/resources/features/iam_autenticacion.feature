Feature: Autenticación de usuarios en plataforma DEMY
  Como usuario de la plataforma
  Quiero poder registrarme, iniciar sesión y verificar mi cuenta
  Para acceder a las funcionalidades del sistema

  # Escenario 1: Registro exitoso de nuevo usuario
  Scenario: Registro de nuevo usuario con credenciales válidas
    Given un email "nuevo@test.com" no registrado en el sistema
    When creo un nuevo usuario con email "nuevo@test.com" y password "Password123"
    Then el usuario queda registrado con estado "PENDING"
    And se genera un código de verificación de 6 dígitos
    And el resultado de la operación es exitoso

  # Escenario 2: Inicio de sesión exitoso
  Scenario: Inicio de sesión con credenciales correctas
    Given existe un usuario "test@test.com" con password "Password123" en estado "VERIFIED"
    When inicio sesión con email "test@test.com" y password "Password123"
    Then obtengo un token JWT como respuesta
    And el código de estado HTTP del usuario es 200
    And el usuario existe en el sistema

  # Escenario 3: Verificación de cuenta con código incorrecto
  Scenario: Verificación falla con código de verificación inválido
    Given existe un usuario "usuario@test.com" con código de verificación "123456" no verificado
    When intento verificar la cuenta con código "999999"
    #    Then la verificación falla
    # And el código de estado HTTP del usuario es 400
    # And el usuario permanece en estado "PENDING"