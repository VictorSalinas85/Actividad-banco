-- 02_seed_catalogs.sql
USE banco_core;

INSERT INTO cat_estado_usuario(codigo, nombre) VALUES
('ACTIVO','Activo'),
('INACTIVO','Inactivo'),
('BLOQUEADO','Bloqueado')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

INSERT INTO cat_estado_cuenta(codigo, nombre) VALUES
('ACTIVA','Activa'),
('BLOQUEADA','Bloqueada'),
('CANCELADA','Cancelada')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

INSERT INTO cat_estado_prestamo(codigo, nombre, orden_visual) VALUES
('EN_ESTUDIO','En estudio',1),
('APROBADO','Aprobado',2),
('RECHAZADO','Rechazado',3),
('DESEMBOLSADO','Desembolsado',4)
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), orden_visual = VALUES(orden_visual);

INSERT INTO cat_estado_transferencia(codigo, nombre, orden_visual) VALUES
('CREADA','Creada',1),
('EN_ESPERA_APROBACION','En espera de aprobacion',2),
('APROBADA','Aprobada',3),
('RECHAZADA','Rechazada',4),
('EJECUTADA','Ejecutada',5),
('VENCIDA','Vencida',6)
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), orden_visual = VALUES(orden_visual);

INSERT INTO cat_estado_sesion(codigo, nombre) VALUES
('ACTIVA','Activa'),
('EXPIRADA','Expirada'),
('REVOCADA','Revocada')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

INSERT INTO cat_tipo_identificacion(codigo, nombre, aplica_a) VALUES
('DNI','Documento Nacional', 'PERSONA'),
('CEDULA','Cedula', 'PERSONA'),
('NIT','Numero Identificacion Tributaria', 'EMPRESA')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), aplica_a = VALUES(aplica_a);

INSERT INTO cat_tipo_cuenta(codigo, nombre, permite_sobregiro) VALUES
('AHORROS','Cuenta de Ahorros',0),
('CORRIENTE','Cuenta Corriente',1),
('PERSONAL','Cuenta Personal',0),
('EMPRESARIAL','Cuenta Empresarial',1)
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), permite_sobregiro = VALUES(permite_sobregiro);

INSERT INTO cat_moneda(codigo, nombre, simbolo) VALUES
('USD','Dolar estadounidense','$'),
('EUR','Euro','EUR'),
('COP','Peso colombiano','COP')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), simbolo = VALUES(simbolo);

INSERT INTO cat_tipo_prestamo(codigo, nombre) VALUES
('PERSONAL','Prestamo personal'),
('HIPOTECARIO','Prestamo hipotecario'),
('EMPRESARIAL','Prestamo empresarial')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

INSERT INTO cat_canal_operacion(codigo, nombre) VALUES
('VENTANILLA','Ventanilla'),
('WEB','Web'),
('MOVIL','Movil'),
('BACKOFFICE','Backoffice')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

INSERT INTO cat_tipo_movimiento(codigo, nombre, naturaleza) VALUES
('DEBITO','Debito','DEBITO'),
('CREDITO','Credito','CREDITO')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), naturaleza = VALUES(naturaleza);

INSERT INTO cat_motivo_rechazo(codigo, nombre) VALUES
('RIESGO_ALTO','Riesgo alto'),
('DOCUMENTACION_INCOMPLETA','Documentacion incompleta'),
('SALDO_INSUFICIENTE','Saldo insuficiente')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

INSERT INTO cat_motivo_bloqueo(codigo, nombre) VALUES
('FRAUDE_SOSPECHOSO','Fraude sospechoso'),
('SOLICITUD_CLIENTE','Solicitud cliente'),
('CUMPLIMIENTO','Cumplimiento normativo')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

INSERT INTO cat_rol_empresa(codigo, nombre) VALUES
('OPERATIVO','Operativo empresa'),
('SUPERVISOR_APROBADOR','Supervisor aprobador empresa'),
('REPRESENTANTE_LEGAL','Representante legal')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

INSERT INTO cat_tipo_operacion(codigo, nombre, categoria) VALUES
('CONSIGNACION','Consignacion','CUENTA'),
('RETIRO','Retiro','CUENTA'),
('TRANSFERENCIA','Transferencia','TRANSFERENCIA'),
('PRESTAMO_SOLICITUD','Solicitud prestamo','PRESTAMO'),
('PRESTAMO_APROBACION','Aprobacion prestamo','PRESTAMO'),
('PRESTAMO_RECHAZO','Rechazo prestamo','PRESTAMO'),
('PRESTAMO_DESEMBOLSO','Desembolso prestamo','PRESTAMO'),
('TRANSFERENCIA_CREADA','Transferencia creada','TRANSFERENCIA'),
('TRANSFERENCIA_APROBADA','Transferencia aprobada','TRANSFERENCIA'),
('TRANSFERENCIA_RECHAZADA','Transferencia rechazada','TRANSFERENCIA'),
('CUENTA_APERTURA','Apertura cuenta','CUENTA'),
('CUENTA_BLOQUEO','Bloqueo cuenta','CUENTA'),
('CUENTA_CANCELACION','Cancelacion cuenta','CUENTA'),
('CLIENTE_PERSONA_CREADA','Cliente persona creada','CLIENTE'),
('CLIENTE_EMPRESA_CREADA','Cliente empresa creada','CLIENTE'),
('EMPRESA_USUARIO_ASOCIADO','Usuario asociado empresa','CLIENTE'),
('SEGURIDAD_SESION_REVOCADA','Sesion revocada','SEGURIDAD')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), categoria = VALUES(categoria);

INSERT INTO cat_parametro_negocio(codigo, nombre, valor_numerico) VALUES
('UMBRAL_TRANSFERENCIA_ALTA','Umbral transferencia alta',10000.00)
ON DUPLICATE KEY UPDATE valor_numerico = VALUES(valor_numerico);

INSERT INTO cat_transicion_estado(entidad, estado_origen_codigo, estado_destino_codigo, rol_requerido_codigo, requiere_motivo) VALUES
('PRESTAMO','EN_ESTUDIO','APROBADO','ANALISTA_INTERNO',0),
('PRESTAMO','EN_ESTUDIO','RECHAZADO','ANALISTA_INTERNO',1),
('PRESTAMO','APROBADO','DESEMBOLSADO','ANALISTA_INTERNO',0),
('TRANSFERENCIA','EN_ESPERA_APROBACION','EJECUTADA','SUPERVISOR_EMPRESA',0),
('TRANSFERENCIA','EN_ESPERA_APROBACION','RECHAZADA','SUPERVISOR_EMPRESA',1),
('TRANSFERENCIA','EN_ESPERA_APROBACION','VENCIDA',NULL,1),
('CUENTA','ACTIVA','BLOQUEADA',NULL,1),
('CUENTA','ACTIVA','CANCELADA',NULL,0)
ON DUPLICATE KEY UPDATE rol_requerido_codigo = VALUES(rol_requerido_codigo), requiere_motivo = VALUES(requiere_motivo);
