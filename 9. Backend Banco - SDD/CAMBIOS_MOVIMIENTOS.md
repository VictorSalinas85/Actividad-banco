# Cambio — Movimientos de Cuenta mostraba "NaN" y fecha vacía

Fecha: 2026-05-29

## Problema

En **Movimientos de Cuenta** las columnas *Saldo anterior* y *Saldo nuevo* mostraban
`$ NaN`, la *Fecha* aparecía como `—` y *Descripción* vacía, aunque el backend sí tenía
los datos.

## Causa

Desajuste de nombres entre el tipo del frontend y el DTO `CtaMovimientoResponse`:

| Frontend (incorrecto) | Backend (real) |
|-----------------------|----------------|
| `saldoAnterior`       | `saldoAntes`   |
| `saldoNuevo`          | `saldoDespues` |
| `creadoEn`            | `fechaMovimiento` |
| `descripcion` (no existe) | `referenciaExterna` |

Al leer campos inexistentes, `Number(undefined)` daba `NaN` y la fecha quedaba sin valor.

## Corrección (solo frontend)

- `types/index.ts`: la interfaz `CtaMovimiento` se alineó con el DTO
  (`saldoAntes`, `saldoDespues`, `fechaMovimiento`, `referenciaExterna`, `tipoOperacionId`,
  `canalId`, `idempotencyKey`, `createdAt`).
- `pages/cuentas/MovimientosPage.tsx`: las columnas usan los nombres correctos; *Descripción*
  muestra `referenciaExterna` (o `—` si no hay nota).

## Verificación

Respuesta real de `GET /api/v1/cuentas/movimientos`:

```json
{ "id": 3, "cuentaId": 11, "monto": 5000000.0,
  "saldoAntes": 200000000.0, "saldoDespues": 205000000.0,
  "fechaMovimiento": "2026-05-29T14:08:48" }
```

Ahora la tabla muestra Saldo anterior `$200.000.000`, Saldo nuevo `$205.000.000` y la fecha
correctamente. `tsc --noEmit` sin errores.
