import { CheckCircle2 } from 'lucide-react'
import CrudTable from '../../components/CrudTable'
import { PageHeader, IdentificationDisplay } from '../../components/common'
import type { Column, Field, TrfTransferenciaAprobacion } from '../../types'
import { formatDateTime } from '../../lib/formatters'

const columns: Column<TrfTransferenciaAprobacion>[] = [
  { key: 'id', header: 'ID' },
  { key: 'transferenciaId', header: 'Transferencia' },
  { key: 'aprobadorId', header: 'Aprobador',
    render: (v) => <IdentificationDisplay kind="usuario" id={v as number} compact /> },
  { key: 'decision', header: 'Decisión' },
  { key: 'observaciones', header: 'Observaciones' },
  { key: 'fechaDecision', header: 'Fecha',
    render: (v) => <span className="text-xs">{formatDateTime(v as string)}</span> },
]

const fields: Field[] = [
  { key: 'transferenciaId', label: 'ID transferencia', type: 'number', required: true },
  { key: 'aprobadorId',     label: 'Aprobador', type: 'usuario-picker', required: true },
  { key: 'decision',        label: 'Decisión (APROBADO/RECHAZADO)', type: 'text', required: true },
  { key: 'observaciones',   label: 'Observaciones', type: 'textarea' },
]

export default function TransferenciaAprobacionesPage() {
  return (
    <div>
      <PageHeader
        title="Aprobaciones de Transferencias"
        description="Histórico de decisiones sobre transferencias pendientes"
        icon={<CheckCircle2 className="h-5 w-5" />}
      />
      <CrudTable<TrfTransferenciaAprobacion>
        title=""
        apiPath="/transferencias/aprobaciones"
        columns={columns}
        fields={fields}
      />
    </div>
  )
}
