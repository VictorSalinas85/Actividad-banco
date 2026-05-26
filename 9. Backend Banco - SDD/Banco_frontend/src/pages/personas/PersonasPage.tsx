import CrudTable from '../../components/CrudTable'
import type { Column, Field, CliPersonaNatural } from '../../types'
import { useAuth } from '../../auth/AuthContext'

const columns: Column<CliPersonaNatural>[] = [
  { key: 'id', header: 'ID' },
  { key: 'numeroIdentificacion', header: 'Identificación' },
  { key: 'primerNombre', header: 'Nombre' },
  { key: 'primerApellido', header: 'Apellido' },
  { key: 'email', header: 'Email' },
  { key: 'telefono', header: 'Teléfono' },
  { key: 'estadoId', header: 'Estado' },
]

const fields: Field[] = [
  { key: 'tipoIdentificacionId', label: 'Tipo Identificación (ID)', type: 'number', required: true },
  { key: 'numeroIdentificacion', label: 'Número de Identificación', type: 'text', required: true },
  { key: 'primerNombre', label: 'Primer Nombre', type: 'text', required: true },
  { key: 'segundoNombre', label: 'Segundo Nombre', type: 'text' },
  { key: 'primerApellido', label: 'Primer Apellido', type: 'text', required: true },
  { key: 'segundoApellido', label: 'Segundo Apellido', type: 'text' },
  { key: 'fechaNacimiento', label: 'Fecha de Nacimiento', type: 'date' },
  { key: 'email', label: 'Email', type: 'email' },
  { key: 'telefono', label: 'Teléfono', type: 'text' },
  { key: 'direccion', label: 'Dirección', type: 'textarea' },
  { key: 'estadoId', label: 'Estado (ID)', type: 'number', required: true },
]

export default function PersonasPage() {
  const { hasRole } = useAuth()
  const canDelete = hasRole('ANALISTA_INTERNO')

  return (
    <CrudTable<CliPersonaNatural>
      title="Personas Naturales"
      apiPath="/personas"
      columns={columns}
      fields={fields}
      canDelete={canDelete}
    />
  )
}
