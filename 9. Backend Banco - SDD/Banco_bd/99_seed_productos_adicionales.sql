-- ============================================================================
-- Seed: ampliar el catalogo de productos bancarios con productos tipicos
-- para que la lista en el frontend sea mas representativa de un banco real.
-- ============================================================================

INSERT IGNORE INTO prd_producto_bancario
  (codigo_producto, nombre_producto, categoria, requiere_aprobacion, activo)
VALUES
  ('CTA_CORRIENTE',        'Cuenta Corriente',           'CUENTAS',   0, 1),
  ('PRESTAMO_HIPOTECARIO', 'Prestamo Hipotecario',       'PRESTAMOS', 1, 1),
  ('PRESTAMO_EMPRESARIAL', 'Prestamo Empresarial',       'PRESTAMOS', 1, 1),
  ('TARJETA_CREDITO',      'Tarjeta de Credito',         'PRESTAMOS', 1, 1),
  ('TARJETA_DEBITO',       'Tarjeta de Debito',          'SERVICIOS', 0, 1);
