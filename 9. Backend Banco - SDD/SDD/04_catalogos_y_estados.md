# 04 - Catalogos y Estados

## Objetivo
Centralizar enums y parametros en tablas para evitar logica hardcodeada.

## Catalogos obligatorios
1. cat_estado_usuario
2. cat_estado_cuenta
3. cat_tipo_cuenta
4. cat_moneda
5. cat_estado_prestamo
6. cat_tipo_prestamo
7. cat_estado_transferencia
8. cat_tipo_operacion
9. cat_parametro_negocio
10. cat_transicion_estado
11. cat_rol_empresa
12. cat_tipo_identificacion
13. cat_canal_operacion
14. cat_tipo_movimiento
15. cat_motivo_rechazo
16. cat_motivo_bloqueo
17. cat_estado_sesion

## Estructura minima por catalogo
- id bigint unsigned pk
- codigo varchar(40) unique not null
- nombre varchar(100) not null
- descripcion varchar(255) null
- activo tinyint(1) not null default 1
- orden_visual int not null default 0
- vigente_desde datetime(6) null
- vigente_hasta datetime(6) null
- created_at datetime(6) not null
- updated_at datetime(6) not null

## Estados iniciales recomendados
### Prestamo
- EN_ESTUDIO
- APROBADO
- RECHAZADO
- DESEMBOLSADO

### Transferencia
- CREADA
- EN_ESPERA_APROBACION
- APROBADA
- RECHAZADA
- EJECUTADA
- VENCIDA

### Roles de Empresa
- OPERATIVO
- SUPERVISOR_APROBADOR
- REPRESENTANTE_LEGAL

### Estado de Sesion
- ACTIVA
- EXPIRADA
- REVOCADA

### Cuenta
- ACTIVA
- BLOQUEADA
- CANCELADA

## Tabla de transiciones de estado
cat_transicion_estado:
- id
- entidad (PRESTAMO, TRANSFERENCIA, CUENTA)
- estado_origen_id
- estado_destino_id
- rol_requerido_id (nullable)
- requiere_motivo tinyint(1)
- activo

Esta tabla evita reglas de transicion dispersas y permite validacion uniforme desde SP.
