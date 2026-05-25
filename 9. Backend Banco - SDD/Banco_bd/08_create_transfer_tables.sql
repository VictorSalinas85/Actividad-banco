-- 08_create_transfer_tables.sql
USE banco_core;

CREATE TABLE IF NOT EXISTS trf_transferencia (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  cuenta_origen_id BIGINT UNSIGNED NOT NULL,
  cuenta_destino_id BIGINT UNSIGNED NOT NULL,
  empresa_id BIGINT UNSIGNED NULL,
  monto DECIMAL(18,2) NOT NULL,
  fecha_creacion DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  fecha_aprobacion DATETIME(6) NULL,
  estado_id BIGINT UNSIGNED NOT NULL,
  creador_usuario_id BIGINT UNSIGNED NOT NULL,
  aprobador_usuario_id BIGINT UNSIGNED NULL,
  canal_id BIGINT UNSIGNED NOT NULL,
  idempotency_key VARCHAR(64) NULL,
  referencia_externa VARCHAR(64) NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  created_by BIGINT UNSIGNED NOT NULL,
  updated_by BIGINT UNSIGNED NOT NULL,
  row_version BIGINT UNSIGNED NOT NULL DEFAULT 1,
  CONSTRAINT chk_trf_monto CHECK (monto > 0),
  CONSTRAINT chk_trf_cuentas_distintas CHECK (cuenta_origen_id <> cuenta_destino_id),
  CONSTRAINT uq_trf_idempotencia UNIQUE (idempotency_key, canal_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS trf_transferencia_aprobacion (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  transferencia_id BIGINT UNSIGNED NOT NULL,
  aprobador_usuario_id BIGINT UNSIGNED NOT NULL,
  decision VARCHAR(20) NOT NULL,
  motivo_rechazo_id BIGINT UNSIGNED NULL,
  comentario VARCHAR(255) NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  CONSTRAINT chk_trf_apr_decision CHECK (decision IN ('APROBADA','RECHAZADA'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
