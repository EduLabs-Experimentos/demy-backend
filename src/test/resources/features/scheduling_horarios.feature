Feature: Gestión de horarios semanales
  Como administrativo de una academia
  Quiero poder crear y gestionar horarios semanales con sesiones de clase
  Para organizar la planificación académica

  # Escenario 1: Creación exitosa de horario semanal
  Scenario: Crear horario semanal con nombre único
    Given no existe un schedule con nombre "Semana 1" para la academia con ID 1
    When creo un horario semanal con nombre "Semana 1" para la academia con ID 1
    Then el horario queda creado exitosamente
    And se devuelve un ID de horario
    #    And el estado HTTP de la respuesta es 201

  # Escenario 2: Agregar sesión de clase al horario
  Scenario: Agregar sesión de clase al horario existente
    Given existe un horario semanal con ID 1 para la academia con ID 1
    When agrego una sesión de clase con inicio "08:00", fin "10:00", día "MONDAY", curso ID 10, salón ID 20, profesor "Carlos" "Pérez"
    Then la sesión queda agregada al horario exitosamente
    And el horario tiene al menos una sesión
    #    And el estado HTTP de la respuesta es 200

  # Escenario 3: Eliminación exitosa de horario
  Scenario: Eliminar horario semanal existente
    Given existe un horario semanal con ID 5 para la academia con ID 1 preparado para eliminar
    When elimino el horario semanal con ID 5
    Then el horario es eliminado exitosamente
    #    And el estado HTTP de la respuesta es 200
    And el mensaje de respuesta es "Schedule deleted successfully"