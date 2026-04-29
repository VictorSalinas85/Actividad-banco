-- V006__procedimientos_core.sql
USE banco_core;

DELIMITER $$

DROP PROCEDURE IF EXISTS sp_aud_registrar_error $$
CREATE PROCEDURE sp_aud_registrar_error(
  IN p_codigo_error VARCHAR(32),
  IN p_modulo VARCHAR(20),
  IN p_mensaje VARCHAR(255),
  IN p_referencia VARCHAR(64),
  IN p_actor_usuario_id BIGINT UNSIGNED,
  IN p_payload JSON
)
BEGIN
  INSERT INTO aud_error_operacion(codigo_error, modulo, mensaje, referencia, actor_usuario_id, payload)
  VALUES (p_codigo_error, p_modulo, p_mensaje, p_referencia, p_actor_usuario_id, p_payload);
END $$

DROP PROCEDURE IF EXISTS sp_aud_registrar_evento $$
CREATE PROCEDURE sp_aud_registrar_evento(
  IN p_tipo_operacion_codigo VARCHAR(40),
  IN p_id_usuario BIGINT UNSIGNED,
  IN p_rol_usuario_id BIGINT UNSIGNED,
  IN p_producto_tipo VARCHAR(40),
  IN p_producto_id VARCHAR(64),
  IN p_datos_detalle JSON
)
BEGIN
  DECLARE v_tipo_operacion_id BIGINT UNSIGNED;

  SELECT id INTO v_tipo_operacion_id
  FROM cat_tipo_operacion
  WHERE codigo = p_tipo_operacion_codigo AND activo = 1
  LIMIT 1;

  IF v_tipo_operacion_id IS NULL THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Tipo de operacion no existe';
  END IF;

  INSERT INTO aud_bitacora_evento(
    tipo_operacion_id,
    id_usuario,
    rol_usuario_id,
    producto_tipo,
    producto_id,
    datos_detalle,
    hash_integridad
  ) VALUES (
    v_tipo_operacion_id,
    p_id_usuario,
    p_rol_usuario_id,
    p_producto_tipo,
    p_producto_id,
    p_datos_detalle,
    SHA2(CONCAT(p_tipo_operacion_codigo, p_producto_tipo, p_producto_id, NOW(6)), 256)
  );
END $$

DROP PROCEDURE IF EXISTS sp_sec_validar_sesion $$
CREATE PROCEDURE sp_sec_validar_sesion(
  IN p_token VARCHAR(128),
  OUT out_code VARCHAR(32),
  OUT out_message VARCHAR(255),
  OUT out_reference VARCHAR(64)
)
BEGIN
  DECLARE v_sesion_id BIGINT UNSIGNED;

  SELECT s.id INTO v_sesion_id
  FROM sec_sesion s
  JOIN cat_estado_sesion es ON es.id = s.estado_sesion_id
  WHERE s.token = p_token
    AND es.codigo = 'ACTIVA'
    AND s.expira_at > NOW(6)
    AND s.revocada_at IS NULL
  LIMIT 1;

  IF v_sesion_id IS NULL THEN
    SET out_code = 'DOM-SEC-001';
    SET out_message = 'Sesion invalida, expirada o revocada';
    SET out_reference = p_token;
  ELSE
    SET out_code = 'OK';
    SET out_message = 'Sesion valida';
    SET out_reference = CAST(v_sesion_id AS CHAR);
  END IF;
END $$

DROP PROCEDURE IF EXISTS sp_sec_revocar_sesion $$
CREATE PROCEDURE sp_sec_revocar_sesion(
  IN p_token VARCHAR(128),
  IN p_actor_usuario_id BIGINT UNSIGNED,
  OUT out_code VARCHAR(32),
  OUT out_message VARCHAR(255),
  OUT out_reference VARCHAR(64)
)
BEGIN
  DECLARE v_estado_revocada_id BIGINT UNSIGNED;

  SELECT id INTO v_estado_revocada_id FROM cat_estado_sesion WHERE codigo = 'REVOCADA' LIMIT 1;

  UPDATE sec_sesion
     SET estado_sesion_id = v_estado_revocada_id,
         revocada_at = NOW(6),
         updated_at = NOW(6)
   WHERE token = p_token;

  IF ROW_COUNT() = 0 THEN
    SET out_code = 'DOM-SEC-001';
    SET out_message = 'Sesion no encontrada';
    SET out_reference = p_token;
  ELSE
    SET out_code = 'OK';
    SET out_message = 'Sesion revocada';
    SET out_reference = p_token;
    CALL sp_aud_registrar_evento('SEGURIDAD_SESION_REVOCADA', p_actor_usuario_id, NULL, 'USUARIO', CAST(p_actor_usuario_id AS CHAR), JSON_OBJECT('token', p_token));
  END IF;
END $$

DROP PROCEDURE IF EXISTS sp_cli_crear_persona $$
CREATE PROCEDURE sp_cli_crear_persona(
  IN p_tipo_ident_codigo VARCHAR(20),
  IN p_identificacion VARCHAR(30),
  IN p_nombre_completo VARCHAR(180),
  IN p_email VARCHAR(180),
  IN p_telefono VARCHAR(20),
  IN p_fecha_nacimiento DATE,
  IN p_direccion VARCHAR(255),
  IN p_actor_usuario_id BIGINT UNSIGNED,
  OUT out_code VARCHAR(32),
  OUT out_message VARCHAR(255),
  OUT out_reference VARCHAR(64)
)
BEGIN
  DECLARE v_tipo_ident_id BIGINT UNSIGNED;
  DECLARE v_estado_activo_id BIGINT UNSIGNED;
  DECLARE v_persona_id BIGINT UNSIGNED;

  DECLARE EXIT HANDLER FOR 1062
  BEGIN
    SET out_code = 'DOM-CLI-001';
    SET out_message = 'Identificacion ya existe';
    SET out_reference = p_identificacion;
    CALL sp_aud_registrar_error(out_code, 'CLI', out_message, out_reference, p_actor_usuario_id, JSON_OBJECT('tipo_ident', p_tipo_ident_codigo));
    ROLLBACK;
  END;

  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    SET out_code = 'ERR';
    SET out_message = 'Error creando persona';
    SET out_reference = p_identificacion;
    CALL sp_aud_registrar_error('ERR-CLI-000', 'CLI', out_message, out_reference, p_actor_usuario_id, NULL);
    ROLLBACK;
  END;

  SELECT id INTO v_tipo_ident_id FROM cat_tipo_identificacion WHERE codigo = p_tipo_ident_codigo AND activo = 1 LIMIT 1;
  SELECT id INTO v_estado_activo_id FROM cat_estado_usuario WHERE codigo = 'ACTIVO' LIMIT 1;

  IF v_tipo_ident_id IS NULL THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Tipo de identificacion invalido';
  END IF;

  IF TIMESTAMPDIFF(YEAR, p_fecha_nacimiento, CURDATE()) < 18 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cliente debe ser mayor de edad';
  END IF;

  START TRANSACTION;

  INSERT INTO cli_persona_natural(
    tipo_identificacion_id, identificacion, nombre_completo, email, telefono,
    fecha_nacimiento, direccion, estado_id, created_by, updated_by
  ) VALUES (
    v_tipo_ident_id, TRIM(p_identificacion), TRIM(p_nombre_completo), TRIM(p_email), TRIM(p_telefono),
    p_fecha_nacimiento, TRIM(p_direccion), v_estado_activo_id, p_actor_usuario_id, p_actor_usuario_id
  );

  SET v_persona_id = LAST_INSERT_ID();

  CALL sp_aud_registrar_evento('CLIENTE_PERSONA_CREADA', p_actor_usuario_id, NULL, 'CLIENTE', CAST(v_persona_id AS CHAR),
    JSON_OBJECT('identificacion', p_identificacion));

  COMMIT;

  SET out_code = 'OK';
  SET out_message = 'Persona creada';
  SET out_reference = CAST(v_persona_id AS CHAR);
END $$

DROP PROCEDURE IF EXISTS sp_cli_crear_empresa $$
CREATE PROCEDURE sp_cli_crear_empresa(
  IN p_tipo_ident_codigo VARCHAR(20),
  IN p_nit VARCHAR(30),
  IN p_razon_social VARCHAR(180),
  IN p_email VARCHAR(180),
  IN p_telefono VARCHAR(20),
  IN p_direccion VARCHAR(255),
  IN p_representante_persona_id BIGINT UNSIGNED,
  IN p_actor_usuario_id BIGINT UNSIGNED,
  OUT out_code VARCHAR(32),
  OUT out_message VARCHAR(255),
  OUT out_reference VARCHAR(64)
)
BEGIN
  DECLARE v_tipo_ident_id BIGINT UNSIGNED;
  DECLARE v_estado_activo_id BIGINT UNSIGNED;
  DECLARE v_empresa_id BIGINT UNSIGNED;

  DECLARE EXIT HANDLER FOR 1062
  BEGIN
    SET out_code = 'DOM-CLI-001';
    SET out_message = 'NIT ya existe';
    SET out_reference = p_nit;
    CALL sp_aud_registrar_error(out_code, 'CLI', out_message, out_reference, p_actor_usuario_id, JSON_OBJECT('tipo_ident', p_tipo_ident_codigo));
    ROLLBACK;
  END;

  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    SET out_code = 'ERR';
    SET out_message = 'Error creando empresa';
    SET out_reference = p_nit;
    CALL sp_aud_registrar_error('ERR-CLI-001', 'CLI', out_message, out_reference, p_actor_usuario_id, NULL);
    ROLLBACK;
  END;

  SELECT id INTO v_tipo_ident_id FROM cat_tipo_identificacion WHERE codigo = p_tipo_ident_codigo AND activo = 1 LIMIT 1;
  SELECT id INTO v_estado_activo_id FROM cat_estado_usuario WHERE codigo = 'ACTIVO' LIMIT 1;

  IF v_tipo_ident_id IS NULL THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Tipo de identificacion invalido';
  END IF;

  START TRANSACTION;

  INSERT INTO cli_empresa(
    tipo_identificacion_id, nit, razon_social, email, telefono, direccion,
    representante_persona_id, estado_id, created_by, updated_by
  ) VALUES (
    v_tipo_ident_id, TRIM(p_nit), TRIM(p_razon_social), TRIM(p_email), TRIM(p_telefono), TRIM(p_direccion),
    p_representante_persona_id, v_estado_activo_id, p_actor_usuario_id, p_actor_usuario_id
  );

  SET v_empresa_id = LAST_INSERT_ID();

  CALL sp_aud_registrar_evento('CLIENTE_EMPRESA_CREADA', p_actor_usuario_id, NULL, 'CLIENTE', CAST(v_empresa_id AS CHAR), JSON_OBJECT('nit', p_nit));

  COMMIT;

  SET out_code = 'OK';
  SET out_message = 'Empresa creada';
  SET out_reference = CAST(v_empresa_id AS CHAR);
END $$

DROP PROCEDURE IF EXISTS sp_cli_asociar_usuario_empresa $$
CREATE PROCEDURE sp_cli_asociar_usuario_empresa(
  IN p_empresa_id BIGINT UNSIGNED,
  IN p_usuario_id BIGINT UNSIGNED,
  IN p_actor_usuario_id BIGINT UNSIGNED,
  OUT out_code VARCHAR(32),
  OUT out_message VARCHAR(255),
  OUT out_reference VARCHAR(64)
)
BEGIN
  DECLARE v_id BIGINT UNSIGNED;

  START TRANSACTION;

  INSERT INTO cli_empresa_usuario(empresa_id, usuario_id, created_by, updated_by)
  VALUES (p_empresa_id, p_usuario_id, p_actor_usuario_id, p_actor_usuario_id)
  ON DUPLICATE KEY UPDATE activo = 1, updated_by = VALUES(updated_by), updated_at = NOW(6);

  SELECT id INTO v_id FROM cli_empresa_usuario WHERE empresa_id = p_empresa_id AND usuario_id = p_usuario_id LIMIT 1;

  CALL sp_aud_registrar_evento('EMPRESA_USUARIO_ASOCIADO', p_actor_usuario_id, NULL, 'CLIENTE', CAST(p_empresa_id AS CHAR), JSON_OBJECT('empresa_usuario_id', v_id));

  COMMIT;

  SET out_code = 'OK';
  SET out_message = 'Usuario asociado a empresa';
  SET out_reference = CAST(v_id AS CHAR);
END $$

DROP PROCEDURE IF EXISTS sp_cli_cambiar_estado_cliente $$
CREATE PROCEDURE sp_cli_cambiar_estado_cliente(
  IN p_cliente_tipo VARCHAR(20),
  IN p_cliente_id BIGINT UNSIGNED,
  IN p_estado_codigo VARCHAR(40),
  IN p_actor_usuario_id BIGINT UNSIGNED,
  OUT out_code VARCHAR(32),
  OUT out_message VARCHAR(255),
  OUT out_reference VARCHAR(64)
)
BEGIN
  DECLARE v_estado_id BIGINT UNSIGNED;

  SELECT id INTO v_estado_id FROM cat_estado_usuario WHERE codigo = p_estado_codigo LIMIT 1;
  IF v_estado_id IS NULL THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Estado de usuario invalido';
  END IF;

  IF p_cliente_tipo = 'PERSONA' THEN
    UPDATE cli_persona_natural SET estado_id = v_estado_id, updated_by = p_actor_usuario_id WHERE id = p_cliente_id;
  ELSEIF p_cliente_tipo = 'EMPRESA' THEN
    UPDATE cli_empresa SET estado_id = v_estado_id, updated_by = p_actor_usuario_id WHERE id = p_cliente_id;
  ELSE
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Tipo de cliente invalido';
  END IF;

  SET out_code = 'OK';
  SET out_message = 'Estado de cliente actualizado';
  SET out_reference = CAST(p_cliente_id AS CHAR);
END $$

DROP PROCEDURE IF EXISTS sp_cli_asignar_rol_empresa_usuario $$
CREATE PROCEDURE sp_cli_asignar_rol_empresa_usuario(
  IN p_empresa_usuario_id BIGINT UNSIGNED,
  IN p_rol_empresa_codigo VARCHAR(40),
  IN p_actor_usuario_id BIGINT UNSIGNED,
  OUT out_code VARCHAR(32),
  OUT out_message VARCHAR(255),
  OUT out_reference VARCHAR(64)
)
BEGIN
  DECLARE v_rol_empresa_id BIGINT UNSIGNED;

  SELECT id INTO v_rol_empresa_id FROM cat_rol_empresa WHERE codigo = p_rol_empresa_codigo AND activo = 1 LIMIT 1;
  IF v_rol_empresa_id IS NULL THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Rol de empresa invalido';
  END IF;

  INSERT INTO cli_empresa_usuario_rol(empresa_usuario_id, rol_empresa_id, created_by)
  VALUES (p_empresa_usuario_id, v_rol_empresa_id, p_actor_usuario_id)
  ON DUPLICATE KEY UPDATE activo = 1;

  SET out_code = 'OK';
  SET out_message = 'Rol empresarial asignado';
  SET out_reference = CAST(p_empresa_usuario_id AS CHAR);
END $$

DROP PROCEDURE IF EXISTS sp_cta_abrir_cuenta $$
CREATE PROCEDURE sp_cta_abrir_cuenta(
  IN p_titular_tipo VARCHAR(20),
  IN p_titular_id BIGINT UNSIGNED,
  IN p_tipo_cuenta_codigo VARCHAR(40),
  IN p_moneda_codigo VARCHAR(10),
  IN p_limite_sobregiro DECIMAL(18,2),
  IN p_actor_usuario_id BIGINT UNSIGNED,
  OUT out_code VARCHAR(32),
  OUT out_message VARCHAR(255),
  OUT out_reference VARCHAR(64)
)
BEGIN
  DECLARE v_tipo_cuenta_id BIGINT UNSIGNED;
  DECLARE v_moneda_id BIGINT UNSIGNED;
  DECLARE v_estado_activa_id BIGINT UNSIGNED;
  DECLARE v_numero_cuenta VARCHAR(24);
  DECLARE v_cuenta_id BIGINT UNSIGNED;

  SELECT id INTO v_tipo_cuenta_id FROM cat_tipo_cuenta WHERE codigo = p_tipo_cuenta_codigo AND activo = 1 LIMIT 1;
  SELECT id INTO v_moneda_id FROM cat_moneda WHERE codigo = p_moneda_codigo AND activo = 1 LIMIT 1;
  SELECT id INTO v_estado_activa_id FROM cat_estado_cuenta WHERE codigo = 'ACTIVA' LIMIT 1;

  IF v_tipo_cuenta_id IS NULL OR v_moneda_id IS NULL THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Tipo de cuenta o moneda invalido';
  END IF;

  SET v_numero_cuenta = CONCAT('10', DATE_FORMAT(UTC_TIMESTAMP(), '%y%m%d%H%i%s'), LPAD(FLOOR(RAND()*100), 2, '0'));

  START TRANSACTION;

  IF p_titular_tipo = 'PERSONA' THEN
    INSERT INTO cta_cuenta(numero_cuenta, tipo_cuenta_id, titular_tipo, titular_persona_id, saldo_actual, limite_sobregiro_autorizado, moneda_id, estado_id, created_by, updated_by)
    VALUES (v_numero_cuenta, v_tipo_cuenta_id, 'PERSONA', p_titular_id, 0, IFNULL(p_limite_sobregiro,0), v_moneda_id, v_estado_activa_id, p_actor_usuario_id, p_actor_usuario_id);
  ELSEIF p_titular_tipo = 'EMPRESA' THEN
    INSERT INTO cta_cuenta(numero_cuenta, tipo_cuenta_id, titular_tipo, titular_empresa_id, saldo_actual, limite_sobregiro_autorizado, moneda_id, estado_id, created_by, updated_by)
    VALUES (v_numero_cuenta, v_tipo_cuenta_id, 'EMPRESA', p_titular_id, 0, IFNULL(p_limite_sobregiro,0), v_moneda_id, v_estado_activa_id, p_actor_usuario_id, p_actor_usuario_id);
  ELSE
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Titular tipo invalido';
  END IF;

  SET v_cuenta_id = LAST_INSERT_ID();

  CALL sp_aud_registrar_evento('CUENTA_APERTURA', p_actor_usuario_id, NULL, 'CUENTA', CAST(v_cuenta_id AS CHAR), JSON_OBJECT('numero_cuenta', v_numero_cuenta));

  COMMIT;

  SET out_code = 'OK';
  SET out_message = 'Cuenta abierta';
  SET out_reference = CAST(v_cuenta_id AS CHAR);
END $$

DROP PROCEDURE IF EXISTS sp_cta_consignar $$
CREATE PROCEDURE sp_cta_consignar(
  IN p_cuenta_id BIGINT UNSIGNED,
  IN p_monto DECIMAL(18,2),
  IN p_canal_codigo VARCHAR(30),
  IN p_idempotency_key VARCHAR(64),
  IN p_actor_usuario_id BIGINT UNSIGNED,
  OUT out_code VARCHAR(32),
  OUT out_message VARCHAR(255),
  OUT out_reference VARCHAR(64)
)
BEGIN
  DECLARE v_canal_id BIGINT UNSIGNED;
  DECLARE v_tipo_mov_id BIGINT UNSIGNED;
  DECLARE v_tipo_op_id BIGINT UNSIGNED;
  DECLARE v_saldo_antes DECIMAL(18,2);

  SELECT id INTO v_canal_id FROM cat_canal_operacion WHERE codigo = p_canal_codigo AND activo = 1 LIMIT 1;
  SELECT id INTO v_tipo_mov_id FROM cat_tipo_movimiento WHERE codigo = 'CREDITO' LIMIT 1;
  SELECT id INTO v_tipo_op_id FROM cat_tipo_operacion WHERE codigo = 'CONSIGNACION' LIMIT 1;

  IF p_monto <= 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Monto invalido';
  END IF;

  START TRANSACTION;

  SELECT saldo_actual INTO v_saldo_antes
  FROM cta_cuenta
  WHERE id = p_cuenta_id
  FOR UPDATE;

  UPDATE cta_cuenta
     SET saldo_actual = saldo_actual + p_monto,
         updated_by = p_actor_usuario_id
   WHERE id = p_cuenta_id;

  INSERT INTO cta_movimiento(
    cuenta_id, tipo_movimiento_id, tipo_operacion_id, canal_id,
    idempotency_key, monto, saldo_antes, saldo_despues, created_by
  ) VALUES (
    p_cuenta_id, v_tipo_mov_id, v_tipo_op_id, v_canal_id,
    p_idempotency_key, p_monto, v_saldo_antes, v_saldo_antes + p_monto, p_actor_usuario_id
  );

  CALL sp_aud_registrar_evento('CONSIGNACION', p_actor_usuario_id, NULL, 'CUENTA', CAST(p_cuenta_id AS CHAR), JSON_OBJECT('monto', p_monto));

  COMMIT;

  SET out_code = 'OK';
  SET out_message = 'Consignacion ejecutada';
  SET out_reference = CAST(p_cuenta_id AS CHAR);
END $$

DROP PROCEDURE IF EXISTS sp_cta_retirar $$
CREATE PROCEDURE sp_cta_retirar(
  IN p_cuenta_id BIGINT UNSIGNED,
  IN p_monto DECIMAL(18,2),
  IN p_canal_codigo VARCHAR(30),
  IN p_idempotency_key VARCHAR(64),
  IN p_actor_usuario_id BIGINT UNSIGNED,
  OUT out_code VARCHAR(32),
  OUT out_message VARCHAR(255),
  OUT out_reference VARCHAR(64)
)
BEGIN
  DECLARE v_canal_id BIGINT UNSIGNED;
  DECLARE v_tipo_mov_id BIGINT UNSIGNED;
  DECLARE v_tipo_op_id BIGINT UNSIGNED;
  DECLARE v_saldo_antes DECIMAL(18,2);
  DECLARE v_sobregiro DECIMAL(18,2);
  DECLARE v_estado_codigo VARCHAR(40);

  SELECT id INTO v_canal_id FROM cat_canal_operacion WHERE codigo = p_canal_codigo AND activo = 1 LIMIT 1;
  SELECT id INTO v_tipo_mov_id FROM cat_tipo_movimiento WHERE codigo = 'DEBITO' LIMIT 1;
  SELECT id INTO v_tipo_op_id FROM cat_tipo_operacion WHERE codigo = 'RETIRO' LIMIT 1;

  IF p_monto <= 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Monto invalido';
  END IF;

  START TRANSACTION;

  SELECT c.saldo_actual, c.limite_sobregiro_autorizado, ec.codigo
    INTO v_saldo_antes, v_sobregiro, v_estado_codigo
  FROM cta_cuenta c
  JOIN cat_estado_cuenta ec ON ec.id = c.estado_id
  WHERE c.id = p_cuenta_id
  FOR UPDATE;

  IF v_estado_codigo IN ('BLOQUEADA','CANCELADA') THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'DOM-TRF-002';
  END IF;

  IF (v_saldo_antes + v_sobregiro) < p_monto THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'DOM-TRF-001';
  END IF;

  UPDATE cta_cuenta
     SET saldo_actual = saldo_actual - p_monto,
         updated_by = p_actor_usuario_id
   WHERE id = p_cuenta_id;

  INSERT INTO cta_movimiento(
    cuenta_id, tipo_movimiento_id, tipo_operacion_id, canal_id,
    idempotency_key, monto, saldo_antes, saldo_despues, created_by
  ) VALUES (
    p_cuenta_id, v_tipo_mov_id, v_tipo_op_id, v_canal_id,
    p_idempotency_key, p_monto, v_saldo_antes, v_saldo_antes - p_monto, p_actor_usuario_id
  );

  CALL sp_aud_registrar_evento('RETIRO', p_actor_usuario_id, NULL, 'CUENTA', CAST(p_cuenta_id AS CHAR), JSON_OBJECT('monto', p_monto));

  COMMIT;

  SET out_code = 'OK';
  SET out_message = 'Retiro ejecutado';
  SET out_reference = CAST(p_cuenta_id AS CHAR);
END $$

DROP PROCEDURE IF EXISTS sp_cta_bloquear_cuenta $$
CREATE PROCEDURE sp_cta_bloquear_cuenta(
  IN p_cuenta_id BIGINT UNSIGNED,
  IN p_motivo_codigo VARCHAR(40),
  IN p_actor_usuario_id BIGINT UNSIGNED,
  OUT out_code VARCHAR(32),
  OUT out_message VARCHAR(255),
  OUT out_reference VARCHAR(64)
)
BEGIN
  DECLARE v_estado_id BIGINT UNSIGNED;
  DECLARE v_motivo_id BIGINT UNSIGNED;

  SELECT id INTO v_estado_id FROM cat_estado_cuenta WHERE codigo = 'BLOQUEADA' LIMIT 1;
  SELECT id INTO v_motivo_id FROM cat_motivo_bloqueo WHERE codigo = p_motivo_codigo LIMIT 1;

  UPDATE cta_cuenta SET estado_id = v_estado_id, updated_by = p_actor_usuario_id WHERE id = p_cuenta_id;

  CALL sp_aud_registrar_evento('CUENTA_BLOQUEO', p_actor_usuario_id, NULL, 'CUENTA', CAST(p_cuenta_id AS CHAR),
    JSON_OBJECT('motivo_id', v_motivo_id, 'motivo_codigo', p_motivo_codigo));

  SET out_code = 'OK';
  SET out_message = 'Cuenta bloqueada';
  SET out_reference = CAST(p_cuenta_id AS CHAR);
END $$

DROP PROCEDURE IF EXISTS sp_cta_cancelar_cuenta $$
CREATE PROCEDURE sp_cta_cancelar_cuenta(
  IN p_cuenta_id BIGINT UNSIGNED,
  IN p_actor_usuario_id BIGINT UNSIGNED,
  OUT out_code VARCHAR(32),
  OUT out_message VARCHAR(255),
  OUT out_reference VARCHAR(64)
)
BEGIN
  DECLARE v_estado_id BIGINT UNSIGNED;
  DECLARE v_saldo DECIMAL(18,2);

  SELECT id INTO v_estado_id FROM cat_estado_cuenta WHERE codigo = 'CANCELADA' LIMIT 1;
  SELECT saldo_actual INTO v_saldo FROM cta_cuenta WHERE id = p_cuenta_id;

  IF v_saldo <> 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede cancelar cuenta con saldo distinto de cero';
  END IF;

  UPDATE cta_cuenta SET estado_id = v_estado_id, updated_by = p_actor_usuario_id WHERE id = p_cuenta_id;

  CALL sp_aud_registrar_evento('CUENTA_CANCELACION', p_actor_usuario_id, NULL, 'CUENTA', CAST(p_cuenta_id AS CHAR), JSON_OBJECT('estado', 'CANCELADA'));

  SET out_code = 'OK';
  SET out_message = 'Cuenta cancelada';
  SET out_reference = CAST(p_cuenta_id AS CHAR);
END $$

DROP PROCEDURE IF EXISTS sp_cre_solicitar_prestamo $$
CREATE PROCEDURE sp_cre_solicitar_prestamo(
  IN p_cliente_tipo VARCHAR(20),
  IN p_cliente_id BIGINT UNSIGNED,
  IN p_tipo_prestamo_codigo VARCHAR(40),
  IN p_monto_solicitado DECIMAL(18,2),
  IN p_plazo_meses INT,
  IN p_cuenta_destino_id BIGINT UNSIGNED,
  IN p_actor_usuario_id BIGINT UNSIGNED,
  OUT out_code VARCHAR(32),
  OUT out_message VARCHAR(255),
  OUT out_reference VARCHAR(64)
)
BEGIN
  DECLARE v_tipo_prestamo_id BIGINT UNSIGNED;
  DECLARE v_estado_estudio_id BIGINT UNSIGNED;
  DECLARE v_prestamo_id BIGINT UNSIGNED;

  SELECT id INTO v_tipo_prestamo_id FROM cat_tipo_prestamo WHERE codigo = p_tipo_prestamo_codigo AND activo = 1 LIMIT 1;
  SELECT id INTO v_estado_estudio_id FROM cat_estado_prestamo WHERE codigo = 'EN_ESTUDIO' LIMIT 1;

  START TRANSACTION;

  IF p_cliente_tipo = 'PERSONA' THEN
    INSERT INTO cre_prestamo(tipo_prestamo_id, cliente_tipo, cliente_persona_id, monto_solicitado, plazo_meses, estado_id, cuenta_destino_desembolso_id, created_by, updated_by)
    VALUES (v_tipo_prestamo_id, 'PERSONA', p_cliente_id, p_monto_solicitado, p_plazo_meses, v_estado_estudio_id, p_cuenta_destino_id, p_actor_usuario_id, p_actor_usuario_id);
  ELSEIF p_cliente_tipo = 'EMPRESA' THEN
    INSERT INTO cre_prestamo(tipo_prestamo_id, cliente_tipo, cliente_empresa_id, monto_solicitado, plazo_meses, estado_id, cuenta_destino_desembolso_id, created_by, updated_by)
    VALUES (v_tipo_prestamo_id, 'EMPRESA', p_cliente_id, p_monto_solicitado, p_plazo_meses, v_estado_estudio_id, p_cuenta_destino_id, p_actor_usuario_id, p_actor_usuario_id);
  ELSE
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Tipo de cliente invalido';
  END IF;

  SET v_prestamo_id = LAST_INSERT_ID();

  CALL sp_aud_registrar_evento('PRESTAMO_SOLICITUD', p_actor_usuario_id, NULL, 'PRESTAMO', CAST(v_prestamo_id AS CHAR), JSON_OBJECT('monto_solicitado', p_monto_solicitado));

  COMMIT;

  SET out_code = 'OK';
  SET out_message = 'Prestamo en estudio';
  SET out_reference = CAST(v_prestamo_id AS CHAR);
END $$

DROP PROCEDURE IF EXISTS sp_cre_aprobar_prestamo $$
CREATE PROCEDURE sp_cre_aprobar_prestamo(
  IN p_prestamo_id BIGINT UNSIGNED,
  IN p_monto_aprobado DECIMAL(18,2),
  IN p_tasa_interes DECIMAL(7,4),
  IN p_analista_usuario_id BIGINT UNSIGNED,
  OUT out_code VARCHAR(32),
  OUT out_message VARCHAR(255),
  OUT out_reference VARCHAR(64)
)
BEGIN
  DECLARE v_rol_ok INT DEFAULT 0;
  DECLARE v_estado_actual VARCHAR(40);
  DECLARE v_estado_aprobado_id BIGINT UNSIGNED;

  SELECT COUNT(*) INTO v_rol_ok
  FROM sec_usuario_rol ur
  JOIN sec_rol r ON r.id = ur.rol_id
  WHERE ur.usuario_id = p_analista_usuario_id AND r.codigo = 'ANALISTA_INTERNO';

  IF v_rol_ok = 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'DOM-SEC-001';
  END IF;

  SELECT ep.codigo INTO v_estado_actual
  FROM cre_prestamo p
  JOIN cat_estado_prestamo ep ON ep.id = p.estado_id
  WHERE p.id = p_prestamo_id;

  IF v_estado_actual <> 'EN_ESTUDIO' THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'DOM-CRE-001';
  END IF;

  SELECT id INTO v_estado_aprobado_id FROM cat_estado_prestamo WHERE codigo = 'APROBADO' LIMIT 1;

  START TRANSACTION;

  UPDATE cre_prestamo
     SET estado_id = v_estado_aprobado_id,
         monto_aprobado = p_monto_aprobado,
         tasa_interes = p_tasa_interes,
         fecha_aprobacion = NOW(6),
         updated_by = p_analista_usuario_id
   WHERE id = p_prestamo_id;

  INSERT INTO cre_prestamo_aprobacion(prestamo_id, analista_usuario_id, decision)
  VALUES (p_prestamo_id, p_analista_usuario_id, 'APROBADO');

  CALL sp_aud_registrar_evento('PRESTAMO_APROBACION', p_analista_usuario_id, NULL, 'PRESTAMO', CAST(p_prestamo_id AS CHAR), JSON_OBJECT('monto_aprobado', p_monto_aprobado));

  COMMIT;

  SET out_code = 'OK';
  SET out_message = 'Prestamo aprobado';
  SET out_reference = CAST(p_prestamo_id AS CHAR);
END $$

DROP PROCEDURE IF EXISTS sp_cre_rechazar_prestamo $$
CREATE PROCEDURE sp_cre_rechazar_prestamo(
  IN p_prestamo_id BIGINT UNSIGNED,
  IN p_motivo_rechazo_codigo VARCHAR(40),
  IN p_comentario VARCHAR(255),
  IN p_analista_usuario_id BIGINT UNSIGNED,
  OUT out_code VARCHAR(32),
  OUT out_message VARCHAR(255),
  OUT out_reference VARCHAR(64)
)
BEGIN
  DECLARE v_rol_ok INT DEFAULT 0;
  DECLARE v_estado_actual VARCHAR(40);
  DECLARE v_estado_rechazado_id BIGINT UNSIGNED;
  DECLARE v_motivo_id BIGINT UNSIGNED;

  SELECT COUNT(*) INTO v_rol_ok
  FROM sec_usuario_rol ur
  JOIN sec_rol r ON r.id = ur.rol_id
  WHERE ur.usuario_id = p_analista_usuario_id AND r.codigo = 'ANALISTA_INTERNO';

  IF v_rol_ok = 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'DOM-SEC-001';
  END IF;

  SELECT ep.codigo INTO v_estado_actual
  FROM cre_prestamo p
  JOIN cat_estado_prestamo ep ON ep.id = p.estado_id
  WHERE p.id = p_prestamo_id;

  IF v_estado_actual <> 'EN_ESTUDIO' THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'DOM-CRE-001';
  END IF;

  SELECT id INTO v_estado_rechazado_id FROM cat_estado_prestamo WHERE codigo = 'RECHAZADO' LIMIT 1;
  SELECT id INTO v_motivo_id FROM cat_motivo_rechazo WHERE codigo = p_motivo_rechazo_codigo LIMIT 1;

  START TRANSACTION;

  UPDATE cre_prestamo
     SET estado_id = v_estado_rechazado_id,
         updated_by = p_analista_usuario_id
   WHERE id = p_prestamo_id;

  INSERT INTO cre_prestamo_aprobacion(prestamo_id, analista_usuario_id, decision, motivo_rechazo_id, comentario)
  VALUES (p_prestamo_id, p_analista_usuario_id, 'RECHAZADO', v_motivo_id, p_comentario);

  CALL sp_aud_registrar_evento('PRESTAMO_RECHAZO', p_analista_usuario_id, NULL, 'PRESTAMO', CAST(p_prestamo_id AS CHAR), JSON_OBJECT('motivo', p_motivo_rechazo_codigo));

  COMMIT;

  SET out_code = 'OK';
  SET out_message = 'Prestamo rechazado';
  SET out_reference = CAST(p_prestamo_id AS CHAR);
END $$

DROP PROCEDURE IF EXISTS sp_cre_desembolsar_prestamo $$
CREATE PROCEDURE sp_cre_desembolsar_prestamo(
  IN p_prestamo_id BIGINT UNSIGNED,
  IN p_analista_usuario_id BIGINT UNSIGNED,
  OUT out_code VARCHAR(32),
  OUT out_message VARCHAR(255),
  OUT out_reference VARCHAR(64)
)
BEGIN
  DECLARE v_rol_ok INT DEFAULT 0;
  DECLARE v_estado_actual VARCHAR(40);
  DECLARE v_estado_desembolsado_id BIGINT UNSIGNED;
  DECLARE v_cuenta_id BIGINT UNSIGNED;
  DECLARE v_monto_aprobado DECIMAL(18,2);
  DECLARE v_saldo_antes DECIMAL(18,2);
  DECLARE v_tipo_mov_id BIGINT UNSIGNED;
  DECLARE v_tipo_op_id BIGINT UNSIGNED;
  DECLARE v_canal_id BIGINT UNSIGNED;

  SELECT COUNT(*) INTO v_rol_ok
  FROM sec_usuario_rol ur
  JOIN sec_rol r ON r.id = ur.rol_id
  WHERE ur.usuario_id = p_analista_usuario_id AND r.codigo = 'ANALISTA_INTERNO';

  IF v_rol_ok = 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'DOM-SEC-001';
  END IF;

  SELECT ep.codigo, p.cuenta_destino_desembolso_id, p.monto_aprobado
    INTO v_estado_actual, v_cuenta_id, v_monto_aprobado
  FROM cre_prestamo p
  JOIN cat_estado_prestamo ep ON ep.id = p.estado_id
  WHERE p.id = p_prestamo_id
  FOR UPDATE;

  IF v_estado_actual <> 'APROBADO' THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'DOM-CRE-001';
  END IF;

  IF v_cuenta_id IS NULL OR v_monto_aprobado IS NULL OR v_monto_aprobado <= 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'DOM-CRE-002';
  END IF;

  SELECT id INTO v_estado_desembolsado_id FROM cat_estado_prestamo WHERE codigo = 'DESEMBOLSADO' LIMIT 1;
  SELECT id INTO v_tipo_mov_id FROM cat_tipo_movimiento WHERE codigo = 'CREDITO' LIMIT 1;
  SELECT id INTO v_tipo_op_id FROM cat_tipo_operacion WHERE codigo = 'PRESTAMO_DESEMBOLSO' LIMIT 1;
  SELECT id INTO v_canal_id FROM cat_canal_operacion WHERE codigo = 'BACKOFFICE' LIMIT 1;

  START TRANSACTION;

  SELECT saldo_actual INTO v_saldo_antes FROM cta_cuenta WHERE id = v_cuenta_id FOR UPDATE;

  UPDATE cta_cuenta
     SET saldo_actual = saldo_actual + v_monto_aprobado,
         updated_by = p_analista_usuario_id
   WHERE id = v_cuenta_id;

  INSERT INTO cta_movimiento(
    cuenta_id, tipo_movimiento_id, tipo_operacion_id, canal_id,
    referencia_externa, monto, saldo_antes, saldo_despues, created_by
  ) VALUES (
    v_cuenta_id, v_tipo_mov_id, v_tipo_op_id, v_canal_id,
    CONCAT('PRESTAMO-', p_prestamo_id), v_monto_aprobado, v_saldo_antes, v_saldo_antes + v_monto_aprobado, p_analista_usuario_id
  );

  UPDATE cre_prestamo
     SET estado_id = v_estado_desembolsado_id,
         fecha_desembolso = NOW(6),
         updated_by = p_analista_usuario_id
   WHERE id = p_prestamo_id;

  INSERT INTO cre_prestamo_desembolso(prestamo_id, analista_usuario_id, cuenta_destino_id, monto)
  VALUES (p_prestamo_id, p_analista_usuario_id, v_cuenta_id, v_monto_aprobado);

  CALL sp_aud_registrar_evento('PRESTAMO_DESEMBOLSO', p_analista_usuario_id, NULL, 'PRESTAMO', CAST(p_prestamo_id AS CHAR), JSON_OBJECT('monto', v_monto_aprobado));

  COMMIT;

  SET out_code = 'OK';
  SET out_message = 'Prestamo desembolsado';
  SET out_reference = CAST(p_prestamo_id AS CHAR);
END $$

DROP PROCEDURE IF EXISTS sp_trf_crear_transferencia $$
CREATE PROCEDURE sp_trf_crear_transferencia(
  IN p_cuenta_origen_id BIGINT UNSIGNED,
  IN p_cuenta_destino_id BIGINT UNSIGNED,
  IN p_empresa_id BIGINT UNSIGNED,
  IN p_monto DECIMAL(18,2),
  IN p_canal_codigo VARCHAR(30),
  IN p_idempotency_key VARCHAR(64),
  IN p_creador_usuario_id BIGINT UNSIGNED,
  OUT out_code VARCHAR(32),
  OUT out_message VARCHAR(255),
  OUT out_reference VARCHAR(64)
)
BEGIN
  DECLARE v_canal_id BIGINT UNSIGNED;
  DECLARE v_estado_pend_id BIGINT UNSIGNED;
  DECLARE v_estado_ejec_id BIGINT UNSIGNED;
  DECLARE v_umbral DECIMAL(18,2);
  DECLARE v_estado_id BIGINT UNSIGNED;
  DECLARE v_transferencia_id BIGINT UNSIGNED;
  DECLARE v_saldo_origen DECIMAL(18,2);
  DECLARE v_sobregiro DECIMAL(18,2);
  DECLARE v_estado_origen_codigo VARCHAR(40);
  DECLARE v_tipo_mov_debito BIGINT UNSIGNED;
  DECLARE v_tipo_mov_credito BIGINT UNSIGNED;
  DECLARE v_tipo_op_trf BIGINT UNSIGNED;
  DECLARE v_saldo_destino DECIMAL(18,2);

  DECLARE EXIT HANDLER FOR 1062
  BEGIN
    SELECT id INTO v_transferencia_id FROM trf_transferencia WHERE idempotency_key = p_idempotency_key AND canal_id = v_canal_id LIMIT 1;
    SET out_code = 'OK';
    SET out_message = 'Transferencia ya registrada por idempotencia';
    SET out_reference = CAST(v_transferencia_id AS CHAR);
    ROLLBACK;
  END;

  SELECT id INTO v_canal_id FROM cat_canal_operacion WHERE codigo = p_canal_codigo AND activo = 1 LIMIT 1;
  SELECT id INTO v_estado_pend_id FROM cat_estado_transferencia WHERE codigo = 'EN_ESPERA_APROBACION' LIMIT 1;
  SELECT id INTO v_estado_ejec_id FROM cat_estado_transferencia WHERE codigo = 'EJECUTADA' LIMIT 1;
  SELECT COALESCE(valor_numerico, 10000.00) INTO v_umbral FROM cat_parametro_negocio WHERE codigo = 'UMBRAL_TRANSFERENCIA_ALTA' LIMIT 1;
  SELECT id INTO v_tipo_mov_debito FROM cat_tipo_movimiento WHERE codigo = 'DEBITO' LIMIT 1;
  SELECT id INTO v_tipo_mov_credito FROM cat_tipo_movimiento WHERE codigo = 'CREDITO' LIMIT 1;
  SELECT id INTO v_tipo_op_trf FROM cat_tipo_operacion WHERE codigo = 'TRANSFERENCIA' LIMIT 1;

  IF p_monto <= 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Monto invalido';
  END IF;

  IF p_monto > v_umbral THEN
    SET v_estado_id = v_estado_pend_id;
  ELSE
    SET v_estado_id = v_estado_ejec_id;
  END IF;

  START TRANSACTION;

  INSERT INTO trf_transferencia(
    cuenta_origen_id, cuenta_destino_id, empresa_id, monto, estado_id,
    creador_usuario_id, canal_id, idempotency_key, created_by, updated_by
  ) VALUES (
    p_cuenta_origen_id, p_cuenta_destino_id, p_empresa_id, p_monto, v_estado_id,
    p_creador_usuario_id, v_canal_id, p_idempotency_key, p_creador_usuario_id, p_creador_usuario_id
  );

  SET v_transferencia_id = LAST_INSERT_ID();

  IF v_estado_id = v_estado_ejec_id THEN
    SELECT c.saldo_actual, c.limite_sobregiro_autorizado, ec.codigo
      INTO v_saldo_origen, v_sobregiro, v_estado_origen_codigo
    FROM cta_cuenta c
    JOIN cat_estado_cuenta ec ON ec.id = c.estado_id
    WHERE c.id = p_cuenta_origen_id
    FOR UPDATE;

    IF v_estado_origen_codigo IN ('BLOQUEADA','CANCELADA') THEN
      SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'DOM-TRF-002';
    END IF;

    IF (v_saldo_origen + v_sobregiro) < p_monto THEN
      SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'DOM-TRF-001';
    END IF;

    SELECT saldo_actual INTO v_saldo_destino FROM cta_cuenta WHERE id = p_cuenta_destino_id FOR UPDATE;

    UPDATE cta_cuenta SET saldo_actual = saldo_actual - p_monto, updated_by = p_creador_usuario_id WHERE id = p_cuenta_origen_id;
    UPDATE cta_cuenta SET saldo_actual = saldo_actual + p_monto, updated_by = p_creador_usuario_id WHERE id = p_cuenta_destino_id;

    INSERT INTO cta_movimiento(cuenta_id, tipo_movimiento_id, tipo_operacion_id, canal_id, referencia_externa, idempotency_key, monto, saldo_antes, saldo_despues, created_by)
    VALUES (p_cuenta_origen_id, v_tipo_mov_debito, v_tipo_op_trf, v_canal_id, CONCAT('TRF-', v_transferencia_id), p_idempotency_key, p_monto, v_saldo_origen, v_saldo_origen - p_monto, p_creador_usuario_id);

    INSERT INTO cta_movimiento(cuenta_id, tipo_movimiento_id, tipo_operacion_id, canal_id, referencia_externa, idempotency_key, monto, saldo_antes, saldo_despues, created_by)
    VALUES (p_cuenta_destino_id, v_tipo_mov_credito, v_tipo_op_trf, v_canal_id, CONCAT('TRF-', v_transferencia_id), CONCAT(p_idempotency_key, '-D'), p_monto, v_saldo_destino, v_saldo_destino + p_monto, p_creador_usuario_id);
  END IF;

  CALL sp_aud_registrar_evento('TRANSFERENCIA_CREADA', p_creador_usuario_id, NULL, 'TRANSFERENCIA', CAST(v_transferencia_id AS CHAR),
    JSON_OBJECT('estado_id', v_estado_id, 'monto', p_monto));

  COMMIT;

  SET out_code = 'OK';
  SET out_message = 'Transferencia creada';
  SET out_reference = CAST(v_transferencia_id AS CHAR);
END $$

DROP PROCEDURE IF EXISTS sp_trf_aprobar_transferencia $$
CREATE PROCEDURE sp_trf_aprobar_transferencia(
  IN p_transferencia_id BIGINT UNSIGNED,
  IN p_aprobador_usuario_id BIGINT UNSIGNED,
  OUT out_code VARCHAR(32),
  OUT out_message VARCHAR(255),
  OUT out_reference VARCHAR(64)
)
BEGIN
  DECLARE v_creador_id BIGINT UNSIGNED;
  DECLARE v_empresa_id BIGINT UNSIGNED;
  DECLARE v_estado_codigo VARCHAR(40);
  DECLARE v_origen BIGINT UNSIGNED;
  DECLARE v_destino BIGINT UNSIGNED;
  DECLARE v_monto DECIMAL(18,2);
  DECLARE v_saldo_origen DECIMAL(18,2);
  DECLARE v_sobregiro DECIMAL(18,2);
  DECLARE v_estado_origen_codigo VARCHAR(40);
  DECLARE v_saldo_destino DECIMAL(18,2);
  DECLARE v_es_supervisor INT DEFAULT 0;
  DECLARE v_estado_ejec_id BIGINT UNSIGNED;
  DECLARE v_tipo_mov_debito BIGINT UNSIGNED;
  DECLARE v_tipo_mov_credito BIGINT UNSIGNED;
  DECLARE v_tipo_op_trf BIGINT UNSIGNED;
  DECLARE v_canal_id BIGINT UNSIGNED;

  SELECT t.creador_usuario_id, t.empresa_id, et.codigo, t.cuenta_origen_id, t.cuenta_destino_id, t.monto, t.canal_id
    INTO v_creador_id, v_empresa_id, v_estado_codigo, v_origen, v_destino, v_monto, v_canal_id
  FROM trf_transferencia t
  JOIN cat_estado_transferencia et ON et.id = t.estado_id
  WHERE t.id = p_transferencia_id
  FOR UPDATE;

  IF v_estado_codigo <> 'EN_ESPERA_APROBACION' THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'DOM-TRF-003';
  END IF;

  IF p_aprobador_usuario_id = v_creador_id THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Maker-checker violado';
  END IF;

  SELECT COUNT(*) INTO v_es_supervisor
  FROM cli_empresa_usuario eu
  JOIN cli_empresa_usuario_rol eur ON eur.empresa_usuario_id = eu.id AND eur.activo = 1
  JOIN cat_rol_empresa re ON re.id = eur.rol_empresa_id
  WHERE eu.empresa_id = v_empresa_id
    AND eu.usuario_id = p_aprobador_usuario_id
    AND eu.activo = 1
    AND re.codigo IN ('SUPERVISOR_APROBADOR','REPRESENTANTE_LEGAL');

  IF v_es_supervisor = 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'DOM-SEC-001';
  END IF;

  SELECT id INTO v_estado_ejec_id FROM cat_estado_transferencia WHERE codigo = 'EJECUTADA' LIMIT 1;
  SELECT id INTO v_tipo_mov_debito FROM cat_tipo_movimiento WHERE codigo = 'DEBITO' LIMIT 1;
  SELECT id INTO v_tipo_mov_credito FROM cat_tipo_movimiento WHERE codigo = 'CREDITO' LIMIT 1;
  SELECT id INTO v_tipo_op_trf FROM cat_tipo_operacion WHERE codigo = 'TRANSFERENCIA' LIMIT 1;

  START TRANSACTION;

  SELECT c.saldo_actual, c.limite_sobregiro_autorizado, ec.codigo
    INTO v_saldo_origen, v_sobregiro, v_estado_origen_codigo
  FROM cta_cuenta c
  JOIN cat_estado_cuenta ec ON ec.id = c.estado_id
  WHERE c.id = v_origen
  FOR UPDATE;

  IF v_estado_origen_codigo IN ('BLOQUEADA','CANCELADA') THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'DOM-TRF-002';
  END IF;

  IF (v_saldo_origen + v_sobregiro) < v_monto THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'DOM-TRF-001';
  END IF;

  SELECT saldo_actual INTO v_saldo_destino FROM cta_cuenta WHERE id = v_destino FOR UPDATE;

  UPDATE cta_cuenta SET saldo_actual = saldo_actual - v_monto, updated_by = p_aprobador_usuario_id WHERE id = v_origen;
  UPDATE cta_cuenta SET saldo_actual = saldo_actual + v_monto, updated_by = p_aprobador_usuario_id WHERE id = v_destino;

  UPDATE trf_transferencia
     SET estado_id = v_estado_ejec_id,
         aprobador_usuario_id = p_aprobador_usuario_id,
         fecha_aprobacion = NOW(6),
         updated_by = p_aprobador_usuario_id
   WHERE id = p_transferencia_id;

  INSERT INTO trf_transferencia_aprobacion(transferencia_id, aprobador_usuario_id, decision)
  VALUES (p_transferencia_id, p_aprobador_usuario_id, 'APROBADA');

  INSERT INTO cta_movimiento(cuenta_id, tipo_movimiento_id, tipo_operacion_id, canal_id, referencia_externa, monto, saldo_antes, saldo_despues, created_by)
  VALUES (v_origen, v_tipo_mov_debito, v_tipo_op_trf, v_canal_id, CONCAT('TRF-', p_transferencia_id), v_monto, v_saldo_origen, v_saldo_origen - v_monto, p_aprobador_usuario_id);

  INSERT INTO cta_movimiento(cuenta_id, tipo_movimiento_id, tipo_operacion_id, canal_id, referencia_externa, monto, saldo_antes, saldo_despues, created_by)
  VALUES (v_destino, v_tipo_mov_credito, v_tipo_op_trf, v_canal_id, CONCAT('TRF-', p_transferencia_id), v_monto, v_saldo_destino, v_saldo_destino + v_monto, p_aprobador_usuario_id);

  CALL sp_aud_registrar_evento('TRANSFERENCIA_APROBADA', p_aprobador_usuario_id, NULL, 'TRANSFERENCIA', CAST(p_transferencia_id AS CHAR), JSON_OBJECT('monto', v_monto));

  COMMIT;

  SET out_code = 'OK';
  SET out_message = 'Transferencia aprobada y ejecutada';
  SET out_reference = CAST(p_transferencia_id AS CHAR);
END $$

DROP PROCEDURE IF EXISTS sp_trf_rechazar_transferencia $$
CREATE PROCEDURE sp_trf_rechazar_transferencia(
  IN p_transferencia_id BIGINT UNSIGNED,
  IN p_aprobador_usuario_id BIGINT UNSIGNED,
  IN p_motivo_rechazo_codigo VARCHAR(40),
  IN p_comentario VARCHAR(255),
  OUT out_code VARCHAR(32),
  OUT out_message VARCHAR(255),
  OUT out_reference VARCHAR(64)
)
BEGIN
  DECLARE v_estado_codigo VARCHAR(40);
  DECLARE v_estado_rech_id BIGINT UNSIGNED;
  DECLARE v_motivo_id BIGINT UNSIGNED;

  SELECT et.codigo INTO v_estado_codigo
  FROM trf_transferencia t
  JOIN cat_estado_transferencia et ON et.id = t.estado_id
  WHERE t.id = p_transferencia_id;

  IF v_estado_codigo <> 'EN_ESPERA_APROBACION' THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'DOM-TRF-003';
  END IF;

  SELECT id INTO v_estado_rech_id FROM cat_estado_transferencia WHERE codigo = 'RECHAZADA' LIMIT 1;
  SELECT id INTO v_motivo_id FROM cat_motivo_rechazo WHERE codigo = p_motivo_rechazo_codigo LIMIT 1;

  UPDATE trf_transferencia
     SET estado_id = v_estado_rech_id,
         aprobador_usuario_id = p_aprobador_usuario_id,
         fecha_aprobacion = NOW(6),
         updated_by = p_aprobador_usuario_id
   WHERE id = p_transferencia_id;

  INSERT INTO trf_transferencia_aprobacion(transferencia_id, aprobador_usuario_id, decision, motivo_rechazo_id, comentario)
  VALUES (p_transferencia_id, p_aprobador_usuario_id, 'RECHAZADA', v_motivo_id, p_comentario);

  CALL sp_aud_registrar_evento('TRANSFERENCIA_RECHAZADA', p_aprobador_usuario_id, NULL, 'TRANSFERENCIA', CAST(p_transferencia_id AS CHAR), JSON_OBJECT('motivo', p_motivo_rechazo_codigo));

  SET out_code = 'OK';
  SET out_message = 'Transferencia rechazada';
  SET out_reference = CAST(p_transferencia_id AS CHAR);
END $$

DROP PROCEDURE IF EXISTS sp_trf_ejecutar_transferencia_directa $$
CREATE PROCEDURE sp_trf_ejecutar_transferencia_directa(
  IN p_cuenta_origen_id BIGINT UNSIGNED,
  IN p_cuenta_destino_id BIGINT UNSIGNED,
  IN p_monto DECIMAL(18,2),
  IN p_canal_codigo VARCHAR(30),
  IN p_idempotency_key VARCHAR(64),
  IN p_usuario_id BIGINT UNSIGNED,
  OUT out_code VARCHAR(32),
  OUT out_message VARCHAR(255),
  OUT out_reference VARCHAR(64)
)
BEGIN
  CALL sp_trf_crear_transferencia(
    p_cuenta_origen_id,
    p_cuenta_destino_id,
    NULL,
    p_monto,
    p_canal_codigo,
    p_idempotency_key,
    p_usuario_id,
    out_code,
    out_message,
    out_reference
  );
END $$

DROP PROCEDURE IF EXISTS sp_trf_vencer_transferencias_pendientes $$
CREATE PROCEDURE sp_trf_vencer_transferencias_pendientes(
  IN p_actor_usuario_id BIGINT UNSIGNED,
  OUT out_code VARCHAR(32),
  OUT out_message VARCHAR(255),
  OUT out_reference VARCHAR(64)
)
BEGIN
  DECLARE v_estado_pend_id BIGINT UNSIGNED;
  DECLARE v_estado_venc_id BIGINT UNSIGNED;

  SELECT id INTO v_estado_pend_id FROM cat_estado_transferencia WHERE codigo = 'EN_ESPERA_APROBACION' LIMIT 1;
  SELECT id INTO v_estado_venc_id FROM cat_estado_transferencia WHERE codigo = 'VENCIDA' LIMIT 1;

  UPDATE trf_transferencia
     SET estado_id = v_estado_venc_id,
         updated_by = p_actor_usuario_id
   WHERE estado_id = v_estado_pend_id
     AND fecha_creacion < (NOW(6) - INTERVAL 60 MINUTE);

  SET out_code = 'OK';
  SET out_message = 'Transferencias pendientes vencidas';
  SET out_reference = CAST(ROW_COUNT() AS CHAR);
END $$

DROP PROCEDURE IF EXISTS sp_trf_consultar_pendientes_aprobacion_empresa $$
CREATE PROCEDURE sp_trf_consultar_pendientes_aprobacion_empresa(
  IN p_empresa_id BIGINT UNSIGNED
)
BEGIN
  SELECT t.id,
         t.cuenta_origen_id,
         t.cuenta_destino_id,
         t.monto,
         t.fecha_creacion,
         u.nombre_completo AS creado_por
  FROM trf_transferencia t
  JOIN cat_estado_transferencia et ON et.id = t.estado_id
  JOIN sec_usuario u ON u.id = t.creador_usuario_id
  WHERE t.empresa_id = p_empresa_id
    AND et.codigo = 'EN_ESPERA_APROBACION'
  ORDER BY t.fecha_creacion ASC;
END $$

DELIMITER ;
