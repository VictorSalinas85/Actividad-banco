import { LineChart } from 'lucide-react'
import CrudTable from '../../components/CrudTable'
import { PageHeader, MoneyValue } from '../../components/common'
import type { Column, CtaMovimiento } from '../../types'
import { formatDateTime } from '../../lib/formatters'
import { useTiposMov } from '../../lib/useEntityList'

export default function MovimientosPage() {
  const tipos = useTiposMov()

  const columns: Column<CtaMovimiento>[] = [
    { key: 'id', header: 'ID' },
    { key: 'cuentaId', header: 'Cuenta' },
    { key: 'tipoMovimientoId', header: 'Tipo',
      render: (v) => {
        const t = tipos.data.find((x) => x.id === v)
        return <code className="text-xs bg-ink-100 px-1.5 py-0.5 rounded">{t?.codigo ?? `#${v}`}</code>
      } },
    { key: 'monto', header: 'Monto', render: (v) => <MoneyValue value={Number(v)} /> },
    { key: 'saldoAntes', header: 'Saldo anterior', render: (v) => <MoneyValue value={Number(v)} /> },
    { key: 'saldoDespues', header: 'Saldo nuevo', render: (v) => <MoneyValue value={Number(v)} /> },
    { key: 'referenciaExterna', header: 'Descripción', render: (v) => <span className="text-xs">{(v as string) ?? '—'}</span> },
    { key: 'fechaMovimiento', header: 'Fecha', render: (v) => <span className="text-xs">{formatDateTime(v as string)}</span> },
  ]

  return (
    <div>
      <PageHeader
        title="Movimientos de Cuenta"
        description="Historial de débitos y créditos por cuenta"
        icon={<LineChart className="h-5 w-5" />}
      />
      <CrudTable<CtaMovimiento>
        title=""
        apiPath="/cuentas/movimientos"
        columns={columns}
        fields={[]}
        canCreate={false}
        canEdit={false}
        canDelete={false}
        readOnly
      />
    </div>
  )
}
