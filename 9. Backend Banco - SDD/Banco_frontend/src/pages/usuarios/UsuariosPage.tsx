import CrudTable from '../../components/CrudTable'
import type { Column, Field, SecUsuario } from '../../types'

const columns: Column<SecUsuario>[] = [
  { key: 'id', header: 'ID' },
  { key: 'username', header: 'Usuario' },
  { key: 'nombreCompleto', header: 'Nombre completo' },
  { key: 'email', header: 'Email' },
  { key: 'telefono', header: 'Teléfono' },
  { key: 'estadoId', header: 'Estado' },
  { key: 'ultimoLoginAt', header: 'Último login' },
]

const fields: Field[] = [
  { key: 'username', label: 'Usuario', type: 'text', required: true },
  { key: 'password', label: 'Contraseña', type: 'text' },
  { key: 'nombreCompleto', label: 'Nombre completo', type: 'text', required: true },
  { key: 'email', label: 'Email', type: 'email', required: true },
  { key: 'telefono', label: 'Teléfono', type: 'text' },
  { key: 'estadoId', label: 'Estado (ID)', type: 'number', required: true },
]

export default function UsuariosPage() {
  return (
    <CrudTable<SecUsuario>
      title="Usuarios del Sistema"
      apiPath="/usuarios"
      columns={columns}
      fields={fields}
    />
  )
}
