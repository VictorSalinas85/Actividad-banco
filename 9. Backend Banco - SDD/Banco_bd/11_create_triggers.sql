-- V005__triggers_auditoria.sql
USE banco_core;

DELIMITER $$

-- cta_cuenta: inmutables y versionado
CREATE TRIGGER trg_cta_cuenta_bu
BEFORE UPDATE ON cta_cuenta
FOR EACH ROW
BEGIN
  IF NEW.created_at <> OLD.created_at OR NEW.created_by <> OLD.created_by THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'created_at/created_by son inmutables';
  END IF;
  IF NEW.numero_cuenta <> OLD.numero_cuenta THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'numero_cuenta es inmutable';
  END IF;
  SET NEW.updated_at = CURRENT_TIMESTAMP(6);
  SET NEW.row_version = OLD.row_version + 1;
END$$

-- cli_persona_natural: inmutable identificacion
CREATE TRIGGER trg_cli_persona_bu
BEFORE UPDATE ON cli_persona_natural
FOR EACH ROW
BEGIN
  IF NEW.created_at <> OLD.created_at OR NEW.created_by <> OLD.created_by THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'created_at/created_by son inmutables';
  END IF;
  IF NEW.tipo_identificacion_id <> OLD.tipo_identificacion_id OR NEW.identificacion <> OLD.identificacion THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'identificacion es inmutable';
  END IF;
  SET NEW.updated_at = CURRENT_TIMESTAMP(6);
  SET NEW.row_version = OLD.row_version + 1;
END$$

-- cli_empresa: inmutable NIT
CREATE TRIGGER trg_cli_empresa_bu
BEFORE UPDATE ON cli_empresa
FOR EACH ROW
BEGIN
  IF NEW.created_at <> OLD.created_at OR NEW.created_by <> OLD.created_by THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'created_at/created_by son inmutables';
  END IF;
  IF NEW.tipo_identificacion_id <> OLD.tipo_identificacion_id OR NEW.nit <> OLD.nit THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'nit es inmutable';
  END IF;
  SET NEW.updated_at = CURRENT_TIMESTAMP(6);
  SET NEW.row_version = OLD.row_version + 1;
END$$

-- sec_usuario: versionado tecnico
CREATE TRIGGER trg_sec_usuario_bu
BEFORE UPDATE ON sec_usuario
FOR EACH ROW
BEGIN
  IF NEW.created_at <> OLD.created_at OR NEW.created_by <> OLD.created_by THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'created_at/created_by son inmutables';
  END IF;
  SET NEW.updated_at = CURRENT_TIMESTAMP(6);
  SET NEW.row_version = OLD.row_version + 1;
END$$

-- cre_prestamo: versionado tecnico
CREATE TRIGGER trg_cre_prestamo_bu
BEFORE UPDATE ON cre_prestamo
FOR EACH ROW
BEGIN
  IF NEW.created_at <> OLD.created_at OR NEW.created_by <> OLD.created_by THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'created_at/created_by son inmutables';
  END IF;
  SET NEW.updated_at = CURRENT_TIMESTAMP(6);
  SET NEW.row_version = OLD.row_version + 1;
END$$

-- trf_transferencia: versionado tecnico
CREATE TRIGGER trg_trf_transferencia_bu
BEFORE UPDATE ON trf_transferencia
FOR EACH ROW
BEGIN
  IF NEW.created_at <> OLD.created_at OR NEW.created_by <> OLD.created_by THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'created_at/created_by son inmutables';
  END IF;
  SET NEW.updated_at = CURRENT_TIMESTAMP(6);
  SET NEW.row_version = OLD.row_version + 1;
END$$

-- auditoria tecnica de cambios en cuenta
CREATE TRIGGER trg_cta_cuenta_ai
AFTER INSERT ON cta_cuenta
FOR EACH ROW
BEGIN
  INSERT INTO aud_cambio_dato(tabla, registro_id, accion, old_data, new_data, sql_user, host_name, trx_ref)
  VALUES (
    'cta_cuenta', CAST(NEW.id AS CHAR), 'INSERT',
    NULL,
    JSON_OBJECT('numero_cuenta', NEW.numero_cuenta, 'saldo_actual', NEW.saldo_actual, 'estado_id', NEW.estado_id),
    CURRENT_USER(), @@hostname, CONNECTION_ID()
  );
END$$

CREATE TRIGGER trg_cta_cuenta_au
AFTER UPDATE ON cta_cuenta
FOR EACH ROW
BEGIN
  INSERT INTO aud_cambio_dato(tabla, registro_id, accion, old_data, new_data, sql_user, host_name, trx_ref)
  VALUES (
    'cta_cuenta', CAST(NEW.id AS CHAR), 'UPDATE',
    JSON_OBJECT('saldo_actual', OLD.saldo_actual, 'estado_id', OLD.estado_id),
    JSON_OBJECT('saldo_actual', NEW.saldo_actual, 'estado_id', NEW.estado_id),
    CURRENT_USER(), @@hostname, CONNECTION_ID()
  );
END$$

-- auditoria tecnica de cambios en prestamo
CREATE TRIGGER trg_cre_prestamo_ai
AFTER INSERT ON cre_prestamo
FOR EACH ROW
BEGIN
  INSERT INTO aud_cambio_dato(tabla, registro_id, accion, old_data, new_data, sql_user, host_name, trx_ref)
  VALUES (
    'cre_prestamo', CAST(NEW.id AS CHAR), 'INSERT', NULL,
    JSON_OBJECT('estado_id', NEW.estado_id, 'monto_solicitado', NEW.monto_solicitado),
    CURRENT_USER(), @@hostname, CONNECTION_ID()
  );
END$$

CREATE TRIGGER trg_cre_prestamo_au
AFTER UPDATE ON cre_prestamo
FOR EACH ROW
BEGIN
  INSERT INTO aud_cambio_dato(tabla, registro_id, accion, old_data, new_data, sql_user, host_name, trx_ref)
  VALUES (
    'cre_prestamo', CAST(NEW.id AS CHAR), 'UPDATE',
    JSON_OBJECT('estado_id', OLD.estado_id, 'monto_aprobado', OLD.monto_aprobado),
    JSON_OBJECT('estado_id', NEW.estado_id, 'monto_aprobado', NEW.monto_aprobado),
    CURRENT_USER(), @@hostname, CONNECTION_ID()
  );
END$$

-- auditoria tecnica de cambios en transferencia
CREATE TRIGGER trg_trf_transferencia_ai
AFTER INSERT ON trf_transferencia
FOR EACH ROW
BEGIN
  INSERT INTO aud_cambio_dato(tabla, registro_id, accion, old_data, new_data, sql_user, host_name, trx_ref)
  VALUES (
    'trf_transferencia', CAST(NEW.id AS CHAR), 'INSERT', NULL,
    JSON_OBJECT('estado_id', NEW.estado_id, 'monto', NEW.monto),
    CURRENT_USER(), @@hostname, CONNECTION_ID()
  );
END$$

CREATE TRIGGER trg_trf_transferencia_au
AFTER UPDATE ON trf_transferencia
FOR EACH ROW
BEGIN
  INSERT INTO aud_cambio_dato(tabla, registro_id, accion, old_data, new_data, sql_user, host_name, trx_ref)
  VALUES (
    'trf_transferencia', CAST(NEW.id AS CHAR), 'UPDATE',
    JSON_OBJECT('estado_id', OLD.estado_id, 'aprobador_usuario_id', OLD.aprobador_usuario_id),
    JSON_OBJECT('estado_id', NEW.estado_id, 'aprobador_usuario_id', NEW.aprobador_usuario_id),
    CURRENT_USER(), @@hostname, CONNECTION_ID()
  );
END$$

-- bitacora de negocio inmutable
CREATE TRIGGER trg_aud_bitacora_bu
BEFORE UPDATE ON aud_bitacora_evento
FOR EACH ROW
BEGIN
  SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'aud_bitacora_evento es inmutable';
END$$

CREATE TRIGGER trg_aud_bitacora_bd
BEFORE DELETE ON aud_bitacora_evento
FOR EACH ROW
BEGIN
  SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'aud_bitacora_evento es inmutable';
END$$

DELIMITER ;
