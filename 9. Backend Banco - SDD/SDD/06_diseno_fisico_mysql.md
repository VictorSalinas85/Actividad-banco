# 06 - Diseno Fisico MySQL

## Version objetivo
- MySQL 8.0+

## Motor y configuraciones base
1. InnoDB para todas las tablas transaccionales.
2. character_set_server utf8mb4.
3. transaction_isolation por defecto REPEATABLE READ, con ajustes por SP criticos.
4. sql_mode estricto (incluye STRICT_TRANS_TABLES).

## Tipos de datos recomendados
1. IDs: bigint unsigned.
2. Montos: decimal(18,2) o decimal(20,4) segun producto.
3. Tasa interes: decimal(7,4).
4. Estados y codigos: varchar corto referenciando catalogos.
5. Fechas de negocio: datetime(6) en UTC.
6. JSON de detalle para auditoria variable.

## DDL base por modulo
1. sec_: usuarios, roles, permisos.
2. cat_: catalogos y parametros.
3. cli_: personas, empresas, vinculaciones.
4. cta_: cuentas, movimientos.
5. cre_: prestamos, aprobaciones, desembolsos.
6. trf_: transferencias, aprobaciones.
7. aud_: bitacora_evento, cambio_dato.

## Indices recomendados
1. cta_cuenta: (numero_cuenta unique), (titular_tipo, titular_id), (estado_id).
2. cre_prestamo: (cliente_tipo, cliente_id), (estado_id), (fecha_solicitud).
3. trf_transferencia: (estado_id, fecha_creacion), (cuenta_origen_id), (creador_id), (estado_id, id_empresa, fecha_creacion).
4. aud_bitacora_evento: (fecha_hora_operacion), (tipo_operacion_id), (producto_tipo, producto_id).

## Politica de FK
- ON DELETE RESTRICT para entidades core.
- ON UPDATE RESTRICT en claves de referencia estable.

## Convenciones de scripts
- V001__init_schema.sql
- V002__catalogos_base.sql
- V003__tablas_core.sql
- V004__sp_core.sql
- V005__triggers_auditoria.sql
