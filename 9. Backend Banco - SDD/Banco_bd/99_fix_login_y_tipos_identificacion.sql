-- ============================================================================
-- HOTFIX: corrige el inicio de sesion y estandariza tipos de identificacion.
--
-- Aplicar este script en una BD existente que ya fue desplegada con la version
-- anterior del seed. Es idempotente: se puede correr varias veces sin riesgo.
--
-- Resuelve dos problemas detectados:
--   1) Los usuarios demo no podian autenticarse porque el seed previo guardaba
--      texto literal ('HASH_ADMIN', etc.) en lugar de un hash BCrypt valido.
--   2) El catalogo cat_tipo_identificacion tenia los codigos DNI/CEDULA/NIT,
--      pero el frontend Wolfstreet ya esta estandarizado con CC/TI/CE/PAS/NIT.
--
-- Ejecutar:
--   mysql -u root -p banco_core < 99_fix_login_y_tipos_identificacion.sql
-- ============================================================================
USE banco_core;

-- ---------------------------------------------------------------------------
-- 1. Catalogo cat_tipo_identificacion: CC, TI, CE, PAS (persona) + NIT (empresa)
-- ---------------------------------------------------------------------------
INSERT INTO cat_tipo_identificacion(codigo, nombre, aplica_a) VALUES
('CC' ,'Cedula de Ciudadania',            'PERSONA'),
('TI' ,'Tarjeta de Identidad',            'PERSONA'),
('CE' ,'Cedula de Extranjeria',           'PERSONA'),
('PAS','Pasaporte',                       'PERSONA'),
('NIT','Numero Identificacion Tributaria','EMPRESA')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), aplica_a = VALUES(aplica_a), activo = 1;

-- Marcar como inactivos los codigos legados sin borrarlos (puede haber
-- registros en cli_persona_natural/cli_empresa con ese tipo).
UPDATE cat_tipo_identificacion SET activo = 0 WHERE codigo IN ('DNI','CEDULA');

-- ---------------------------------------------------------------------------
-- 2. Roles funcionales (sec_rol)
-- ---------------------------------------------------------------------------
INSERT INTO sec_rol(codigo, nombre) VALUES
('ANALISTA_INTERNO',           'Analista interno del banco'),
('EMPLEADO_VENTANILLA',        'Empleado de ventanilla'),
('EMPLEADO_COMERCIAL',         'Empleado comercial'),
('CLIENTE_PERSONA',            'Cliente persona natural'),
('CLIENTE_EMPRESA_ADMIN',      'Cliente empresa administrador'),
('EMPLEADO_EMPRESA_OPERATIVO', 'Empleado empresa operativo'),
('SUPERVISOR_EMPRESA',         'Supervisor empresa')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

-- ---------------------------------------------------------------------------
-- 3. Usuarios demo con hash BCrypt valido (Spring Security BCryptPasswordEncoder)
--    Contrasenas:
--      admin         -> Admin123*       ventanilla    -> Ventanilla123*
--      analista      -> Analista123*    comercial     -> Comercial123*
--      cliente       -> Cliente123*     empresa_admin -> Empresa123*
--      empresa_op    -> Operativo123*   empresa_super -> Supervisor123*
-- ---------------------------------------------------------------------------
INSERT INTO sec_usuario(username, hash_password, nombre_completo, email, telefono, estado_id, created_by, updated_by)
SELECT 'admin',         '$2b$10$hib1FPERVWhj0hUEUi9Aveq/yt4LZ.ViycormHOwQxa6Mt7f0gi.2', 'Administrador Wolfstreet',    'admin@wolfstreet.local',         '3000000000', eu.id, 0, 0 FROM cat_estado_usuario eu WHERE eu.codigo = 'ACTIVO'
ON DUPLICATE KEY UPDATE hash_password = VALUES(hash_password), nombre_completo = VALUES(nombre_completo), email = VALUES(email), estado_id = VALUES(estado_id);

INSERT INTO sec_usuario(username, hash_password, nombre_completo, email, telefono, estado_id, created_by, updated_by)
SELECT 'analista',      '$2b$10$mpz/bH4xLM6HD59GiD9j6eXcfsQrgrOIHIsL9/KymQkrlQlsg9JKq', 'Analista Interno',            'analista@wolfstreet.local',      '3000000001', eu.id, 0, 0 FROM cat_estado_usuario eu WHERE eu.codigo = 'ACTIVO'
ON DUPLICATE KEY UPDATE hash_password = VALUES(hash_password), nombre_completo = VALUES(nombre_completo), email = VALUES(email), estado_id = VALUES(estado_id);

INSERT INTO sec_usuario(username, hash_password, nombre_completo, email, telefono, estado_id, created_by, updated_by)
SELECT 'ventanilla',    '$2b$10$iovvEbABBXI8fx7EqL6c1uWM9y7S2IZuwjvjVL1nOw7OXX2fmlZL2', 'Empleado Ventanilla',         'ventanilla@wolfstreet.local',    '3000000002', eu.id, 0, 0 FROM cat_estado_usuario eu WHERE eu.codigo = 'ACTIVO'
ON DUPLICATE KEY UPDATE hash_password = VALUES(hash_password), nombre_completo = VALUES(nombre_completo), email = VALUES(email), estado_id = VALUES(estado_id);

INSERT INTO sec_usuario(username, hash_password, nombre_completo, email, telefono, estado_id, created_by, updated_by)
SELECT 'comercial',     '$2b$10$3vDkXCvT3yFI0/PB56m7e.LY5A0PoF0svZV5xQbikEeS.Y.b.wSHK', 'Empleado Comercial',          'comercial@wolfstreet.local',     '3000000003', eu.id, 0, 0 FROM cat_estado_usuario eu WHERE eu.codigo = 'ACTIVO'
ON DUPLICATE KEY UPDATE hash_password = VALUES(hash_password), nombre_completo = VALUES(nombre_completo), email = VALUES(email), estado_id = VALUES(estado_id);

INSERT INTO sec_usuario(username, hash_password, nombre_completo, email, telefono, estado_id, created_by, updated_by)
SELECT 'cliente',       '$2b$10$zRiz5i/ZjGvnS0P.eLXaE.E6HPYasRjSFIEV48VK8B4Fc3awP.w4K', 'Cliente Persona Natural',     'cliente@wolfstreet.local',       '3000000004', eu.id, 0, 0 FROM cat_estado_usuario eu WHERE eu.codigo = 'ACTIVO'
ON DUPLICATE KEY UPDATE hash_password = VALUES(hash_password), nombre_completo = VALUES(nombre_completo), email = VALUES(email), estado_id = VALUES(estado_id);

INSERT INTO sec_usuario(username, hash_password, nombre_completo, email, telefono, estado_id, created_by, updated_by)
SELECT 'empresa_admin', '$2b$10$mrelX3L7SpAgf60OsEIMF.TZqr9nJZ6fGVnlovh649HOvmMradCVG', 'Administrador Empresa ACME',  'empresa_admin@wolfstreet.local', '3000000005', eu.id, 0, 0 FROM cat_estado_usuario eu WHERE eu.codigo = 'ACTIVO'
ON DUPLICATE KEY UPDATE hash_password = VALUES(hash_password), nombre_completo = VALUES(nombre_completo), email = VALUES(email), estado_id = VALUES(estado_id);

INSERT INTO sec_usuario(username, hash_password, nombre_completo, email, telefono, estado_id, created_by, updated_by)
SELECT 'empresa_op',    '$2b$10$cb99sBwVwJBc8cdihHl8XO9iJ3CZVP8rw1ndDjnMGrHca4uYKLgzK', 'Operativo Empresa ACME',      'empresa_op@wolfstreet.local',    '3000000006', eu.id, 0, 0 FROM cat_estado_usuario eu WHERE eu.codigo = 'ACTIVO'
ON DUPLICATE KEY UPDATE hash_password = VALUES(hash_password), nombre_completo = VALUES(nombre_completo), email = VALUES(email), estado_id = VALUES(estado_id);

INSERT INTO sec_usuario(username, hash_password, nombre_completo, email, telefono, estado_id, created_by, updated_by)
SELECT 'empresa_super', '$2b$10$BHasLg.kp4XmFPXJUzaFmu8E3yPSIPGyBsIKFM7d3NDt52.o86jJG', 'Supervisor Empresa ACME',     'empresa_super@wolfstreet.local', '3000000007', eu.id, 0, 0 FROM cat_estado_usuario eu WHERE eu.codigo = 'ACTIVO'
ON DUPLICATE KEY UPDATE hash_password = VALUES(hash_password), nombre_completo = VALUES(nombre_completo), email = VALUES(email), estado_id = VALUES(estado_id);

-- Si la BD venia con 'analista1' (seed antiguo), regenerarle el hash valido.
UPDATE sec_usuario
SET hash_password = '$2b$10$mpz/bH4xLM6HD59GiD9j6eXcfsQrgrOIHIsL9/KymQkrlQlsg9JKq'
WHERE username = 'analista1';

-- ---------------------------------------------------------------------------
-- 4. Mapeo de usuarios a roles
-- ---------------------------------------------------------------------------
INSERT INTO sec_usuario_rol(usuario_id, rol_id, created_by)
SELECT u.id, r.id, 0
FROM sec_usuario u
JOIN sec_rol r ON r.codigo IN (
  CASE
    WHEN u.username IN ('admin','analista','analista1') THEN 'ANALISTA_INTERNO'
    WHEN u.username = 'ventanilla'    THEN 'EMPLEADO_VENTANILLA'
    WHEN u.username = 'comercial'     THEN 'EMPLEADO_COMERCIAL'
    WHEN u.username = 'cliente'       THEN 'CLIENTE_PERSONA'
    WHEN u.username = 'empresa_admin' THEN 'CLIENTE_EMPRESA_ADMIN'
    WHEN u.username = 'empresa_op'    THEN 'EMPLEADO_EMPRESA_OPERATIVO'
    WHEN u.username = 'empresa_super' THEN 'SUPERVISOR_EMPRESA'
    ELSE NULL
  END
)
WHERE u.username IN ('admin','analista','analista1','ventanilla','comercial','cliente','empresa_admin','empresa_op','empresa_super')
ON DUPLICATE KEY UPDATE created_by = VALUES(created_by);

-- ---------------------------------------------------------------------------
-- 5. Re-asignar registros antiguos de personas/empresas a los nuevos codigos
--    (DNI/CEDULA -> CC, NIT se mantiene).
--    Solo aplica si existen registros previos con esos tipos.
-- ---------------------------------------------------------------------------
UPDATE cli_persona_natural p
JOIN cat_tipo_identificacion ti_old ON ti_old.id = p.tipo_identificacion_id
                                   AND ti_old.codigo IN ('DNI','CEDULA')
JOIN cat_tipo_identificacion ti_new ON ti_new.codigo = 'CC'
SET p.tipo_identificacion_id = ti_new.id;

-- ---------------------------------------------------------------------------
-- 6. Verificacion final
-- ---------------------------------------------------------------------------
SELECT 'Tipos identificacion activos' AS info, codigo, nombre, aplica_a
FROM cat_tipo_identificacion WHERE activo = 1 ORDER BY aplica_a, codigo;

SELECT 'Usuarios demo' AS info,
       u.username,
       LEFT(u.hash_password, 7) AS hash_prefix,
       GROUP_CONCAT(r.codigo ORDER BY r.codigo SEPARATOR ', ') AS roles
FROM sec_usuario u
LEFT JOIN sec_usuario_rol ur ON ur.usuario_id = u.id
LEFT JOIN sec_rol r          ON r.id = ur.rol_id
WHERE u.username IN ('admin','analista','ventanilla','comercial','cliente','empresa_admin','empresa_op','empresa_super')
GROUP BY u.id, u.username
ORDER BY u.username;
