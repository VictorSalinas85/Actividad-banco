# Frontend — Cambios y avances

> Commit: `08bea6b` — *feat(frontend): completar capa de pages, pickers de entidades y alineación con DTOs*
> Rama: `main` · Fecha: 2026-05-27

---

## 1. Contexto

El frontend había quedado a medio reconstruir tras un cambio de rama anterior:
las páginas refactorizadas (`PersonasPage`, `EmpresasPage`, `EmpresaUsuariosPage`)
ya importaban módulos de `lib/`, `components/common/`, `App.tsx` y pages de
banca/operaciones que **no existían en disco**. El proyecto **no compilaba**.

Este commit reconstruye toda esa infraestructura y deja el frontend funcionalmente
completo, alineado con los DTOs Java del backend.

---

## 2. Resumen de lo que se hizo

| Bloque | Estado | Detalle |
|---|---|---|
| `types/index.ts` | ✅ Reescrito | Alineado 1:1 con DTOs Java |
| `lib/` | ✅ Creado | constants, formatters, useEntityList |
| `components/common/` | ✅ Creado | 9 componentes + index |
| `App.tsx` | ✅ Creado | Rutas con RequireAuth por rol |
| Pages CRUD nuevas | ✅ 10 pages | Cuentas, Préstamos, Transferencias, Productos, Auditoría |
| Pages Ops (SPs) | ✅ 6 pages | Clientes, Cuentas, Préstamos, Transferencias, Sesiones, Auditoría |
| `CatalogosPage` | ✅ Actualizada | Muestra y edita columna `aplicaA` |
| Tailwind + CSS | ✅ Extendido | Paleta `ink`, clases `.badge*`, `.help-text` |
| `package.json` | ✅ +lucide-react | Iconos en todas las pages |

**Total:** 35 archivos modificados/creados · +1603 / −19 líneas.

---

## 3. Alineación crítica con el backend

Verificado contra los DTOs Java existentes:

| Entidad | Frontend (antes) | Frontend (ahora) | Backend Java |
|---|---|---|---|
| `CliPersonaNatural` | `numeroIdentificacion`, `primerNombre`, `primerApellido`… | `identificacion`, `nombreCompleto` | ✅ `CliPersonaNaturalResponse.java` |
| `CliEmpresa` | `representanteLegalId`, `sectorEconomico` | `tipoIdentificacionId`, `representantePersonaId` | ✅ `CliEmpresaResponse.java` |
| `CtaCuenta` | `usuarioId`, `empresaId` | `titularPersonaId`, `titularEmpresaId` | ✅ |
| `CrePrestamo` | `usuarioId`, `empresaId` | `clientePersonaId`, `clienteEmpresaId` | ✅ |
| `CatTipoIdentificacion` | (sin `aplicaA`) | `aplicaA: PERSONA \| EMPRESA \| AMBOS` | ✅ `CatTipoIdentificacionResponse.java` |

Esto elimina los errores silenciosos donde el formulario enviaba un campo
inexistente y el backend lo descartaba.

---

## 4. Infraestructura nueva

### 4.1 `src/types/index.ts` (reescrito)

- Modelos persona/empresa/cuenta/préstamo/transferencia alineados con DTOs.
- Nuevo tipo `TipoIdentificacion` con `aplicaA`.
- `Field` extendido: ahora soporta `'person-picker' | 'empresa-picker' | 'usuario-picker'`.

### 4.2 `src/lib/`

| Archivo | Propósito |
|---|---|
| `constants.ts` | `ROLES` (alias), `ROLE_LABELS` (etiquetas en español) |
| `formatters.ts` | `formatDate`, `formatDateTime`, `formatMoney` (es-CO, COP) |
| `useEntityList.ts` | Hook con caché global por path + 15 hooks específicos (`useTiposIdent`, `useEstadosCuenta`, `useMonedas`, etc.) |

### 4.3 `src/components/common/`

| Componente | Función |
|---|---|
| `PageHeader` | Header consistente con icono Lucide |
| `StatusBadge` | Badge coloreado según estado (ACTIVO/PENDIENTE/BLOQUEADO…) |
| `MoneyValue` | Formateo monetario tabular |
| `EmptyState` | Estado vacío reutilizable |
| `ConfirmDialog` | Modal de confirmación |
| `StatCard` | Tarjeta de métrica |
| **`EntityPicker`** | Buscador de persona/empresa/usuario por nombre o identificación |
| **`IdentificationDisplay`** | Renderiza un ID interno como "Nombre + Identificación" (con caché y carga perezosa) |
| `ParticipanteDisplay` | Variante que decide entre persona/empresa automáticamente |

Los dos últimos (en negrita) son la pieza clave: **el usuario ya nunca ve IDs
internos crudos** en columnas de tablas ni en formularios — siempre ve la
identificación legible.

### 4.4 `src/App.tsx`

Rutas declarativas con `RequireAuth` por rol. Cobertura:

- `/dashboard` — todos los roles
- `/catalogos /usuarios /roles /sesiones` — ANALISTA_INTERNO
- `/personas /empresas /empresa-usuarios` — internos + EMP_ADMIN
- `/cuentas /movimientos /prestamos /transferencias /productos` — según rol
- `/prestamos/aprobaciones`, `/prestamos/desembolsos`
- `/transferencias/aprobaciones`
- `/ops/{clientes,cuentas,prestamos,transferencias,sesiones,auditoria}`
- `/auditoria/{bitacora,cambios,errores}` — ANALISTA_INTERNO

### 4.5 Estilos

`tailwind.config.js`: nueva paleta `ink` (alias de `slate`) usada en chips de código.
`index.css`: clases `.badge`, `.badge-success`, `.badge-muted`, `.badge-warn`,
`.badge-danger`, `.help-text`.

---

## 5. Pages nuevas

### 5.1 CRUD de banca

| Page | Path | Endpoint |
|---|---|---|
| `CuentasPage` | `/cuentas` | `/cuentas` |
| `MovimientosPage` | `/movimientos` | `/cuentas/movimientos` |
| `PrestamosPage` | `/prestamos` | `/prestamos` |
| `PrestamoAprobacionesPage` | `/prestamos/aprobaciones` | `/prestamos/aprobaciones` |
| `PrestamoDesembolsosPage` | `/prestamos/desembolsos` | `/prestamos/desembolsos` |
| `TransferenciasPage` | `/transferencias` | `/transferencias` |
| `TransferenciaAprobacionesPage` | `/transferencias/aprobaciones` | `/transferencias/aprobaciones` |
| `ProductosPage` | `/productos` | `/productos` |

Todas usan `CrudTable` con `EntityPicker` para titulares/clientes y
`IdentificationDisplay` para mostrar referencias.

### 5.2 Auditoría

| Page | Path | Endpoint |
|---|---|---|
| `BitacoraPage` | `/auditoria/bitacora` | `/auditoria/bitacora` |
| `CambiosPage` | `/auditoria/cambios` | `/auditoria/cambios` |
| `ErroresPage` | `/auditoria/errores` | `/auditoria/errores` |

Solo lectura, con resaltado rojo/verde en cambios de datos.

### 5.3 Ops (ejecución directa de stored procedures)

| Page | SPs invocados |
|---|---|
| `OpsClientesPage` | `crearPersona`, `crearEmpresa`, `cambiarEstadoCliente`, `asociarUsuario`, `asignarRolEmpresa` |
| `OpsCuentasPage` | `abrirCuenta`, `consignar`, `retirar`, `bloquearCuenta`, `cancelarCuenta` |
| `OpsPrestamosPage` | `solicitarPrestamo`, `aprobarPrestamo`, `rechazarPrestamo`, `desembolsarPrestamo` |
| `OpsTransferenciasPage` | `crearTransferencia`, `aprobarTransferencia`, `rechazarTransferencia`, `ejecutarTransferenciaDirecta`, `vencerTransferenciasPendientes`, `pendientesEmpresa` |
| `OpsSesionesPage` | `validarSesion`, `revocarSesion` |
| `OpsAuditoriaPage` | `registrarEvento`, `registrarError` |

Cada SP se ejecuta vía `OpSection` y el resultado (`SpResultado.codigo/mensaje/dato`)
se muestra inline con código de color.

---

## 6. Usuarios demo creados (recordatorio)

Inserción de datos demo realizada en sesiones anteriores y verificada con
login real. Las 8 cuentas funcionan contra `POST /api/v1/auth/login`:

| Rol del problema | Usuario | Contraseña |
|---|---|---|
| Administrador del Sistema | `admin` | `Admin123*` |
| Analista Interno del Banco | `analista` | `Analista123*` |
| Empleado de Ventanilla | `ventanilla` | `Ventanilla123*` |
| Empleado Comercial | `comercial` | `Comercial123*` |
| Cliente Persona Natural | `cliente` | `Cliente123*` |
| Cliente Empresa / Rep. Legal | `empresa_admin` | `Empresa123*` |
| Empleado Empresa Operativo | `empresa_op` | `Operativo123*` |
| Supervisor de Empresa | `empresa_super` | `Supervisor123*` |

Datos demo asociados:
- **Personas:** Juan Pérez (CC 1010101010), María Torres (CC 2020202020,
  rep. legal ACME), Pedro Joven (TI), Lucía Extranjera (CE), Mark Traveler (PAS).
- **Empresa:** ACME Corp SAS (NIT 900123456-7).
- **Catálogo tipos identificación:** CC, NIT, TI, CE, PAS con `aplicaA` correcto.

---

## 7. Cómo levantar el frontend

```powershell
cd Banco_frontend
npm install        # asegura lucide-react agregado al package.json
npm run dev        # http://localhost:3000  (proxy a localhost:8080/api/v1)
npm run build      # tsc --noEmit + vite build
```

El backend debe estar corriendo en `http://localhost:8080`.

---

## 8. Pendientes / próximos pasos sugeridos

1. **Verificar `npm run build`** localmente — no fue posible desde la sesión
   automatizada porque `node` no estaba en el PATH del shell. Si `tsc` reporta
   algún error, lo más probable es algún mismatch menor de tipo.
2. **Smoke test manual** de cada page CRUD con los 8 usuarios para confirmar
   permisos por rol.
3. **Productos bancarios** — el endpoint existe pero el catálogo de tipos puede
   necesitar datos demo si no se han seedeado.
4. **Sidebar** — actualmente no expone sub-rutas (`/prestamos/aprobaciones`,
   `/transferencias/aprobaciones`); se accede solo por URL o desde la página
   padre. Si se desea, se puede ampliar el componente `Sidebar.tsx`.
5. **Manejo de errores 4xx** en pages Ops — actualmente se muestra el
   `SpResultado.mensaje` directo del backend; está bien para desarrollo, pero
   en producción convendría mapear códigos a mensajes amigables.

---

## 9. Estructura final del frontend

```
Banco_frontend/src/
├── api/
│   ├── client.ts              (axios + interceptors JWT)
│   └── resources.ts           (catalogosApi, opsApi, crudFactory, etc.)
├── auth/
│   ├── AuthContext.tsx
│   └── RequireAuth.tsx
├── components/
│   ├── CrudTable.tsx          (soporta person/empresa/usuario picker)
│   ├── Layout.tsx
│   ├── Modal.tsx
│   ├── OpSection.tsx          (form genérico para invocar SPs)
│   ├── Sidebar.tsx
│   ├── Spinner.tsx
│   └── common/
│       ├── index.ts
│       ├── ConfirmDialog.tsx
│       ├── EmptyState.tsx
│       ├── EntityPicker.tsx        ⭐
│       ├── IdentificationDisplay.tsx ⭐
│       ├── MoneyValue.tsx
│       ├── PageHeader.tsx
│       ├── StatCard.tsx
│       └── StatusBadge.tsx
├── lib/
│   ├── constants.ts
│   ├── formatters.ts
│   └── useEntityList.ts        (caché global de catálogos)
├── pages/
│   ├── Dashboard.tsx
│   ├── Login.tsx
│   ├── NoAutorizado.tsx
│   ├── auditoria/      {Bitacora, Cambios, Errores}
│   ├── catalogos/      {Catalogos}
│   ├── cuentas/        {Cuentas, Movimientos}
│   ├── empresas/       {Empresas, EmpresaUsuarios}
│   ├── ops/            {Clientes, Cuentas, Prestamos, Transferencias, Sesiones, Auditoria}
│   ├── personas/       {Personas}
│   ├── prestamos/      {Prestamos, Aprobaciones, Desembolsos}
│   ├── productos/      {Productos}
│   ├── transferencias/ {Transferencias, Aprobaciones}
│   └── usuarios/       {Usuarios, Roles, Sesiones}
├── types/
│   └── index.ts                (alineado con DTOs Java)
├── App.tsx                     (rutas + RequireAuth)
├── main.tsx
└── index.css
```
