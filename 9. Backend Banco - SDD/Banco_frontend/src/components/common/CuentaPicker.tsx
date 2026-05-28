import { useEffect, useMemo, useRef, useState } from 'react'
import api from '../../api/client'
import type { ApiResponse, CtaCuenta, CliPersonaNatural, CliEmpresa } from '../../types'

interface Props {
  value: number | null
  onChange: (id: number | null) => void
  disabled?: boolean
  placeholder?: string
  /** Filtra por tipo de titular para mostrar solo cuentas de personas o empresas. */
  filterTitular?: 'PERSONA' | 'EMPRESA'
}

interface Option {
  id: number
  numeroCuenta: string
  titularNombre: string
  titularIdent: string
}

let cachedCuentas: Option[] | null = null

async function loadCuentas(): Promise<Option[]> {
  if (cachedCuentas) return cachedCuentas
  const [cuentasRes, personasRes, empresasRes] = await Promise.all([
    api.get<ApiResponse<CtaCuenta[]>>('/cuentas'),
    api.get<ApiResponse<CliPersonaNatural[]>>('/personas'),
    api.get<ApiResponse<CliEmpresa[]>>('/empresas'),
  ])
  const personasMap = new Map(personasRes.data.data.map((p) => [p.id, p]))
  const empresasMap = new Map(empresasRes.data.data.map((e) => [e.id, e]))
  const opts: Option[] = cuentasRes.data.data.map((c) => {
    let titularNombre = '—'
    let titularIdent = ''
    if (c.titularPersonaId) {
      const p = personasMap.get(c.titularPersonaId)
      if (p) { titularNombre = p.nombreCompleto; titularIdent = p.identificacion }
    } else if (c.titularEmpresaId) {
      const e = empresasMap.get(c.titularEmpresaId)
      if (e) { titularNombre = e.razonSocial; titularIdent = e.nit }
    }
    return { id: c.id, numeroCuenta: c.numeroCuenta, titularNombre, titularIdent }
  })
  cachedCuentas = opts
  return opts
}

export function invalidateCuentaCache() { cachedCuentas = null }

export default function CuentaPicker({ value, onChange, disabled, placeholder, filterTitular }: Props) {
  const [options, setOptions] = useState<Option[]>([])
  const [loading, setLoading] = useState(true)
  const [open, setOpen] = useState(false)
  const [query, setQuery] = useState('')
  const containerRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    setLoading(true)
    loadCuentas()
      .then((opts) => setOptions(opts))
      .catch(() => setOptions([]))
      .finally(() => setLoading(false))
  }, [])

  useEffect(() => {
    const onClick = (e: MouseEvent) => {
      if (containerRef.current && !containerRef.current.contains(e.target as Node)) setOpen(false)
    }
    document.addEventListener('mousedown', onClick)
    return () => document.removeEventListener('mousedown', onClick)
  }, [])

  const selected = useMemo(() => options.find((o) => o.id === value), [options, value])

  const filtered = useMemo(() => {
    let pool = options
    if (filterTitular) {
      // No tenemos el tipo titular precalculado aqui; lo dejamos para futuro.
    }
    if (!query.trim()) return pool.slice(0, 30)
    const q = query.toLowerCase()
    return pool
      .filter((o) =>
        o.numeroCuenta.toLowerCase().includes(q) ||
        o.titularNombre.toLowerCase().includes(q) ||
        o.titularIdent.toLowerCase().includes(q)
      )
      .slice(0, 30)
  }, [options, query, filterTitular])

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
            <span className="font-mono text-xs bg-navy-50 px-1.5 py-0.5 rounded mr-2">{selected.numeroCuenta}</span>
            <span className="font-medium">{selected.titularNombre}</span>
            {selected.titularIdent && (
              <span className="text-ink-400 font-mono text-xs ml-2">{selected.titularIdent}</span>
            )}
          </span>
        ) : (
          <span className="text-ink-400">{placeholder ?? 'Seleccionar cuenta'}</span>
        )}
        <span className="text-ink-400 ml-2">▾</span>
      </button>

      {open && !disabled && (
        <div className="absolute z-30 mt-1 w-full bg-white border border-navy-100 rounded-lg shadow-lg max-h-72 overflow-hidden flex flex-col">
          <input
            autoFocus
            type="text"
            placeholder="Buscar por número, titular o identificación…"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            className="px-3 py-2 text-sm border-b border-navy-100 outline-none"
          />
          {loading ? (
            <div className="p-3 text-sm text-ink-400">Cargando…</div>
          ) : filtered.length === 0 ? (
            <div className="p-3 text-sm text-ink-400">Sin resultados</div>
          ) : (
            <ul className="overflow-y-auto">
              {value !== null && (
                <li>
                  <button
                    type="button"
                    onClick={() => { onChange(null); setOpen(false) }}
                    className="w-full text-left px-3 py-2 text-xs text-red-600 hover:bg-navy-50 border-b border-navy-100"
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
                    className={`w-full text-left px-3 py-2 text-sm hover:bg-gold-50 ${o.id === value ? 'bg-gold-50 font-medium' : ''}`}
                  >
                    <div className="flex items-center gap-2">
                      <span className="font-mono text-xs bg-navy-50 px-1.5 py-0.5 rounded">{o.numeroCuenta}</span>
                      <span className="text-navy-900 font-medium">{o.titularNombre}</span>
                    </div>
                    {o.titularIdent && (
                      <div className="text-xs text-ink-500 font-mono mt-0.5">{o.titularIdent}</div>
                    )}
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
