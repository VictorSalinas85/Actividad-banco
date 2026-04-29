# 08 - Estrategia de Procedimientos Almacenados

## Principios
1. Cada caso de uso transaccional relevante se implementa como SP.
2. Un SP = una responsabilidad funcional.
3. Validacion de permiso + validacion de negocio + persistencia + auditoria en una sola unidad transaccional.

## Contrato estandar de salida
- out_code varchar(32)
- out_message varchar(255)
- out_reference varchar(64)

## SP sugeridos por modulo
### Seguridad
1. sp_sec_validar_sesion
2. sp_sec_revocar_sesion

### Clientes
1. sp_cli_crear_persona
2. sp_cli_crear_empresa
3. sp_cli_asociar_usuario_empresa
4. sp_cli_cambiar_estado_cliente
5. sp_cli_asignar_rol_empresa_usuario

### Cuentas
1. sp_cta_abrir_cuenta
2. sp_cta_consignar
3. sp_cta_retirar (debe considerar sobregiro)
4. sp_cta_bloquear_cuenta
5. sp_cta_cancelar_cuenta

### Prestamos
1. sp_cre_solicitar_prestamo
2. sp_cre_aprobar_prestamo
3. sp_cre_rechazar_prestamo
4. sp_cre_desembolsar_prestamo

### Transferencias
1. sp_trf_crear_transferencia
2. sp_trf_aprobar_transferencia (debe considerar sobregiro en origen)
3. sp_trf_rechazar_transferencia
4. sp_trf_ejecutar_transferencia_directa
5. sp_trf_vencer_transferencias_pendientes
6. sp_trf_consultar_pendientes_aprobacion_empresa

### Auditoria
1. sp_aud_registrar_evento
2. sp_aud_registrar_error

## Plantilla minima de flujo interno por SP critico
1. Validar actor y permisos.
2. Validar estado de entidades.
3. Iniciar transaccion.
4. Bloquear filas necesarias (FOR UPDATE).
5. Validar unicidad de identificacion manejando excepcion de concurrencia.
6. Validar maker-checker cuando aplique.
7. Resolver idempotencia (retornar resultado previo o continuar).
8. Aplicar cambios.
9. Registrar bitacora.
10. Commit.
11. Manejar excepcion con rollback + error de dominio.
