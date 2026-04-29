# 05 - Reglas de Negocio e Invariantes

## Regla 1 - Unicidad de identificacion
- Descripcion: DNI/Cedula/NIT no se repite en el sistema.
- Implementacion: unique index y SP de alta.

## Regla 2 - Operatividad de cuentas
- Descripcion: no operar en cuentas BLOQUEADA o CANCELADA.
- Implementacion: validacion en SP transaccionales.

## Regla 3 - Prestamo: ciclo de estado
- Descripcion: EN_ESTUDIO -> APROBADO/RECHAZADO; APROBADO -> DESEMBOLSADO.
- Implementacion: tabla cat_transicion_estado + SP.

## Regla 4 - Aprobacion de prestamo por analista
- Descripcion: solo rol analista interno aprueba o rechaza.
- Implementacion: SP verifica rol y permisos.

## Regla 5 - Desembolso valido
- Descripcion: requiere cuenta destino activa y monto_aprobado > 0.
- Implementacion: SP de desembolso con lock de cuenta y bitacora.

## Regla 6 - Transferencia de alto monto
- Descripcion: supera umbral -> EN_ESPERA_APROBACION.
- Implementacion: parametro en cat_parametro_negocio y SP de creacion.

## Regla 7 - Vencimiento por tiempo
- Descripcion: si espera aprobacion > 60 min -> VENCIDA.
- Implementacion: evento scheduler + SP de vencimiento.

## Regla 8 - Suficiencia de fondos
- Descripcion: no ejecutar transferencia con saldo insuficiente. Se debe considerar el limite de sobregiro autorizado para calcular el saldo disponible.
- Implementacion: SP con SELECT FOR UPDATE y validacion previa.

## Regla 9 - Trazabilidad obligatoria
- Descripcion: toda operacion critica registra evento en bitacora.
- Implementacion: insercion obligatoria dentro de la misma transaccion.

## Regla 10 - Segregacion por rol y alcance
- Descripcion: usuario solo opera sobre productos autorizados.
- Implementacion: vistas/consultas filtradas + SP con validacion de titularidad/empresa.

## Regla 11 - Unicidad de cliente concurrente
- Descripcion: la creacion de un cliente con una identificacion existente debe fallar de forma controlada bajo concurrencia.
- Implementacion: SP de alta de cliente debe manejar la excepcion de `DUPLICATE KEY` y devolver un error de dominio especifico.

## Regla 12 - Segregacion maker-checker en empresa
- Descripcion: el mismo usuario no puede crear y aprobar una transferencia que requiere autorizacion.
- Implementacion: validacion en SP de aprobacion contra id_usuario_creador.

## Regla 13 - Sobregiro condicionado
- Descripcion: el sobregiro solo aplica a tipos de cuenta autorizados y dentro del limite vigente.
- Implementacion: validacion en SP de retiro/transferencia usando tipo de cuenta + limite_sobregiro_autorizado.

## Regla 14 - Idempotencia operativa
- Descripcion: una misma operacion externa no debe ejecutarse dos veces.
- Implementacion: unique key por idempotency_key + tipo_operacion + canal, gestionada por SP.
