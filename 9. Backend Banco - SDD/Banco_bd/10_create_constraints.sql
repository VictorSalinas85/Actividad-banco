-- 10_create_constraints.sql
USE banco_core;

-- Seguridad
ALTER TABLE sec_usuario
  ADD CONSTRAINT fk_sec_usuario_estado FOREIGN KEY (estado_id) REFERENCES cat_estado_usuario(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE sec_usuario_rol
  ADD CONSTRAINT fk_sec_ur_usuario FOREIGN KEY (usuario_id) REFERENCES sec_usuario(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_sec_ur_rol FOREIGN KEY (rol_id) REFERENCES sec_rol(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE sec_sesion
  ADD CONSTRAINT fk_sec_sesion_usuario FOREIGN KEY (usuario_id) REFERENCES sec_usuario(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_sec_sesion_estado FOREIGN KEY (estado_sesion_id) REFERENCES cat_estado_sesion(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT;

-- Clientes
ALTER TABLE cli_persona_natural
  ADD CONSTRAINT fk_cli_persona_tipo_ident FOREIGN KEY (tipo_identificacion_id) REFERENCES cat_tipo_identificacion(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_cli_persona_estado FOREIGN KEY (estado_id) REFERENCES cat_estado_usuario(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE cli_empresa
  ADD CONSTRAINT fk_cli_empresa_tipo_ident FOREIGN KEY (tipo_identificacion_id) REFERENCES cat_tipo_identificacion(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_cli_empresa_rep FOREIGN KEY (representante_persona_id) REFERENCES cli_persona_natural(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_cli_empresa_estado FOREIGN KEY (estado_id) REFERENCES cat_estado_usuario(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE cli_empresa_usuario
  ADD CONSTRAINT fk_cli_emp_user_empresa FOREIGN KEY (empresa_id) REFERENCES cli_empresa(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_cli_emp_user_usuario FOREIGN KEY (usuario_id) REFERENCES sec_usuario(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE cli_empresa_usuario_rol
  ADD CONSTRAINT fk_cli_eur_empresa_usuario FOREIGN KEY (empresa_usuario_id) REFERENCES cli_empresa_usuario(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_cli_eur_rol FOREIGN KEY (rol_empresa_id) REFERENCES cat_rol_empresa(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT;

-- Cuentas
ALTER TABLE cta_cuenta
  ADD CONSTRAINT fk_cta_tipo FOREIGN KEY (tipo_cuenta_id) REFERENCES cat_tipo_cuenta(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_cta_persona FOREIGN KEY (titular_persona_id) REFERENCES cli_persona_natural(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_cta_empresa FOREIGN KEY (titular_empresa_id) REFERENCES cli_empresa(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_cta_moneda FOREIGN KEY (moneda_id) REFERENCES cat_moneda(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_cta_estado FOREIGN KEY (estado_id) REFERENCES cat_estado_cuenta(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE cta_movimiento
  ADD CONSTRAINT fk_mov_cuenta FOREIGN KEY (cuenta_id) REFERENCES cta_cuenta(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_mov_tipo_mov FOREIGN KEY (tipo_movimiento_id) REFERENCES cat_tipo_movimiento(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_mov_tipo_op FOREIGN KEY (tipo_operacion_id) REFERENCES cat_tipo_operacion(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_mov_canal FOREIGN KEY (canal_id) REFERENCES cat_canal_operacion(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT;

-- Prestamos
ALTER TABLE cre_prestamo
  ADD CONSTRAINT fk_cre_tipo_prestamo FOREIGN KEY (tipo_prestamo_id) REFERENCES cat_tipo_prestamo(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_cre_estado FOREIGN KEY (estado_id) REFERENCES cat_estado_prestamo(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_cre_cli_persona FOREIGN KEY (cliente_persona_id) REFERENCES cli_persona_natural(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_cre_cli_empresa FOREIGN KEY (cliente_empresa_id) REFERENCES cli_empresa(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_cre_cuenta_destino FOREIGN KEY (cuenta_destino_desembolso_id) REFERENCES cta_cuenta(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE cre_prestamo_aprobacion
  ADD CONSTRAINT fk_cre_apr_prestamo FOREIGN KEY (prestamo_id) REFERENCES cre_prestamo(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_cre_apr_analista FOREIGN KEY (analista_usuario_id) REFERENCES sec_usuario(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_cre_apr_motivo FOREIGN KEY (motivo_rechazo_id) REFERENCES cat_motivo_rechazo(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE cre_prestamo_desembolso
  ADD CONSTRAINT fk_cre_des_prestamo FOREIGN KEY (prestamo_id) REFERENCES cre_prestamo(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_cre_des_analista FOREIGN KEY (analista_usuario_id) REFERENCES sec_usuario(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_cre_des_cuenta FOREIGN KEY (cuenta_destino_id) REFERENCES cta_cuenta(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT;

-- Transferencias
ALTER TABLE trf_transferencia
  ADD CONSTRAINT fk_trf_origen FOREIGN KEY (cuenta_origen_id) REFERENCES cta_cuenta(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_trf_destino FOREIGN KEY (cuenta_destino_id) REFERENCES cta_cuenta(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_trf_empresa FOREIGN KEY (empresa_id) REFERENCES cli_empresa(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_trf_estado FOREIGN KEY (estado_id) REFERENCES cat_estado_transferencia(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_trf_creador FOREIGN KEY (creador_usuario_id) REFERENCES sec_usuario(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_trf_aprobador FOREIGN KEY (aprobador_usuario_id) REFERENCES sec_usuario(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_trf_canal FOREIGN KEY (canal_id) REFERENCES cat_canal_operacion(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE trf_transferencia_aprobacion
  ADD CONSTRAINT fk_trf_apr_transferencia FOREIGN KEY (transferencia_id) REFERENCES trf_transferencia(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_trf_apr_usuario FOREIGN KEY (aprobador_usuario_id) REFERENCES sec_usuario(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_trf_apr_motivo FOREIGN KEY (motivo_rechazo_id) REFERENCES cat_motivo_rechazo(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT;

-- Auditoria
ALTER TABLE aud_bitacora_evento
  ADD CONSTRAINT fk_aud_bit_tipo_op FOREIGN KEY (tipo_operacion_id) REFERENCES cat_tipo_operacion(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_aud_bit_usuario FOREIGN KEY (id_usuario) REFERENCES sec_usuario(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT,
  ADD CONSTRAINT fk_aud_bit_rol FOREIGN KEY (rol_usuario_id) REFERENCES sec_rol(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE aud_error_operacion
  ADD CONSTRAINT fk_aud_error_usuario FOREIGN KEY (actor_usuario_id) REFERENCES sec_usuario(id)
  ON UPDATE RESTRICT ON DELETE RESTRICT;
