# 07 - Estrategia de Triggers

## Objetivo
Usar triggers solo para invariantes locales y auditoria tecnica automatica.

## Triggers BEFORE INSERT
1. Completar created_at/updated_at si no vienen informados.
2. Normalizar campos de texto claves (trim, mayusculas segun politica).
3. Rechazar registros con estado invalido cuando aplique.

## Triggers BEFORE UPDATE
1. Proteger campos inmutables: created_at, created_by, identificacion, numero_cuenta.
2. Actualizar updated_at automaticamente.
3. Incrementar row_version.
4. Rechazar transiciones de estado invalidas cuando se intente update directo.

## Triggers AFTER INSERT/UPDATE
1. Escribir huella tecnica en aud_cambio_dato.
2. Registrar metadatos de sesion (usuario SQL, host, trx id si aplica).

## Reglas que NO deben ir en triggers
1. Ejecucion completa de transferencias.
2. Desembolso de prestamo.
3. Validaciones complejas de autorizacion por rol y alcance.
4. Vencimiento por tiempo.

## Motivo
Estas reglas son multi-entidad, transaccionales y requieren mensajes de error de dominio claros. Deben centralizarse en SP.

## Buenas practicas
1. Trigger por tabla y evento con responsabilidad unica.
2. Prohibido hacer llamadas en cascada complejas.
3. Documentar cada trigger con tabla afectada e impacto.
