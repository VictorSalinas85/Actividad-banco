-- V004__indices_y_constraints.sql
USE banco_core;

-- Indices recomendados por SDD
CREATE INDEX idx_cta_cuenta_titular ON cta_cuenta (titular_tipo, titular_persona_id, titular_empresa_id);
CREATE INDEX idx_cta_cuenta_estado ON cta_cuenta (estado_id);

CREATE INDEX idx_cre_prestamo_cliente_estado ON cre_prestamo (cliente_tipo, cliente_persona_id, cliente_empresa_id, estado_id);
CREATE INDEX idx_cre_prestamo_fecha ON cre_prestamo (created_at);

CREATE INDEX idx_trf_estado_fecha ON trf_transferencia (estado_id, fecha_creacion);
CREATE INDEX idx_trf_origen ON trf_transferencia (cuenta_origen_id);
CREATE INDEX idx_trf_creador ON trf_transferencia (creador_usuario_id);
CREATE INDEX idx_trf_pend_empresa ON trf_transferencia (estado_id, empresa_id, fecha_creacion);

CREATE INDEX idx_aud_bit_fecha ON aud_bitacora_evento (fecha_hora_operacion);
CREATE INDEX idx_aud_bit_tipo ON aud_bitacora_evento (tipo_operacion_id);
CREATE INDEX idx_aud_bit_producto ON aud_bitacora_evento (producto_tipo, producto_id);

CREATE INDEX idx_mov_cuenta_fecha ON cta_movimiento (cuenta_id, fecha_movimiento);
CREATE INDEX idx_mov_tipo_op ON cta_movimiento (tipo_operacion_id, canal_id);

CREATE INDEX idx_sesion_usuario_estado ON sec_sesion (usuario_id, estado_sesion_id, expira_at);
CREATE INDEX idx_error_modulo_fecha ON aud_error_operacion (modulo, created_at);
