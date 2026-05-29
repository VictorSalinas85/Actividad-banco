# Cambios — Numeración automática de cuentas, búsqueda de titular y formato de dinero

Fecha: 2026-05-29

Resumen de las mejoras hechas sobre el flujo de **creación de cuentas bancarias** y la
**captura de montos** en toda la aplicación (base de datos, backend Spring Boot y frontend React).

---

## 1. Número de cuenta asignado automáticamente

El número de cuenta ya **no se digita**: lo asigna el sistema con un consecutivo anual.

- **Formato:** `AAAA` (año actual) + 7 dígitos = **11 caracteres**.
- **Primero del año:** `AAAA0000000` (p. ej. `20260000000`).
- A partir de ahí incrementa de uno en uno (`20260000001`, `20260000002`, …).
- El consecutivo se calcula con `MAX(numero_cuenta)` del año en curso + 1, de modo que en
  2027 vuelve a arrancar en `20270000000`.

Se implementó en los **dos caminos** de creación para que se comporten igual:

| Camino | Archivo | Detalle |
|--------|---------|---------|
| Stored procedure (Operaciones · Abrir cuenta) | `Banco_bd/12_create_stored_procedures.sql` → `sp_cta_abrir_cuenta` | Reemplaza la generación aleatoria anterior (`'10' + timestamp + random`) por el consecutivo anual dentro de la transacción. |
| CRUD REST (`POST /api/v1/cuentas`) | `CtaCuentaCrudService.java` + `CtaCuentaRepository.java` | Método `generarNumeroCuenta()` con la misma lógica; consulta `findMaxNumeroCuentaByPrefijoAnio`. |

Ambos comparten la tabla `cta_cuenta`, así que el consecutivo es único sin importar por dónde se cree.

---

## 2. Previsualización del número en el formulario

Los formularios muestran, **antes de crear**, el número que se asignará.

- **Backend:** nuevo endpoint `GET /api/v1/cuentas/proximo-numero`
  (`CuentaController.proximoNumero` → `CtaCuentaCrudService.proximoNumeroCuenta()`), que
  devuelve el siguiente número sin crear nada.
- **Frontend:**
  - `cuentasApi.proximoNumero()` en `api/resources.ts`.
  - `CrudTable` recibe el prop `onOpenCreate` para precargar valores al abrir el formulario
    de "Nuevo"; en `CuentasPage` el campo **"Número de cuenta"** vuelve como **solo lectura**
    con el valor previsto.
  - En `OpsCuentasPage` (Abrir cuenta) se muestra arriba del formulario y se refresca tras
    cada apertura exitosa.

> Es un *preview*: el número definitivo se fija al guardar (protegido por el índice `UNIQUE`
> de `numero_cuenta`), por lo que en concurrencia podría adelantarse.

---

## 3. Búsqueda del titular por nombre (Operaciones · Abrir cuenta)

Antes había que digitar la identificación exacta (CC/NIT). Ahora se puede buscar al titular
escribiendo **nombre o identificación**.

- Nuevo componente `Banco_frontend/src/components/common/TitularLookup.tsx`: typeahead sobre
  el directorio en memoria (personas + empresas), filtra por nombre/razón social o documento,
  marca cada resultado con `[PERSONA]` / `[EMPRESA]` y permite limpiar la selección.
- `OpsCuentasPage` usa `TitularLookup` en lugar del lookup por identificación.

El formulario CRUD (`Cuentas Bancarias · Nuevo`) ya tenía búsqueda por nombre con su
selector propio, así que se dejó como estaba.

---

## 4. Corrección del error 500 al crear cuenta por CRUD

El formulario CRUD no envía `estadoId`, `titularTipo`, `fechaApertura` ni `createdBy`, lo que
provocaba `The given id must not be null` (HTTP 500). Se hizo robusto el backend
(`CtaCuentaCrudService.crear`) aplicando valores por defecto sensatos:

- **Estado:** si no llega, se usa `ACTIVA` (cuenta nueva).
- **Tipo de titular:** se deduce de cuál id viene (persona o empresa). Si vienen ambos o
  ninguno, devuelve **400** con mensaje claro (`DOM-CTA-003`).
- **Fecha de apertura:** `now()` si no se envía.
- **Creador (`created_by`/`updated_by`):** se resuelve del usuario autenticado vía
  `CrudContextResolver`.
- **Saldo y límite de sobregiro:** `0` si llegan vacíos.

Resultado: la creación desde el formulario funciona y los casos inválidos responden 400
limpio en vez de 500.

---

## 5. Formato de miles al digitar dinero (toda la app)

Nuevo componente `Banco_frontend/src/components/common/MoneyInput.tsx`: input que va
agregando los **puntos de miles** mientras se escribe (formato es-CO: punto para miles,
coma para decimales) y hacia afuera entrega siempre un número limpio para las APIs.

Se integró como nuevo tipo de campo `money`:

- `FieldType` (en `types/index.ts`) incluye `'money'`.
- `CrudTable` y `OpSection` renderizan `MoneyInput` para los campos `money`.

Campos migrados de `number` → `money` (o de `<input type=number>` → `MoneyInput`):

| Pantalla | Campos |
|----------|--------|
| Cuentas · Nuevo | Saldo inicial, Límite de sobregiro |
| Operaciones · Cuentas | Límite de sobregiro, Monto (consignar / retirar) |
| Operaciones · Préstamos | Monto solicitado, Monto aprobado |
| Operaciones · Transferencias | Monto (crear y directa) |
| Préstamos · CRUD | Monto solicitado |
| Préstamos · Desembolsos | Monto principal, intereses, cargos |
| Transferencias · CRUD | Monto |

> Quedan como número normal los campos que **no** son dinero: plazo en meses y tasa de interés (%).

---

## Archivos tocados

**Base de datos**
- `Banco_bd/12_create_stored_procedures.sql`

**Backend**
- `api/controllers/crud/CuentaController.java`
- `domain/services/crud/CtaCuentaCrudService.java`
- `domain/repositories/CtaCuentaRepository.java`

**Frontend (nuevos)**
- `components/common/TitularLookup.tsx`
- `components/common/MoneyInput.tsx`

**Frontend (modificados)**
- `api/resources.ts`, `types/index.ts`
- `components/CrudTable.tsx`, `components/OpSection.tsx`, `components/common/index.ts`
- `pages/cuentas/CuentasPage.tsx`
- `pages/ops/OpsCuentasPage.tsx`, `pages/ops/OpsPrestamosPage.tsx`, `pages/ops/OpsTransferenciasPage.tsx`
- `pages/prestamos/PrestamosPage.tsx`, `pages/prestamos/PrestamoDesembolsosPage.tsx`
- `pages/transferencias/TransferenciasPage.tsx`

## Cómo aplicar en otro entorno

El cambio del SP ya está versionado en `12_create_stored_procedures.sql`. Para aplicarlo a una
base existente basta con re-ejecutar ese script (cada procedimiento hace `DROP ... IF EXISTS`),
o correr el orquestador. El backend y el frontend no requieren pasos extra más allá de
recompilar/reiniciar.
