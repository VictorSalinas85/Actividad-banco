import CrudTable from '../../components/CrudTable'
import type { Column, Field, CliEmpresa } from '../../types'
import { useAuth } from '../../auth/AuthContext'

const columns: Column<CliEmpresa>[] = [
  { key: 'id', header: 'ID' },
  { key: 'nit', header: 'NIT' },
  { key: 'razonSocial', header: 'Razón Social' },
  { key: 'sectorEconomico', header: 'Sector' },
  { key: 'email', header: 'Email' },
  { key: 'telefono', header: 'Teléfono' },
  { key: 'estadoId', header: 'Estado' },
]

const fields: Field[] = [
  { key: 'nit', label: 'NIT', type: 'text', required: true },
  { key: 'razonSocial', label: 'Razón Social', type: 'text', required: true },
  { key: 'sectorEconomico', label: 'Sector Económico', type: 'text' },
  { key: 'email', label: 'Email', type: 'email' },
  { key: 'telefono', label: 'Teléfono', type: 'text' },
  { key: 'direccion', label: 'Dirección', type: 'textarea' },
  { key: 'estadoId', label: 'Estado (ID)', type: 'number', required: true },
  { key: 'representanteLegalId', label: 'Representante Legal (ID usuario)', type: 'number' },
]

export default function EmpresasPage() {
  const { hasRole } = useAuth()
  const canDelete = hasRole('ANALISTA_INTERNO')

  return (
    <CrudTable<CliEmpresa>
      title="Empresas Cliente"
      apiPath="/empresas"
      columns={columns}
      fields={fields}
      canDelete={canDelete}
    />
  )
}
