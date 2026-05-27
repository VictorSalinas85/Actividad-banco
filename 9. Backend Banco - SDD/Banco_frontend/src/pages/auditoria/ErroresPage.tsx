import { AlertTriangle } from 'lucide-react'
import CrudTable from '../../components/CrudTable'
import { PageHeader, IdentificationDisplay } from '../../components/common'
import type { Column, AudErrorOperacion } from '../../types'
import { formatDateTime } from '../../lib/formatters'

const columns: Column<AudErrorOperacion>[] = [
  { key: 'id', header: 'ID' },
  { key: 'tipoErrorId', header: 'Tipo' },
  { key: 'usuarioId', header: 'Usuario',
    render: (v) => v ? <IdentificationDisplay kind="usuario" id={v as number} compact /> : <span className="text-slate-400">—</span> },
  { key: 'operacion', header: 'Operación' },
  { key: 'descripcion', header: 'Descripción' },
  { key: 'ipOrigen', header: 'IP' },
  { key: 'creadoEn', header: 'Fecha',
    render: (v) => <span className="text-xs">{formatDateTime(v as string)}</span> },
]

export default function ErroresPage() {
  return (
    <div>
      <PageHeader
        title="Errores de Operación"
        description="Errores capturados por la capa de aplicación y SPs"
        icon={<AlertTriangle className="h-5 w-5" />}
      />
      <CrudTable<AudErrorOperacion>
        title=""
        apiPath="/auditoria/errores"
        columns={columns}
        fields={[]}
        readOnly
      />
    </div>
  )
}
