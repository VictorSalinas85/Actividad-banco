# 10 - Seguridad y Control de Acceso

## Objetivo
Aplicar defensa en profundidad: permisos SQL, roles funcionales y validacion de alcance por datos.

## Capas de seguridad
1. Seguridad de autenticacion en capa aplicacion.
2. Seguridad de autorizacion funcional en SP.
3. Seguridad de datos por filtro de alcance (empresa, cliente, rol).
4. Seguridad operacional por usuarios SQL con privilegios minimos.

## Roles funcionales del dominio
1. CLIENTE_PERSONA
2. CLIENTE_EMPRESA_ADMIN
3. EMPLEADO_VENTANILLA
4. EMPLEADO_COMERCIAL
5. EMPLEADO_EMPRESA_OPERATIVO
6. SUPERVISOR_EMPRESA
7. ANALISTA_INTERNO

## Roles tecnicos SQL sugeridos
1. app_reader
2. app_writer
3. app_executor_sp
4. app_audit_reader
5. dba_admin

## Reglas clave
1. Bloquear DML directo sobre tablas core para cuentas de aplicacion.
2. Forzar uso de SP para mutaciones criticas.
3. Registrar actor funcional y usuario SQL en auditoria.
4. Cifrar datos sensibles fuera de DB o con funciones nativas segun politica.
5. Aplicar segregacion maker-checker en aprobaciones de alto impacto.
6. Evitar SQL dinamico en SP criticos; si es inevitable, validar listas blancas.
7. Ejecutar SP con privilegios minimos y estrategia definida de SECURITY DEFINER/INVOKER.

## Controles adicionales
1. Politica de contrasenas y rotacion de credenciales.
2. Conexion TLS obligatoria.
3. Segregacion de ambientes dev/qa/prod con secretos independientes.
4. Bloqueo y expiracion de sesion por inactividad.
5. Trazabilidad de intentos fallidos de autorizacion.
