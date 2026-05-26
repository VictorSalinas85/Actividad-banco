# Firmas API REST — Sistema Bancario

**Base URL:** `http://localhost:8080`  
**Versión:** 1.0.0  
**Autenticación:** Bearer JWT en header `Authorization: Bearer <token>`  
**Swagger UI:** `http://localhost:8080/swagger-ui.html`

---

## Roles del sistema

| Código | Descripción |
|--------|-------------|
| `ANALISTA_INTERNO` | Acceso total al sistema |
| `EMPLEADO_VENTANILLA` | Operaciones de caja y cuentas |
| `EMPLEADO_COMERCIAL` | Gestión de créditos y empresas |
| `CLIENTE_PERSONA` | Operaciones propias de persona natural |
| `CLIENTE_EMPRESA_ADMIN` | Administración de empresa cliente |
| `EMPLEADO_EMPRESA_OPERATIVO` | Operaciones de empresa cliente |
| `SUPERVISOR_EMPRESA` | Supervisión y aprobaciones de empresa |

---

## Autenticación — `/api/v1/auth`

| Método | Endpoint | Acceso | Descripción |
|--------|----------|--------|-------------|
| `POST` | `/api/v1/auth/login` | Público | Autenticar usuario, retorna JWT |
| `GET` | `/api/v1/auth/me` | Autenticado | Información del usuario actual |

### POST /api/v1/auth/login
**Request:**
```json
{ "username": "string", "password": "string" }
```
**Response 200:**
```json
{
  "status": "OK",
  "data": {
    "token": "eyJ...",
    "type": "Bearer",
    "userId": 1,
    "username": "admin",
    "nombreCompleto": "Admin Sistema",
    "email": "admin@banco.com",
    "roles": ["ANALISTA_INTERNO"],
    "expiresIn": 86400
  }
}
```

### GET /api/v1/auth/me
**Response 200:**
```json
{
  "status": "OK",
  "data": {
    "id": 1,
    "username": "admin",
    "nombreCompleto": "Admin Sistema",
    "email": "admin@banco.com",
    "telefono": "3001234567",
    "estadoId": 1,
    "roles": ["ANALISTA_INTERNO"],
    "ultimoLoginAt": "2025-01-01T10:00:00",
    "createdAt": "2024-01-01T00:00:00"
  }
}
```

---

## Catálogos — `/api/v1/catalogos`

**Acceso:** `ANALISTA_INTERNO`

Cada catálogo expone 5 endpoints estándar: `GET /`, `GET /{id}`, `POST /`, `PUT /{id}`, `DELETE /{id}`

| Recurso | Base path |
|---------|-----------|
| Estado de usuario | `/api/v1/catalogos/estados-usuario` |
| Estado de cuenta | `/api/v1/catalogos/estados-cuenta` |
| Tipo de cuenta | `/api/v1/catalogos/tipos-cuenta` |
| Moneda | `/api/v1/catalogos/monedas` |
| Estado de préstamo | `/api/v1/catalogos/estados-prestamo` |
| Tipo de préstamo | `/api/v1/catalogos/tipos-prestamo` |
| Estado de transferencia | `/api/v1/catalogos/estados-transferencia` |
| Tipo de operación | `/api/v1/catalogos/tipos-operacion` |
| Canal de operación | `/api/v1/catalogos/canales-operacion` |
| Motivo de rechazo | `/api/v1/catalogos/motivos-rechazo` |
| Motivo de bloqueo | `/api/v1/catalogos/motivos-bloqueo` |
| Estado de sesión | `/api/v1/catalogos/estados-sesion` |
| Rol de empresa | `/api/v1/catalogos/roles-empresa` |
| Tipo de identificación | `/api/v1/catalogos/tipos-identificacion` |
| Tipo de movimiento | `/api/v1/catalogos/tipos-movimiento` |
| Parámetro de negocio | `/api/v1/catalogos/parametros-negocio` |
| Transición de estado | `/api/v1/catalogos/transiciones-estado` |

---

## Usuarios y Seguridad — `/api/v1/usuarios`

**Acceso:** `ANALISTA_INTERNO`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/v1/usuarios` | Listar usuarios |
| `GET` | `/api/v1/usuarios/{id}` | Obtener usuario por ID |
| `POST` | `/api/v1/usuarios` | Crear usuario |
| `PUT` | `/api/v1/usuarios/{id}` | Actualizar usuario |
| `DELETE` | `/api/v1/usuarios/{id}` | Eliminar usuario |
| `GET` | `/api/v1/usuarios/roles` | Listar roles del sistema |
| `GET` | `/api/v1/usuarios/roles/{id}` | Obtener rol por ID |
| `POST` | `/api/v1/usuarios/roles` | Crear rol |
| `PUT` | `/api/v1/usuarios/roles/{id}` | Actualizar rol |
| `DELETE` | `/api/v1/usuarios/roles/{id}` | Eliminar rol |
| `GET` | `/api/v1/usuarios/asignaciones-rol` | Listar asignaciones usuario-rol |
| `GET` | `/api/v1/usuarios/asignaciones-rol/{id}` | Obtener asignación por ID |
| `POST` | `/api/v1/usuarios/asignaciones-rol` | Asignar rol a usuario |
| `DELETE` | `/api/v1/usuarios/asignaciones-rol/{id}` | Revocar asignación de rol |
| `GET` | `/api/v1/usuarios/sesiones` | Listar sesiones (solo lectura) |
| `GET` | `/api/v1/usuarios/sesiones/{id}` | Obtener sesión por ID |

---

## Personas Naturales — `/api/v1/personas`

**Acceso:** `ANALISTA_INTERNO`, `EMPLEADO_VENTANILLA`, `EMPLEADO_COMERCIAL`  
(DELETE solo `ANALISTA_INTERNO`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/v1/personas` | Listar clientes persona natural |
| `GET` | `/api/v1/personas/{id}` | Obtener persona natural por ID |
| `POST` | `/api/v1/personas` | Crear registro de persona natural |
| `PUT` | `/api/v1/personas/{id}` | Actualizar persona natural |
| `DELETE` | `/api/v1/personas/{id}` | Eliminar persona natural |

---

## Empresas — `/api/v1/empresas`

**Acceso:** `ANALISTA_INTERNO`, `EMPLEADO_COMERCIAL`  
(DELETE solo `ANALISTA_INTERNO`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/v1/empresas` | Listar empresas |
| `GET` | `/api/v1/empresas/{id}` | Obtener empresa por ID |
| `POST` | `/api/v1/empresas` | Crear empresa |
| `PUT` | `/api/v1/empresas/{id}` | Actualizar empresa |
| `DELETE` | `/api/v1/empresas/{id}` | Eliminar empresa |
| `GET` | `/api/v1/empresas/usuarios` | Listar usuarios de empresa |
| `GET` | `/api/v1/empresas/usuarios/{id}` | Obtener usuario de empresa |
| `POST` | `/api/v1/empresas/usuarios` | Crear usuario de empresa |
| `PUT` | `/api/v1/empresas/usuarios/{id}` | Actualizar usuario de empresa |
| `DELETE` | `/api/v1/empresas/usuarios/{id}` | Eliminar usuario de empresa |
| `GET` | `/api/v1/empresas/usuarios/roles` | Listar roles de usuario-empresa |
| `GET` | `/api/v1/empresas/usuarios/roles/{id}` | Obtener rol de usuario-empresa |
| `POST` | `/api/v1/empresas/usuarios/roles` | Asignar rol a usuario-empresa |
| `DELETE` | `/api/v1/empresas/usuarios/roles/{id}` | Eliminar rol de usuario-empresa |

---

## Cuentas Bancarias — `/api/v1/cuentas`

**Acceso:** `ANALISTA_INTERNO`, `EMPLEADO_VENTANILLA`, `EMPLEADO_COMERCIAL`, `SUPERVISOR_EMPRESA`  
(mutaciones requieren `ANALISTA_INTERNO` o `EMPLEADO_VENTANILLA`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/v1/cuentas` | Listar cuentas |
| `GET` | `/api/v1/cuentas/{id}` | Obtener cuenta por ID |
| `POST` | `/api/v1/cuentas` | Crear cuenta |
| `PUT` | `/api/v1/cuentas/{id}` | Actualizar cuenta |
| `DELETE` | `/api/v1/cuentas/{id}` | Eliminar cuenta |
| `GET` | `/api/v1/cuentas/movimientos` | Listar movimientos |
| `GET` | `/api/v1/cuentas/movimientos/{id}` | Obtener movimiento por ID |
| `POST` | `/api/v1/cuentas/movimientos` | Registrar movimiento |
| `PUT` | `/api/v1/cuentas/movimientos/{id}` | Actualizar movimiento |
| `DELETE` | `/api/v1/cuentas/movimientos/{id}` | Eliminar movimiento |

---

## Préstamos — `/api/v1/prestamos`

**Acceso:** `ANALISTA_INTERNO`, `EMPLEADO_COMERCIAL`  
(DELETE solo `ANALISTA_INTERNO`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/v1/prestamos` | Listar préstamos |
| `GET` | `/api/v1/prestamos/{id}` | Obtener préstamo por ID |
| `POST` | `/api/v1/prestamos` | Crear préstamo |
| `PUT` | `/api/v1/prestamos/{id}` | Actualizar préstamo |
| `DELETE` | `/api/v1/prestamos/{id}` | Eliminar préstamo |
| `GET` | `/api/v1/prestamos/aprobaciones` | Listar aprobaciones de préstamo |
| `GET` | `/api/v1/prestamos/aprobaciones/{id}` | Obtener aprobación por ID |
| `POST` | `/api/v1/prestamos/aprobaciones` | Registrar aprobación |
| `PUT` | `/api/v1/prestamos/aprobaciones/{id}` | Actualizar aprobación |
| `DELETE` | `/api/v1/prestamos/aprobaciones/{id}` | Eliminar aprobación |
| `GET` | `/api/v1/prestamos/desembolsos` | Listar desembolsos |
| `GET` | `/api/v1/prestamos/desembolsos/{id}` | Obtener desembolso por ID |
| `POST` | `/api/v1/prestamos/desembolsos` | Registrar desembolso |
| `PUT` | `/api/v1/prestamos/desembolsos/{id}` | Actualizar desembolso |
| `DELETE` | `/api/v1/prestamos/desembolsos/{id}` | Eliminar desembolso |

---

## Transferencias — `/api/v1/transferencias`

**Acceso:** `ANALISTA_INTERNO`, `EMPLEADO_VENTANILLA`, `SUPERVISOR_EMPRESA`, `EMPLEADO_EMPRESA_OPERATIVO`  
(mutaciones y aprobaciones: `ANALISTA_INTERNO` o `SUPERVISOR_EMPRESA`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/v1/transferencias` | Listar transferencias |
| `GET` | `/api/v1/transferencias/{id}` | Obtener transferencia por ID |
| `POST` | `/api/v1/transferencias` | Crear transferencia |
| `PUT` | `/api/v1/transferencias/{id}` | Actualizar transferencia |
| `DELETE` | `/api/v1/transferencias/{id}` | Eliminar transferencia |
| `GET` | `/api/v1/transferencias/aprobaciones` | Listar aprobaciones |
| `GET` | `/api/v1/transferencias/aprobaciones/{id}` | Obtener aprobación por ID |
| `POST` | `/api/v1/transferencias/aprobaciones` | Registrar aprobación |
| `PUT` | `/api/v1/transferencias/aprobaciones/{id}` | Actualizar aprobación |
| `DELETE` | `/api/v1/transferencias/aprobaciones/{id}` | Eliminar aprobación |

---

## Productos Bancarios — `/api/v1/productos`

**Acceso:** Cualquier usuario autenticado puede leer. Mutaciones: `ANALISTA_INTERNO`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/v1/productos` | Listar productos bancarios |
| `GET` | `/api/v1/productos/{id}` | Obtener producto por ID |
| `POST` | `/api/v1/productos` | Crear producto |
| `PUT` | `/api/v1/productos/{id}` | Actualizar producto |
| `DELETE` | `/api/v1/productos/{id}` | Eliminar producto |

---

## Auditoría — `/api/v1/auditoria`

**Acceso:** `ANALISTA_INTERNO` (solo lectura)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/v1/auditoria/bitacora` | Listar eventos de bitácora |
| `GET` | `/api/v1/auditoria/bitacora/{id}` | Obtener evento por ID |
| `GET` | `/api/v1/auditoria/cambios` | Listar cambios de datos |
| `GET` | `/api/v1/auditoria/cambios/{id}` | Obtener cambio por ID |
| `GET` | `/api/v1/auditoria/errores` | Listar errores de operación |
| `GET` | `/api/v1/auditoria/errores/{id}` | Obtener error por ID |

---

## Operaciones de Negocio (Stored Procedures)

### Clientes — `/api/v1/ops/clientes`

| Método | Endpoint | Roles | SP invocado |
|--------|----------|-------|-------------|
| `POST` | `/api/v1/ops/clientes/crear-persona` | ANALISTA, VENTANILLA, COMERCIAL | `sp_cli_crear_persona` |
| `POST` | `/api/v1/ops/clientes/crear-empresa` | ANALISTA, COMERCIAL | `sp_cli_crear_empresa` |
| `POST` | `/api/v1/ops/clientes/cambiar-estado` | ANALISTA, VENTANILLA, COMERCIAL | `sp_cli_cambiar_estado_cliente` |
| `POST` | `/api/v1/ops/clientes/asociar-usuario` | ANALISTA, COMERCIAL | `sp_cli_asociar_usuario_empresa` |
| `POST` | `/api/v1/ops/clientes/asignar-rol` | ANALISTA, CLI_EMPRESA_ADMIN | `sp_cli_asignar_rol_empresa_usuario` |

### Cuentas — `/api/v1/ops/cuentas`

| Método | Endpoint | Roles | SP invocado |
|--------|----------|-------|-------------|
| `POST` | `/api/v1/ops/cuentas/abrir` | ANALISTA, VENTANILLA, COMERCIAL | `sp_cta_abrir_cuenta` |
| `POST` | `/api/v1/ops/cuentas/bloquear` | ANALISTA, VENTANILLA | `sp_cta_bloquear_cuenta` |
| `POST` | `/api/v1/ops/cuentas/cancelar` | ANALISTA, VENTANILLA | `sp_cta_cancelar_cuenta` |
| `POST` | `/api/v1/ops/cuentas/consignar` | ANALISTA, VENTANILLA, CLI_PERSONA, CLI_EMPRESA_ADMIN, EMP_EMPRESA_OP | `sp_cta_consignar` |
| `POST` | `/api/v1/ops/cuentas/retirar` | ANALISTA, VENTANILLA, CLI_PERSONA, CLI_EMPRESA_ADMIN, EMP_EMPRESA_OP | `sp_cta_retirar` |

### Préstamos — `/api/v1/ops/prestamos`

| Método | Endpoint | Roles | SP invocado |
|--------|----------|-------|-------------|
| `POST` | `/api/v1/ops/prestamos/solicitar` | ANALISTA, COMERCIAL, CLI_PERSONA, CLI_EMPRESA_ADMIN | `sp_cre_solicitar_prestamo` |
| `POST` | `/api/v1/ops/prestamos/aprobar` | ANALISTA, COMERCIAL | `sp_cre_aprobar_prestamo` |
| `POST` | `/api/v1/ops/prestamos/rechazar` | ANALISTA, COMERCIAL | `sp_cre_rechazar_prestamo` |
| `POST` | `/api/v1/ops/prestamos/desembolsar` | ANALISTA, VENTANILLA, COMERCIAL | `sp_cre_desembolsar_prestamo` |

### Transferencias — `/api/v1/ops/transferencias`

| Método | Endpoint | Roles | SP invocado |
|--------|----------|-------|-------------|
| `POST` | `/api/v1/ops/transferencias/crear` | ANALISTA, VENTANILLA, CLI_PERSONA, CLI_EMPRESA_ADMIN, EMP_EMPRESA_OP | `sp_trf_crear_transferencia` |
| `POST` | `/api/v1/ops/transferencias/aprobar` | ANALISTA, SUPERVISOR_EMPRESA | `sp_trf_aprobar_transferencia` |
| `POST` | `/api/v1/ops/transferencias/rechazar` | ANALISTA, SUPERVISOR_EMPRESA | `sp_trf_rechazar_transferencia` |
| `POST` | `/api/v1/ops/transferencias/ejecutar-directa` | ANALISTA, VENTANILLA, CLI_PERSONA | `sp_trf_ejecutar_transferencia_directa` |
| `POST` | `/api/v1/ops/transferencias/vencer-pendientes` | ANALISTA | `sp_trf_vencer_transferencias_pendientes` |
| `POST` | `/api/v1/ops/transferencias/pendientes-empresa` | ANALISTA, SUPERVISOR_EMPRESA, CLI_EMPRESA_ADMIN | `sp_trf_consultar_pendientes_aprobacion_empresa` |

### Sesiones — `/api/v1/ops/sesiones`

| Método | Endpoint | Roles | SP invocado |
|--------|----------|-------|-------------|
| `POST` | `/api/v1/ops/sesiones/validar` | ANALISTA, VENTANILLA, COMERCIAL | `sp_sec_validar_sesion` |
| `POST` | `/api/v1/ops/sesiones/revocar` | ANALISTA, VENTANILLA, COMERCIAL | `sp_sec_revocar_sesion` |

### Auditoría (escritura) — `/api/v1/ops/auditoria`

| Método | Endpoint | Roles | SP invocado |
|--------|----------|-------|-------------|
| `POST` | `/api/v1/ops/auditoria/registrar-evento` | ANALISTA | `sp_aud_registrar_evento` |
| `POST` | `/api/v1/ops/auditoria/registrar-error` | ANALISTA | `sp_aud_registrar_error` |

---

## Formato de respuesta

### Éxito
```json
{
  "status": "OK",
  "message": "...",
  "data": { }
}
```

### Error
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Entidad no encontrada",
  "codigoError": "ENTIDAD_NO_ENCONTRADA",
  "path": "/api/v1/...",
  "timestamp": "2025-01-01T10:00:00",
  "details": []
}
```

### Códigos HTTP

| Código | Situación |
|--------|-----------|
| `200` | Operación exitosa |
| `201` | Recurso creado |
| `400` | Error de validación o dominio |
| `401` | Sin autenticación o token inválido |
| `403` | Sin permisos para la operación |
| `404` | Entidad no encontrada |
| `409` | Conflicto (duplicado, integridad de datos) |
| `422` | Regla de negocio violada (saldo, estado inválido…) |
| `500` | Error interno o fallo de stored procedure |

---

## Variables de entorno

| Variable | Default | Descripción |
|----------|---------|-------------|
| `JWT_SECRET` | *(dev default)* | Clave HMAC-SHA256 para firmar tokens (mín. 32 chars) |
| `JWT_EXPIRATION` | `86400` | Duración del token en segundos |
| `FRONTEND_ALLOWED_ORIGINS` | `http://localhost:3000,...` | Orígenes CORS permitidos (coma-separados) |
| `SPRING_DATASOURCE_URL` | — | URL JDBC de la base de datos |
| `SPRING_DATASOURCE_USERNAME` | — | Usuario de base de datos |
| `SPRING_DATASOURCE_PASSWORD` | — | Contraseña de base de datos |
