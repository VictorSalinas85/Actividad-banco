import { useEffect, useState } from 'react'
import api from '../api/client'
import type { ApiResponse, Catalogo, TipoIdentificacion } from '../types'

interface EntityListResult<T> {
  data: T[]
  loading: boolean
  error: string | null
  reload: () => void
}

const cache = new Map<string, unknown[]>()
const subscribers = new Map<string, Set<() => void>>()

function notify(path: string) {
  subscribers.get(path)?.forEach((cb) => cb())
}

function useEntityList<T = Catalogo>(path: string): EntityListResult<T> {
  const [data, setData] = useState<T[]>(() => (cache.get(path) as T[]) ?? [])
  const [loading, setLoading] = useState(!cache.has(path))
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    let active = true
    const set = subscribers.get(path) ?? new Set()
    const cb = () => {
      if (active) setData((cache.get(path) as T[]) ?? [])
    }
    set.add(cb)
    subscribers.set(path, set)

    if (!cache.has(path)) {
      setLoading(true)
      api.get<ApiResponse<T[]>>(path)
        .then((res) => {
          cache.set(path, (res.data.data ?? []) as unknown[])
          if (active) {
            setData(res.data.data ?? [])
            setError(null)
          }
          notify(path)
        })
        .catch((err) => {
          if (active) setError(err.message ?? 'Error cargando datos')
        })
        .finally(() => { if (active) setLoading(false) })
    }

    return () => {
      active = false
      set.delete(cb)
    }
  }, [path])

  const reload = () => {
    cache.delete(path)
    setLoading(true)
    api.get<ApiResponse<T[]>>(path)
      .then((res) => {
        cache.set(path, (res.data.data ?? []) as unknown[])
        setData(res.data.data ?? [])
        notify(path)
      })
      .catch((err) => setError(err.message ?? 'Error'))
      .finally(() => setLoading(false))
  }

  return { data, loading, error, reload }
}

export const useTiposIdent     = () => useEntityList<TipoIdentificacion>('/catalogos/tipos-identificacion')
export const useEstadosUsuario = () => useEntityList<Catalogo>('/catalogos/estados-usuario')
export const useEstadosCuenta  = () => useEntityList<Catalogo>('/catalogos/estados-cuenta')
export const useTiposCuenta    = () => useEntityList<Catalogo>('/catalogos/tipos-cuenta')
export const useMonedas        = () => useEntityList<Catalogo>('/catalogos/monedas')
export const useTiposMov       = () => useEntityList<Catalogo>('/catalogos/tipos-movimiento')
export const useEstadosPrest   = () => useEntityList<Catalogo>('/catalogos/estados-prestamo')
export const useTiposPrest     = () => useEntityList<Catalogo>('/catalogos/tipos-prestamo')
export const useEstadosTrans   = () => useEntityList<Catalogo>('/catalogos/estados-transferencia')
export const useTiposTrans     = () => useEntityList<Catalogo>('/catalogos/tipos-operacion')
export const useCanalesOp      = () => useEntityList<Catalogo>('/catalogos/canales-operacion')
export const useMotivosRechazo = () => useEntityList<Catalogo>('/catalogos/motivos-rechazo')
export const useMotivosBloqueo = () => useEntityList<Catalogo>('/catalogos/motivos-bloqueo')
export const useEstadosSesion  = () => useEntityList<Catalogo>('/catalogos/estados-sesion')
export const useRolesEmpresa   = () => useEntityList<Catalogo>('/catalogos/roles-empresa')

export default useEntityList
