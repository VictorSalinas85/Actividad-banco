# Cambios — Revisión y corrección de permisos por rol

Fecha: 2026-05-29

## Problema reportado

Al ingresar como **Cliente Persona Natural** aparecía de entrada el mensaje
*"No tienes permisos para esta acción"*, cuando el cliente debería poder entrar y usar
solo lo propio de su rol. La revisión encontró desajustes de permisos que afectaban a
varios roles.

## Causa raíz

1. **Catálogos restringidos a ANALISTA.** Los catálogos (`/api/v1/catalogos/*`) eran
   legibles solo por `ANALISTA_INTERNO`, pero los dropdowns de **todos** los formularios
   (tipos de cuenta, monedas, canales, motivos, etc.) los consultan. Cualquier otro rol
   recibía 403 al abrir un formulario.
2. **Directorio restringido.** Los lookups de titular/cuenta usan un "directorio" que lee
   `/personas`, `/empresas` y `/cuentas`. Esas lecturas estaban limitadas a empleados, así
   que clientes (y otros roles) recibían 403 al usar cualquier formulario con búsqueda.
3. **Frontend desalineado del backend.** Rutas, menú lateral y accesos rápidos del dashboard
   ofrecían a algunos roles páginas cuyo `GET` el backend les negaba (p. ej. el cliente tenía
   el atajo "Mis cuentas" → `/cuentas`, que el backend no permitía → 403 inmediato).
4. **Código HTTP incorrecto.** Una denegación de autorización a un usuario **ya autenticado**
   respondía `401` (no `403`), y el frontend interpreta `401` como "sesión expirada" y
   desloguea.

## Correcciones

### Backend — modelo "lectura abierta, escritura/operación por rol"

- **Catálogos** (`CatalogosController`): la clase pasa a `@PreAuthorize("isAuthenticated()")`
  (lectura para cualquier autenticado). Las escrituras `POST/PUT/DELETE /catalogos/**` se
  restringen a `ANALISTA_INTERNO` mediante reglas por URL en `SecurityConfig`.
- **Directorio** (`ClientePersonaNaturalController`, `ClienteEmpresaController`,
  `CuentaController`): la clase pasa a `isAuthenticated()` para permitir **GET** a cualquier
  autenticado; cada `POST/PUT/DELETE` queda anotado con su rol (sin abrir escrituras). En
  Empresas se añadieron las anotaciones de escritura que antes heredaban de la clase.
- **`JwtAccessDeniedHandler`** (nuevo): las denegaciones de autorización de un usuario
  autenticado ahora responden **403** (antes caían al entry point y devolvían 401). Registrado
  en `SecurityConfig` vía `accessDeniedHandler`.

> Nota de diseño: abrir las **lecturas** a usuarios autenticados es una simplificación propia
> de este proyecto académico (un cliente puede listar datos de referencia y del directorio).
> Las **acciones sensibles** (crear/editar/eliminar, abrir cuenta, aprobar, desembolsar,
> gestionar usuarios/auditoría) siguen restringidas por rol. En un entorno productivo, las
> lecturas del directorio deberían acotarse al propio cliente (endpoints con alcance por dueño).

### Frontend — cada rol ve y ejecuta solo lo suyo

- **Rutas** (`App.tsx`): los arreglos de roles por ruta se alinearon con las autoridades del
  backend (se quitaron roles que producían 403, p. ej. clientes y empleados de empresa de las
  páginas CRUD de cuentas/empresas/transferencias; se añadió Comercial a `ops/cuentas`).
- **Menú lateral** (`Sidebar.tsx`): "Empresas" solo Analista/Comercial; "Ops. Cuentas" añade
  Comercial.
- **Accesos rápidos del dashboard** (`Dashboard.tsx`): los atajos de los roles cliente/empresa
  ahora apuntan a páginas que sí pueden usar (operaciones y productos) en lugar de páginas CRUD
  administrativas.
- **Páginas de Operaciones**: cada sub-formulario se muestra solo a los roles autorizados en el
  backend (`OpsCuentasPage`, `OpsPrestamosPage`, `OpsTransferenciasPage`, `OpsClientesPage`).
  Así, por ejemplo, el cliente solo ve *Consignar/Retirar*, *Crear/Directa* de transferencia y
  *Solicitar préstamo*, no *Abrir/Bloquear/Cancelar*.
- **Botones de escritura en CRUD**: en `CuentasPage` y `TransferenciasPage` se ocultan
  *Nuevo/Editar/Eliminar* a los roles que solo tienen lectura.

## Verificación (API, por rol)

| Caso | Resultado |
|------|-----------|
| Cliente — GET catálogos, personas, empresas, cuentas, productos | **200** |
| Cliente — ejecutar consignar/retirar/transferir/solicitar (autorización) | **pasa** (no 403) |
| Cliente — POST catálogos / POST cuentas / GET usuarios | **403** |
| Cliente — abrir cuenta (operación no permitida) | **403** |
| Ventanilla — POST catálogos | **403** (antes 401) |
| Sin token — GET /personas | **401** |

## Matriz de capacidades (resumen)

- **Analista Interno:** acceso total (configuración, clientes, banca, operaciones, auditoría).
- **Empleado Ventanilla:** personas, cuentas, movimientos, transferencias; ops de cuentas
  (abrir/bloquear/cancelar/consignar/retirar), desembolso, transferencias (crear/directa), sesiones.
- **Empleado Comercial:** personas, empresas, usuarios de empresa, préstamos; ops de clientes,
  ops de cuentas (abrir), ops de préstamos (solicitar/aprobar/rechazar/desembolsar), sesiones.
- **Supervisor de Empresa:** ver cuentas y transferencias; aprobar/rechazar transferencias,
  pendientes por empresa.
- **Cliente Persona Natural:** dashboard, productos; consignar/retirar, transferir, solicitar préstamo.
- **Administrador de Empresa:** dashboard, productos; ops de cuentas (consignar/retirar),
  transferencias, asignar rol de empresa, pendientes por empresa.
- **Operativo de Empresa:** dashboard, productos; consignar/retirar, crear transferencia.

## Archivos tocados

**Backend**
- `security/SecurityConfig.java`, `security/JwtAccessDeniedHandler.java` (nuevo)
- `api/controllers/crud/CatalogosController.java`
- `api/controllers/crud/ClientePersonaNaturalController.java`
- `api/controllers/crud/ClienteEmpresaController.java`
- `api/controllers/crud/CuentaController.java`

**Frontend**
- `App.tsx`, `components/Sidebar.tsx`, `pages/Dashboard.tsx`
- `pages/ops/OpsCuentasPage.tsx`, `pages/ops/OpsPrestamosPage.tsx`,
  `pages/ops/OpsTransferenciasPage.tsx`, `pages/ops/OpsClientesPage.tsx`
- `pages/cuentas/CuentasPage.tsx`, `pages/transferencias/TransferenciasPage.tsx`
