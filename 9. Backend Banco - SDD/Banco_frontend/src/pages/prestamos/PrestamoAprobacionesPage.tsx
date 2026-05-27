import { CheckCircle2 } from 'lucide-react'
import CrudTable from '../../components/CrudTable'
import { PageHeader, IdentificationDisplay } from '../../components/common'
import type { Column, Field, CrePrestamoAprobacion } from '../../types'
import { formatDateTime } from '../../lib/formatters'

const columns: Column<CrePrestamoAprobacion>[] = [
  { key: 'id', header: 'ID' },
  { key: 'prestamoId', header: 'Préstamo' },
  { key: 'aprobadorId', header: 'Aprobador',
    render: (v) => <IdentificationDisplay kind="usuario" id={v as number} compact /> },
  { key: 'decision', header: 'Decisión' },
  { key: 'observaciones', header: 'Observaciones' },
  { key: 'fechaDecision', header: 'Fecha',
    render: (v) => <span className="text-xs">{formatDateTime(v as string)}</span> },
]

const fields: Field[] = [
  { key: 'prestamoId',    label: 'ID del préstamo', type: 'number', required: true },
  { key: 'aprobadorId',   label: 'Aprobador', type: 'usuario-picker', required: true },
  { key: 'decision',      label: 'Decisión (APROBADO/RECHAZADO)', type: 'text', required: true },
  { key: 'observaciones', label: 'Observaciones', type: 'textarea' },
]

export default function PrestamoAprobacionesPage() {
  return (
    <div>
      <PageHeader
        title="Aprobaciones de Préstamos"
        description="Decisiones registradas en el flujo de aprobación crediticia"
        icon={<CheckCircle2 className="h-5 w-5" />}
      />
      <CrudTable<CrePrestamoAprobacion>
        title=""
        apiPath="/prestamos/aprobaciones"
        columns={columns}
        fields={fields}
      />
    </div>
  )
}
