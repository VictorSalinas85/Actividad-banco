# Firmas de Endpoints — Backend Banco

## Estado del Backend y Alcance de este Documento

El backend ubicado en `Banco/` es un proyecto Spring Boot 4.0.6 con Java 17 que se encuentra
en **etapa de scaffold inicial**. Al momento de este análisis, el único archivo Java implementado
es `BancoApplication.java` (punto de entrada). No existen controllers, servicios, DTOs ni
configuración de seguridad en código.

Este documento define los **contratos de API diseñados** derivados del Software Design Document
(SDD) del proyecto. Cada endpoint está clasificado como:

- `[DISEÑADO]` — especificado en el SDD, pendiente de implementación en código.

Cuando el backend implemente un endpoint, el contrato aquí descrito debe respetarse sin cambios
para no romper el frontend.

---

## Configuración Base del Servidor

| Parámetro         | Valor                          | Fuente                      |
|-------------------|--------------------------------|-----------------------------|
| Puerto            | `8080` (predeterminado)        | No configurado explícitamente en `application.properties` |
| Prefijo de API    | `/api/v1` (convención propuesta) | No configurado explícitamente |
| Base URL local    | `http://localhost:8080/api/v1` | —                           |
| Base de datos     | MariaDB en `localhost:3306/banco_bd` | `application.properties` |
| JPA DDL           | `validate` (no auto-crea tablas) | `application.properties` |

> **Nota importante para el frontend:** Cuando el backend esté en producción, reemplaza
> `http://localhost:8080` por la URL del servidor desplegado. Se recomienda almacenar la
> base URL en una variable de entorno (`VITE_API_URL`, `REACT_APP_API_URL`, etc.).

---

## Autenticación Global

El proyecto incluye la dependencia `spring-boot-starter-security` pero aún no tiene
configuración implementada. El esquema de seguridad previsto es **JWT Bearer Token**.

Una vez implementado, todos los endpoints protegidos requieren:

```
Authorization: Bearer <jwt_token>
Content-Type: application/json
```

Los endpoints de catálogos y login no requieren token.

---

## Módulos y Endpoints

### Índice

| # | Módulo | Método | Ruta | Estado |
|---|--------|--------|------|--------|
| 01 | Auth | POST | `/api/v1/auth/login` | DISEÑADO |
| 02 | Auth | POST | `/api/v1/auth/logout` | DISEÑADO |
| 03 | Clientes / Personas | POST | `/api/v1/clientes/personas` | DISEÑADO |
| 04 | Clientes / Personas | GET | `/api/v1/clientes/personas` | DISEÑADO |
| 05 | Clientes / Personas | GET | `/api/v1/clientes/personas/{id}` | DISEÑADO |
| 06 | Clientes / Personas | PATCH | `/api/v1/clientes/personas/{id}/estado` | DISEÑADO |
| 07 | Clientes / Empresas | POST | `/api/v1/clientes/empresas` | DISEÑADO |
| 08 | Clientes / Empresas | GET | `/api/v1/clientes/empresas` | DISEÑADO |
| 09 | Clientes / Empresas | GET | `/api/v1/clientes/empresas/{id}` | DISEÑADO |
| 10 | Clientes / Empresas | POST | `/api/v1/clientes/empresas/{empresaId}/usuarios` | DISEÑADO |
| 11 | Clientes / Empresas | PATCH | `/api/v1/clientes/empresas/{empresaId}/usuarios/{usuarioId}/rol` | DISEÑADO |
| 12 | Cuentas | POST | `/api/v1/cuentas` | DISEÑADO |
| 13 | Cuentas | GET | `/api/v1/cuentas/{numeroCuenta}` | DISEÑADO |
| 14 | Cuentas | GET | `/api/v1/cuentas/{numeroCuenta}/movimientos` | DISEÑADO |
| 15 | Cuentas | POST | `/api/v1/cuentas/{numeroCuenta}/consignar` | DISEÑADO |
| 16 | Cuentas | POST | `/api/v1/cuentas/{numeroCuenta}/retirar` | DISEÑADO |
| 17 | Cuentas | PATCH | `/api/v1/cuentas/{numeroCuenta}/bloquear` | DISEÑADO |
| 18 | Cuentas | PATCH | `/api/v1/cuentas/{numeroCuenta}/cancelar` | DISEÑADO |
| 19 | Préstamos | POST | `/api/v1/prestamos` | DISEÑADO |
| 20 | Préstamos | GET | `/api/v1/prestamos` | DISEÑADO |
| 21 | Préstamos | GET | `/api/v1/prestamos/{id}` | DISEÑADO |
| 22 | Préstamos | PATCH | `/api/v1/prestamos/{id}/aprobar` | DISEÑADO |
| 23 | Préstamos | PATCH | `/api/v1/prestamos/{id}/rechazar` | DISEÑADO |
| 24 | Préstamos | POST | `/api/v1/prestamos/{id}/desembolsar` | DISEÑADO |
| 25 | Transferencias | POST | `/api/v1/transferencias` | DISEÑADO |
| 26 | Transferencias | GET | `/api/v1/transferencias` | DISEÑADO |
| 27 | Transferencias | GET | `/api/v1/transferencias/{id}` | DISEÑADO |
| 28 | Transferencias | GET | `/api/v1/transferencias/pendientes` | DISEÑADO |
| 29 | Transferencias | PATCH | `/api/v1/transferencias/{id}/aprobar` | DISEÑADO |
| 30 | Transferencias | PATCH | `/api/v1/transferencias/{id}/rechazar` | DISEÑADO |
| 31 | Catálogos | GET | `/api/v1/catalogos/tipos-identificacion` | DISEÑADO |
| 32 | Catálogos | GET | `/api/v1/catalogos/tipos-cuenta` | DISEÑADO |
| 33 | Catálogos | GET | `/api/v1/catalogos/monedas` | DISEÑADO |
| 34 | Catálogos | GET | `/api/v1/catalogos/estados-prestamo` | DISEÑADO |
| 35 | Catálogos | GET | `/api/v1/catalogos/tipos-prestamo` | DISEÑADO |
| 36 | Catálogos | GET | `/api/v1/catalogos/estados-transferencia` | DISEÑADO |
| 37 | Catálogos | GET | `/api/v1/catalogos/roles-empresa` | DISEÑADO |

---

## Formato Estándar de Respuesta

El backend debe retornar siempre esta estructura envolvente:

```json
{
  "code": "OK",
  "message": "Operación exitosa",
  "reference": "REF-0001234",
  "data": { }
}
```

Y en caso de error:

```json
{
  "code": "DOM-TRF-001",
  "message": "Saldo insuficiente en cuenta origen",
  "reference": null,
  "data": null
}
```

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `code` | `string` | `"OK"` en éxito; código de error de dominio en fallo (ej. `DOM-TRF-001`) |
| `message` | `string` | Descripción legible por el usuario |
| `reference` | `string \| null` | ID o número de referencia de la operación |
| `data` | `object \| array \| null` | Payload de respuesta |

---

---

## MÓDULO 1 — AUTENTICACIÓN

---

### Endpoint 01 — Iniciar sesión `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `POST` |
| **Ruta** | `/api/v1/auth/login` |
| **Controlador** | `AuthController` (no implementado aún) |
| **SP asociado** | `sp_sec_validar_sesion` |

**Descripción:** Autentica a un usuario con su nombre de usuario y contraseña. Retorna un JWT
que debe enviarse en todas las peticiones posteriores como `Authorization: Bearer <token>`.

**Autenticación requerida:** No.

**Parámetros:**

| Tipo | Nombre | Requerido | Descripción |
|------|--------|-----------|-------------|
| Body | `username` | Sí | Nombre de usuario registrado |
| Body | `password` | Sí | Contraseña en texto plano (se encripta en tránsito via HTTPS) |

**Request de ejemplo:**
```json
{
  "username": "jperez",
  "password": "MiClave2024!"
}
```

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Sesión iniciada correctamente",
  "reference": "SES-000321",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 3600,
    "usuario": {
      "id": 5,
      "username": "jperez",
      "roles": ["CLIENTE_PERSONA"],
      "estado": "ACTIVO"
    }
  }
}
```

**Códigos de estado:**

| Código | Situación |
|--------|-----------|
| `200` | Login exitoso |
| `401` | Credenciales inválidas |
| `423` | Cuenta bloqueada o suspendida |
| `500` | Error interno del servidor |

**Errores comunes:**

| Code | Mensaje |
|------|---------|
| `DOM-SEC-001` | Usuario sin permiso o credenciales incorrectas |

**Validaciones importantes:**
- `username` y `password` no deben estar vacíos.
- El estado de sesión se registra en `cat_estado_sesion` con valor `ACTIVA`.

**Ejemplo de consumo con `axios`:**
```js
const login = async (username, password) => {
  const response = await axios.post('http://localhost:8080/api/v1/auth/login', {
    username,
    password,
  });
  const { token } = response.data.data;
  localStorage.setItem('token', token);
  return response.data.data;
};
```

---

### Endpoint 02 — Cerrar sesión `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `POST` |
| **Ruta** | `/api/v1/auth/logout` |
| **Controlador** | `AuthController` (no implementado aún) |
| **SP asociado** | `sp_sec_revocar_sesion` |

**Descripción:** Invalida el token JWT activo del usuario, cambiando el estado de sesión a
`REVOCADA` en base de datos.

**Autenticación requerida:** Sí — `Authorization: Bearer <token>`.

**Parámetros:** Ninguno en body. El token se toma del header.

**Request de ejemplo:**
```
POST /api/v1/auth/logout
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Sesión cerrada correctamente",
  "reference": "SES-000321",
  "data": null
}
```

**Códigos de estado:**

| Código | Situación |
|--------|-----------|
| `200` | Sesión cerrada |
| `401` | Token inválido o ya expirado |

**Ejemplo de consumo con `axios`:**
```js
const logout = async () => {
  const token = localStorage.getItem('token');
  await axios.post(
    'http://localhost:8080/api/v1/auth/logout',
    {},
    { headers: { Authorization: `Bearer ${token}` } }
  );
  localStorage.removeItem('token');
};
```

---

---

## MÓDULO 2 — CLIENTES: PERSONAS NATURALES

---

### Endpoint 03 — Crear persona natural `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `POST` |
| **Ruta** | `/api/v1/clientes/personas` |
| **Controlador** | `ClientePersonaController` (no implementado) |
| **SP asociado** | `sp_cli_crear_persona` |

**Descripción:** Registra una nueva persona natural en el sistema. El SP valida unicidad
de identificación de forma concurrente y segura (maneja `DUPLICATE KEY`).

**Autenticación requerida:** Sí — roles: `EMPLEADO_VENTANILLA`, `EMPLEADO_COMERCIAL`.

**Parámetros (Body):**

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| `tipoIdentificacionId` | `number` | Sí | ID del catálogo `cat_tipo_identificacion` |
| `identificacion` | `string` | Sí | Número de documento. Único global |
| `nombres` | `string` | Sí | Nombres completos |
| `apellidos` | `string` | Sí | Apellidos completos |
| `fechaNacimiento` | `string` | Sí | Formato `YYYY-MM-DD` |
| `correo` | `string` | No | Correo electrónico |
| `telefono` | `string` | No | Teléfono de contacto |

**Request de ejemplo:**
```json
{
  "tipoIdentificacionId": 1,
  "identificacion": "1098765432",
  "nombres": "Juan Carlos",
  "apellidos": "Pérez Gómez",
  "fechaNacimiento": "1990-05-15",
  "correo": "jcperez@ejemplo.com",
  "telefono": "3001234567"
}
```

**Respuesta exitosa (`201 Created`):**
```json
{
  "code": "OK",
  "message": "Persona natural creada correctamente",
  "reference": "CLI-000045",
  "data": {
    "id": 45,
    "tipoIdentificacion": "CEDULA_CIUDADANIA",
    "identificacion": "1098765432",
    "nombres": "Juan Carlos",
    "apellidos": "Pérez Gómez",
    "fechaNacimiento": "1990-05-15",
    "correo": "jcperez@ejemplo.com",
    "telefono": "3001234567",
    "estado": "ACTIVO",
    "fechaCreacion": "2026-05-24T10:30:00Z"
  }
}
```

**Códigos de estado:**

| Código | Situación |
|--------|-----------|
| `201` | Persona creada exitosamente |
| `400` | Campos requeridos faltantes o formato inválido |
| `409` | Identificación ya registrada en el sistema |
| `401` | No autenticado |
| `403` | Rol no autorizado |

**Errores comunes:**

| Code | Mensaje |
|------|---------|
| `DOM-CLI-001` | Identificación de cliente ya existe |

**Validaciones:**
- `identificacion` debe ser único en todo el sistema (índice UNIQUE).
- `tipoIdentificacionId` debe existir en el catálogo activo.
- `fechaNacimiento` debe ser fecha pasada.

**Ejemplo con `axios`:**
```js
const crearPersona = async (datos) => {
  const token = localStorage.getItem('token');
  const response = await axios.post(
    'http://localhost:8080/api/v1/clientes/personas',
    datos,
    { headers: { Authorization: `Bearer ${token}` } }
  );
  return response.data;
};
```

---

### Endpoint 04 — Listar personas naturales `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `GET` |
| **Ruta** | `/api/v1/clientes/personas` |
| **Controlador** | `ClientePersonaController` (no implementado) |
| **SP asociado** | Consulta directa / vista |

**Descripción:** Retorna el listado paginado de personas naturales registradas.

**Autenticación requerida:** Sí — roles: `EMPLEADO_VENTANILLA`, `EMPLEADO_COMERCIAL`, `ANALISTA_INTERNO`.

**Parámetros (Query):**

| Nombre | Tipo | Requerido | Descripción |
|--------|------|-----------|-------------|
| `page` | `number` | No | Página (base 0, default `0`) |
| `size` | `number` | No | Elementos por página (default `20`) |
| `estado` | `string` | No | Filtrar por estado (`ACTIVO`, `INACTIVO`, `BLOQUEADO`) |
| `identificacion` | `string` | No | Filtro por número de identificación |

**Request de ejemplo:**
```
GET /api/v1/clientes/personas?page=0&size=10&estado=ACTIVO
```

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Consulta exitosa",
  "reference": null,
  "data": {
    "contenido": [
      {
        "id": 45,
        "tipoIdentificacion": "CEDULA_CIUDADANIA",
        "identificacion": "1098765432",
        "nombres": "Juan Carlos",
        "apellidos": "Pérez Gómez",
        "estado": "ACTIVO"
      }
    ],
    "paginaActual": 0,
    "totalPaginas": 3,
    "totalElementos": 58
  }
}
```

**Códigos de estado:**

| Código | Situación |
|--------|-----------|
| `200` | Consulta exitosa |
| `401` | No autenticado |
| `403` | Rol no autorizado |

**Ejemplo con `axios`:**
```js
const listarPersonas = async (page = 0, size = 20, filtros = {}) => {
  const token = localStorage.getItem('token');
  const params = { page, size, ...filtros };
  const response = await axios.get(
    'http://localhost:8080/api/v1/clientes/personas',
    { headers: { Authorization: `Bearer ${token}` }, params }
  );
  return response.data.data;
};
```

---

### Endpoint 05 — Obtener persona natural por ID `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `GET` |
| **Ruta** | `/api/v1/clientes/personas/{id}` |
| **Controlador** | `ClientePersonaController` (no implementado) |

**Descripción:** Retorna el detalle completo de una persona natural por su ID interno.

**Autenticación requerida:** Sí.

**Parámetros (Path):**

| Nombre | Tipo | Requerido | Descripción |
|--------|------|-----------|-------------|
| `id` | `number` | Sí | ID interno de la persona |

**Request de ejemplo:**
```
GET /api/v1/clientes/personas/45
```

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Consulta exitosa",
  "reference": null,
  "data": {
    "id": 45,
    "tipoIdentificacion": "CEDULA_CIUDADANIA",
    "identificacion": "1098765432",
    "nombres": "Juan Carlos",
    "apellidos": "Pérez Gómez",
    "fechaNacimiento": "1990-05-15",
    "correo": "jcperez@ejemplo.com",
    "telefono": "3001234567",
    "estado": "ACTIVO",
    "fechaCreacion": "2026-05-24T10:30:00Z",
    "cuentas": [
      { "numeroCuenta": "001-0000123-4", "tipoCuenta": "AHORROS", "estado": "ACTIVA" }
    ]
  }
}
```

**Códigos de estado:**

| Código | Situación |
|--------|-----------|
| `200` | Persona encontrada |
| `404` | No existe persona con ese ID |
| `401` | No autenticado |

**Ejemplo con `fetch`:**
```js
const obtenerPersona = async (id) => {
  const token = localStorage.getItem('token');
  const res = await fetch(`http://localhost:8080/api/v1/clientes/personas/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(`HTTP ${res.status}`);
  return res.json();
};
```

---

### Endpoint 06 — Cambiar estado de persona natural `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `PATCH` |
| **Ruta** | `/api/v1/clientes/personas/{id}/estado` |
| **Controlador** | `ClientePersonaController` (no implementado) |
| **SP asociado** | `sp_cli_cambiar_estado_cliente` |

**Descripción:** Cambia el estado operativo de una persona natural (`ACTIVO`, `INACTIVO`,
`BLOQUEADO`). La transición debe ser válida según `cat_transicion_estado`.

**Autenticación requerida:** Sí — rol: `ANALISTA_INTERNO`, `EMPLEADO_COMERCIAL`.

**Parámetros:**

| Tipo | Nombre | Requerido | Descripción |
|------|--------|-----------|-------------|
| Path | `id` | Sí | ID de la persona |
| Body | `nuevoEstado` | Sí | Código del nuevo estado |
| Body | `motivo` | No | Motivo del cambio (requerido si `requiere_motivo = 1`) |

**Request de ejemplo:**
```json
{
  "nuevoEstado": "BLOQUEADO",
  "motivo": "Reporte de actividad sospechosa"
}
```

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Estado actualizado correctamente",
  "reference": "CLI-000045",
  "data": {
    "id": 45,
    "estadoAnterior": "ACTIVO",
    "estadoNuevo": "BLOQUEADO",
    "fechaCambio": "2026-05-24T11:00:00Z"
  }
}
```

**Códigos de estado:**

| Código | Situación |
|--------|-----------|
| `200` | Estado cambiado |
| `400` | Transición de estado no permitida |
| `404` | Persona no encontrada |
| `401` | No autenticado |
| `403` | Rol sin permiso para esta transición |

---

---

## MÓDULO 3 — CLIENTES: EMPRESAS

---

### Endpoint 07 — Crear empresa `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `POST` |
| **Ruta** | `/api/v1/clientes/empresas` |
| **Controlador** | `ClienteEmpresaController` (no implementado) |
| **SP asociado** | `sp_cli_crear_empresa` |

**Descripción:** Registra una nueva empresa cliente en el sistema. El NIT debe ser único.

**Autenticación requerida:** Sí — roles: `EMPLEADO_COMERCIAL`, `EMPLEADO_VENTANILLA`.

**Parámetros (Body):**

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| `nit` | `string` | Sí | NIT de la empresa, único en el sistema |
| `razonSocial` | `string` | Sí | Razón social legal |
| `correo` | `string` | No | Correo corporativo |
| `telefono` | `string` | No | Teléfono principal |
| `representanteId` | `number` | Sí | ID de la persona natural que actúa como representante legal |

**Request de ejemplo:**
```json
{
  "nit": "900123456-7",
  "razonSocial": "Comercializadora ABC S.A.S",
  "correo": "info@comercializadoraabc.com",
  "telefono": "6012345678",
  "representanteId": 45
}
```

**Respuesta exitosa (`201 Created`):**
```json
{
  "code": "OK",
  "message": "Empresa creada correctamente",
  "reference": "EMP-000012",
  "data": {
    "id": 12,
    "nit": "900123456-7",
    "razonSocial": "Comercializadora ABC S.A.S",
    "correo": "info@comercializadoraabc.com",
    "telefono": "6012345678",
    "estado": "ACTIVO",
    "fechaCreacion": "2026-05-24T10:45:00Z"
  }
}
```

**Códigos de estado:**

| Código | Situación |
|--------|-----------|
| `201` | Empresa creada |
| `400` | Campos inválidos |
| `409` | NIT ya registrado |
| `404` | `representanteId` no existe |
| `401` | No autenticado |

**Ejemplo con `axios`:**
```js
const crearEmpresa = async (datos) => {
  const token = localStorage.getItem('token');
  const response = await axios.post(
    'http://localhost:8080/api/v1/clientes/empresas',
    datos,
    { headers: { Authorization: `Bearer ${token}` } }
  );
  return response.data;
};
```

---

### Endpoint 08 — Listar empresas `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `GET` |
| **Ruta** | `/api/v1/clientes/empresas` |
| **Controlador** | `ClienteEmpresaController` (no implementado) |

**Descripción:** Listado paginado de empresas registradas.

**Autenticación requerida:** Sí.

**Parámetros (Query):** `page`, `size`, `estado`, `nit` (mismo patrón que personas).

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Consulta exitosa",
  "reference": null,
  "data": {
    "contenido": [
      {
        "id": 12,
        "nit": "900123456-7",
        "razonSocial": "Comercializadora ABC S.A.S",
        "estado": "ACTIVO"
      }
    ],
    "paginaActual": 0,
    "totalPaginas": 2,
    "totalElementos": 18
  }
}
```

**Códigos de estado:** `200`, `401`, `403`.

---

### Endpoint 09 — Obtener empresa por ID `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `GET` |
| **Ruta** | `/api/v1/clientes/empresas/{id}` |
| **Controlador** | `ClienteEmpresaController` (no implementado) |

**Descripción:** Detalle completo de una empresa, incluyendo usuarios vinculados.

**Autenticación requerida:** Sí.

**Parámetros (Path):** `id` — ID interno de la empresa.

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Consulta exitosa",
  "reference": null,
  "data": {
    "id": 12,
    "nit": "900123456-7",
    "razonSocial": "Comercializadora ABC S.A.S",
    "correo": "info@comercializadoraabc.com",
    "estado": "ACTIVO",
    "usuarios": [
      {
        "usuarioId": 5,
        "username": "jperez",
        "rolEmpresa": "REPRESENTANTE_LEGAL"
      }
    ]
  }
}
```

**Códigos de estado:** `200`, `404`, `401`.

---

### Endpoint 10 — Asociar usuario a empresa `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `POST` |
| **Ruta** | `/api/v1/clientes/empresas/{empresaId}/usuarios` |
| **Controlador** | `ClienteEmpresaController` (no implementado) |
| **SP asociado** | `sp_cli_asociar_usuario_empresa` |

**Descripción:** Vincula un usuario existente a una empresa con un rol de empresa
(`OPERATIVO`, `SUPERVISOR_APROBADOR`, `REPRESENTANTE_LEGAL`).

**Autenticación requerida:** Sí — roles: `EMPLEADO_COMERCIAL`.

**Parámetros:**

| Tipo | Nombre | Requerido | Descripción |
|------|--------|-----------|-------------|
| Path | `empresaId` | Sí | ID de la empresa |
| Body | `usuarioId` | Sí | ID del usuario a vincular |
| Body | `rolEmpresa` | Sí | Código de rol (`OPERATIVO`, `SUPERVISOR_APROBADOR`, `REPRESENTANTE_LEGAL`) |

**Request de ejemplo:**
```json
{
  "usuarioId": 7,
  "rolEmpresa": "OPERATIVO"
}
```

**Respuesta exitosa (`201 Created`):**
```json
{
  "code": "OK",
  "message": "Usuario asociado a la empresa correctamente",
  "reference": "EMP-USR-000007",
  "data": {
    "empresaId": 12,
    "usuarioId": 7,
    "rolEmpresa": "OPERATIVO",
    "fechaVinculacion": "2026-05-24T11:00:00Z"
  }
}
```

**Códigos de estado:**

| Código | Situación |
|--------|-----------|
| `201` | Usuario vinculado |
| `404` | Empresa o usuario no encontrado |
| `409` | Usuario ya vinculado a esta empresa |
| `401` | No autenticado |
| `403` | Rol no autorizado |

---

### Endpoint 11 — Asignar rol de empresa a usuario `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `PATCH` |
| **Ruta** | `/api/v1/clientes/empresas/{empresaId}/usuarios/{usuarioId}/rol` |
| **Controlador** | `ClienteEmpresaController` (no implementado) |
| **SP asociado** | `sp_cli_asignar_rol_empresa_usuario` |

**Descripción:** Cambia el rol que tiene un usuario dentro de una empresa.

**Autenticación requerida:** Sí — rol: `EMPLEADO_COMERCIAL`.

**Parámetros:**

| Tipo | Nombre | Requerido | Descripción |
|------|--------|-----------|-------------|
| Path | `empresaId` | Sí | ID de la empresa |
| Path | `usuarioId` | Sí | ID del usuario |
| Body | `nuevoRol` | Sí | Nuevo rol de empresa |

**Request de ejemplo:**
```json
{
  "nuevoRol": "SUPERVISOR_APROBADOR"
}
```

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Rol actualizado correctamente",
  "reference": "EMP-USR-000007",
  "data": {
    "empresaId": 12,
    "usuarioId": 7,
    "rolAnterior": "OPERATIVO",
    "rolNuevo": "SUPERVISOR_APROBADOR"
  }
}
```

**Códigos de estado:** `200`, `400`, `404`, `401`, `403`.

---

---

## MÓDULO 4 — CUENTAS

---

### Endpoint 12 — Abrir cuenta `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `POST` |
| **Ruta** | `/api/v1/cuentas` |
| **Controlador** | `CuentaController` (no implementado) |
| **SP asociado** | `sp_cta_abrir_cuenta` |

**Descripción:** Abre una nueva cuenta bancaria para un titular (persona natural o empresa).
Genera automáticamente el número de cuenta.

**Autenticación requerida:** Sí — roles: `EMPLEADO_VENTANILLA`, `EMPLEADO_COMERCIAL`.

**Parámetros (Body):**

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| `tipoCuentaId` | `number` | Sí | ID del catálogo `cat_tipo_cuenta` |
| `monedaId` | `number` | Sí | ID del catálogo `cat_moneda` |
| `titularTipo` | `string` | Sí | `PERSONA` o `EMPRESA` |
| `titularId` | `number` | Sí | ID del titular (persona o empresa) |
| `saldoInicial` | `number` | No | Monto inicial en la cuenta (decimal >= 0) |
| `limiteSobregirosAutorizado` | `number` | No | Límite de sobregiro en la moneda de la cuenta (default `0.00`) |

**Request de ejemplo:**
```json
{
  "tipoCuentaId": 1,
  "monedaId": 1,
  "titularTipo": "PERSONA",
  "titularId": 45,
  "saldoInicial": 500000.00,
  "limiteSobregirosAutorizado": 0.00
}
```

**Respuesta exitosa (`201 Created`):**
```json
{
  "code": "OK",
  "message": "Cuenta abierta correctamente",
  "reference": "CTA-001-0000123",
  "data": {
    "numeroCuenta": "001-0000123-4",
    "tipoCuenta": "AHORROS",
    "moneda": "COP",
    "titularTipo": "PERSONA",
    "titularId": 45,
    "saldoActual": 500000.00,
    "limiteSobregirosAutorizado": 0.00,
    "estado": "ACTIVA",
    "fechaApertura": "2026-05-24T10:00:00Z"
  }
}
```

**Códigos de estado:**

| Código | Situación |
|--------|-----------|
| `201` | Cuenta abierta exitosamente |
| `400` | Campos inválidos o titular no encontrado |
| `404` | Tipo de cuenta o moneda no existe en catálogos |
| `401` | No autenticado |
| `403` | Rol no autorizado |

**Validaciones:**
- `saldoInicial` >= 0.
- El titular debe existir y estar en estado `ACTIVO`.
- `tipoCuentaId` y `monedaId` deben existir y estar activos en sus catálogos.

**Ejemplo con `axios`:**
```js
const abrirCuenta = async (datos) => {
  const token = localStorage.getItem('token');
  const response = await axios.post(
    'http://localhost:8080/api/v1/cuentas',
    datos,
    { headers: { Authorization: `Bearer ${token}` } }
  );
  return response.data;
};
```

---

### Endpoint 13 — Obtener cuenta por número `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `GET` |
| **Ruta** | `/api/v1/cuentas/{numeroCuenta}` |
| **Controlador** | `CuentaController` (no implementado) |

**Descripción:** Retorna el detalle de una cuenta dado su número único. Incluye saldo actual,
estado y datos del titular.

**Autenticación requerida:** Sí.

**Parámetros (Path):**

| Nombre | Tipo | Requerido | Descripción |
|--------|------|-----------|-------------|
| `numeroCuenta` | `string` | Sí | Número único de cuenta (ej. `001-0000123-4`) |

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Consulta exitosa",
  "reference": null,
  "data": {
    "numeroCuenta": "001-0000123-4",
    "tipoCuenta": "AHORROS",
    "moneda": "COP",
    "saldoActual": 1250000.00,
    "saldoDisponible": 1250000.00,
    "limiteSobregirosAutorizado": 0.00,
    "estado": "ACTIVA",
    "titular": {
      "tipo": "PERSONA",
      "id": 45,
      "nombreCompleto": "Juan Carlos Pérez Gómez",
      "identificacion": "1098765432"
    },
    "fechaApertura": "2026-05-24T10:00:00Z"
  }
}
```

**Nota sobre `saldoDisponible`:** `saldoActual + limiteSobregirosAutorizado`.

**Códigos de estado:** `200`, `404`, `401`.

---

### Endpoint 14 — Consultar movimientos de cuenta `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `GET` |
| **Ruta** | `/api/v1/cuentas/{numeroCuenta}/movimientos` |
| **Controlador** | `CuentaController` (no implementado) |

**Descripción:** Retorna el historial de movimientos de una cuenta ordenado por fecha
descendente. Los movimientos son registros inmutables.

**Autenticación requerida:** Sí.

**Parámetros:**

| Tipo | Nombre | Requerido | Descripción |
|------|--------|-----------|-------------|
| Path | `numeroCuenta` | Sí | Número de cuenta |
| Query | `desde` | No | Fecha inicio `YYYY-MM-DD` |
| Query | `hasta` | No | Fecha fin `YYYY-MM-DD` |
| Query | `page` | No | Página (default `0`) |
| Query | `size` | No | Tamaño (default `20`) |

**Request de ejemplo:**
```
GET /api/v1/cuentas/001-0000123-4/movimientos?desde=2026-05-01&hasta=2026-05-24&page=0&size=10
```

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Consulta exitosa",
  "reference": null,
  "data": {
    "numeroCuenta": "001-0000123-4",
    "movimientos": [
      {
        "id": 1001,
        "tipoMovimiento": "CONSIGNACION",
        "monto": 500000.00,
        "saldoResultante": 1250000.00,
        "descripcion": "Consignación en ventanilla",
        "canal": "VENTANILLA",
        "referencia": "MOV-001001",
        "fechaMovimiento": "2026-05-24T10:30:00Z"
      }
    ],
    "paginaActual": 0,
    "totalPaginas": 5,
    "totalElementos": 47
  }
}
```

**Códigos de estado:** `200`, `404`, `401`.

**Recomendación para el frontend:** Muestra `monto` en verde si es crédito (CONSIGNACION,
TRANSFERENCIA_ENTRANTE) y en rojo si es débito (RETIRO, TRANSFERENCIA_SALIENTE).

---

### Endpoint 15 — Consignar en cuenta `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `POST` |
| **Ruta** | `/api/v1/cuentas/{numeroCuenta}/consignar` |
| **Controlador** | `CuentaController` (no implementado) |
| **SP asociado** | `sp_cta_consignar` |

**Descripción:** Acredita un monto a la cuenta especificada. Crea un movimiento inmutable y
registra bitácora. Opera dentro de una transacción atómica.

**Autenticación requerida:** Sí — roles: `EMPLEADO_VENTANILLA`, `CLIENTE_PERSONA`.

**Parámetros:**

| Tipo | Nombre | Requerido | Descripción |
|------|--------|-----------|-------------|
| Path | `numeroCuenta` | Sí | Número de cuenta destino |
| Body | `monto` | Sí | Monto a consignar (decimal > 0) |
| Body | `descripcion` | No | Descripción de la operación |
| Body | `canalOperacionId` | Sí | ID del canal (`VENTANILLA`, `WEB`, `APP`) |
| Body | `idempotencyKey` | No | Clave de idempotencia para evitar duplicados |

**Request de ejemplo:**
```json
{
  "monto": 250000.00,
  "descripcion": "Consignación mensual",
  "canalOperacionId": 1,
  "idempotencyKey": "front-tx-20260524-001"
}
```

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Consignación exitosa",
  "reference": "MOV-001002",
  "data": {
    "movimientoId": 1002,
    "numeroCuenta": "001-0000123-4",
    "monto": 250000.00,
    "saldoAnterior": 1000000.00,
    "saldoNuevo": 1250000.00,
    "fecha": "2026-05-24T11:00:00Z"
  }
}
```

**Códigos de estado:**

| Código | Situación |
|--------|-----------|
| `200` | Consignación exitosa |
| `400` | Monto inválido (cero o negativo) |
| `404` | Cuenta no encontrada |
| `422` | Cuenta no operativa (bloqueada o cancelada) |
| `401` | No autenticado |

**Validaciones:**
- `monto` > 0.
- La cuenta debe estar en estado `ACTIVA`.
- Si `idempotencyKey` ya fue procesada, retorna la respuesta original sin duplicar.

**Ejemplo con `axios`:**
```js
const consignar = async (numeroCuenta, monto, descripcion) => {
  const token = localStorage.getItem('token');
  const response = await axios.post(
    `http://localhost:8080/api/v1/cuentas/${numeroCuenta}/consignar`,
    { monto, descripcion, canalOperacionId: 2 },
    { headers: { Authorization: `Bearer ${token}` } }
  );
  return response.data;
};
```

---

### Endpoint 16 — Retirar de cuenta `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `POST` |
| **Ruta** | `/api/v1/cuentas/{numeroCuenta}/retirar` |
| **Controlador** | `CuentaController` (no implementado) |
| **SP asociado** | `sp_cta_retirar` |

**Descripción:** Debita un monto de la cuenta. Valida fondos suficientes considerando el
límite de sobregiro autorizado. Utiliza `SELECT FOR UPDATE` para evitar condiciones de carrera.

**Autenticación requerida:** Sí — roles: `EMPLEADO_VENTANILLA`, `CLIENTE_PERSONA`.

**Parámetros:**

| Tipo | Nombre | Requerido | Descripción |
|------|--------|-----------|-------------|
| Path | `numeroCuenta` | Sí | Número de cuenta origen |
| Body | `monto` | Sí | Monto a retirar (decimal > 0) |
| Body | `descripcion` | No | Descripción |
| Body | `canalOperacionId` | Sí | Canal de operación |
| Body | `idempotencyKey` | No | Clave de idempotencia |

**Request de ejemplo:**
```json
{
  "monto": 100000.00,
  "descripcion": "Retiro en cajero",
  "canalOperacionId": 3,
  "idempotencyKey": "front-tx-20260524-002"
}
```

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Retiro exitoso",
  "reference": "MOV-001003",
  "data": {
    "movimientoId": 1003,
    "numeroCuenta": "001-0000123-4",
    "monto": 100000.00,
    "saldoAnterior": 1250000.00,
    "saldoNuevo": 1150000.00,
    "fecha": "2026-05-24T11:30:00Z"
  }
}
```

**Códigos de estado:**

| Código | Situación |
|--------|-----------|
| `200` | Retiro exitoso |
| `400` | Monto inválido |
| `404` | Cuenta no encontrada |
| `422` | Saldo insuficiente (`DOM-TRF-001`) o cuenta no operativa (`DOM-TRF-002`) |

**Validaciones:**
- `saldoDisponible = saldoActual + limiteSobregirosAutorizado`.
- El retiro solo se permite si `monto <= saldoDisponible`.
- El sobregiro solo aplica a tipos de cuenta autorizados.

---

### Endpoint 17 — Bloquear cuenta `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `PATCH` |
| **Ruta** | `/api/v1/cuentas/{numeroCuenta}/bloquear` |
| **Controlador** | `CuentaController` (no implementado) |
| **SP asociado** | `sp_cta_bloquear_cuenta` |

**Descripción:** Cambia el estado de la cuenta a `BLOQUEADA`. No se pueden realizar
consignaciones ni retiros en una cuenta bloqueada.

**Autenticación requerida:** Sí — roles: `ANALISTA_INTERNO`, `EMPLEADO_COMERCIAL`.

**Parámetros:**

| Tipo | Nombre | Requerido | Descripción |
|------|--------|-----------|-------------|
| Path | `numeroCuenta` | Sí | Número de cuenta |
| Body | `motivoBloqueoId` | Sí | ID del catálogo `cat_motivo_bloqueo` |
| Body | `observacion` | No | Texto adicional |

**Request de ejemplo:**
```json
{
  "motivoBloqueoId": 2,
  "observacion": "Cuenta reportada por el cliente por pérdida de tarjeta"
}
```

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Cuenta bloqueada correctamente",
  "reference": "CTA-001-0000123",
  "data": {
    "numeroCuenta": "001-0000123-4",
    "estadoAnterior": "ACTIVA",
    "estadoNuevo": "BLOQUEADA",
    "fechaBloqueo": "2026-05-24T12:00:00Z"
  }
}
```

**Códigos de estado:** `200`, `400`, `404`, `401`, `403`.

---

### Endpoint 18 — Cancelar cuenta `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `PATCH` |
| **Ruta** | `/api/v1/cuentas/{numeroCuenta}/cancelar` |
| **Controlador** | `CuentaController` (no implementado) |
| **SP asociado** | `sp_cta_cancelar_cuenta` |

**Descripción:** Cancela definitivamente una cuenta. Esta operación es irreversible.
Normalmente requiere que el saldo sea $0.00.

**Autenticación requerida:** Sí — rol: `ANALISTA_INTERNO`.

**Parámetros:**

| Tipo | Nombre | Requerido | Descripción |
|------|--------|-----------|-------------|
| Path | `numeroCuenta` | Sí | Número de cuenta |
| Body | `motivoId` | Sí | Motivo de cancelación |
| Body | `observacion` | No | Observaciones |

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Cuenta cancelada correctamente",
  "reference": "CTA-001-0000123",
  "data": {
    "numeroCuenta": "001-0000123-4",
    "estadoNuevo": "CANCELADA",
    "fechaCancelacion": "2026-05-24T12:30:00Z"
  }
}
```

**Códigos de estado:** `200`, `400` (saldo distinto de cero), `404`, `401`, `403`.

---

---

## MÓDULO 5 — PRÉSTAMOS

---

### Endpoint 19 — Solicitar préstamo `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `POST` |
| **Ruta** | `/api/v1/prestamos` |
| **Controlador** | `PrestamoController` (no implementado) |
| **SP asociado** | `sp_cre_solicitar_prestamo` |

**Descripción:** Registra una solicitud de préstamo para un cliente. El préstamo queda
en estado `EN_ESTUDIO`. Solo un analista puede cambiarlo a `APROBADO` o `RECHAZADO`.

**Autenticación requerida:** Sí — roles: `EMPLEADO_COMERCIAL`, `CLIENTE_PERSONA`.

**Parámetros (Body):**

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| `clienteTipo` | `string` | Sí | `PERSONA` o `EMPRESA` |
| `clienteId` | `number` | Sí | ID del cliente solicitante |
| `tipoPrestamId` | `number` | Sí | ID del catálogo `cat_tipo_prestamo` |
| `montoSolicitado` | `number` | Sí | Monto solicitado (decimal > 0) |
| `plazoMeses` | `number` | Sí | Plazo en meses |
| `tasaInteresAnual` | `number` | No | Si no se provee, se toma del catálogo |
| `proposito` | `string` | No | Descripción del propósito del crédito |

**Request de ejemplo:**
```json
{
  "clienteTipo": "PERSONA",
  "clienteId": 45,
  "tipoPrestamId": 1,
  "montoSolicitado": 15000000.00,
  "plazoMeses": 36,
  "proposito": "Compra de vehículo"
}
```

**Respuesta exitosa (`201 Created`):**
```json
{
  "code": "OK",
  "message": "Solicitud de préstamo registrada",
  "reference": "PRE-000088",
  "data": {
    "id": 88,
    "clienteTipo": "PERSONA",
    "clienteId": 45,
    "tipoPrestamo": "CONSUMO",
    "montoSolicitado": 15000000.00,
    "plazoMeses": 36,
    "estado": "EN_ESTUDIO",
    "fechaSolicitud": "2026-05-24T13:00:00Z"
  }
}
```

**Códigos de estado:**

| Código | Situación |
|--------|-----------|
| `201` | Solicitud registrada |
| `400` | Campos inválidos |
| `404` | Cliente no encontrado |
| `422` | Cliente en estado no operable |
| `401` | No autenticado |

**Recomendación frontend:** Muestra un mensaje claro indicando que la solicitud queda
`EN_ESTUDIO` y será revisada por un analista.

---

### Endpoint 20 — Listar préstamos `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `GET` |
| **Ruta** | `/api/v1/prestamos` |
| **Controlador** | `PrestamoController` (no implementado) |

**Descripción:** Listado paginado de préstamos. El filtro por cliente o estado permite
consultas dirigidas.

**Autenticación requerida:** Sí.

**Parámetros (Query):** `page`, `size`, `estado` (`EN_ESTUDIO`, `APROBADO`, `RECHAZADO`,
`DESEMBOLSADO`), `clienteId`, `clienteTipo`.

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Consulta exitosa",
  "reference": null,
  "data": {
    "contenido": [
      {
        "id": 88,
        "clienteNombre": "Juan Carlos Pérez Gómez",
        "tipoPrestamo": "CONSUMO",
        "montoSolicitado": 15000000.00,
        "estado": "EN_ESTUDIO",
        "fechaSolicitud": "2026-05-24T13:00:00Z"
      }
    ],
    "paginaActual": 0,
    "totalPaginas": 4,
    "totalElementos": 72
  }
}
```

---

### Endpoint 21 — Obtener préstamo por ID `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `GET` |
| **Ruta** | `/api/v1/prestamos/{id}` |
| **Controlador** | `PrestamoController` (no implementado) |

**Descripción:** Detalle completo de un préstamo, incluyendo historial de aprobaciones
y desembolso si aplica.

**Autenticación requerida:** Sí.

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Consulta exitosa",
  "reference": null,
  "data": {
    "id": 88,
    "tipoPrestamo": "CONSUMO",
    "montoSolicitado": 15000000.00,
    "montoAprobado": null,
    "plazoMeses": 36,
    "tasaInteresAnual": null,
    "estado": "EN_ESTUDIO",
    "cliente": {
      "tipo": "PERSONA",
      "id": 45,
      "nombreCompleto": "Juan Carlos Pérez Gómez"
    },
    "aprobacion": null,
    "desembolso": null,
    "fechaSolicitud": "2026-05-24T13:00:00Z"
  }
}
```

**Códigos de estado:** `200`, `404`, `401`.

---

### Endpoint 22 — Aprobar préstamo `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `PATCH` |
| **Ruta** | `/api/v1/prestamos/{id}/aprobar` |
| **Controlador** | `PrestamoController` (no implementado) |
| **SP asociado** | `sp_cre_aprobar_prestamo` |

**Descripción:** Cambia el estado de un préstamo de `EN_ESTUDIO` a `APROBADO`.
Solo puede ejecutarlo un usuario con rol `ANALISTA_INTERNO`.

**Autenticación requerida:** Sí — rol: `ANALISTA_INTERNO`.

**Parámetros:**

| Tipo | Nombre | Requerido | Descripción |
|------|--------|-----------|-------------|
| Path | `id` | Sí | ID del préstamo |
| Body | `montoAprobado` | Sí | Monto finalmente aprobado |
| Body | `tasaInteresAnual` | Sí | Tasa anual efectiva aprobada |
| Body | `observacion` | No | Comentarios del analista |

**Request de ejemplo:**
```json
{
  "montoAprobado": 12000000.00,
  "tasaInteresAnual": 18.50,
  "observacion": "Aprobado con reducción de monto por capacidad de pago"
}
```

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Préstamo aprobado",
  "reference": "PRE-000088",
  "data": {
    "id": 88,
    "estadoAnterior": "EN_ESTUDIO",
    "estadoNuevo": "APROBADO",
    "montoAprobado": 12000000.00,
    "tasaInteresAnual": 18.50,
    "fechaAprobacion": "2026-05-24T14:00:00Z",
    "analistaId": 3
  }
}
```

**Códigos de estado:**

| Código | Situación |
|--------|-----------|
| `200` | Préstamo aprobado |
| `400` | Transición inválida (`DOM-CRE-001`) o monto <= 0 |
| `404` | Préstamo no encontrado |
| `403` | Usuario no es analista |

---

### Endpoint 23 — Rechazar préstamo `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `PATCH` |
| **Ruta** | `/api/v1/prestamos/{id}/rechazar` |
| **Controlador** | `PrestamoController` (no implementado) |
| **SP asociado** | `sp_cre_rechazar_prestamo` |

**Descripción:** Cambia el estado de `EN_ESTUDIO` a `RECHAZADO`. Solo analista.

**Autenticación requerida:** Sí — rol: `ANALISTA_INTERNO`.

**Parámetros:**

| Tipo | Nombre | Requerido | Descripción |
|------|--------|-----------|-------------|
| Path | `id` | Sí | ID del préstamo |
| Body | `motivoRechazoId` | Sí | ID del catálogo `cat_motivo_rechazo` |
| Body | `observacion` | No | Comentarios |

**Request de ejemplo:**
```json
{
  "motivoRechazoId": 3,
  "observacion": "Historial crediticio insuficiente"
}
```

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Préstamo rechazado",
  "reference": "PRE-000088",
  "data": {
    "id": 88,
    "estadoNuevo": "RECHAZADO",
    "motivoRechazo": "HISTORIAL_INSUFICIENTE",
    "fechaRechazo": "2026-05-24T14:30:00Z"
  }
}
```

**Códigos de estado:** `200`, `400` (`DOM-CRE-001`), `404`, `403`.

---

### Endpoint 24 — Desembolsar préstamo `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `POST` |
| **Ruta** | `/api/v1/prestamos/{id}/desembolsar` |
| **Controlador** | `PrestamoController` (no implementado) |
| **SP asociado** | `sp_cre_desembolsar_prestamo` |

**Descripción:** Acredita el monto aprobado a la cuenta destino del cliente. El préstamo
pasa a estado `DESEMBOLSADO`. Requiere cuenta destino activa y monto_aprobado > 0.

**Autenticación requerida:** Sí — roles: `ANALISTA_INTERNO`, `EMPLEADO_COMERCIAL`.

**Parámetros:**

| Tipo | Nombre | Requerido | Descripción |
|------|--------|-----------|-------------|
| Path | `id` | Sí | ID del préstamo (debe estar en `APROBADO`) |
| Body | `cuentaDestinoNro` | Sí | Número de cuenta donde se desembolsa |

**Request de ejemplo:**
```json
{
  "cuentaDestinoNro": "001-0000123-4"
}
```

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Desembolso realizado correctamente",
  "reference": "PRE-DES-000088",
  "data": {
    "prestamoId": 88,
    "estadoNuevo": "DESEMBOLSADO",
    "montoDesembolsado": 12000000.00,
    "cuentaDestino": "001-0000123-4",
    "movimientoId": 1010,
    "fechaDesembolso": "2026-05-24T15:00:00Z"
  }
}
```

**Códigos de estado:**

| Código | Situación |
|--------|-----------|
| `200` | Desembolso exitoso |
| `400` | Préstamo no está en estado `APROBADO` |
| `404` | Préstamo o cuenta destino no encontrados |
| `422` | Cuenta destino no operativa (`DOM-CRE-002`) |
| `401` | No autenticado |

**Validaciones:**
- El préstamo debe estar en estado `APROBADO` (transición APROBADO → DESEMBOLSADO).
- La cuenta destino debe estar `ACTIVA`.
- `montoAprobado` > 0.

---

---

## MÓDULO 6 — TRANSFERENCIAS

---

### Endpoint 25 — Crear transferencia `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `POST` |
| **Ruta** | `/api/v1/transferencias` |
| **Controlador** | `TransferenciaController` (no implementado) |
| **SP asociado** | `sp_trf_crear_transferencia` / `sp_trf_ejecutar_transferencia_directa` |

**Descripción:** Crea una transferencia entre cuentas. Si el monto supera el umbral
definido en `cat_parametro_negocio`, la transferencia queda en `EN_ESPERA_APROBACION`
y debe ser aprobada por un `SUPERVISOR_EMPRESA`. Si el monto está dentro del umbral,
se ejecuta directamente (`EJECUTADA`).

**Autenticación requerida:** Sí — roles: `CLIENTE_PERSONA`, `CLIENTE_EMPRESA_ADMIN`,
`EMPLEADO_EMPRESA_OPERATIVO`.

**Parámetros (Body):**

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| `cuentaOrigenNro` | `string` | Sí | Número de cuenta origen |
| `cuentaDestinoNro` | `string` | Sí | Número de cuenta destino |
| `monto` | `number` | Sí | Monto a transferir (decimal > 0) |
| `descripcion` | `string` | No | Concepto de la transferencia |
| `idExterno` | `string` | No | ID externo para idempotencia |

**Request de ejemplo:**
```json
{
  "cuentaOrigenNro": "001-0000123-4",
  "cuentaDestinoNro": "001-0000456-7",
  "monto": 5000000.00,
  "descripcion": "Pago factura proveedor",
  "idExterno": "EXT-PAGO-20260524-001"
}
```

**Respuesta exitosa — transferencia directa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Transferencia ejecutada correctamente",
  "reference": "TRF-000201",
  "data": {
    "id": 201,
    "cuentaOrigen": "001-0000123-4",
    "cuentaDestino": "001-0000456-7",
    "monto": 5000000.00,
    "estado": "EJECUTADA",
    "fechaEjecucion": "2026-05-24T15:30:00Z"
  }
}
```

**Respuesta exitosa — requiere aprobación (`202 Accepted`):**
```json
{
  "code": "OK",
  "message": "Transferencia en espera de aprobación por monto alto",
  "reference": "TRF-000202",
  "data": {
    "id": 202,
    "monto": 50000000.00,
    "estado": "EN_ESPERA_APROBACION",
    "fechaCreacion": "2026-05-24T15:35:00Z",
    "fechaVencimiento": "2026-05-24T16:35:00Z"
  }
}
```

**Códigos de estado:**

| Código | Situación |
|--------|-----------|
| `200` | Transferencia ejecutada directamente |
| `202` | Transferencia en espera de aprobación |
| `400` | Campos inválidos o monto <= 0 |
| `404` | Cuenta origen o destino no encontrada |
| `422` | Saldo insuficiente (`DOM-TRF-001`), cuenta no operativa (`DOM-TRF-002`) |
| `401` | No autenticado |

**Validaciones importantes:**
- Monto > 0.
- Cuentas origen y destino deben estar `ACTIVAS`.
- `saldoDisponibleOrigen >= monto` (considerando sobregiro).
- Si `idExterno` ya fue procesado, retorna resultado original sin duplicar.

**Recomendación frontend:** El código `202` indica flujo maker-checker activo. Muestra
un mensaje informando que la transferencia está pendiente de aprobación.

---

### Endpoint 26 — Listar transferencias `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `GET` |
| **Ruta** | `/api/v1/transferencias` |
| **Controlador** | `TransferenciaController` (no implementado) |

**Descripción:** Listado paginado de transferencias filtradas por cuenta, estado o rango
de fechas.

**Autenticación requerida:** Sí.

**Parámetros (Query):**

| Nombre | Tipo | Descripción |
|--------|------|-------------|
| `cuentaNro` | `string` | Filtra por cuenta origen o destino |
| `estado` | `string` | `CREADA`, `EN_ESPERA_APROBACION`, `APROBADA`, `RECHAZADA`, `EJECUTADA`, `VENCIDA` |
| `desde` | `string` | Fecha inicio `YYYY-MM-DD` |
| `hasta` | `string` | Fecha fin `YYYY-MM-DD` |
| `page` | `number` | Página |
| `size` | `number` | Tamaño |

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Consulta exitosa",
  "reference": null,
  "data": {
    "contenido": [
      {
        "id": 201,
        "cuentaOrigen": "001-0000123-4",
        "cuentaDestino": "001-0000456-7",
        "monto": 5000000.00,
        "estado": "EJECUTADA",
        "fecha": "2026-05-24T15:30:00Z"
      }
    ],
    "paginaActual": 0,
    "totalPaginas": 2,
    "totalElementos": 31
  }
}
```

---

### Endpoint 27 — Obtener transferencia por ID `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `GET` |
| **Ruta** | `/api/v1/transferencias/{id}` |
| **Controlador** | `TransferenciaController` (no implementado) |

**Descripción:** Detalle completo de una transferencia, incluyendo historial de aprobaciones.

**Autenticación requerida:** Sí.

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Consulta exitosa",
  "reference": null,
  "data": {
    "id": 202,
    "cuentaOrigen": "001-0000123-4",
    "cuentaDestino": "001-0000456-7",
    "monto": 50000000.00,
    "estado": "EN_ESPERA_APROBACION",
    "descripcion": "Pago factura proveedor",
    "creadorId": 5,
    "aprobaciones": [],
    "fechaCreacion": "2026-05-24T15:35:00Z",
    "fechaVencimiento": "2026-05-24T16:35:00Z"
  }
}
```

**Códigos de estado:** `200`, `404`, `401`.

---

### Endpoint 28 — Consultar transferencias pendientes de aprobación `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `GET` |
| **Ruta** | `/api/v1/transferencias/pendientes` |
| **Controlador** | `TransferenciaController` (no implementado) |
| **SP asociado** | `sp_trf_consultar_pendientes_aprobacion_empresa` |

**Descripción:** Lista las transferencias que están en estado `EN_ESPERA_APROBACION`
para la empresa del usuario autenticado. Endpoint destinado a supervisores que deben
aprobar o rechazar.

**Autenticación requerida:** Sí — rol: `SUPERVISOR_EMPRESA`.

**Parámetros (Query):** `page`, `size`.

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Consulta exitosa",
  "reference": null,
  "data": {
    "contenido": [
      {
        "id": 202,
        "cuentaOrigen": "001-0000123-4",
        "monto": 50000000.00,
        "descripcion": "Pago factura proveedor",
        "creadorUsername": "jperez",
        "fechaCreacion": "2026-05-24T15:35:00Z",
        "minutosRestantes": 43
      }
    ],
    "paginaActual": 0,
    "totalElementos": 3
  }
}
```

**Recomendación frontend:** Muestra `minutosRestantes` como alerta visual; si es <= 10,
usa color rojo para indicar próximo vencimiento.

---

### Endpoint 29 — Aprobar transferencia `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `PATCH` |
| **Ruta** | `/api/v1/transferencias/{id}/aprobar` |
| **Controlador** | `TransferenciaController` (no implementado) |
| **SP asociado** | `sp_trf_aprobar_transferencia` |

**Descripción:** Aprueba una transferencia en estado `EN_ESPERA_APROBACION` y la ejecuta.
Aplica la regla maker-checker: el aprobador no puede ser el mismo usuario que creó la
transferencia. Valida saldo al momento de la aprobación.

**Autenticación requerida:** Sí — rol: `SUPERVISOR_EMPRESA`.

**Parámetros:**

| Tipo | Nombre | Requerido | Descripción |
|------|--------|-----------|-------------|
| Path | `id` | Sí | ID de la transferencia |
| Body | `observacion` | No | Comentarios del aprobador |

**Request de ejemplo:**
```json
{
  "observacion": "Aprobado según contrato marco vigente"
}
```

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Transferencia aprobada y ejecutada",
  "reference": "TRF-000202",
  "data": {
    "id": 202,
    "estadoAnterior": "EN_ESPERA_APROBACION",
    "estadoNuevo": "EJECUTADA",
    "aprobadorId": 9,
    "fechaAprobacion": "2026-05-24T16:00:00Z"
  }
}
```

**Códigos de estado:**

| Código | Situación |
|--------|-----------|
| `200` | Aprobada y ejecutada |
| `400` | Estado inválido (`DOM-TRF-003`), maker = checker, o saldo insuficiente en el momento |
| `404` | Transferencia no encontrada |
| `422` | Cuenta origen no operativa al momento de ejecutar |
| `403` | Usuario no autorizado (no es supervisor de esa empresa) |

**Validaciones críticas:**
- El usuario aprobador NO puede ser el mismo que creó la transferencia (maker-checker, Regla 12 del SDD).
- La transferencia no debe estar `VENCIDA`.
- Saldo debe ser suficiente al momento de ejecutar (se vuelve a validar en el SP).

---

### Endpoint 30 — Rechazar transferencia `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `PATCH` |
| **Ruta** | `/api/v1/transferencias/{id}/rechazar` |
| **Controlador** | `TransferenciaController` (no implementado) |
| **SP asociado** | `sp_trf_rechazar_transferencia` |

**Descripción:** Rechaza una transferencia en estado `EN_ESPERA_APROBACION`. El saldo
no se ve afectado.

**Autenticación requerida:** Sí — rol: `SUPERVISOR_EMPRESA`.

**Parámetros:**

| Tipo | Nombre | Requerido | Descripción |
|------|--------|-----------|-------------|
| Path | `id` | Sí | ID de la transferencia |
| Body | `motivoRechazoId` | Sí | ID del catálogo `cat_motivo_rechazo` |
| Body | `observacion` | No | Comentarios del rechazo |

**Request de ejemplo:**
```json
{
  "motivoRechazoId": 2,
  "observacion": "Monto supera el presupuesto aprobado del mes"
}
```

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Transferencia rechazada",
  "reference": "TRF-000202",
  "data": {
    "id": 202,
    "estadoNuevo": "RECHAZADA",
    "motivoRechazo": "PRESUPUESTO_EXCEDIDO",
    "fechaRechazo": "2026-05-24T16:05:00Z"
  }
}
```

**Códigos de estado:** `200`, `400` (`DOM-TRF-003`), `404`, `403`.

---

---

## MÓDULO 7 — CATÁLOGOS

Los catálogos son datos de referencia (tablas `cat_*`). Son endpoints de solo lectura,
no requieren autenticación, y su respuesta puede cachearse en el frontend (5–15 min).

---

### Endpoint 31 — Tipos de identificación `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `GET` |
| **Ruta** | `/api/v1/catalogos/tipos-identificacion` |
| **Controlador** | `CatalogoController` (no implementado) |

**Autenticación requerida:** No.

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Consulta exitosa",
  "reference": null,
  "data": [
    { "id": 1, "codigo": "CEDULA_CIUDADANIA", "nombre": "Cédula de ciudadanía" },
    { "id": 2, "codigo": "CEDULA_EXTRANJERIA", "nombre": "Cédula de extranjería" },
    { "id": 3, "codigo": "PASAPORTE", "nombre": "Pasaporte" },
    { "id": 4, "codigo": "NIT", "nombre": "NIT" }
  ]
}
```

---

### Endpoint 32 — Tipos de cuenta `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `GET` |
| **Ruta** | `/api/v1/catalogos/tipos-cuenta` |

**Autenticación requerida:** No.

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Consulta exitosa",
  "reference": null,
  "data": [
    { "id": 1, "codigo": "AHORROS", "nombre": "Cuenta de Ahorros" },
    { "id": 2, "codigo": "CORRIENTE", "nombre": "Cuenta Corriente" }
  ]
}
```

---

### Endpoint 33 — Monedas `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `GET` |
| **Ruta** | `/api/v1/catalogos/monedas` |

**Autenticación requerida:** No.

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Consulta exitosa",
  "reference": null,
  "data": [
    { "id": 1, "codigo": "COP", "nombre": "Peso colombiano" },
    { "id": 2, "codigo": "USD", "nombre": "Dólar estadounidense" }
  ]
}
```

---

### Endpoint 34 — Estados de préstamo `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `GET` |
| **Ruta** | `/api/v1/catalogos/estados-prestamo` |

**Autenticación requerida:** No.

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Consulta exitosa",
  "reference": null,
  "data": [
    { "id": 1, "codigo": "EN_ESTUDIO", "nombre": "En Estudio" },
    { "id": 2, "codigo": "APROBADO", "nombre": "Aprobado" },
    { "id": 3, "codigo": "RECHAZADO", "nombre": "Rechazado" },
    { "id": 4, "codigo": "DESEMBOLSADO", "nombre": "Desembolsado" }
  ]
}
```

---

### Endpoint 35 — Tipos de préstamo `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `GET` |
| **Ruta** | `/api/v1/catalogos/tipos-prestamo` |

**Autenticación requerida:** No.

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Consulta exitosa",
  "reference": null,
  "data": [
    { "id": 1, "codigo": "CONSUMO", "nombre": "Préstamo de Consumo" },
    { "id": 2, "codigo": "HIPOTECARIO", "nombre": "Crédito Hipotecario" },
    { "id": 3, "codigo": "EMPRESARIAL", "nombre": "Crédito Empresarial" }
  ]
}
```

---

### Endpoint 36 — Estados de transferencia `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `GET` |
| **Ruta** | `/api/v1/catalogos/estados-transferencia` |

**Autenticación requerida:** No.

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Consulta exitosa",
  "reference": null,
  "data": [
    { "id": 1, "codigo": "CREADA", "nombre": "Creada" },
    { "id": 2, "codigo": "EN_ESPERA_APROBACION", "nombre": "En Espera de Aprobación" },
    { "id": 3, "codigo": "APROBADA", "nombre": "Aprobada" },
    { "id": 4, "codigo": "RECHAZADA", "nombre": "Rechazada" },
    { "id": 5, "codigo": "EJECUTADA", "nombre": "Ejecutada" },
    { "id": 6, "codigo": "VENCIDA", "nombre": "Vencida" }
  ]
}
```

---

### Endpoint 37 — Roles de empresa `[DISEÑADO]`

| Campo | Valor |
|-------|-------|
| **Método HTTP** | `GET` |
| **Ruta** | `/api/v1/catalogos/roles-empresa` |

**Autenticación requerida:** No.

**Respuesta exitosa (`200 OK`):**
```json
{
  "code": "OK",
  "message": "Consulta exitosa",
  "reference": null,
  "data": [
    { "id": 1, "codigo": "OPERATIVO", "nombre": "Operativo" },
    { "id": 2, "codigo": "SUPERVISOR_APROBADOR", "nombre": "Supervisor Aprobador" },
    { "id": 3, "codigo": "REPRESENTANTE_LEGAL", "nombre": "Representante Legal" }
  ]
}
```

---

---

## Resumen de Códigos de Error de Dominio

| Código | Módulo | Descripción |
|--------|--------|-------------|
| `DOM-TRF-001` | Transferencias | Saldo insuficiente en cuenta origen |
| `DOM-TRF-002` | Transferencias / Cuentas | Cuenta origen no operativa |
| `DOM-TRF-003` | Transferencias | Transferencia en estado inválido para la operación |
| `DOM-CRE-001` | Préstamos | Préstamo no aprobable en estado actual |
| `DOM-CRE-002` | Préstamos | Cuenta destino de desembolso inválida |
| `DOM-SEC-001` | Seguridad | Usuario sin permiso para la operación |
| `DOM-CLI-001` | Clientes | Identificación de cliente ya existe |
| `DOM-TRX-001` | Global | Conflicto de concurrencia; operación reintentable |

---

## Roles Funcionales y Permisos por Módulo

| Rol | Auth | Personas | Empresas | Cuentas | Préstamos | Transferencias |
|-----|------|----------|----------|---------|-----------|----------------|
| `CLIENTE_PERSONA` | ✓ | Solo ver propio | — | Consignar/Retirar propio | Solicitar | Crear |
| `CLIENTE_EMPRESA_ADMIN` | ✓ | — | Ver propia | — | — | Crear |
| `EMPLEADO_VENTANILLA` | ✓ | Crear/Ver | — | Abrir/Consignar/Retirar | — | — |
| `EMPLEADO_COMERCIAL` | ✓ | Crear/Ver | Crear/Ver | Abrir | Solicitar | — |
| `EMPLEADO_EMPRESA_OPERATIVO` | ✓ | — | — | — | — | Crear |
| `SUPERVISOR_EMPRESA` | ✓ | — | — | — | — | Aprobar/Rechazar |
| `ANALISTA_INTERNO` | ✓ | Ver/Cambiar estado | Ver | Bloquear/Cancelar | Aprobar/Rechazar/Desembolsar | — |

---

## Recomendaciones Generales para el Frontend

1. **Interceptor de axios:** Configura un interceptor que añada el token JWT en todos los
   requests y redirige al login si recibe `401`.

2. **Caché de catálogos:** Los endpoints `/api/v1/catalogos/*` no cambian frecuentemente.
   Cárgalos una sola vez al inicio de la sesión y guárdalos en estado global.

3. **Manejo de errores:** Lee siempre el campo `code` de la respuesta, no solo el status HTTP.
   El `code` te dará el error de dominio específico para mostrar mensajes útiles al usuario.

4. **Idempotencia:** En operaciones de consignación, retiro y transferencia, genera un
   `idempotencyKey` único (UUID o timestamp + ID de sesión) para evitar duplicados por
   reintentos de red.

5. **Paginación:** Todos los listados usan `page` (base 0) y `size`. Implementa un
   componente de paginación reutilizable.

6. **Transferencias con `202 Accepted`:** Este código indica flujo maker-checker activo.
   Muestra un estado diferenciado en la UI ("Pendiente de aprobación").

7. **Variables de entorno:** Define `VITE_API_URL=http://localhost:8080/api/v1` (o el
   equivalente de tu framework) y nunca hardcodees la URL base en los componentes.

---

## Ejemplo de Cliente HTTP Base (axios)

```js
// src/api/client.js
import axios from 'axios';

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1',
  headers: { 'Content-Type': 'application/json' },
});

// Inyecta el token en cada request
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

// Redirige al login si el token expira
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default apiClient;
```

---

*Documento generado el 2026-05-24 a partir del análisis del código fuente en `Banco/`
y del Software Design Document en `SDD/`. Todos los endpoints están marcados como
`[DISEÑADO]` porque el backend se encuentra en fase de scaffold y aún no tiene
controllers implementados.*
