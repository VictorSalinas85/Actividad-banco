import { useState, useEffect, useCallback } from 'react'
import toast from 'react-hot-toast'
import Modal from './Modal'
import Spinner from './Spinner'
import type { Column, Field } from '../types'
import api from '../api/client'
import type { ApiResponse } from '../types'

interface Props<T extends object> {
  title: string
  apiPath: string
  columns: Column<T>[]
  fields: Field[]
  canCreate?: boolean
  canEdit?: boolean
  canDelete?: boolean
  idKey?: string
  readOnly?: boolean
}

export default function CrudTable<T extends object>({
  title,
  apiPath,
  columns,
  fields,
  canCreate = true,
  canEdit = true,
  canDelete = true,
  idKey = 'id',
  readOnly = false,
}: Props<T>) {
  const [items, setItems] = useState<T[]>([])
  const [loading, setLoading] = useState(true)
  const [showModal, setShowModal] = useState(false)
  const [editItem, setEditItem] = useState<T | null>(null)
  const [form, setForm] = useState<Record<string, unknown>>({})
  const [submitting, setSubmitting] = useState(false)

  const fetchItems = useCallback(async () => {
    setLoading(true)
    try {
      const res = await api.get<ApiResponse<T[]>>(apiPath)
      setItems(res.data.data ?? [])
    } catch {
      toast.error('Error cargando datos')
    } finally {
      setLoading(false)
    }
  }, [apiPath])

  useEffect(() => { fetchItems() }, [fetchItems])

  const openCreate = () => {
    setEditItem(null)
    const defaults: Record<string, unknown> = {}
    fields.forEach((f) => {
      if (f.type === 'checkbox') defaults[f.key] = false
      else if (f.type === 'number') defaults[f.key] = ''
      else defaults[f.key] = ''
    })
    setForm(defaults)
    setShowModal(true)
  }

  const openEdit = (item: T) => {
    setEditItem(item)
    const filled: Record<string, unknown> = {}
    fields.forEach((f) => { filled[f.key] = (item as Record<string, unknown>)[f.key] ?? '' })
    setForm(filled)
    setShowModal(true)
  }

  const handleDelete = async (item: T) => {
    if (!confirm(`¿Eliminar este registro?`)) return
    try {
      await api.delete(`${apiPath}/${(item as Record<string, unknown>)[idKey]}`)
      toast.success('Registro eliminado')
      fetchItems()
    } catch (err: unknown) {
      const msg = (err as { response?: { data?: { message?: string } } })
        .response?.data?.message ?? 'Error al eliminar'
      toast.error(msg)
    }
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setSubmitting(true)
    try {
      if (editItem) {
        await api.put(`${apiPath}/${(editItem as Record<string, unknown>)[idKey]}`, form)
        toast.success('Registro actualizado')
      } else {
        await api.post(apiPath, form)
        toast.success('Registro creado')
      }
      setShowModal(false)
      fetchItems()
    } catch (err: unknown) {
      const msg = (err as { response?: { data?: { message?: string } } })
        .response?.data?.message ?? 'Error al guardar'
      toast.error(msg)
    } finally {
      setSubmitting(false)
    }
  }

  const handleChange = (key: string, value: unknown) => {
    setForm((prev) => ({ ...prev, [key]: value }))
  }

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-extrabold text-navy-900 tracking-wide">{title}</h1>
        {!readOnly && canCreate && (
          <button onClick={openCreate} className="btn-primary flex items-center gap-2">
            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
            </svg>
            Nuevo
          </button>
        )}
      </div>

      <div className="card p-0 overflow-hidden">
        {loading ? (
          <Spinner className="py-16" />
        ) : items.length === 0 ? (
          <div className="text-center py-16 text-slate-400">
            <svg className="w-12 h-12 mx-auto mb-3 opacity-40" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1} d="M9 17v-2m3 2v-4m3 4v-6m2 10H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
            No hay registros
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr>
                  {columns.map((col) => (
                    <th key={String(col.key)} className="table-header">{col.header}</th>
                  ))}
                  {!readOnly && (canEdit || canDelete) && (
                    <th className="table-header text-right">Acciones</th>
                  )}
                </tr>
              </thead>
              <tbody>
                {items.map((item, i) => (
                  <tr key={i} className="table-row">
                    {columns.map((col) => (
                      <td key={String(col.key)} className="table-cell">
                        {col.render
                          ? col.render((item as Record<string, unknown>)[col.key as string], item)
                          : String((item as Record<string, unknown>)[col.key as string] ?? '')}
                      </td>
                    ))}
                    {!readOnly && (canEdit || canDelete) && (
                      <td className="table-cell text-right space-x-2">
                        {canEdit && (
                          <button
                            onClick={() => openEdit(item)}
                            className="text-navy-700 hover:text-gold-600 text-xs font-semibold transition-colors"
                          >
                            Editar
                          </button>
                        )}
                        {canDelete && (
                          <button
                            onClick={() => handleDelete(item)}
                            className="text-red-600 hover:text-red-800 text-xs font-medium"
                          >
                            Eliminar
                          </button>
                        )}
                      </td>
                    )}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      <Modal
        open={showModal}
        title={editItem ? 'Editar registro' : 'Nuevo registro'}
        onClose={() => setShowModal(false)}
        size="md"
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          {fields.map((field) => (
            <div key={field.key}>
              <label className="label">{field.label}</label>
              {field.type === 'textarea' ? (
                <textarea
                  className="input"
                  value={String(form[field.key] ?? '')}
                  onChange={(e) => handleChange(field.key, e.target.value)}
                  required={field.required}
                  rows={3}
                />
              ) : field.type === 'select' ? (
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
                  readOnly={field.readOnly}
                />
              )}
            </div>
          ))}
          <div className="flex justify-end gap-3 pt-2">
            <button type="button" onClick={() => setShowModal(false)} className="btn-secondary">
              Cancelar
            </button>
            <button type="submit" className="btn-primary" disabled={submitting}>
              {submitting ? 'Guardando...' : editItem ? 'Actualizar' : 'Crear'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  )
}
