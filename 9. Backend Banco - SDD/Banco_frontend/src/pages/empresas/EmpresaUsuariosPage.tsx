import CrudTable from '../../components/CrudTable'
import type { Column, Field, CliEmpresaUsuario } from '../../types'

const columns: Column<CliEmpresaUsuario>[] = [
  { key: 'id', header: 'ID' },
  { key: 'empresaId', header: 'Empresa ID' },
  { key: 'usuarioId', header: 'Usuario ID' },
  { key: 'cargo', header: 'Cargo' },
  {
    key: 'activo',
    header: 'Activo',
    render: (v) => (
      <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${v ? 'bg-green-100 text-green-700' : 'bg-slate-100 text-slate-500'}`}>
        {v ? 'Sí' : 'No'}
      </span>
    ),
  },
  { key: 'vinculadoEn', header: 'Vinculado' },
]

const fields: Field[] = [
  { key: 'empresaId', label: 'Empresa (ID)', type: 'number', required: true },
  { key: 'usuarioId', label: 'Usuario (ID)', type: 'number', required: true },
  { key: 'cargo', label: 'Cargo', type: 'text' },
  { key: 'activo', label: 'Activo', type: 'checkbox' },
]

export default function EmpresaUsuariosPage() {
  return (
    <CrudTable<CliEmpresaUsuario>
      title="Usuarios de Empresa"
      apiPath="/empresas/usuarios"
      columns={columns}
      fields={fields}
    />
  )
}
