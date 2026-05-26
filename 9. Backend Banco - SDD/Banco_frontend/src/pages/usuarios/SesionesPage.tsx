import CrudTable from '../../components/CrudTable'
import type { Column, SecSesion } from '../../types'

const columns: Column<SecSesion>[] = [
  { key: 'id', header: 'ID' },
  { key: 'usuarioId', header: 'Usuario ID' },
  { key: 'ipOrigen', header: 'IP Origen' },
  { key: 'estadoId', header: 'Estado' },
  { key: 'creadaEn', header: 'Creada' },
  { key: 'expiraEn', header: 'Expira' },
  { key: 'cerradaEn', header: 'Cerrada' },
]

export default function SesionesPage() {
  return (
    <CrudTable<SecSesion>
      title="Sesiones Activas"
      apiPath="/usuarios/sesiones"
      columns={columns}
      fields={[]}
      canCreate={false}
      canEdit={false}
      canDelete={false}
      readOnly
    />
  )
}
