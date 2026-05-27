# Usuarios Demo y Permisos por Rol

> Documento de referencia para pruebas funcionales del sistema Wolfstreet Bank.
> Fecha: 2026-05-27 · Endpoint de login: `POST /api/v1/auth/login`

---

## 1. Credenciales de los 8 usuarios sembrados

Todos los usuarios están en la tabla `sec_usuario` de la BD `banco_bd`.
Las contraseñas siguen el patrón `<Nombre>123*` y están almacenadas con BCrypt.

| # | Rol funcional | Username | Contraseña |
|---|---|---|---|
| 1 | Administrador del Sistema | `admin` | `Admin123*` |
| 2 | Analista Interno del Banco | `analista` | `Analista123*` |
| 3 | Empleado de Ventanilla | `ventanilla` | `Ventanilla123*` |
| 4 | Empleado Comercial | `comercial` | `Comercial123*` |
| 5 | Cliente Persona Natural | `cliente` | `Cliente123*` |
| 6 | Cliente Empresa (Rep. legal) | `empresa_admin` | `Empresa123*` |
| 7 | Empleado Empresa Operativo | `empresa_op` | `Operativo123*` |
| 8 | Supervisor de Empresa | `empresa_super` | `Supervisor123*` |

> `admin` está mapeado al rol `ANALISTA_INTERNO` para tener acceso total durante las pruebas.

### Datos demo asociados

- **Personas naturales:** Juan Pérez (CC 1010101010), María Torres (CC 2020202020 — representante legal de ACME), Pedro Joven (TI), Lucía Extranjera (CE), Mark Traveler (PAS).
- **Empresa:** ACME Corp SAS (NIT 900123456-7).
- **Catálogo `cat_tipo_identificacion`:** CC, NIT, TI, CE, PAS con `aplicaA` correcto (PERSONA / EMPRESA / AMBOS).

---

## 2. Matriz de roles vs módulos

Leyenda: ● = acceso total · ◐ = acceso parcial (solo lectura o restringido) · — = sin acceso

| Módulo | ANALISTA_INTERNO | EMPLEADO_VENTANILLA | EMPLEADO_COMERCIAL | CLIENTE_PERSONA | CLIENTE_EMPRESA_ADMIN | EMPLEADO_EMPRESA_OPERATIVO | SUPERVISOR_EMPRESA |
|---|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| Catálogos (CRUD)               | ● | — | — | — | — | — | — |
| Usuarios / Roles / Sesiones    | ● | — | — | — | — | — | — |
| Personas Naturales (CRUD)      | ● | ● | ● | — | — | — | — |
| Empresas (CRUD)                | ● | — | ● | — | ◐ | — | — |
| Usuarios de Empresa            | ● | — | ● | — | ◐ | — | — |
| Cuentas (consulta)             | ● | ● | ● | — | ◐ | ◐ | ● |
| Movimientos                    | ● | ● | — | — | — | — | — |
| Préstamos (CRUD)               | ● | — | ● | — | — | — | — |
| Transferencias (CRUD)          | ● | ● | — | — | — | ● | ● |
| Productos bancarios            | ● | ● | ● | ● | ● | ● | ● |
| Ops. Clientes (SPs)            | ● | ● | ● | — | ◐ | — | — |
| Ops. Cuentas (SPs)             | ● | ● | — | ◐ | ◐ | ◐ | — |
| Ops. Préstamos (SPs)           | ● | ◐ | ● | ◐ | ◐ | — | — |
| Ops. Transferencias (SPs)      | ● | ● | — | ◐ | ◐ | ◐ | ● |
| Ops. Sesiones                  | ● | ● | ● | — | — | — | — |
| Ops. Auditoría                 | ● | — | — | — | — | — | — |
| Auditoría (bitácora/cambios/errores) | ● | — | — | — | — | — | — |

---

## 3. Detalle por rol

### 3.1 `ANALISTA_INTERNO` — `admin` / `analista`

> Rol con acceso total. Es el rol que usa el personal del banco para configurar y operar todo el sistema.

**Puede hacer:**
- CRUD completo de **todos los catálogos** (estados, tipos, monedas, motivos, parámetros de negocio, transiciones de estado, roles de empresa, tipos de identificación).
- CRUD de **usuarios del sistema**, asignación de roles, gestión de sesiones.
- CRUD de **personas naturales** y **empresas** (incluye borrado físico).
- CRUD de **cuentas**, **movimientos**, **préstamos**, **aprobaciones**, **desembolsos**, **transferencias** y sus aprobaciones.
- CRUD de **productos bancarios**.
- Ejecutar **todos los stored procedures**: crear cliente, abrir/bloquear/cancelar cuenta, consignar, retirar, solicitar/aprobar/rechazar/desembolsar préstamo, crear/aprobar/rechazar/ejecutar/vencer transferencias, validar/revocar sesiones, registrar eventos de auditoría y errores.
- Consultar **bitácora de eventos, cambios de datos y errores operacionales**.

**Restricciones:** ninguna funcional. Es el único rol que puede borrar registros (DELETE) en personas, empresas, préstamos, transferencias, etc.

---

### 3.2 `EMPLEADO_VENTANILLA` — `ventanilla`

> Personal de ventanilla del banco. Atiende clientes en sucursal: caja, apertura de cuentas básicas, desembolsos.

**Puede hacer:**
- CRUD de **personas naturales** (sin borrado físico).
- Consultar **cuentas** y **movimientos**.
- Crear y consultar **transferencias** (no puede aprobar ni borrar).
- Ejecutar SPs de cuentas: **abrir, bloquear, cancelar, consignar, retirar**.
- Ejecutar SPs de transferencias: **crear, ejecutar transferencia directa**.
- Ejecutar SP de préstamos: **desembolsar** (entrega física del dinero).
- Ejecutar SPs de clientes: **crear persona, cambiar estado de cliente**.
- Ejecutar SPs de sesiones: **validar, revocar**.

**No puede:** crear empresas, aprobar préstamos ni transferencias, acceder a catálogos, ver auditoría, ni borrar registros.

---

### 3.3 `EMPLEADO_COMERCIAL` — `comercial`

> Ejecutivo comercial del banco. Gestiona la relación con clientes empresariales y solicitudes de crédito.

**Puede hacer:**
- CRUD de **personas naturales** y **empresas** (sin borrado físico).
- CRUD de **usuarios de empresa** y sus roles.
- CRUD de **préstamos**: solicitudes, aprobaciones, desembolsos (lectura/escritura, no borrado).
- Consultar **cuentas**.
- Ejecutar SPs de clientes: **crear persona, crear empresa, cambiar estado, asociar usuario a empresa**.
- Ejecutar SPs de préstamos: **solicitar, aprobar, rechazar, desembolsar**.
- Ejecutar SP de cuentas: **abrir cuenta**.
- Ejecutar SPs de sesiones: **validar, revocar**.

**No puede:** operar caja (consignar/retirar), aprobar transferencias, acceder a catálogos ni auditoría.

---

### 3.4 `CLIENTE_PERSONA` — `cliente`

> Cliente persona natural del banco. Opera sus propios productos desde el portal.

**Puede hacer:**
- Ver **productos bancarios** disponibles.
- Operar sobre **sus** cuentas: **consignar, retirar**.
- Solicitar **préstamos personales**.
- Crear y ejecutar **transferencias directas** desde sus cuentas.

**No puede:** ver datos de otros clientes, aprobar nada, acceder a CRUDs administrativos, ver auditoría. Los SPs validan internamente que el cliente sea el titular del producto.

---

### 3.5 `CLIENTE_EMPRESA_ADMIN` — `empresa_admin`

> Representante legal o administrador principal de una empresa cliente.

**Puede hacer:**
- Consultar **su empresa** y gestionar sus **usuarios de empresa**.
- Asignar **roles de empresa** a sus usuarios (operativo, supervisor).
- Consultar **cuentas** de su empresa.
- Operar cuentas empresariales: **consignar, retirar**.
- Solicitar **préstamos** para la empresa.
- Crear **transferencias** (que pueden requerir aprobación de supervisor).
- Consultar **transferencias pendientes de aprobación** de su empresa.
- Ejecutar SP **crear empresa** (auto-registro) y **asignar rol** dentro de su empresa.

**No puede:** aprobar transferencias por encima del umbral (eso es del supervisor), ejecutar transferencias directas como cliente persona, acceder a catálogos ni auditoría.

---

### 3.6 `EMPLEADO_EMPRESA_OPERATIVO` — `empresa_op`

> Empleado interno de una empresa cliente. Realiza operaciones diarias en nombre de la empresa.

**Puede hacer:**
- Consultar **cuentas** y **transferencias** de la empresa.
- Operar cuentas: **consignar, retirar**.
- Crear **transferencias** (todas pasan por aprobación del supervisor si superan umbral).

**No puede:** aprobar transferencias, gestionar usuarios de la empresa, modificar datos de la empresa, ni nada del lado administrativo del banco.

---

### 3.7 `SUPERVISOR_EMPRESA` — `empresa_super`

> Supervisor financiero de una empresa cliente. Función principal: aprobar/rechazar transferencias que superan el umbral.

**Puede hacer:**
- Consultar **cuentas** y **transferencias** de la empresa.
- **Aprobar o rechazar transferencias** pendientes (vía CRUD `/transferencias/aprobaciones` y SP `sp_trf_aprobar_transferencia` / `sp_trf_rechazar_transferencia`).
- Consultar **transferencias pendientes** de la empresa.
- Editar transferencias.

**No puede:** crear transferencias propias, operar cuentas (consignar/retirar), gestionar usuarios, ver auditoría.

---

## 4. Flujos de aprobación clave

### 4.1 Préstamos

```
[Cliente o Comercial]            [Comercial / Analista]              [Ventanilla / Comercial]
   solicitar  ──────────►  aprobar / rechazar  ───────────────────►  desembolsar
                                  (decide)                              (entrega)
```

| Acción | SP | Roles autorizados |
|---|---|---|
| Solicitar | `sp_cre_solicitar_prestamo` | ANALISTA_INTERNO, EMPLEADO_COMERCIAL, CLIENTE_PERSONA, CLIENTE_EMPRESA_ADMIN |
| Aprobar | `sp_cre_aprobar_prestamo` | ANALISTA_INTERNO, EMPLEADO_COMERCIAL |
| Rechazar | `sp_cre_rechazar_prestamo` | ANALISTA_INTERNO, EMPLEADO_COMERCIAL |
| Desembolsar | `sp_cre_desembolsar_prestamo` | ANALISTA_INTERNO, EMPLEADO_VENTANILLA, EMPLEADO_COMERCIAL |

### 4.2 Transferencias empresariales

```
[Operativo / Admin / Ventanilla]                [Supervisor empresa / Analista]
        crear  ─────► ¿monto > umbral?  ──►  aprobar / rechazar  ──►  ejecutar
                                 │                                       (automático tras aprobación)
                                 └─── ejecutar directa (sin aprobación, solo bajo umbral)
```

| Acción | SP | Roles autorizados |
|---|---|---|
| Crear | `sp_trf_crear_transferencia` | ANALISTA_INTERNO, EMPLEADO_VENTANILLA, CLIENTE_PERSONA, CLIENTE_EMPRESA_ADMIN, EMPLEADO_EMPRESA_OPERATIVO |
| Aprobar | `sp_trf_aprobar_transferencia` | ANALISTA_INTERNO, SUPERVISOR_EMPRESA |
| Rechazar | `sp_trf_rechazar_transferencia` | ANALISTA_INTERNO, SUPERVISOR_EMPRESA |
| Ejecutar directa | `sp_trf_ejecutar_transferencia_directa` | ANALISTA_INTERNO, EMPLEADO_VENTANILLA, CLIENTE_PERSONA |
| Vencer pendientes | `sp_trf_vencer_transferencias_pendientes` | ANALISTA_INTERNO |
| Consultar pendientes empresa | `sp_trf_consultar_pendientes_aprobacion_empresa` | ANALISTA_INTERNO, SUPERVISOR_EMPRESA, CLIENTE_EMPRESA_ADMIN |

---

## 5. Plan de pruebas sugerido

Para validar la matriz de permisos, sigue este orden de login:

1. **`analista` / `Analista123*`** → Verifica que el sidebar muestre **todos** los grupos (Configuración, Clientes, Banca, Operaciones, Auditoría). Entra a un catálogo y modifica un registro.
2. **`ventanilla` / `Ventanilla123*`** → El sidebar debe mostrar solo Personas, Cuentas, Movimientos, Transferencias, Ops. Cuentas, Ops. Sesiones. Intenta abrir una cuenta para Juan Pérez y consignar.
3. **`comercial` / `Comercial123*`** → Crea un préstamo nuevo para una persona y simula la aprobación.
4. **`cliente` / `Cliente123*`** → Solo dashboard, productos, Ops. Cuentas y Ops. Transferencias. Intenta transferir desde una de tus cuentas.
5. **`empresa_admin` / `Empresa123*`** → Gestiona la empresa ACME, agrega un usuario operativo, crea una transferencia que supere el umbral.
6. **`empresa_super` / `Supervisor123*`** → Aprueba la transferencia que dejó pendiente `empresa_admin`.
7. **`empresa_op` / `Operativo123*`** → Crea una transferencia normal y verifica que quede pendiente.
8. **`admin` / `Admin123*`** → Equivalente a `analista`; úsalo para verificar Ops. Auditoría y registros de bitácora.

### Cómo verificar que la restricción funciona

- Intenta acceder a una URL no permitida directamente (ej. `cliente` → `http://localhost:3000/catalogos`). Debe redirigir a `/no-autorizado`.
- En las llamadas a la API, un usuario sin permisos recibe `403 Forbidden`.

---

## 6. Notas operativas

- **Login:** `POST /api/v1/auth/login` con body `{ "username": "...", "password": "..." }` retorna un JWT que el frontend guarda en `localStorage` y reenvía como `Authorization: Bearer <token>`.
- **Expiración del JWT:** 86400 segundos (24 h) por defecto, configurable vía `JWT_EXPIRATION`.
- **Hashing de contraseñas:** BCrypt (rounds por defecto de Spring Security).
- **Si una contraseña no funciona:** verifica que el seed `14_create_test_data.sql` se haya ejecutado tras los catálogos y la creación de roles, y que `sec_usuario_rol` tenga el vínculo correcto (ver consulta de verificación abajo).

### Consulta SQL para verificar usuarios y roles

```sql
SELECT u.username, GROUP_CONCAT(r.codigo ORDER BY r.codigo SEPARATOR ', ') AS roles
FROM sec_usuario u
LEFT JOIN sec_usuario_rol ur ON ur.usuario_id = u.id
LEFT JOIN sec_rol r ON r.id = ur.rol_id
GROUP BY u.id, u.username
ORDER BY u.username;
```

Resultado esperado: las 8 filas con sus roles correspondientes según la tabla del punto 1.
