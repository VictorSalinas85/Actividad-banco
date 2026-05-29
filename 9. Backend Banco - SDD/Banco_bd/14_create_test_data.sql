-- 14_create_test_data.sql
-- Semilla completa de datos demo para Wolfstreet Bank.
-- Incluye los 8 usuarios documentados en SDD/17_usuarios_demo_y_permisos.md
-- con sus contrasenas almacenadas como hash BCrypt valido para Spring Security
-- (BCryptPasswordEncoder admite los prefijos $2a$, $2b$ y $2y$).
--
-- Login real verificado contra POST /api/v1/auth/login.
-- Cualquier ejecucion repetida es idempotente gracias a ON DUPLICATE KEY UPDATE.
USE banco_core;

-- ===========================================================================
-- 1. Roles funcionales del sistema (sec_rol)
-- ===========================================================================
INSERT INTO sec_rol(codigo, nombre) VALUES
('ANALISTA_INTERNO',           'Analista interno del banco'),
('EMPLEADO_VENTANILLA',        'Empleado de ventanilla'),
('EMPLEADO_COMERCIAL',         'Empleado comercial'),
('CLIENTE_PERSONA',            'Cliente persona natural'),
('CLIENTE_EMPRESA_ADMIN',      'Cliente empresa administrador'),
('EMPLEADO_EMPRESA_OPERATIVO', 'Empleado empresa operativo'),
('SUPERVISOR_EMPRESA',         'Supervisor empresa')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

-- ===========================================================================
-- 2. Usuarios demo con hash BCrypt valido
-- Contrasenas (texto plano para QA, NUNCA usar en produccion):
--   admin          -> Admin123*
--   analista       -> Analista123*
--   ventanilla     -> Ventanilla123*
--   comercial      -> Comercial123*
--   cliente        -> Cliente123*
--   empresa_admin  -> Empresa123*
--   empresa_op     -> Operativo123*
--   empresa_super  -> Supervisor123*
-- ===========================================================================
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

-- Alias historico: el documento de pruebas y casos T006 usan 'analista1'.
INSERT INTO sec_usuario(username, hash_password, nombre_completo, email, telefono, estado_id, created_by, updated_by)
SELECT 'analista1',     '$2b$10$mpz/bH4xLM6HD59GiD9j6eXcfsQrgrOIHIsL9/KymQkrlQlsg9JKq', 'Analista Interno (legacy)',   'analista1@wolfstreet.local',     '3000000010', eu.id, 0, 0 FROM cat_estado_usuario eu WHERE eu.codigo = 'ACTIVO'
ON DUPLICATE KEY UPDATE hash_password = VALUES(hash_password), nombre_completo = VALUES(nombre_completo), email = VALUES(email), estado_id = VALUES(estado_id);

-- ===========================================================================
-- 3. Mapeo usuario -> rol (sec_usuario_rol)
-- admin se mapea a ANALISTA_INTERNO para tener acceso total en pruebas.
-- ===========================================================================
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

-- ===========================================================================
-- 4. Productos bancarios base
-- ===========================================================================
INSERT INTO prd_producto_bancario(codigo_producto, nombre_producto, categoria, requiere_aprobacion) VALUES
('CTA_AHORROS',       'Cuenta Ahorros',         'CUENTAS',   0),
('CTA_EMPRESARIAL',   'Cuenta Empresarial',     'CUENTAS',   0),
('PRESTAMO_PERSONAL', 'Prestamo Personal',      'PRESTAMOS', 1),
('TRANSFERENCIA',     'Transferencia Bancaria', 'SERVICIOS', 0)
ON DUPLICATE KEY UPDATE nombre_producto = VALUES(nombre_producto), requiere_aprobacion = VALUES(requiere_aprobacion);

-- ===========================================================================
-- 5. Personas naturales demo
-- Cada persona se identifica con un tipo CC/TI/CE/PAS conforme al frontend.
-- ===========================================================================
INSERT INTO cli_persona_natural(tipo_identificacion_id, identificacion, nombre_completo, email, telefono, fecha_nacimiento, direccion, estado_id, created_by, updated_by)
SELECT ti.id, '1010101010', 'Juan Perez',       'juan.perez@demo.local',       '3111111111', '1985-05-12', 'Calle 100 #15-20',  eu.id, 0, 0
FROM cat_tipo_identificacion ti JOIN cat_estado_usuario eu ON eu.codigo = 'ACTIVO' WHERE ti.codigo = 'CC'
ON DUPLICATE KEY UPDATE nombre_completo = VALUES(nombre_completo), email = VALUES(email), telefono = VALUES(telefono);

INSERT INTO cli_persona_natural(tipo_identificacion_id, identificacion, nombre_completo, email, telefono, fecha_nacimiento, direccion, estado_id, created_by, updated_by)
SELECT ti.id, '2020202020', 'Maria Torres',     'maria.torres@demo.local',     '3122222222', '1990-08-21', 'Carrera 7 #45-12',  eu.id, 0, 0
FROM cat_tipo_identificacion ti JOIN cat_estado_usuario eu ON eu.codigo = 'ACTIVO' WHERE ti.codigo = 'CC'
ON DUPLICATE KEY UPDATE nombre_completo = VALUES(nombre_completo), email = VALUES(email), telefono = VALUES(telefono);

INSERT INTO cli_persona_natural(tipo_identificacion_id, identificacion, nombre_completo, email, telefono, fecha_nacimiento, direccion, estado_id, created_by, updated_by)
SELECT ti.id, '3030303030', 'Pedro Joven',      'pedro.joven@demo.local',      '3133333333', '2008-02-14', 'Av Caracas 30-40',  eu.id, 0, 0
FROM cat_tipo_identificacion ti JOIN cat_estado_usuario eu ON eu.codigo = 'ACTIVO' WHERE ti.codigo = 'TI'
ON DUPLICATE KEY UPDATE nombre_completo = VALUES(nombre_completo), email = VALUES(email), telefono = VALUES(telefono);

INSERT INTO cli_persona_natural(tipo_identificacion_id, identificacion, nombre_completo, email, telefono, fecha_nacimiento, direccion, estado_id, created_by, updated_by)
SELECT ti.id, '4040404040', 'Lucia Extranjera', 'lucia.extranjera@demo.local', '3144444444', '1992-11-03', 'Calle 72 #11-30',   eu.id, 0, 0
FROM cat_tipo_identificacion ti JOIN cat_estado_usuario eu ON eu.codigo = 'ACTIVO' WHERE ti.codigo = 'CE'
ON DUPLICATE KEY UPDATE nombre_completo = VALUES(nombre_completo), email = VALUES(email), telefono = VALUES(telefono);

INSERT INTO cli_persona_natural(tipo_identificacion_id, identificacion, nombre_completo, email, telefono, fecha_nacimiento, direccion, estado_id, created_by, updated_by)
SELECT ti.id, 'P5050505',   'Mark Traveler',    'mark.traveler@demo.local',    '3155555555', '1988-09-17', 'Cra 13 #82-15',     eu.id, 0, 0
FROM cat_tipo_identificacion ti JOIN cat_estado_usuario eu ON eu.codigo = 'ACTIVO' WHERE ti.codigo = 'PAS'
ON DUPLICATE KEY UPDATE nombre_completo = VALUES(nombre_completo), email = VALUES(email), telefono = VALUES(telefono);

-- ===========================================================================
-- 6. Empresa demo ACME Corp SAS (representante legal = Maria Torres CC 2020202020)
-- ===========================================================================
INSERT INTO cli_empresa(tipo_identificacion_id, nit, razon_social, email, telefono, direccion, representante_persona_id, estado_id, created_by, updated_by)
SELECT
  (SELECT id FROM cat_tipo_identificacion WHERE codigo = 'NIT' LIMIT 1),
  '900123456-7',
  'ACME Corp SAS',
  'contacto@acme.demo.local',
  '6017654321',
  'Av El Dorado 100',
  (SELECT id FROM cli_persona_natural WHERE identificacion = '2020202020' LIMIT 1),
  (SELECT id FROM cat_estado_usuario WHERE codigo = 'ACTIVO' LIMIT 1),
  0, 0
ON DUPLICATE KEY UPDATE razon_social = VALUES(razon_social), email = VALUES(email), telefono = VALUES(telefono), direccion = VALUES(direccion);
