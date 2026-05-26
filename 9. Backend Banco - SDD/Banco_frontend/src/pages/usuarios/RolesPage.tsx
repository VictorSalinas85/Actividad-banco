import CrudTable from '../../components/CrudTable'
import type { Column, Field, SecRol } from '../../types'

const columns: Column<SecRol>[] = [
  { key: 'id', header: 'ID' },
  { key: 'codigoRolSistema', header: 'Código' },
  { key: 'nombreRol', header: 'Nombre' },
  { key: 'descripcion', header: 'Descripción' },
  {
    key: 'activo',
    header: 'Activo',
    render: (v) => (
      <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${v ? 'bg-green-100 text-green-700' : 'bg-slate-100 text-slate-500'}`}>
        {v ? 'Sí' : 'No'}
      </span>
    ),
  },
]

const fields: Field[] = [
  { key: 'codigoRolSistema', label: 'Código del rol', type: 'text', required: true },
  { key: 'nombreRol', label: 'Nombre del rol', type: 'text', required: true },
  { key: 'descripcion', label: 'Descripción', type: 'textarea' },
  { key: 'activo', label: 'Activo', type: 'checkbox' },
]

export default function RolesPage() {
  return (
    <CrudTable<SecRol>
      title="Roles del Sistema"
      apiPath="/usuarios/roles"
      columns={columns}
      fields={fields}
    />
  )
}
