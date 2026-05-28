-- ============================================================================
-- Vistas legibles para consulta humana desde phpMyAdmin u otro cliente SQL.
--
-- Muestran el codigo del catalogo (CC, NIT, TI, COP, AHORROS, ACTIVO, etc.)
-- en lugar del id numerico crudo, sin alterar las tablas base.
-- ============================================================================

DROP VIEW IF EXISTS v_personas;
CREATE VIEW v_personas AS
SELECT
  p.id,
  ti.codigo  AS tipo_identificacion,
  ti.nombre  AS tipo_identificacion_nombre,
  p.identificacion,
  p.nombre_completo,
  p.email,
  p.telefono,
  p.fecha_nacimiento,
  p.direccion,
  eu.codigo  AS estado,
  eu.nombre  AS estado_nombre,
  p.created_at,
  p.updated_at
FROM cli_persona_natural p
LEFT JOIN cat_tipo_identificacion ti ON ti.id = p.tipo_identificacion_id
LEFT JOIN cat_estado_usuario      eu ON eu.id = p.estado_id;

DROP VIEW IF EXISTS v_empresas;
CREATE VIEW v_empresas AS
SELECT
  e.id,
  ti.codigo  AS tipo_identificacion,
  ti.nombre  AS tipo_identificacion_nombre,
  e.nit,
  e.razon_social,
  e.email,
  e.telefono,
  e.direccion,
  rp.identificacion AS rep_legal_identificacion,
  rp.nombre_completo AS rep_legal_nombre,
  eu.codigo  AS estado,
  eu.nombre  AS estado_nombre,
  e.created_at,
  e.updated_at
FROM cli_empresa e
LEFT JOIN cat_tipo_identificacion ti ON ti.id = e.tipo_identificacion_id
LEFT JOIN cat_estado_usuario      eu ON eu.id = e.estado_id
LEFT JOIN cli_persona_natural     rp ON rp.id = e.representante_persona_id;

DROP VIEW IF EXISTS v_cuentas;
CREATE VIEW v_cuentas AS
SELECT
  c.id,
  c.numero_cuenta,
  tc.codigo  AS tipo_cuenta,
  tc.nombre  AS tipo_cuenta_nombre,
  m.codigo   AS moneda,
  c.titular_tipo,
  COALESCE(p.identificacion, e.nit)            AS titular_identificacion,
  COALESCE(p.nombre_completo, e.razon_social)  AS titular_nombre,
  c.saldo_actual,
  c.limite_sobregiro_autorizado,
  ec.codigo  AS estado,
  ec.nombre  AS estado_nombre,
  c.fecha_apertura,
  c.created_at
FROM cta_cuenta c
LEFT JOIN cat_tipo_cuenta     tc ON tc.id = c.tipo_cuenta_id
LEFT JOIN cat_moneda          m  ON m.id  = c.moneda_id
LEFT JOIN cat_estado_cuenta   ec ON ec.id = c.estado_id
LEFT JOIN cli_persona_natural p  ON p.id  = c.titular_persona_id
LEFT JOIN cli_empresa         e  ON e.id  = c.titular_empresa_id;

DROP VIEW IF EXISTS v_movimientos;
CREATE VIEW v_movimientos AS
SELECT
  mov.id,
  c.numero_cuenta,
  tm.codigo AS tipo_movimiento,
  tm.nombre AS tipo_movimiento_nombre,
  cn.codigo AS canal,
  mov.monto,
  mov.saldo_antes,
  mov.saldo_despues,
  mov.referencia_externa,
  mov.idempotency_key,
  mov.fecha_movimiento
FROM cta_movimiento mov
LEFT JOIN cta_cuenta          c  ON c.id  = mov.cuenta_id
LEFT JOIN cat_tipo_movimiento tm ON tm.id = mov.tipo_movimiento_id
LEFT JOIN cat_canal_operacion cn ON cn.id = mov.canal_id;

DROP VIEW IF EXISTS v_prestamos;
CREATE VIEW v_prestamos AS
SELECT
  pr.id,
  pr.cliente_tipo,
  COALESCE(p.identificacion, e.nit)            AS cliente_identificacion,
  COALESCE(p.nombre_completo, e.razon_social)  AS cliente_nombre,
  tp.codigo AS tipo_prestamo,
  pr.monto_solicitado,
  pr.monto_aprobado,
  pr.tasa_interes,
  pr.plazo_meses,
  ep.codigo AS estado,
  ep.nombre AS estado_nombre,
  pr.fecha_aprobacion,
  pr.fecha_desembolso,
  cd.numero_cuenta AS cuenta_desembolso,
  pr.created_at
FROM cre_prestamo pr
LEFT JOIN cli_persona_natural p  ON p.id  = pr.cliente_persona_id
LEFT JOIN cli_empresa         e  ON e.id  = pr.cliente_empresa_id
LEFT JOIN cat_tipo_prestamo   tp ON tp.id = pr.tipo_prestamo_id
LEFT JOIN cat_estado_prestamo ep ON ep.id = pr.estado_id
LEFT JOIN cta_cuenta          cd ON cd.id = pr.cuenta_destino_desembolso_id;

DROP VIEW IF EXISTS v_transferencias;
CREATE VIEW v_transferencias AS
SELECT
  t.id,
  co.numero_cuenta AS cuenta_origen,
  cd.numero_cuenta AS cuenta_destino,
  t.monto,
  cn.codigo  AS canal,
  et.codigo  AS estado,
  et.nombre  AS estado_nombre,
  uc.username AS creado_por,
  ua.username AS aprobado_por,
  t.fecha_creacion,
  t.fecha_aprobacion
FROM trf_transferencia t
LEFT JOIN cta_cuenta              co ON co.id = t.cuenta_origen_id
LEFT JOIN cta_cuenta              cd ON cd.id = t.cuenta_destino_id
LEFT JOIN cat_canal_operacion     cn ON cn.id = t.canal_id
LEFT JOIN cat_estado_transferencia et ON et.id = t.estado_id
LEFT JOIN sec_usuario             uc ON uc.id = t.creador_usuario_id
LEFT JOIN sec_usuario             ua ON ua.id = t.aprobador_usuario_id;
