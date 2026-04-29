# 03 - Modelo Logico Relacional

## Entidades principales
1. sec_usuario
2. sec_rol
3. sec_usuario_rol
4. cli_persona_natural
5. cli_empresa
6. cli_empresa_usuario
7. cli_empresa_usuario_rol
8. cta_cuenta
9. cta_movimiento
10. cre_prestamo
11. cre_prestamo_aprobacion
12. cre_prestamo_desembolso
13. trf_transferencia
14. trf_transferencia_aprobacion
15. aud_bitacora_evento
16. aud_cambio_dato
17. aud_error_operacion

## Relaciones clave
1. sec_usuario N:M sec_rol via sec_usuario_rol.
2. cli_empresa 1:N cli_empresa_usuario.
3. cli_empresa_usuario 1:N cli_empresa_usuario_rol (rol dentro de la empresa).
4. cli_persona_natural 1:N cta_cuenta.
5. cli_empresa 1:N cta_cuenta.
6. cli_persona_natural y cli_empresa 1:N cre_prestamo.
7. cta_cuenta 1:N cta_movimiento.
8. trf_transferencia referencia cta_cuenta origen y destino.
9. cre_prestamo_desembolso referencia cta_cuenta destino.
10. cta_cuenta puede tener limite_sobregiro_autorizado.

## Normalizacion recomendada
- 3FN para entidades core.
- Denormalizacion controlada solo para consultas de alto volumen.
- Campos derivados no persistir salvo justificacion operativa.

## Identificadores de negocio
- cliente_identificacion unica global, cualificada por tipo_identificacion_id.
- empresa_nit unico.
- numero_cuenta unico.
- id_transferencia externo opcional para idempotencia.

## Estrategia de saldos
- cta_cuenta.saldo_actual como valor operativo.
- cta_cuenta.limite_sobregiro_autorizado para calculo de saldo disponible.
- cta_movimiento como historial inmutable.
- toda variacion de saldo debe crear movimiento con referencia a operacion origen.

## Notas de consistencia
- Si se usa idempotency_key en operaciones externas, debe modelarse en tablas transaccionales (trf_transferencia y opcionalmente cta_movimiento).
- aud_error_operacion debe registrar codigo de error, modulo, actor y referencia transaccional.
