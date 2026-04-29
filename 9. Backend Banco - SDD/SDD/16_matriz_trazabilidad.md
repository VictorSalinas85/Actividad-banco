# 16 - Matriz de Trazabilidad

## Objetivo
Relacionar cada regla del negocio con su implementacion SQL y su prueba.

## Plantilla de matriz
Columnas:
1. id_regla
2. descripcion_regla
3. documento_origen
4. componente_sql (tabla/constraint/trigger/sp)
5. script_version
6. caso_prueba
7. resultado_esperado
8. estado (pendiente/en_progreso/cubierta)

## Registros iniciales sugeridos
1. R-CRE-001 | Prestamo solo cambia de EN_ESTUDIO a APROBADO/RECHAZADO | banco.md | cat_transicion_estado + sp_cre_aprobar_prestamo/sp_cre_rechazar_prestamo | V0XX | T-CRE-001 | cambio valido o error | pendiente
2. R-CRE-002 | Desembolso solo desde APROBADO | banco.md | sp_cre_desembolsar_prestamo | V0XX | T-CRE-002 | desembolso valido | pendiente
3. R-TRF-001 | Transferencia monto > 0 | banco.md | check + sp_trf_crear_transferencia | V0XX | T-TRF-001 | rechazo si <= 0 | pendiente
4. R-TRF-002 | Transferencia alto monto requiere aprobacion | banco.md | sp_trf_crear_transferencia + parametro umbral | V0XX | T-TRF-002 | estado pendiente | pendiente
5. R-TRF-003 | Pendiente > 60 min pasa a VENCIDA | banco.md | event scheduler + sp_trf_vencer_transferencias_pendientes | V0XX | T-TRF-003 | vencimiento automatico | pendiente
6. R-CTA-001 | No operar cuenta bloqueada/cancelada | banco.md | sp_cta_* + sp_trf_* | V0XX | T-CTA-001 | rechazo operativo | pendiente
7. R-CLI-001 | Identificacion duplicada bajo concurrencia retorna error de dominio | banco.md | unique + handler 1062 en sp_cli_crear_persona/sp_cli_crear_empresa | V0XX | T-CLI-001 | error controlado DOM-CLI-001 | pendiente
8. R-TRF-004 | Maker-checker en transferencias de alto monto | banco.md | sp_trf_aprobar_transferencia | V0XX | T-TRF-004 | rechazo si creador=aprobador | pendiente
9. R-TRF-005 | Idempotencia evita doble ejecucion financiera | banco.md | unique idempotency_key + sp_trf_* | V0XX | T-TRF-005 | no duplicidad de movimientos | pendiente
10. R-SEC-001 | DML directo en tablas core prohibido para app | banco.md | roles SQL + grants + uso obligatorio de SP | V0XX | T-SEC-001 | acceso denegado | pendiente

## Regla de mantenimiento
Ninguna historia de implementacion se considera cerrada sin fila cubierta en esta matriz.
