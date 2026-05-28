import { useCallback, useState } from 'react'
import toast from 'react-hot-toast'
import type { Field, SpResultado, ApiResponse } from '../types'
import { useAuth } from '../auth/AuthContext'
import { EntityPicker, CuentaPicker, invalidateCuentaCache } from './common'
import { invalidateDirectorioCache } from './common/directorio'

interface Props {
  title: string
  description?: string
  fields: Field[]
  onSubmit: (data: Record<string, unknown>) => Promise<{ data: ApiResponse<SpResultado | unknown> }>
  resultLabel?: string
  /**
   * Claves del payload donde se debe auto-inyectar el ID del usuario autenticado.
   * Por defecto cubre los nombres que usan los DTOs Java de Ops:
   * actorUsuarioId, creadorUsuarioId, analistaUsuarioId, aprobadorUsuarioId, usuarioId.
   */
  actorKeys?: string[]
}

const DEFAULT_ACTOR_KEYS = [
  'actorUsuarioId',
  'creadorUsuarioId',
  'analistaUsuarioId',
  'aprobadorUsuarioId',
  'usuarioId',
]

export default function OpSection({
  title, description, fields, onSubmit, resultLabel, actorKeys = DEFAULT_ACTOR_KEYS,
}: Props) {
  const { user } = useAuth()

  const buildDefaults = useCallback((): Record<string, unknown> => {
    const defaults: Record<string, unknown> = {}
    fields.forEach((f) => {
      if (f.type === 'checkbox') defaults[f.key] = false
      else if (f.type === 'number') defaults[f.key] = ''
      else if (f.type.endsWith('-picker')) defaults[f.key] = null
      else defaults[f.key] = ''
    })
    return defaults
  }, [fields])

  const [form, setForm] = useState<Record<string, unknown>>(buildDefaults)
  const [submitting, setSubmitting] = useState(false)
  const [result, setResult] = useState<SpResultado | null>(null)
  // Bump del key del form para forzar remount de los pickers tras submit
  // (asi limpian su seleccion interna).
  const [formKey, setFormKey] = useState(0)

  const handleChange = (key: string, value: unknown) =>
    setForm((prev) => ({ ...prev, [key]: value }))

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setSubmitting(true)
    setResult(null)
    try {
      // Auto-inyectar el id del usuario autenticado en las claves de actor.
      const payload: Record<string, unknown> = { ...form }
      if (user?.id != null) {
        for (const k of actorKeys) {
          if (payload[k] == null || payload[k] === '') payload[k] = user.id
        }
      }
      // Limpiar strings vacíos / null en pickers para no enviar basura.
      for (const k of Object.keys(payload)) {
        if (payload[k] === '' || payload[k] === null) delete payload[k]
      }

      const res = await onSubmit(payload)
      const data = res.data.data as SpResultado | undefined
      const exito = !data || !('code' in data) || data.code === 'OK'

      if (data && typeof data === 'object' && 'code' in data) {
        setResult(data)
        if (data.code === 'OK') toast.success(data.message ?? 'Operación exitosa')
        else toast.error(`[${data.code}] ${data.message ?? 'Error en la operación'}`)
      } else {
        toast.success('Operación ejecutada correctamente')
      }

      // Tras una operación exitosa, limpiar el formulario e invalidar caches
      // para que las consultas posteriores (listados, pickers) traigan datos
      // frescos del backend.
      if (exito) {
        invalidateCuentaCache()
        invalidateDirectorioCache()
        setForm(buildDefaults())
        setFormKey((k) => k + 1)
      }
    } catch (err: unknown) {
      const msg = (err as { response?: { data?: { message?: string } } })
        .response?.data?.message ?? 'Error al ejecutar'
      toast.error(msg)
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="card mb-6">
      <h2 className="text-lg font-bold text-navy-900 mb-1">{title}</h2>
      {description && <p className="text-sm text-ink-500 mb-4">{description}</p>}

      <form key={formKey} onSubmit={handleSubmit} className="space-y-3">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
          {fields.map((field) => (
            <div key={field.key}>
              <label className="label">{field.label}{field.required && <span className="text-red-500"> *</span>}</label>

              {field.type === 'select' ? (
                <select
                  className="input"
                  value={String(form[field.key] ?? '')}
                  onChange={(e) => handleChange(field.key, e.target.value)}
                  required={field.required}
                >
                  <option value="">-- Seleccionar --</option>
                  {field.options?.map((opt) => (
                    <option key={String(opt.value)} value={String(opt.value)}>{opt.label}</option>
                  ))}
                </select>

              ) : field.type === 'checkbox' ? (
                <input
                  type="checkbox"
                  className="w-4 h-4 accent-gold-500"
                  checked={!!form[field.key]}
                  onChange={(e) => handleChange(field.key, e.target.checked)}
                />

              ) : field.type === 'textarea' ? (
                <textarea
                  className="input"
                  rows={2}
                  value={String(form[field.key] ?? '')}
                  onChange={(e) => handleChange(field.key, e.target.value)}
                  required={field.required}
                />

              ) : field.type === 'person-picker' ? (
                <EntityPicker
                  kind="persona"
                  value={(form[field.key] as number | null) ?? null}
                  onChange={(id) => handleChange(field.key, id)}
                />

              ) : field.type === 'empresa-picker' ? (
                <EntityPicker
                  kind="empresa"
                  value={(form[field.key] as number | null) ?? null}
                  onChange={(id) => handleChange(field.key, id)}
                />

              ) : field.type === 'usuario-picker' ? (
                <EntityPicker
                  kind="usuario"
                  value={(form[field.key] as number | null) ?? null}
                  onChange={(id) => handleChange(field.key, id)}
                />

              ) : field.type === 'cuenta-picker' ? (
                <CuentaPicker
                  value={(form[field.key] as number | null) ?? null}
                  onChange={(id) => handleChange(field.key, id)}
                />

              ) : (
                <input
                  type={field.type}
                  className="input"
                  value={String(form[field.key] ?? '')}
                  onChange={(e) =>
                    handleChange(
                      field.key,
                      field.type === 'number' ? Number(e.target.value) : e.target.value
                    )
                  }
                  required={field.required}
                />
              )}

              {field.help && <p className="help-text">{field.help}</p>}
            </div>
          ))}
        </div>

        <div className="flex items-center gap-4 pt-2">
          <button type="submit" className="btn-primary" disabled={submitting}>
            {submitting ? 'Ejecutando…' : 'Ejecutar'}
          </button>
          {result && (
            <div
              className={`text-sm px-3 py-1.5 rounded-md ${
                result.code === 'OK'
                  ? 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                  : 'bg-red-50 text-red-700 border border-red-200'
              }`}
            >
              <span className="font-semibold">{resultLabel ?? 'Resultado'}:</span>{' '}
              [{result.code}] {result.message}
              {result.reference != null && result.reference !== '' && ` · Ref: ${result.reference}`}
            </div>
          )}
        </div>
      </form>
    </div>
  )
}
