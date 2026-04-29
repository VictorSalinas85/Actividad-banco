# 01 - Glosario y Convenciones

## Glosario funcional
- Cliente Persona Natural: titular individual de productos.
- Cliente Empresa: entidad juridica con usuarios delegados.
- Cuenta Bancaria: producto de deposito con saldo y estado.
- Prestamo: producto de credito con ciclo de aprobacion.
- Transferencia: movimiento de fondos entre cuentas.
- Bitacora: registro inmutable de eventos de negocio.

## Convenciones de modelado
1. Nombres en snake_case.
2. Tablas por modulo con prefijo: sec_, cat_, cli_, cta_, cre_, trf_, aud_.
3. PK: id bigint unsigned auto_increment.
4. FK: id_<entidad_referenciada>.
5. Timestamps en UTC con datetime(6).

## Campos base obligatorios en tablas de negocio
- created_at datetime(6) not null
- updated_at datetime(6) not null
- created_by bigint unsigned not null
- updated_by bigint unsigned not null
- row_version bigint unsigned not null default 1
- estado_id bigint unsigned not null (si aplica)

## Convenciones de errores de dominio
- Formato: DOM-<MODULO>-<NUMERO>
- Ejemplos:
  - DOM-TRF-001: saldo insuficiente
  - DOM-CRE-002: transicion de estado invalida
  - DOM-SEC-003: usuario sin permiso

## Convenciones de procedimientos almacenados
- Prefijo por modulo: sp_sec_, sp_cli_, sp_cta_, sp_cre_, sp_trf_, sp_aud_.
- Todo SP transaccional debe devolver:
  - out_code varchar(32)
  - out_message varchar(255)
  - out_reference varchar(64)
