import { useEffect, useMemo } from 'react'
import { useDirectorio, findCuentasByIdentificacion, type CuentaDir } from './directorio'

interface Props {
  /** Identificacion del cliente (CC/NIT) que el usuario digita. */
  identificacion: string
  onIdentificacionChange: (id: string) => void
  /** Id interno de la cuenta seleccionada (lo que se manda al backend). */
  cuentaId: number | null
  onCuentaIdChange: (id: number | null) => void
  required?: boolean
  /** Texto que describe la acción (ej. "bloquear", "cancelar"). */
  actionLabel?: string
}

/**
 * Picker para operaciones sobre la cuenta de un titular:
 *  1) digita la identificación → valida y muestra el nombre,
 *  2) lista todas las cuentas del titular como tarjetas,
 *  3) el usuario hace click en la tarjeta de la cuenta a operar.
 */
export default function CuentaPorIdentificacionPicker({
  identificacion, onIdentificacionChange,
  cuentaId, onCuentaIdChange,
  required, actionLabel = 'operar',
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
    <div className="space-y-3">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
        <div>
          <label className="label">Identificación del titular {required && <span className="text-red-500">*</span>}</label>
          <input
            type="text"
            className="input"
            value={identificacion}
            onChange={(e) => onIdentificacionChange(e.target.value)}
            placeholder="Digite CC, NIT, CE, TI o PAS"
            required={required}
          />
          {identificacion.trim() !== '' && (
            <p className="help-text">
              {loading ? (
                <span className="text-ink-400">Validando identificación…</span>
              ) : !cliente ? (
                <span className="text-amber-700">⚠ Identificación no encontrada en el directorio</span>
              ) : (
                <span className="text-emerald-700">✓ Identificación válida ({cliente.tipo})</span>
              )}
            </p>
          )}
        </div>
        <div>
          <label className="label">Nombre del titular (auto-validado)</label>
          <input
            type="text"
            className="input bg-navy-50"
            value={cliente?.nombre ?? ''}
            readOnly
            placeholder="Se completa al validar la identificación"
          />
        </div>
      </div>

      {cliente && (
        <div>
          <label className="label">
            Cuentas de <span className="font-semibold">{cliente.nombre}</span>
            {cuentas.length > 0 && (
              <span className="ml-2 text-xs text-ink-500">
                ({cuentas.length} {cuentas.length === 1 ? 'cuenta' : 'cuentas'} — seleccione una para {actionLabel})
              </span>
            )}
          </label>

          {cuentas.length === 0 ? (
            <p className="text-xs text-amber-700 mt-1">
              ⚠ El titular {cliente.nombre} no tiene cuentas asociadas.
            </p>
          ) : (
            <ul className="grid grid-cols-1 md:grid-cols-2 gap-2 mt-1">
              {cuentas.map((c) => {
                const selected = c.id === cuentaId
                return (
                  <li key={c.id}>
                    <button
                      type="button"
                      onClick={() => onCuentaIdChange(selected ? null : c.id)}
                      className={`w-full text-left px-4 py-3 rounded-md border-2 transition-all ${
                        selected
                          ? 'border-gold-500 bg-gold-50 shadow-sm'
                          : 'border-navy-200 bg-white hover:border-navy-400 hover:bg-navy-50'
                      }`}
                      aria-pressed={selected}
                    >
                      <div className="flex items-center justify-between">
                        <span className="font-mono text-sm font-semibold text-navy-900">{c.numeroCuenta}</span>
                        {selected && <span className="text-xs font-bold text-gold-700">✓ SELECCIONADA</span>}
                      </div>
                      <div className="mt-1 text-xs text-ink-600 grid grid-cols-2 gap-x-3">
                        <span>Saldo: <span className="font-mono">${Number(c.saldoActual).toLocaleString('es-CO')}</span></span>
                        <span>ID: <span className="font-mono">{c.titularIdent}</span></span>
                      </div>
                    </button>
                  </li>
                )
              })}
            </ul>
          )}

          {required && cliente && cuentas.length > 0 && cuentaId == null && (
            <p className="text-xs text-amber-700 mt-2">
              Seleccione la cuenta a {actionLabel} antes de continuar.
            </p>
          )}
        </div>
      )}
    </div>
  )
}
