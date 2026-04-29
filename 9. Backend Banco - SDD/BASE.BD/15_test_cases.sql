-- V009__casos_prueba.sql
USE banco_core;

-- ==========================
-- T000 - Preparacion minima
-- ==========================
SET @out_code = NULL;
SET @out_message = NULL;
SET @out_reference = NULL;

-- Crear usuario supervisor y operativo para pruebas empresa
INSERT INTO sec_usuario(username, hash_password, nombre_completo, email, telefono, estado_id, created_by, updated_by)
SELECT 'supervisor_emp', 'HASH_SUP', 'Supervisor Empresa', 'supervisor@empresa.local', '3000000002', eu.id, 1, 1
FROM cat_estado_usuario eu WHERE eu.codigo = 'ACTIVO'
ON DUPLICATE KEY UPDATE nombre_completo = VALUES(nombre_completo);

INSERT INTO sec_usuario(username, hash_password, nombre_completo, email, telefono, estado_id, created_by, updated_by)
SELECT 'operativo_emp', 'HASH_OP', 'Operativo Empresa', 'operativo@empresa.local', '3000000003', eu.id, 1, 1
FROM cat_estado_usuario eu WHERE eu.codigo = 'ACTIVO'
ON DUPLICATE KEY UPDATE nombre_completo = VALUES(nombre_completo);

-- ==========================
-- T001 - Alta cliente duplicado
-- Esperado: segunda alta falla con DOM-CLI-001
-- ==========================
CALL sp_cli_crear_persona('CEDULA','1001001001','Juan Perez','juan.perez@demo.local','3001112233','1990-01-10','Calle 1',1,@out_code,@out_message,@out_reference);
SELECT 'T001-A' AS test_id, @out_code AS out_code, @out_message AS out_message, @out_reference AS out_reference;

CALL sp_cli_crear_persona('CEDULA','1001001001','Juan Perez','juan.perez@demo.local','3001112233','1990-01-10','Calle 1',1,@out_code,@out_message,@out_reference);
SELECT 'T001-B' AS test_id, @out_code AS out_code, @out_message AS out_message, @out_reference AS out_reference;

-- ==========================
-- T002 - Apertura cuenta y saldo insuficiente en transferencia
-- Esperado: DOM-TRF-001 en transferencia directa
-- ==========================
SET @persona_id = (SELECT id FROM cli_persona_natural WHERE identificacion = '1001001001' LIMIT 1);

CALL sp_cta_abrir_cuenta('PERSONA', @persona_id, 'AHORROS', 'USD', 0, 1, @out_code, @out_message, @out_reference);
SET @cuenta_1 = CAST(@out_reference AS UNSIGNED);
SELECT 'T002-A' AS test_id, @out_code, @out_message, @cuenta_1 AS cuenta_origen;

CALL sp_cli_crear_persona('CEDULA','1001001002','Maria Gomez','maria.gomez@demo.local','3001112244','1992-03-12','Calle 2',1,@out_code,@out_message,@out_reference);
SET @persona_id2 = (SELECT id FROM cli_persona_natural WHERE identificacion = '1001001002' LIMIT 1);
CALL sp_cta_abrir_cuenta('PERSONA', @persona_id2, 'AHORROS', 'USD', 0, 1, @out_code, @out_message, @out_reference);
SET @cuenta_2 = CAST(@out_reference AS UNSIGNED);

CALL sp_trf_ejecutar_transferencia_directa(@cuenta_1, @cuenta_2, 500.00, 'WEB', 'IDEMP-T002', 1, @out_code, @out_message, @out_reference);
SELECT 'T002-B' AS test_id, @out_code AS out_code, @out_message AS out_message, @out_reference AS out_reference;

-- ==========================
-- T003 - Transferencia alto monto queda pendiente aprobacion
-- Esperado: estado EN_ESPERA_APROBACION
-- ==========================
CALL sp_cli_crear_empresa('NIT','900100200','Empresa Demo SAS','empresa@demo.local','3002223344','Zona Industrial',@persona_id,1,@out_code,@out_message,@out_reference);
SET @empresa_id = CAST(@out_reference AS UNSIGNED);

SET @usuario_supervisor = (SELECT id FROM sec_usuario WHERE username = 'supervisor_emp' LIMIT 1);
SET @usuario_operativo = (SELECT id FROM sec_usuario WHERE username = 'operativo_emp' LIMIT 1);

CALL sp_cli_asociar_usuario_empresa(@empresa_id, @usuario_supervisor, 1, @out_code, @out_message, @out_reference);
SET @emp_user_sup = CAST(@out_reference AS UNSIGNED);
CALL sp_cli_asignar_rol_empresa_usuario(@emp_user_sup, 'SUPERVISOR_APROBADOR', 1, @out_code, @out_message, @out_reference);

CALL sp_cli_asociar_usuario_empresa(@empresa_id, @usuario_operativo, 1, @out_code, @out_message, @out_reference);
SET @emp_user_op = CAST(@out_reference AS UNSIGNED);
CALL sp_cli_asignar_rol_empresa_usuario(@emp_user_op, 'OPERATIVO', 1, @out_code, @out_message, @out_reference);

CALL sp_cta_abrir_cuenta('EMPRESA', @empresa_id, 'EMPRESARIAL', 'USD', 1000, 1, @out_code, @out_message, @out_reference);
SET @cta_emp_origen = CAST(@out_reference AS UNSIGNED);

CALL sp_cta_consignar(@cta_emp_origen, 50000.00, 'VENTANILLA', 'IDEMP-DEP-EMP', 1, @out_code, @out_message, @out_reference);

CALL sp_cta_abrir_cuenta('PERSONA', @persona_id2, 'AHORROS', 'USD', 0, 1, @out_code, @out_message, @out_reference);
SET @cta_emp_destino = CAST(@out_reference AS UNSIGNED);

CALL sp_trf_crear_transferencia(@cta_emp_origen, @cta_emp_destino, @empresa_id, 20000.00, 'WEB', 'IDEMP-T003', @usuario_operativo, @out_code, @out_message, @out_reference);
SET @trf_alta = CAST(@out_reference AS UNSIGNED);
SELECT 'T003-A' AS test_id, @out_code, @out_message, @trf_alta AS transferencia_id;

SELECT 'T003-B' AS test_id, et.codigo AS estado_transferencia
FROM trf_transferencia t
JOIN cat_estado_transferencia et ON et.id = t.estado_id
WHERE t.id = @trf_alta;

-- ==========================
-- T004 - Maker-checker
-- Esperado: creador no puede aprobar su propia transferencia
-- ==========================
CALL sp_trf_aprobar_transferencia(@trf_alta, @usuario_operativo, @out_code, @out_message, @out_reference);
SELECT 'T004' AS test_id, @out_code AS out_code, @out_message AS out_message, @out_reference AS out_reference;

-- ==========================
-- T005 - Aprobacion correcta por supervisor
-- Esperado: transferencia EJECUTADA
-- ==========================
CALL sp_trf_aprobar_transferencia(@trf_alta, @usuario_supervisor, @out_code, @out_message, @out_reference);
SELECT 'T005-A' AS test_id, @out_code, @out_message, @out_reference;

SELECT 'T005-B' AS test_id, et.codigo AS estado_transferencia
FROM trf_transferencia t
JOIN cat_estado_transferencia et ON et.id = t.estado_id
WHERE t.id = @trf_alta;

-- ==========================
-- T006 - Prestamo solo analista puede aprobar
-- Esperado: usuario no analista falla, analista aprueba
-- ==========================
CALL sp_cre_solicitar_prestamo('PERSONA', @persona_id, 'PERSONAL', 3000.00, 12, @cuenta_1, 1, @out_code, @out_message, @out_reference);
SET @prestamo_1 = CAST(@out_reference AS UNSIGNED);
SELECT 'T006-A' AS test_id, @out_code, @out_message, @prestamo_1 AS prestamo_id;

-- intento con usuario no analista
CALL sp_cre_aprobar_prestamo(@prestamo_1, 2500.00, 0.1850, 1, @out_code, @out_message, @out_reference);
SELECT 'T006-B' AS test_id, @out_code, @out_message, @out_reference;

-- intento con analista valido
SET @analista_id = (SELECT id FROM sec_usuario WHERE username = 'analista1' LIMIT 1);
CALL sp_cre_aprobar_prestamo(@prestamo_1, 2500.00, 0.1850, @analista_id, @out_code, @out_message, @out_reference);
SELECT 'T006-C' AS test_id, @out_code, @out_message, @out_reference;

CALL sp_cre_desembolsar_prestamo(@prestamo_1, @analista_id, @out_code, @out_message, @out_reference);
SELECT 'T006-D' AS test_id, @out_code, @out_message, @out_reference;

-- ==========================
-- T007 - Idempotencia en transferencia
-- Esperado: segundo intento no duplica efectos
-- ==========================
CALL sp_trf_ejecutar_transferencia_directa(@cuenta_2, @cuenta_1, 50.00, 'WEB', 'IDEMP-T007', 1, @out_code, @out_message, @out_reference);
SELECT 'T007-A' AS test_id, @out_code, @out_message, @out_reference;

CALL sp_trf_ejecutar_transferencia_directa(@cuenta_2, @cuenta_1, 50.00, 'WEB', 'IDEMP-T007', 1, @out_code, @out_message, @out_reference);
SELECT 'T007-B' AS test_id, @out_code, @out_message, @out_reference;

SELECT 'T007-C' AS test_id, COUNT(*) AS movimientos_idempotencia
FROM cta_movimiento m
JOIN cat_tipo_operacion op ON op.id = m.tipo_operacion_id
JOIN cat_canal_operacion c ON c.id = m.canal_id
WHERE op.codigo = 'TRANSFERENCIA' AND c.codigo = 'WEB' AND m.idempotency_key = 'IDEMP-T007';

-- ==========================
-- T008 - Auditoria de eventos
-- Esperado: existen registros en bitacora
-- ==========================
SELECT 'T008' AS test_id, COUNT(*) AS eventos_en_bitacora
FROM aud_bitacora_evento;
