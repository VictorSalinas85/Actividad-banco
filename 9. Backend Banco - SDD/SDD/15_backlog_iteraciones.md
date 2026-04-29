# 15 - Backlog por Iteraciones

## Iteracion 1 - Fundaciones
1. Crear schema base y catalogos.
2. Crear entidades sec y cli.
3. Cargar estados y parametros.
4. Pruebas de integridad estructural.

## Iteracion 2 - Cuentas
1. Crear cta_cuenta y cta_movimiento.
2. Implementar sp_cta_abrir_cuenta, sp_cta_consignar, sp_cta_retirar.
3. Implementar auditoria basica de cuentas.
4. Pruebas de saldo y bloqueo de cuenta.

## Iteracion 3 - Prestamos
1. Crear tablas cre_.
2. Implementar sp_cre_solicitar_prestamo, sp_cre_aprobar_prestamo, sp_cre_rechazar_prestamo, sp_cre_desembolsar_prestamo.
3. Validar transiciones y permisos de analista.
4. Pruebas end-to-end de prestamo.

## Iteracion 4 - Transferencias
1. Crear tablas trf_.
2. Implementar flujo directo y flujo con aprobacion.
3. Implementar job de vencimiento.
4. Pruebas de concurrencia y idempotencia.

## Iteracion 5 - Seguridad y hardening
1. Roles SQL y privilegios minimos.
2. Bloqueo DML directo en tablas core.
3. Ajuste de indices y planes de consulta.
4. Pruebas de seguridad por rol.

## Iteracion 6 - Cierre
1. Matriz final de trazabilidad.
2. Benchmark basico de operaciones criticas.
3. Plan de despliegue productivo y rollback.
