-- 06_create_account_tables.sql
USE banco_core;

CREATE TABLE IF NOT EXISTS cta_cuenta (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  numero_cuenta VARCHAR(24) NOT NULL UNIQUE,
  tipo_cuenta_id BIGINT UNSIGNED NOT NULL,
  titular_tipo VARCHAR(20) NOT NULL,
  titular_persona_id BIGINT UNSIGNED NULL,
  titular_empresa_id BIGINT UNSIGNED NULL,
  saldo_actual DECIMAL(18,2) NOT NULL DEFAULT 0.00,
  limite_sobregiro_autorizado DECIMAL(18,2) NOT NULL DEFAULT 0.00,
  moneda_id BIGINT UNSIGNED NOT NULL,
  estado_id BIGINT UNSIGNED NOT NULL,
  fecha_apertura DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  created_by BIGINT UNSIGNED NOT NULL,
  updated_by BIGINT UNSIGNED NOT NULL,
  row_version BIGINT UNSIGNED NOT NULL DEFAULT 1,
  CONSTRAINT chk_cta_titular_tipo CHECK (titular_tipo IN ('PERSONA','EMPRESA')),
  CONSTRAINT chk_cta_titular_rel CHECK (
    (titular_tipo = 'PERSONA' AND titular_persona_id IS NOT NULL AND titular_empresa_id IS NULL) OR
    (titular_tipo = 'EMPRESA' AND titular_empresa_id IS NOT NULL AND titular_persona_id IS NULL)
  )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS cta_movimiento (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  cuenta_id BIGINT UNSIGNED NOT NULL,
  tipo_movimiento_id BIGINT UNSIGNED NOT NULL,
  tipo_operacion_id BIGINT UNSIGNED NOT NULL,
  canal_id BIGINT UNSIGNED NOT NULL,
  referencia_externa VARCHAR(64) NULL,
  idempotency_key VARCHAR(64) NULL,
  monto DECIMAL(18,2) NOT NULL,
  saldo_antes DECIMAL(18,2) NOT NULL,
  saldo_despues DECIMAL(18,2) NOT NULL,
  fecha_movimiento DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  created_by BIGINT UNSIGNED NOT NULL,
  CONSTRAINT chk_mov_monto_pos CHECK (monto > 0),
  CONSTRAINT uq_mov_idempotencia UNIQUE (idempotency_key, tipo_operacion_id, canal_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
