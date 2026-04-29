# 12 - Pruebas SQL y Criterios de Aceptacion

## Objetivo
Validar que la especificacion implementada cumple negocio, consistencia y rendimiento basico.

## Tipos de pruebas
1. Pruebas de estructura (DDL, FK, indices, constraints).
2. Pruebas de catalogos y estados.
3. Pruebas de SP unitarias por caso de uso.
4. Pruebas de integracion de flujos criticos.
5. Pruebas de concurrencia para transferencias y desembolsos.
6. Pruebas de simulacion de deadlock.
7. Pruebas de seguridad y autorizacion por rol.
8. Pruebas de auditoria e inmutabilidad.

## Casos minimos obligatorios
1. Alta de cliente con identificacion duplicada debe fallar.
2. Transferencia con saldo insuficiente debe fallar sin afectar saldos.
3. Transferencia alto monto debe quedar pendiente aprobacion.
4. Transferencia pendiente vencida despues de 60 min.
5. Prestamo en estudio solo cambia por analista.
6. Desembolso solo desde prestamo aprobado.
7. Bitacora registra todos los eventos criticos.
8. Dos transferencias cruzadas (A->B, B->A) no generan deadlock irrecuperable.
9. Creacion concurrente del mismo cliente retorna error de dominio controlado.
10. El mismo usuario no puede crear y aprobar la misma transferencia de alto monto.
11. Reintento con la misma idempotency_key no duplica impacto financiero.
12. Usuario sin rol/permisos no puede ejecutar SP critico.
13. Intento de DML directo en tablas core desde cuenta de aplicacion debe fallar.

## Criterios de aceptacion
1. 100 por ciento de reglas criticas cubiertas por pruebas.
2. Cero inconsistencias de saldo bajo pruebas concurrentes definidas.
3. Cero cambios de estado fuera de transiciones permitidas.
4. Cada SP critico devuelve codigo de salida estandar.
5. Toda denegacion de permiso deja evidencia auditable.

## Evidencias esperadas
1. Script de prueba automatizada.
2. Resultado de ejecucion por ambiente.
3. Matriz regla -> prueba -> evidencia.
