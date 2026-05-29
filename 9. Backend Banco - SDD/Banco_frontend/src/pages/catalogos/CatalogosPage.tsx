import { useState } from 'react'
import CrudTable from '../../components/CrudTable'
import type { Column, Field, Catalogo } from '../../types'

const CATALOGS = [
  { key: 'estados-usuario', label: 'Estados de Usuario' },
  { key: 'estados-cuenta', label: 'Estados de Cuenta' },
  { key: 'tipos-cuenta', label: 'Tipos de Cuenta' },
  { key: 'monedas', label: 'Monedas' },
  { key: 'estados-prestamo', label: 'Estados de Préstamo' },
  { key: 'tipos-prestamo', label: 'Tipos de Préstamo' },
  { key: 'estados-transferencia', label: 'Estados de Transferencia' },
  { key: 'tipos-operacion', label: 'Tipos de Operación' },
  { key: 'canales-operacion', label: 'Canales de Operación' },
  { key: 'motivos-rechazo', label: 'Motivos de Rechazo' },
  { key: 'motivos-bloqueo', label: 'Motivos de Bloqueo' },
  { key: 'estados-sesion', label: 'Estados de Sesión' },
  { key: 'roles-empresa', label: 'Roles de Empresa' },
  { key: 'tipos-identificacion', label: 'Tipos de Identificación' },
  { key: 'tipos-movimiento', label: 'Tipos de Movimiento' },
  { key: 'parametros-negocio', label: 'Parámetros de Negocio' },
  { key: 'transiciones-estado', label: 'Transiciones de Estado' },
]

const columns: Column<Catalogo>[] = [
  { key: 'id', header: 'ID' },
  { key: 'codigo', header: 'Código',
    render: (v) => v ? <code className="text-xs bg-ink-100 px-1.5 py-0.5 rounded font-mono">{String(v)}</code> : <span className="text-slate-400">—</span> },
  { key: 'nombre', header: 'Nombre' },
  { key: 'aplicaA', header: 'Aplica a',
    render: (v) => v ? <span className="text-xs text-slate-600">{String(v)}</span> : <span className="text-slate-400">—</span> },
  { key: 'descripcion', header: 'Descripción' },
  {
    key: 'activo',
    header: 'Activo',
    render: (v) => (
      <span className={`badge ${v ? 'badge-success' : 'badge-muted'}`}>{v ? 'Sí' : 'No'}</span>
    ),
  },
]

const fields: Field[] = [
  { key: 'codigo', label: 'Código', type: 'text', required: true, immutableOnEdit: true,
    help: 'Identificador único del catálogo. No se puede modificar después de crear.' },
  { key: 'nombre', label: 'Nombre', type: 'text', required: true },
  { key: 'descripcion', label: 'Descripción', type: 'textarea' },
  { key: 'aplicaA', label: 'Aplica a (solo tipos identificación: PERSONA/EMPRESA/AMBOS)', type: 'text' },
  { key: 'activo', label: 'Activo', type: 'checkbox' },
]

export default function CatalogosPage() {
  const [selected, setSelected] = useState(CATALOGS[0].key)
  const current = CATALOGS.find((c) => c.key === selected)!

  return (
    <div>
      <h1 className="text-2xl font-bold text-slate-800 mb-6">Catálogos del Sistema</h1>
      <div className="flex gap-6">
        <div className="w-56 shrink-0">
          <div className="bg-white rounded-lg border border-slate-200 overflow-hidden shadow-sm">
            {CATALOGS.map((cat) => (
              <button
                key={cat.key}
                onClick={() => setSelected(cat.key)}
                className={`w-full text-left px-4 py-2.5 text-sm border-b border-slate-100 last:border-0 transition-colors ${
                  selected === cat.key
                    ? 'bg-blue-700 text-white font-medium'
                    : 'text-slate-700 hover:bg-slate-50'
                }`}
              >
                {cat.label}
              </button>
            ))}
          </div>
        </div>

        <div className="flex-1">
          <CrudTable<Catalogo>
            key={selected}
            title={current.label}
            apiPath={`/catalogos/${selected}`}
            columns={columns}
            fields={fields}
          />
        </div>
      </div>
    </div>
  )
}
