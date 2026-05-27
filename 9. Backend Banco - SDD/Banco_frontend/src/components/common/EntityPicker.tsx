import { useEffect, useMemo, useRef, useState } from 'react'
import api from '../../api/client'
import type { ApiResponse, CliPersonaNatural, CliEmpresa, SecUsuario } from '../../types'

type Kind = 'persona' | 'empresa' | 'usuario'

interface Props {
  kind: Kind
  value: number | null
  onChange: (id: number | null) => void
  disabled?: boolean
  placeholder?: string
}

interface Option {
  id: number
  primary: string
  secondary: string
}

const PATHS: Record<Kind, string> = {
  persona: '/personas',
  empresa: '/empresas',
  usuario: '/usuarios',
}

const cache = new Map<Kind, Option[]>()

function toOption(kind: Kind, raw: unknown): Option {
  if (kind === 'persona') {
    const p = raw as CliPersonaNatural
    return { id: p.id, primary: p.nombreCompleto, secondary: p.identificacion }
  }
  if (kind === 'empresa') {
    const e = raw as CliEmpresa
    return { id: e.id, primary: e.razonSocial, secondary: e.nit }
  }
  const u = raw as SecUsuario
  return { id: u.id, primary: u.nombreCompleto, secondary: u.username }
}

export default function EntityPicker({ kind, value, onChange, disabled, placeholder }: Props) {
  const [options, setOptions] = useState<Option[]>(() => cache.get(kind) ?? [])
  const [loading, setLoading] = useState(!cache.has(kind))
  const [open, setOpen] = useState(false)
  const [query, setQuery] = useState('')
  const containerRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    if (cache.has(kind)) return
    setLoading(true)
    api.get<ApiResponse<unknown[]>>(PATHS[kind])
      .then((res) => {
        const opts = (res.data.data ?? []).map((r) => toOption(kind, r))
        cache.set(kind, opts)
        setOptions(opts)
      })
      .catch(() => setOptions([]))
      .finally(() => setLoading(false))
  }, [kind])

  useEffect(() => {
    const onClick = (e: MouseEvent) => {
      if (containerRef.current && !containerRef.current.contains(e.target as Node)) {
        setOpen(false)
      }
    }
    document.addEventListener('mousedown', onClick)
    return () => document.removeEventListener('mousedown', onClick)
  }, [])

  const selected = useMemo(() => options.find((o) => o.id === value), [options, value])

  const filtered = useMemo(() => {
    if (!query.trim()) return options.slice(0, 30)
    const q = query.toLowerCase()
    return options
      .filter((o) =>
        o.primary.toLowerCase().includes(q) || o.secondary.toLowerCase().includes(q)
      )
      .slice(0, 30)
  }, [options, query])

  return (
    <div ref={containerRef} className="relative">
      <button
        type="button"
        disabled={disabled}
        onClick={() => setOpen((o) => !o)}
        className="input flex items-center justify-between text-left w-full disabled:opacity-60"
      >
        {selected ? (
          <span className="truncate">
            <span className="font-medium">{selected.primary}</span>
            <span className="text-slate-400 font-mono text-xs ml-2">{selected.secondary}</span>
          </span>
        ) : (
          <span className="text-slate-400">{placeholder ?? `Seleccionar ${kind}`}</span>
        )}
        <span className="text-slate-400 ml-2">▾</span>
      </button>

      {open && !disabled && (
        <div className="absolute z-30 mt-1 w-full bg-white border border-slate-200 rounded-lg shadow-lg max-h-72 overflow-hidden flex flex-col">
          <input
            autoFocus
            type="text"
            placeholder="Buscar por nombre o identificación..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            className="px-3 py-2 text-sm border-b border-slate-100 outline-none"
          />
          {loading ? (
            <div className="p-3 text-sm text-slate-400">Cargando…</div>
          ) : filtered.length === 0 ? (
            <div className="p-3 text-sm text-slate-400">Sin resultados</div>
          ) : (
            <ul className="overflow-y-auto">
              {value !== null && (
                <li>
                  <button
                    type="button"
                    onClick={() => { onChange(null); setOpen(false) }}
                    className="w-full text-left px-3 py-2 text-xs text-red-600 hover:bg-slate-50 border-b border-slate-100"
                  >
                    Limpiar selección
                  </button>
                </li>
              )}
              {filtered.map((o) => (
                <li key={o.id}>
                  <button
                    type="button"
                    onClick={() => { onChange(o.id); setOpen(false); setQuery('') }}
                    className={`w-full text-left px-3 py-2 text-sm hover:bg-blue-50 ${
                      o.id === value ? 'bg-blue-50 font-medium' : ''
                    }`}
                  >
                    <div className="text-slate-800">{o.primary}</div>
                    <div className="text-xs text-slate-500 font-mono">{o.secondary}</div>
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
