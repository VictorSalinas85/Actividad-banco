import { History } from 'lucide-react'
import CrudTable from '../../components/CrudTable'
import { PageHeader } from '../../components/common'
import type { Column, AudCambioDato } from '../../types'

const columns: Column<AudCambioDato>[] = [
  { key: 'id', header: 'ID' },
  { key: 'bitacoraId', header: 'Bitácora' },
  { key: 'tabla', header: 'Tabla' },
  { key: 'columna', header: 'Columna' },
  { key: 'valorAnterior', header: 'Valor anterior',
    render: (v) => <span className="text-xs font-mono text-red-600">{String(v ?? '')}</span> },
  { key: 'valorNuevo', header: 'Valor nuevo',
    render: (v) => <span className="text-xs font-mono text-green-700">{String(v ?? '')}</span> },
]

export default function CambiosPage() {
  return (
    <div>
      <PageHeader
        title="Cambios de Datos"
        description="Trazabilidad fila por fila de modificaciones"
        icon={<History className="h-5 w-5" />}
      />
      <CrudTable<AudCambioDato>
        title=""
        apiPath="/auditoria/cambios"
        columns={columns}
        fields={[]}
        readOnly
      />
    </div>
  )
}
