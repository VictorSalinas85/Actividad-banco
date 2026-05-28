import { useEffect, useMemo, useState } from 'react'
import { useDirectorio, findCuentasByIdentificacion, type CuentaDir } from './directorio'

interface Props {
  /** Identificacion del cliente (CC/NIT) que el usuario digita. */
  identificacion: string
  onIdentificacionChange: (id: string) => void
  /** Id interno de la cuenta seleccionada (lo que se manda al backend). */
  cuentaId: number | null
  onCuentaIdChange: (id: number | null) => void
  required?: boolean
}

/**
 * Compuesto: input de identificacion + select de cuentas del cliente.
 * Util para operaciones como bloquear o cancelar cuenta, donde primero
 * se identifica al titular y luego se elige cual de sus cuentas operar.
 */
export default function CuentaPorIdentificacionPicker({
  identificacion, onIdentificacionChange,
  cuentaId, onCuentaIdChange,
  required,
}: Props) {
  const { data, loading } = useDirectorio()
  const { cliente, cuentas } = useMemo(() => {
    if (!data) return { cliente: null, cuentas: [] as CuentaDir[] }
    return findCuentasByIdentificacion(data, identificacion)
  }, [data, identificacion])

  // Si la identificacion cambia, limpiar la seleccion de cuenta para
  // evitar mandar la cuenta de otro cliente.
  useEffect(() => {
    if (!cuentas.find((c) => c.id === cuentaId)) {
      onCuentaIdChange(null)
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [identificacion, cuentas.length])

  return (
    <div className="space-y-2">
      <input
        type="text"
        className="input"
        value={identificacion}
        onChange={(e) => onIdentificacionChange(e.target.value)}
        placeholder="Identificación del titular (CC, NIT, …)"
        required={required}
      />

      {identificacion.trim() !== '' && (
        <div className="text-xs">
          {loading ? (
            <span className="text-ink-400">Cargando…</span>
          ) : !cliente ? (
            <span className="text-amber-700">⚠ Identificación no encontrada</span>
          ) : (
            <span className="text-emerald-700">
              ✓ <span className="font-semibold">{cliente.nombre}</span>
              <span className="text-ink-500 ml-2 font-mono">[{cliente.tipo}]</span>
            </span>
          )}
        </div>
      )}

      {cliente && (
        <div>
          <label className="label">Cuenta del titular</label>
          {cuentas.length === 0 ? (
            <p className="text-xs text-amber-700">
              El titular no tiene cuentas asociadas.
            </p>
          ) : (
            <select
              className="input"
              value={cuentaId ?? ''}
              onChange={(e) => onCuentaIdChange(e.target.value ? Number(e.target.value) : null)}
              required={required}
            >
              <option value="">-- Seleccione una cuenta --</option>
              {cuentas.map((c) => (
                <option key={c.id} value={c.id}>
                  {c.numeroCuenta} · saldo ${Number(c.saldoActual).toLocaleString('es-CO')}
                </option>
              ))}
            </select>
          )}
        </div>
      )}
    </div>
  )
}
