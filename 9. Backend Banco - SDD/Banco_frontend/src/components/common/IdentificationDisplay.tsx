import { useEffect, useState } from 'react'
import api from '../../api/client'
import type { ApiResponse, CliPersonaNatural, CliEmpresa, SecUsuario } from '../../types'

type Kind = 'persona' | 'empresa' | 'usuario'

interface CachedEntity {
  primary: string
  secondary: string
}

const PATHS: Record<Kind, string> = {
  persona: '/personas',
  empresa: '/empresas',
  usuario: '/usuarios',
}

const cache = new Map<string, CachedEntity | 'loading' | 'missing'>()
const pending = new Map<Kind, Promise<void>>()
const subscribers = new Set<() => void>()

function notify() { subscribers.forEach((cb) => cb()) }

function keyOf(kind: Kind, id: number) { return `${kind}:${id}` }

function toEntity(kind: Kind, raw: unknown): CachedEntity {
  if (kind === 'persona') {
    const p = raw as CliPersonaNatural
    return { primary: p.nombreCompleto, secondary: p.identificacion }
  }
  if (kind === 'empresa') {
    const e = raw as CliEmpresa
    return { primary: e.razonSocial, secondary: e.nit }
  }
  const u = raw as SecUsuario
  return { primary: u.nombreCompleto, secondary: u.username }
}

function ensureLoaded(kind: Kind): Promise<void> {
  const existing = pending.get(kind)
  if (existing) return existing
  const p = api.get<ApiResponse<unknown[]>>(PATHS[kind])
    .then((res) => {
      ;(res.data.data ?? []).forEach((raw) => {
        const id = (raw as { id: number }).id
        cache.set(keyOf(kind, id), toEntity(kind, raw))
      })
      notify()
    })
    .catch(() => { /* swallow */ })
    .finally(() => { pending.delete(kind) })
  pending.set(kind, p)
  return p
}

interface DisplayProps {
  kind: Kind
  id?: number | null
  compact?: boolean
}

export default function IdentificationDisplay({ kind, id, compact }: DisplayProps) {
  const [, force] = useState(0)
  useEffect(() => {
    if (id == null) return
    const k = keyOf(kind, id)
    if (!cache.has(k)) {
      cache.set(k, 'loading')
      ensureLoaded(kind)
    }
    const cb = () => force((n) => n + 1)
    subscribers.add(cb)
    return () => { subscribers.delete(cb) }
  }, [kind, id])

  if (id == null) return <span className="text-slate-400">—</span>
  const entry = cache.get(keyOf(kind, id))
  if (entry === 'loading' || !entry) {
    return <span className="text-xs text-slate-400">#{id}</span>
  }
  if (entry === 'missing') {
    return <span className="text-xs text-slate-400">#{id} (no encontrado)</span>
  }

  if (compact) {
    return (
      <span className="inline-flex flex-col leading-tight">
        <span className="text-sm text-slate-800">{entry.primary}</span>
        <span className="text-xs text-slate-500 font-mono">{entry.secondary}</span>
      </span>
    )
  }
  return (
    <span>
      <span className="font-medium text-slate-800">{entry.primary}</span>
      <span className="text-xs text-slate-500 font-mono ml-2">{entry.secondary}</span>
    </span>
  )
}

interface ParticipanteProps {
  personaId?: number | null
  empresaId?: number | null
  compact?: boolean
}

export function ParticipanteDisplay({ personaId, empresaId, compact }: ParticipanteProps) {
  if (personaId) return <IdentificationDisplay kind="persona" id={personaId} compact={compact} />
  if (empresaId) return <IdentificationDisplay kind="empresa" id={empresaId} compact={compact} />
  return <span className="text-slate-400">—</span>
}
