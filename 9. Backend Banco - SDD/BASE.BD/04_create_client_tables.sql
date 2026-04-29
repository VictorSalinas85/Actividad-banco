-- 04_create_client_tables.sql
USE banco_core;

CREATE TABLE IF NOT EXISTS cli_persona_natural (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  tipo_identificacion_id BIGINT UNSIGNED NOT NULL,
  identificacion VARCHAR(30) NOT NULL,
  nombre_completo VARCHAR(180) NOT NULL,
  email VARCHAR(180) NOT NULL,
  telefono VARCHAR(20) NOT NULL,
  fecha_nacimiento DATE NOT NULL,
  direccion VARCHAR(255) NOT NULL,
  estado_id BIGINT UNSIGNED NOT NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  created_by BIGINT UNSIGNED NOT NULL,
  updated_by BIGINT UNSIGNED NOT NULL,
  row_version BIGINT UNSIGNED NOT NULL DEFAULT 1,
  CONSTRAINT uq_cli_persona_ident UNIQUE (tipo_identificacion_id, identificacion),
  CONSTRAINT chk_cli_persona_email CHECK (email LIKE '%@%'),
  CONSTRAINT chk_cli_persona_telefono CHECK (CHAR_LENGTH(telefono) BETWEEN 7 AND 15)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS cli_empresa (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  tipo_identificacion_id BIGINT UNSIGNED NOT NULL,
  nit VARCHAR(30) NOT NULL,
  razon_social VARCHAR(180) NOT NULL,
  email VARCHAR(180) NOT NULL,
  telefono VARCHAR(20) NOT NULL,
  direccion VARCHAR(255) NOT NULL,
  representante_persona_id BIGINT UNSIGNED NOT NULL,
  estado_id BIGINT UNSIGNED NOT NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  created_by BIGINT UNSIGNED NOT NULL,
  updated_by BIGINT UNSIGNED NOT NULL,
  row_version BIGINT UNSIGNED NOT NULL DEFAULT 1,
  CONSTRAINT uq_cli_empresa_nit UNIQUE (tipo_identificacion_id, nit),
  CONSTRAINT chk_cli_empresa_email CHECK (email LIKE '%@%'),
  CONSTRAINT chk_cli_empresa_telefono CHECK (CHAR_LENGTH(telefono) BETWEEN 7 AND 15)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS cli_empresa_usuario (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  empresa_id BIGINT UNSIGNED NOT NULL,
  usuario_id BIGINT UNSIGNED NOT NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  created_by BIGINT UNSIGNED NOT NULL,
  updated_by BIGINT UNSIGNED NOT NULL,
  CONSTRAINT uq_cli_empresa_usuario UNIQUE (empresa_id, usuario_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS cli_empresa_usuario_rol (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  empresa_usuario_id BIGINT UNSIGNED NOT NULL,
  rol_empresa_id BIGINT UNSIGNED NOT NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  created_by BIGINT UNSIGNED NOT NULL,
  CONSTRAINT uq_cli_emp_user_rol UNIQUE (empresa_usuario_id, rol_empresa_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
