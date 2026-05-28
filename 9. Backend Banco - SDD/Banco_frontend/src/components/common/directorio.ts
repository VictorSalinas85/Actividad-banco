/**
 * Directorio central de clientes y cuentas para los lookups de Ops.
 * Mantiene un cache en memoria con (re)carga bajo demanda para que los
 * componentes de busqueda por identificacion o numero de cuenta no tengan
 * que pegarle al backend en cada keystroke.
 */
import api from '../../api/client'
import type { ApiResponse, CliPersonaNatural, CliEmpresa, CtaCuenta } from '../../types'

export interface PersonaDir   extends CliPersonaNatural {}
export interface EmpresaDir   extends CliEmpresa {}

export interface CuentaDir extends CtaCuenta {
  titularNombre: string
  titularIdent: string
}

interface DirectorioData {
  personas: PersonaDir[]
  empresas: EmpresaDir[]
  cuentas: CuentaDir[]
}

let cache: DirectorioData | null = null
let inflight: Promise<DirectorioData> | null = null
const subscribers = new Set<() => void>()

function notify() { subscribers.forEach((fn) => fn()) }

export function subscribeDirectorio(fn: () => void) {
  subscribers.add(fn)
  return () => { subscribers.delete(fn) }
}

export function invalidateDirectorioCache() {
  cache = null
  inflight = null
  notify()
}

export async function getDirectorio(): Promise<DirectorioData> {
  if (cache) return cache
  if (inflight) return inflight
  inflight = (async () => {
    const [personasRes, empresasRes, cuentasRes] = await Promise.all([
      api.get<ApiResponse<PersonaDir[]>>('/personas'),
      api.get<ApiResponse<EmpresaDir[]>>('/empresas'),
      api.get<ApiResponse<CtaCuenta[]>>('/cuentas'),
    ])
    const personas = personasRes.data.data ?? []
    const empresas = empresasRes.data.data ?? []
    const personasMap = new Map(personas.map((p) => [p.id, p]))
    const empresasMap = new Map(empresas.map((e) => [e.id, e]))
    const cuentas: CuentaDir[] = (cuentasRes.data.data ?? []).map((c) => {
      let titularNombre = '—'
      let titularIdent = ''
      if (c.titularPersonaId) {
        const p = personasMap.get(c.titularPersonaId)
        if (p) { titularNombre = p.nombreCompleto; titularIdent = p.identificacion }
      } else if (c.titularEmpresaId) {
        const e = empresasMap.get(c.titularEmpresaId)
        if (e) { titularNombre = e.razonSocial; titularIdent = e.nit }
      }
      return { ...c, titularNombre, titularIdent }
    })
    cache = { personas, empresas, cuentas }
    return cache
  })()
  try {
    return await inflight
  } finally {
    inflight = null
  }
}

/** Busca persona o empresa por identificacion (CC, NIT, etc). */
export function findClienteByIdentificacion(
  data: DirectorioData,
  identificacion: string,
  tipo: 'PERSONA' | 'EMPRESA' | 'AMBOS' = 'AMBOS',
): { tipo: 'PERSONA' | 'EMPRESA', id: number, nombre: string } | null {
  const id = identificacion.trim()
  if (!id) return null
  if (tipo !== 'EMPRESA') {
    const p = data.personas.find((p) => p.identificacion === id)
    if (p) return { tipo: 'PERSONA', id: p.id, nombre: p.nombreCompleto }
  }
  if (tipo !== 'PERSONA') {
    const e = data.empresas.find((e) => e.nit === id)
    if (e) return { tipo: 'EMPRESA', id: e.id, nombre: e.razonSocial }
  }
  return null
}

/** Busca cuenta por su numero de cuenta. */
export function findCuentaByNumero(data: DirectorioData, numero: string): CuentaDir | null {
  const n = numero.trim()
  if (!n) return null
  return data.cuentas.find((c) => c.numeroCuenta === n) ?? null
}

/** Devuelve todas las cuentas de un cliente identificado por CC/NIT. */
export function findCuentasByIdentificacion(
  data: DirectorioData,
  identificacion: string,
): { cliente: { tipo: 'PERSONA' | 'EMPRESA', id: number, nombre: string } | null, cuentas: CuentaDir[] } {
  const cliente = findClienteByIdentificacion(data, identificacion)
  if (!cliente) return { cliente: null, cuentas: [] }
  const cuentas = data.cuentas.filter((c) =>
    cliente.tipo === 'PERSONA'
      ? c.titularPersonaId === cliente.id
      : c.titularEmpresaId === cliente.id
  )
  return { cliente, cuentas }
}

/** Hook React para usar el directorio reactivamente. */
import { useEffect, useState } from 'react'

export function useDirectorio() {
  const [data, setData] = useState<DirectorioData | null>(cache)
  const [loading, setLoading] = useState(!cache)

  useEffect(() => {
    let alive = true
    const refresh = async () => {
      setLoading(true)
      try {
        const d = await getDirectorio()
        if (alive) setData(d)
      } finally {
        if (alive) setLoading(false)
      }
    }
    if (!cache) refresh()
    const unsub = subscribeDirectorio(() => { refresh() })
    return () => { alive = false; unsub() }
  }, [])

  return { data, loading, refresh: () => { invalidateDirectorioCache() } }
}
