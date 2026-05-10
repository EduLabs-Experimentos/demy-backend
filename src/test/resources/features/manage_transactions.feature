Feature: Gestionar transacciones financieras
  Para mantener el control economico de la academia
  Como administrador
  Quiero registrar, actualizar y eliminar ingresos y egresos financieros

  Scenario: US025 - Registro exitoso de ingreso financiero
    Given existe una academia financiera con id 5
    When registro una transaccion financiera con tipo "income", categoria "student enrollment", metodo "cash", monto 150.00 y moneda PEN
    Then debe crearse una transaccion con tipo "INCOME", categoria "STUDENT_ENROLLMENT", metodo "CASH", monto 150.00 y moneda "PEN"
    And el mensaje final de finance es "Test Passed"

  Scenario: US026 - Actualizacion exitosa de egreso financiero
    Given existe una academia financiera con id 5
    And existe una transaccion financiera registrada con id 1
    When actualizo la transaccion financiera con tipo "expense", categoria "office supplies", metodo "debit card", monto 80.00 y moneda PEN
    Then debe actualizarse la transaccion con tipo "EXPENSE", categoria "OFFICE_SUPPLIES", metodo "DEBIT_CARD", monto 80.00 y moneda "PEN"
    And el mensaje final de finance es "Test Passed"

  Scenario: US027 - Eliminacion exitosa de ingreso o egreso financiero
    Given existe una academia financiera con id 5
    And existe una transaccion financiera registrada con id 1
    When elimino la transaccion financiera
    Then la transaccion financiera debe eliminarse correctamente
    And el mensaje final de finance es "Test Passed"

  Scenario: US025 - Registro rechazado por monto negativo
    Given existe una academia financiera con id 5
    When registro una transaccion financiera con tipo "income", categoria "student enrollment", metodo "cash", monto -50.00 y moneda PEN
    Then el mensaje final de finance es "Error"