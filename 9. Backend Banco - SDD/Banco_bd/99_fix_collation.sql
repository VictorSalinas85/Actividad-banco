-- ============================================================================
-- Fix: normaliza el collation de todas las tablas/columnas a utf8mb4_unicode_ci
--
-- Problema: las columnas de catalogos (codigo, descripcion, etc.) fueron
-- creadas con utf8mb4_general_ci, mientras la BD por defecto usa
-- utf8mb4_unicode_ci. Esto causa el error MySQL 1267
-- "Illegal mix of collations" cuando un stored procedure compara
-- una columna contra un parametro de entrada.
--
-- Sintoma: sp_cli_crear_persona y similares retornan code='ERR' sin causa
-- aparente, porque el SQLEXCEPTION handler oculta el 1267 real.
--
-- Solucion: forzar utf8mb4_unicode_ci en toda la BD y en cada tabla.
-- ============================================================================

ALTER DATABASE banco_bd CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Catalogos
ALTER TABLE cat_canal_operacion       CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE cat_estado_cuenta         CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE cat_estado_prestamo       CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE cat_estado_sesion         CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE cat_estado_transferencia  CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE cat_estado_usuario        CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE cat_moneda                CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE cat_motivo_bloqueo        CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE cat_motivo_rechazo        CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE cat_parametro_negocio     CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE cat_rol_empresa           CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE cat_tipo_cuenta           CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE cat_tipo_identificacion   CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE cat_tipo_movimiento       CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE cat_tipo_operacion        CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE cat_tipo_prestamo         CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE cat_transicion_estado     CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Seguridad
ALTER TABLE sec_rol           CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE sec_usuario       CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE sec_usuario_rol   CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE sec_sesion        CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Clientes
ALTER TABLE cli_persona_natural        CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE cli_empresa                CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE cli_empresa_usuario        CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE cli_empresa_usuario_rol    CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Productos
ALTER TABLE prd_producto_bancario      CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Cuentas
ALTER TABLE cta_cuenta        CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE cta_movimiento    CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Prestamos
ALTER TABLE cre_prestamo              CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE cre_prestamo_aprobacion   CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE cre_prestamo_desembolso   CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Transferencias
ALTER TABLE trf_transferencia             CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE trf_transferencia_aprobacion  CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Auditoria
ALTER TABLE aud_bitacora_evento   CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE aud_cambio_dato       CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE aud_error_operacion   CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
