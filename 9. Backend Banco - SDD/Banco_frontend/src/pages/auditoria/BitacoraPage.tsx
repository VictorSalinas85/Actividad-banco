import { FileText } from 'lucide-react'
import CrudTable from '../../components/CrudTable'
import { PageHeader, IdentificationDisplay } from '../../components/common'
import type { Column, AudBitacoraEvento } from '../../types'
import { formatDateTime } from '../../lib/formatters'

const columns: Column<AudBitacoraEvento>[] = [
  { key: 'id', header: 'ID' },
  { key: 'tipoEventoId', header: 'Tipo evento' },
  { key: 'usuarioId', header: 'Usuario',
    render: (v) => v ? <IdentificationDisplay kind="usuario" id={v as number} compact /> : <span className="text-slate-400">—</span> },
  { key: 'entidadAfectada', header: 'Entidad' },
  { key: 'entidadId', header: 'ID entidad' },
  { key: 'descripcion', header: 'Descripción' },
  { key: 'ipOrigen', header: 'IP' },
  { key: 'creadoEn', header: 'Fecha',
    render: (v) => <span className="text-xs">{formatDateTime(v as string)}</span> },
]

export default function BitacoraPage() {
  return (
    <div>
      <PageHeader
        title="Bitácora de Eventos"
        description="Registro auditable de operaciones del sistema"
        icon={<FileText className="h-5 w-5" />}
      />
      <CrudTable<AudBitacoraEvento>
        title=""
        apiPath="/auditoria/bitacora"
        columns={columns}
        fields={[]}
        readOnly
      />
    </div>
  )
}
