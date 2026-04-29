# 11 - Transacciones, Concurrencia y Errores

## Objetivo
Evitar inconsistencias de saldo, dobles ejecuciones y estados invalidos en flujos concurrentes.

## Reglas transaccionales
1. Toda operacion monetaria debe usar START TRANSACTION.
2. Lock explicito de filas de cuenta con SELECT ... FOR UPDATE.
3. Orden de bloqueo determinista por id de cuenta para minimizar deadlocks.
4. Commit atomico de cambios + auditoria.
5. Definir politica de reintento para deadlock y lock wait timeout en capa de aplicacion.

## Idempotencia
1. Entrada opcional: idempotency_key por operacion externa.
2. Unico por tipo_operacion + canal + ventana temporal.
3. Si ya existe, retornar referencia previa sin reprocesar.

## Aislamiento
1. Mantener REPEATABLE READ para consistencia general.
2. Evaluar READ COMMITTED en lecturas operativas de alto volumen.

## Manejo de errores en SP
1. DECLARE EXIT HANDLER FOR SQLEXCEPTION.
2. DECLARE HANDLER FOR 1062 DO ... (para manejar errores de llave duplicada).
3. ROLLBACK en bloque de excepcion.
4. SIGNAL SQLSTATE con codigo de dominio estandar.
5. Registrar error tecnico en aud_error_operacion.

## Errores de dominio minimos
1. DOM-TRF-001 saldo insuficiente.
2. DOM-TRF-002 cuenta origen no operativa.
3. DOM-TRF-003 transferencia en estado invalido.
4. DOM-CRE-001 prestamo no aprobable en estado actual.
5. DOM-CRE-002 cuenta destino desembolso invalida.
6. DOM-SEC-001 usuario sin permiso.
7. DOM-CLI-001 identificacion de cliente ya existe.
8. DOM-TRX-001 transaccion reintentable por conflicto de concurrencia.
