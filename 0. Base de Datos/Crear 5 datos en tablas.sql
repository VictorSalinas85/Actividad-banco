BEGIN;

-- Limpieza (opcional). Si no quieres borrar datos, comenta este bloque.
TRUNCATE TABLE
  public.Bitacora_de_Operaciones,
  public.Transferencia_Detalle,
  public.Transferencia,
  public.Prestamo,
  public.Cuenta_Bancaria,
  public.Producto_Bancario,
  public.Usuarios_del_Sistema,
  public.Clientes,
  public.Roles_Sistema
RESTART IDENTITY CASCADE;

-- 1) Roles_Sistema (5)
INSERT INTO public.Roles_Sistema (Nombre_Rol, Descripcion) VALUES
('Comercial', 'Gestiona clientes y radica solicitudes'),
('Analista', 'Analiza y valida información'),
('Supervisor', 'Aprueba operaciones que lo requieran'),
('Operativo_Empresa', 'Usuario de empresa para cargar operaciones'),
('Administrador', 'Administra usuarios y configuraciones');

-- 2) Clientes (5) - 3 Personas + 2 Empresas
-- Nota: Representante_Legal debe existir en Clientes (se usa una persona ya creada).
--       ID_Usuario_Comercial se deja NULL aquí por la dependencia circular con Usuarios_del_Sistema.
INSERT INTO public.Clientes (
  Numero_Identificacion, Tipo_Cliente, Correo_Electronico, Numero_de_telefono, Direccion, Rol_Cliente,
  Nombre_Completo, Fecha_de_nacimiento, Razon_Social, Representante_Legal, ID_Usuario_Comercial
) VALUES
-- Personas
('1032456789', 'P', 'juan.perez@gmail.com', '3004567890', 'Cra 45 #12-34, Medellín, Antioquia', 'Cliente',
 'Juan David Pérez Gómez', DATE '1994-05-12', NULL, NULL, NULL),

('1002345678', 'P', 'maria.rodriguez@gmail.com', '3012345678', 'Calle 10 #23-56, Bogotá D.C.', 'Cliente',
 'María Fernanda Rodríguez', DATE '1992-11-03', NULL, NULL, NULL),

('1015678901', 'P', 'carlos.gomez@gmail.com', '3029876543', 'Av 6N #18-20, Cali, Valle del Cauca', 'Cliente',
 'Carlos Andrés Gómez', DATE '1989-08-21', NULL, NULL, NULL),

-- Empresas
('900123456-7', 'E', 'contacto@andina.com.co', '6045550101', 'Cra 50 #30-20, Medellín, Antioquia', 'Empresa',
 NULL, NULL, 'Constructora Andina S.A.S.', '1032456789', NULL),

('900999888-3', 'E', 'soporte@tecbogota.com.co', '6015550404', 'Cl 26 #92-15, Bogotá D.C.', 'Empresa',
 NULL, NULL, 'Tecnologías Bogotá S.A.S.', '1032456789', NULL);

-- 3) Usuarios_del_Sistema (5)
-- IDs explícitos para que sea fácil referenciarlos luego (aunque sean identity).
INSERT INTO public.Usuarios_del_Sistema (
  ID_Usuario, Numero_Identificacion_Cliente, Nombre_Completo, ID_Identificacion, Correo_Electronico, Telefono,
  Fecha_Nacimiento, Direccion, ID_Rol, Estado_Usuario
) VALUES
(1, '1032456789', 'Juan David Pérez Gómez', '1032456789', 'juan.perez@gmail.com', '3004567890',
 DATE '1994-05-12', 'Medellín, Antioquia', 1, 'Activo'),  -- Comercial

(2, '1002345678', 'María Fernanda Rodríguez', '1002345678', 'maria.rodriguez@gmail.com', '3012345678',
 DATE '1992-11-03', 'Bogotá D.C.', 2, 'Activo'),          -- Analista

(3, '1015678901', 'Carlos Andrés Gómez', '1015678901', 'carlos.gomez@gmail.com', '3029876543',
 DATE '1989-08-21', 'Cali, Valle', 3, 'Activo'),          -- Supervisor

(4, '900123456-7', 'Admin Andina', '900123456-7', 'admin@andina.com.co', '3201112233',
 NULL, 'Medellín, Antioquia', 4, 'Activo'),              -- Operativo_Empresa

(5, '900999888-3', 'Admin TecBogotá', '900999888-3', 'admin@tecbogota.com.co', '3203334455',
 NULL, 'Bogotá D.C.', 4, 'Activo');                      -- Operativo_Empresa

-- Ahora sí se asigna el comercial a los clientes (Clientes.ID_Usuario_Comercial -> Usuarios_del_Sistema.ID_Usuario)
UPDATE public.Clientes
SET ID_Usuario_Comercial = 1
WHERE Numero_Identificacion IN ('1032456789','1002345678','1015678901','900123456-7','900999888-3');

-- 4) Producto_Bancario (5)
INSERT INTO public.Producto_Bancario (Codigo_Producto, Nombre_Producto, Categoria, Requiere_Aprobacion) VALUES
('CTA_AHO', 'Cuenta de Ahorros', 'Cuenta', FALSE),
('CTA_COR', 'Cuenta Corriente', 'Cuenta', FALSE),
('PRE_CON', 'Préstamo de Consumo', 'Préstamo', TRUE),
('PRE_HIP', 'Crédito Hipotecario', 'Préstamo', TRUE),
('PRE_COM', 'Crédito Comercial', 'Préstamo', TRUE);

-- 5) Cuenta_Bancaria (5)
-- Saldos aleatorios (COP) y fechas de apertura aleatorias.
INSERT INTO public.Cuenta_Bancaria (
  Numero_Cuenta, Codigo_producto_cuenta, Tipo_Cuenta, ID_Titular, Saldo_Actual, Moneda, Estado_Cuenta, Fecha_Apertura
) VALUES
('053000100001', 'CTA_AHO', 'Ahorros',   '1032456789', ROUND(( 800000 + random()*7000000)::numeric, 2), 'COP', 'Activa', CURRENT_DATE - (random()*900)::int),
('053000100002', 'CTA_AHO', 'Ahorros',   '1002345678', ROUND(( 900000 + random()*6500000)::numeric, 2), 'COP', 'Activa', CURRENT_DATE - (random()*900)::int),
('053000100003', 'CTA_AHO', 'Ahorros',   '1015678901', ROUND(( 700000 + random()*6000000)::numeric, 2), 'COP', 'Activa', CURRENT_DATE - (random()*900)::int),
('053000200001', 'CTA_COR', 'Corriente','900123456-7', ROUND((2000000 + random()*12000000)::numeric, 2), 'COP', 'Activa', CURRENT_DATE - (random()*900)::int),
('053000200002', 'CTA_COR', 'Corriente','900999888-3', ROUND((1500000 + random()*10000000)::numeric, 2), 'COP', 'Activa', CURRENT_DATE - (random()*900)::int);

-- 6) Prestamo (5)
-- Montos/fechas coherentes. Algunos aprobados y otros pendientes (Monto_Aprobado/fechas pueden ir NULL).
INSERT INTO public.Prestamo (
  ID_Prestamo, Codigo_producto_prestamo, Tipo_Prestamo, ID_Cliente_Solicitante,
  Monto_Solicitado, Monto_Aprobado, Tasa_Interes, Plazo_Meses, Estado_Prestamo,
  Fecha_Aprobacion, Fecha_Desembolso, Cuenta_Destino_Desembolso
) VALUES
(1, 'PRE_CON', 'Consumo',     '1032456789',  8500000,  8500000, 0.018500, 24,  'Aprobado',  CURRENT_DATE - 20, CURRENT_DATE - 18, '053000100001'),
(2, 'PRE_CON', 'Consumo',     '1002345678', 12000000,  NULL,     0.019900, 36,  'Pendiente', NULL,            NULL,            '053000100002'),
(3, 'PRE_HIP', 'Hipotecario', '1015678901', 150000000, 145000000,0.012300, 180, 'Aprobado',  CURRENT_DATE - 45, CURRENT_DATE - 40, '053000100003'),
(4, 'PRE_COM', 'Comercial',   '900123456-7', 80000000,  80000000, 0.015000, 60,  'Aprobado',  CURRENT_DATE - 30, CURRENT_DATE - 25, '053000200001'),
(5, 'PRE_COM', 'Comercial',   '900999888-3', 65000000,  NULL,     0.016500, 48,  'Pendiente', NULL,            NULL,            '053000200002');

-- 7) Transferencia (5) - SIMPLE/MASIVA
INSERT INTO public.Transferencia (
  ID_Transferencia, Tipo_Transferencia, Cuenta_Origen, Monto_Total, Fecha_Creacion, Fecha_Aprobacion,
  Estado_Transferencia, ID_Usuario_Creador, ID_Usuario_Aprobador
) VALUES
(1, 'SIMPLE', '053000100001',  250000, NOW() - (random()*INTERVAL '20 days'), NOW() - (random()*INTERVAL '19 days'), 'Ejecutada', 1, 3),
(2, 'SIMPLE', '053000200001', 1500000, NOW() - (random()*INTERVAL '10 days'), NULL,                                'Pendiente', 4, NULL),
(3, 'SIMPLE', '053000100002',  120000, NOW() - (random()*INTERVAL '15 days'), NOW() - (random()*INTERVAL '14 days'), 'Ejecutada', 2, 3),
(4, 'MASIVA', '053000200002',  980000, NOW() - (random()*INTERVAL '5 days'),  NOW() - (random()*INTERVAL '4 days'),  'Ejecutada', 5, 3),
(5, 'MASIVA', '053000100003',   60000, NOW() - (random()*INTERVAL '3 days'),  NULL,                                'Pendiente', 2, NULL);

-- 8) Transferencia_Detalle (5) - 1 detalle por transferencia (para SIMPLE y ejemplo de MASIVA)
INSERT INTO public.Transferencia_Detalle (
  ID_Transferencia, No_Item, Cuenta_Destino, Monto, Concepto
) VALUES
(1, 1, '053000100002',  250000, 'Transferencia entre clientes'),
(2, 1, '053000100003', 1500000, 'Pago a proveedor (ejemplo)'),
(3, 1, '053000100001',  120000, 'Transferencia entre cuentas'),
(4, 1, '053000100002',  980000, 'Pago masivo (ejemplo, 1 ítem)'),
(5, 1, '053000100001',   60000, 'Pago masivo (ejemplo, 1 ítem)');

-- 9) Bitacora_de_Operaciones (5) - JSONB para trazabilidad
INSERT INTO public.Bitacora_de_Operaciones (
  ID_Bitacora, Tipo_Operacion, Fecha_Hora_Operacion, ID_Usuario, Rol_Usuario, ID_Producto_Afectado, Datos_Detalle
) VALUES
('BIT-CO-0001', 'Creacion_Cuenta', NOW() - INTERVAL '25 days', 2, 'Analista', '053000100001',
 jsonb_build_object('cliente','1032456789','producto','CTA_AHO','ciudad','Medellín','resultado','OK')),

('BIT-CO-0002', 'Solicitud_Prestamo', NOW() - INTERVAL '20 days', 1, 'Comercial', '1',
 jsonb_build_object('cliente','1032456789','tipo','Consumo','monto_solicitado',8500000,'resultado','Enviado')),

('BIT-CO-0003', 'Aprobacion_Transferencia', NOW() - INTERVAL '14 days', 3, 'Supervisor', '1',
 jsonb_build_object('transferencia',1,'estado','Ejecutada','monto',250000,'resultado','OK')),

('BIT-CO-0004', 'Creacion_Transferencia_Masiva', NOW() - INTERVAL '5 days', 5, 'Operativo_Empresa', '4',
 jsonb_build_object('transferencia',4,'tipo','MASIVA','items',1,'estado','Ejecutada')),

('BIT-CO-0005', 'Operacion_Pendiente', NOW() - INTERVAL '2 days', 2, 'Analista', '5',
 jsonb_build_object('transferencia',5,'estado','Pendiente','nota','Falta aprobación'));

COMMIT;