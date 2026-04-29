-- 07_create_loan_tables.sql
USE banco_core;

CREATE TABLE IF NOT EXISTS cre_prestamo (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  tipo_prestamo_id BIGINT UNSIGNED NOT NULL,
  cliente_tipo VARCHAR(20) NOT NULL,
  cliente_persona_id BIGINT UNSIGNED NULL,
  cliente_empresa_id BIGINT UNSIGNED NULL,
  monto_solicitado DECIMAL(18,2) NOT NULL,
  monto_aprobado DECIMAL(18,2) NULL,
  tasa_interes DECIMAL(7,4) NULL,
  plazo_meses INT NOT NULL,
  estado_id BIGINT UNSIGNED NOT NULL,
  fecha_aprobacion DATETIME(6) NULL,
  fecha_desembolso DATETIME(6) NULL,
  cuenta_destino_desembolso_id BIGINT UNSIGNED NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  created_by BIGINT UNSIGNED NOT NULL,
  updated_by BIGINT UNSIGNED NOT NULL,
  row_version BIGINT UNSIGNED NOT NULL DEFAULT 1,
  CONSTRAINT chk_cre_cliente_tipo CHECK (cliente_tipo IN ('PERSONA','EMPRESA')),
  CONSTRAINT chk_cre_cliente_rel CHECK (
    (cliente_tipo = 'PERSONA' AND cliente_persona_id IS NOT NULL AND cliente_empresa_id IS NULL) OR
    (cliente_tipo = 'EMPRESA' AND cliente_empresa_id IS NOT NULL AND cliente_persona_id IS NULL)
  ),
  CONSTRAINT chk_cre_monto_solicitado CHECK (monto_solicitado > 0),
  CONSTRAINT chk_cre_plazo CHECK (plazo_meses > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS cre_prestamo_aprobacion (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  prestamo_id BIGINT UNSIGNED NOT NULL,
  analista_usuario_id BIGINT UNSIGNED NOT NULL,
  decision VARCHAR(20) NOT NULL,
  motivo_rechazo_id BIGINT UNSIGNED NULL,
  comentario VARCHAR(255) NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  CONSTRAINT chk_cre_apr_decision CHECK (decision IN ('APROBADO','RECHAZADO'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS cre_prestamo_desembolso (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  prestamo_id BIGINT UNSIGNED NOT NULL UNIQUE,
  analista_usuario_id BIGINT UNSIGNED NOT NULL,
  cuenta_destino_id BIGINT UNSIGNED NOT NULL,
  monto DECIMAL(18,2) NOT NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  CONSTRAINT chk_cre_des_monto CHECK (monto > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
