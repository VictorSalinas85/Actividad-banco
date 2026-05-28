import { useEffect, useState } from 'react'
import { useDirectorio, findCuentaByNumero, type CuentaDir } from './directorio'

interface Props {
  /** Numero de cuenta tal como lo digita el usuario. */
  value: string
  onChange: (numero: string) => void
  /** Callback al resolver — el padre normalmente guarda el id de la cuenta. */
  onResolve?: (cuenta: CuentaDir | null) => void
  required?: boolean
  placeholder?: string
  disabled?: boolean
}

/**
 * Input de numero de cuenta que valida contra el directorio y muestra
 * el titular (nombre + identificacion) si existe.
 */
export default function CuentaNumeroLookup({
  value, onChange, onResolve, required, placeholder, disabled,
}: Props) {
  const { data, loading } = useDirectorio()
  const [resolved, setResolved] = useState<CuentaDir | null>(null)

  useEffect(() => {
    if (!data) { setResolved(null); onResolve?.(null); return }
    const r = findCuentaByNumero(data, value)
    setResolved(r)
    onResolve?.(r)
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [data, value])

  return (
    <div>
      <input
        type="text"
        className="input font-mono"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        placeholder={placeholder ?? 'Digite el número de cuenta'}
        required={required}
        disabled={disabled}
      />
      {value.trim() !== '' && (
        <div className="mt-1.5 text-xs">
          {loading ? (
            <span className="text-ink-400">Cargando directorio…</span>
          ) : resolved ? (
            <span className="text-emerald-700">
              ✓ Titular: <span className="font-semibold">{resolved.titularNombre}</span>
              {resolved.titularIdent && (
                <span className="text-ink-500 ml-2 font-mono">{resolved.titularIdent}</span>
              )}
            </span>
          ) : (
            <span className="text-amber-700">⚠ Cuenta no encontrada</span>
          )}
        </div>
      )}
    </div>
  )
}
