import { useState } from 'react'
import toast from 'react-hot-toast'
import type { Field, SpResultado, ApiResponse } from '../types'

interface Props {
  title: string
  description?: string
  fields: Field[]
  onSubmit: (data: Record<string, unknown>) => Promise<{ data: ApiResponse<SpResultado | unknown> }>
  resultLabel?: string
}

export default function OpSection({ title, description, fields, onSubmit, resultLabel }: Props) {
  const [form, setForm] = useState<Record<string, unknown>>(() => {
    const defaults: Record<string, unknown> = {}
    fields.forEach((f) => {
      if (f.type === 'checkbox') defaults[f.key] = false
      else if (f.type === 'number') defaults[f.key] = ''
      else defaults[f.key] = ''
    })
    return defaults
  })
  const [submitting, setSubmitting] = useState(false)
  const [result, setResult] = useState<SpResultado | null>(null)

  const handleChange = (key: string, value: unknown) =>
    setForm((prev) => ({ ...prev, [key]: value }))

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setSubmitting(true)
    setResult(null)
    try {
      const res = await onSubmit(form)
      const data = res.data.data as SpResultado | undefined
      if (data && 'codigo' in data) {
        setResult(data)
        if (data.codigo === 0) {
          toast.success(data.mensaje)
        } else {
          toast.error(data.mensaje)
        }
      } else {
        toast.success('Operación ejecutada correctamente')
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
      <h2 className="text-lg font-semibold text-slate-800 mb-1">{title}</h2>
      {description && <p className="text-sm text-slate-500 mb-4">{description}</p>}

      <form onSubmit={handleSubmit} className="space-y-3">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
          {fields.map((field) => (
            <div key={field.key}>
              <label className="label">{field.label}</label>
              {field.type === 'select' ? (
                <select
                  className="input"
                  value={String(form[field.key] ?? '')}
                  onChange={(e) => handleChange(field.key, e.target.value)}
                  required={field.required}
                >
                  <option value="">-- Seleccionar --</option>
                  {field.options?.map((opt) => (
                    <option key={opt.value} value={opt.value}>{opt.label}</option>
                  ))}
                </select>
              ) : field.type === 'checkbox' ? (
                <input
                  type="checkbox"
                  className="w-4 h-4 text-blue-600"
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
            </div>
          ))}
        </div>

        <div className="flex items-center gap-4 pt-2">
          <button type="submit" className="btn-primary" disabled={submitting}>
            {submitting ? 'Ejecutando...' : 'Ejecutar'}
          </button>
          {result && (
            <div
              className={`text-sm px-3 py-1.5 rounded-md ${
                result.codigo === 0
                  ? 'bg-green-50 text-green-700 border border-green-200'
                  : 'bg-red-50 text-red-700 border border-red-200'
              }`}
            >
              <span className="font-medium">{resultLabel ?? 'Resultado'}:</span>{' '}
              [{result.codigo}] {result.mensaje}
              {result.dato != null && ` | Dato: ${result.dato}`}
            </div>
          )}
        </div>
      </form>
    </div>
  )
}
