-- 14_create_test_data.sql
USE banco_core;

-- Roles funcionales
INSERT INTO sec_rol(codigo, nombre) VALUES
('CLIENTE_PERSONA','Cliente persona natural'),
('CLIENTE_EMPRESA_ADMIN','Cliente empresa administrador'),
('EMPLEADO_VENTANILLA','Empleado de ventanilla'),
('EMPLEADO_COMERCIAL','Empleado comercial'),
('EMPLEADO_EMPRESA_OPERATIVO','Empleado empresa operativo'),
('SUPERVISOR_EMPRESA','Supervisor empresa'),
('ANALISTA_INTERNO','Analista interno')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

-- Usuarios base
INSERT INTO sec_usuario(username, hash_password, nombre_completo, email, telefono, estado_id, created_by, updated_by)
SELECT 'admin', 'HASH_ADMIN', 'Administrador Banco', 'admin@banco.local', '3000000000', eu.id, 0, 0
FROM cat_estado_usuario eu WHERE eu.codigo = 'ACTIVO'
ON DUPLICATE KEY UPDATE nombre_completo = VALUES(nombre_completo), estado_id = VALUES(estado_id);

INSERT INTO sec_usuario(username, hash_password, nombre_completo, email, telefono, estado_id, created_by, updated_by)
SELECT 'analista1', 'HASH_ANALISTA', 'Analista Interno 1', 'analista1@banco.local', '3000000001', eu.id, 1, 1
FROM cat_estado_usuario eu WHERE eu.codigo = 'ACTIVO'
ON DUPLICATE KEY UPDATE nombre_completo = VALUES(nombre_completo), estado_id = VALUES(estado_id);

INSERT INTO sec_usuario_rol(usuario_id, rol_id, created_by)
SELECT u.id, r.id, 1
FROM sec_usuario u
JOIN sec_rol r ON r.codigo = 'ANALISTA_INTERNO'
WHERE u.username = 'analista1'
ON DUPLICATE KEY UPDATE created_by = VALUES(created_by);

INSERT INTO prd_producto_bancario(codigo_producto, nombre_producto, categoria, requiere_aprobacion)
VALUES
('CTA_AHORROS','Cuenta Ahorros','CUENTAS',0),
('CTA_EMPRESARIAL','Cuenta Empresarial','CUENTAS',0),
('PRESTAMO_PERSONAL','Prestamo Personal','PRESTAMOS',1),
('TRANSFERENCIA','Transferencia Bancaria','SERVICIOS',0)
ON DUPLICATE KEY UPDATE nombre_producto = VALUES(nombre_producto), requiere_aprobacion = VALUES(requiere_aprobacion);
