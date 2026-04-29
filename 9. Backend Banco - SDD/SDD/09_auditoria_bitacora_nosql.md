# 09 - Auditoria y Bitacora con Detalle Flexible

## Objetivo
Garantizar trazabilidad completa de operaciones criticas con estructura flexible para detalle variable.

## Enfoque recomendado en MySQL
Aunque el enunciado menciona NoSQL, para simplificar operacion inicial se puede usar tabla InnoDB con columna JSON.

Tabla: aud_bitacora_evento
- id bigint unsigned pk
- tipo_operacion_id bigint unsigned not null
- fecha_hora_operacion datetime(6) not null
- id_usuario bigint unsigned not null
- rol_usuario_id bigint unsigned not null
- producto_tipo varchar(40) not null
- producto_id varchar(64) not null
- datos_detalle json not null
- hash_integridad char(64) null
- created_at datetime(6) not null

## Criterios de inmutabilidad
1. No permitir UPDATE o DELETE directo sobre aud_bitacora_evento.
2. Trigger BEFORE UPDATE/DELETE que dispare error.
3. Insercion solo por SP de dominio o SP de auditoria.

## Eventos minimos obligatorios
1. Solicitud de prestamo creada.
2. Prestamo aprobado/rechazado.
3. Prestamo desembolsado.
4. Transferencia creada.
5. Transferencia aprobada/rechazada/ejecutada/vencida.
6. Bloqueo o cancelacion de productos.
7. Intentos fallidos por permisos o validaciones.

## Estrategia de retencion
1. Retencion activa 24 meses en tabla principal.
2. Archivado historico por particion temporal o export.
3. Politica de acceso restringido para datos sensibles.
