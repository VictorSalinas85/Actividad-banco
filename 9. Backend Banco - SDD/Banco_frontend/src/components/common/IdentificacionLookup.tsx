import { useEffect, useState } from 'react'
import { useDirectorio, findClienteByIdentificacion } from './directorio'

interface Props {
  /** Identificacion en el form (lo que el usuario digita). */
  value: string
  onChange: (identificacion: string) => void
  /**
   * Callback cuando se resuelve la identificacion contra el directorio.
   * Util para que el padre guarde el id interno y/o tipo en su form.
   */
  onResolve?: (resultado: {
    tipo: 'PERSONA' | 'EMPRESA'
    id: number
    nombre: string
  } | null) => void
  /** Restringir busqueda a un tipo. Por defecto busca en ambos. */
  tipo?: 'PERSONA' | 'EMPRESA' | 'AMBOS'
  required?: boolean
  placeholder?: string
  disabled?: boolean
}

/**
 * Input de identificacion (CC/NIT/TI/CE/PAS) que muestra debajo el
 * nombre del cliente resuelto. Si no encuentra match, muestra aviso.
 */
export default function IdentificacionLookup({
  value, onChange, onResolve, tipo = 'AMBOS', required, placeholder, disabled,
}: Props) {
  const { data, loading } = useDirectorio()
  const [resolved, setResolved] = useState<{ tipo: 'PERSONA' | 'EMPRESA', id: number, nombre: string } | null>(null)

  useEffect(() => {
    if (!data) { setResolved(null); onResolve?.(null); return }
    const r = findClienteByIdentificacion(data, value, tipo)
    setResolved(r)
    onResolve?.(r)
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [data, value, tipo])

  return (
    <div>
      <input
        type="text"
        className="input"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        placeholder={placeholder ?? 'Digite la identificación (CC, NIT, TI, CE, PAS)'}
        required={required}
        disabled={disabled}
      />
      {value.trim() !== '' && (
        <div className="mt-1.5 text-xs">
          {loading ? (
            <span className="text-ink-400">Cargando directorio…</span>
          ) : resolved ? (
            <span className="text-emerald-700">
              ✓ <span className="font-semibold">{resolved.nombre}</span>
              <span className="text-ink-500 ml-2 font-mono">[{resolved.tipo}]</span>
            </span>
          ) : (
            <span className="text-amber-700">⚠ Identificación no encontrada</span>
          )}
        </div>
      )}
    </div>
  )
}
