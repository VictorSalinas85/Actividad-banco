import { useEffect, useMemo, useRef, useState } from 'react'
import { useDirectorio } from './directorio'

export interface TitularResuelto {
  tipo: 'PERSONA' | 'EMPRESA'
  id: number
  nombre: string
  identificacion: string
}

interface Props {
  value: TitularResuelto | null
  onChange: (titular: TitularResuelto | null) => void
  /** Restringir a un tipo. Por defecto permite persona y empresa. */
  tipo?: 'PERSONA' | 'EMPRESA' | 'AMBOS'
  required?: boolean
  placeholder?: string
  disabled?: boolean
}

/**
 * Selector de titular (persona o empresa) con búsqueda por nombre o identificación.
 * Se apoya en el directorio en memoria, así que filtra sin pegarle al backend en
 * cada tecla. Pensado para formularios donde puede haber muchos clientes y digitar
 * la identificación exacta resulta incómodo.
 */
export default function TitularLookup({
  value, onChange, tipo = 'AMBOS', required, placeholder, disabled,
}: Props) {
  const { data, loading } = useDirectorio()
  const [open, setOpen] = useState(false)
  const [query, setQuery] = useState('')
  const containerRef = useRef<HTMLDivElement>(null)

  const options = useMemo<TitularResuelto[]>(() => {
    if (!data) return []
    const personas: TitularResuelto[] = tipo === 'EMPRESA' ? [] : data.personas.map((p) => ({
      tipo: 'PERSONA', id: p.id, nombre: p.nombreCompleto, identificacion: p.identificacion,
    }))
    const empresas: TitularResuelto[] = tipo === 'PERSONA' ? [] : data.empresas.map((e) => ({
      tipo: 'EMPRESA', id: e.id, nombre: e.razonSocial, identificacion: e.nit,
    }))
    return [...personas, ...empresas]
  }, [data, tipo])

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase()
    if (!q) return options.slice(0, 30)
    return options
      .filter((o) => o.nombre.toLowerCase().includes(q) || o.identificacion.toLowerCase().includes(q))
      .slice(0, 30)
  }, [options, query])

  useEffect(() => {
    const onClick = (e: MouseEvent) => {
      if (containerRef.current && !containerRef.current.contains(e.target as Node)) {
        setOpen(false)
      }
    }
    document.addEventListener('mousedown', onClick)
    return () => document.removeEventListener('mousedown', onClick)
  }, [])

  return (
    <div ref={containerRef} className="relative">
      <button
        type="button"
        disabled={disabled}
        onClick={() => setOpen((o) => !o)}
        className="input flex items-center justify-between text-left w-full disabled:opacity-60"
      >
        {value ? (
          <span className="truncate">
            <span className="font-medium">{value.nombre}</span>
            <span className="text-slate-400 font-mono text-xs ml-2">{value.identificacion}</span>
            <span className="text-ink-500 text-xs ml-2">[{value.tipo}]</span>
          </span>
        ) : (
          <span className="text-slate-400">{placeholder ?? 'Buscar titular por nombre o identificación'}</span>
        )}
        <span className="text-slate-400 ml-2">▾</span>
      </button>

      {/* Campo oculto para que la validación required del form funcione */}
      {required && (
        <input
          tabIndex={-1}
          aria-hidden
          className="sr-only"
          value={value ? String(value.id) : ''}
          onChange={() => {}}
          required
        />
      )}

      {open && !disabled && (
        <div className="absolute z-30 mt-1 w-full bg-white border border-slate-200 rounded-lg shadow-lg max-h-72 overflow-hidden flex flex-col">
          <input
            autoFocus
            type="text"
            placeholder="Escriba el nombre o la identificación…"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            className="px-3 py-2 text-sm border-b border-slate-100 outline-none"
          />
          {loading ? (
            <div className="p-3 text-sm text-slate-400">Cargando directorio…</div>
          ) : filtered.length === 0 ? (
            <div className="p-3 text-sm text-slate-400">Sin resultados</div>
          ) : (
            <ul className="overflow-y-auto">
              {value !== null && (
                <li>
                  <button
                    type="button"
                    onClick={() => { onChange(null); setOpen(false); setQuery('') }}
                    className="w-full text-left px-3 py-2 text-xs text-red-600 hover:bg-slate-50 border-b border-slate-100"
                  >
                    Limpiar selección
                  </button>
                </li>
              )}
              {filtered.map((o) => (
                <li key={`${o.tipo}-${o.id}`}>
                  <button
                    type="button"
                    onClick={() => { onChange(o); setOpen(false); setQuery('') }}
                    className={`w-full text-left px-3 py-2 text-sm hover:bg-blue-50 ${
                      value && o.tipo === value.tipo && o.id === value.id ? 'bg-blue-50 font-medium' : ''
                    }`}
                  >
                    <div className="text-slate-800">
                      {o.nombre}
                      <span className="text-ink-400 text-xs ml-2">[{o.tipo}]</span>
                    </div>
                    <div className="text-xs text-slate-500 font-mono">{o.identificacion}</div>
                  </button>
                </li>
              ))}
            </ul>
          )}
        </div>
      )}
    </div>
  )
}
