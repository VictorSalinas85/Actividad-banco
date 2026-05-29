import { Wallet } from 'lucide-react'
import CrudTable from '../../components/CrudTable'
import { PageHeader, MoneyValue } from '../../components/common'
import type { Column, Field, CrePrestamoDesembolso } from '../../types'
import { formatDateTime } from '../../lib/formatters'

const columns: Column<CrePrestamoDesembolso>[] = [
  { key: 'id', header: 'ID' },
  { key: 'prestamoId', header: 'Préstamo' },
  { key: 'cuentaDesembolsoId', header: 'Cuenta destino' },
  { key: 'montoPrincipal',  header: 'Principal',  render: (v) => <MoneyValue value={Number(v)} /> },
  { key: 'montoIntereses',  header: 'Intereses',  render: (v) => <MoneyValue value={Number(v)} /> },
  { key: 'montoCargos',     header: 'Cargos',     render: (v) => <MoneyValue value={Number(v)} /> },
  { key: 'fechaDesembolso', header: 'Fecha',
    render: (v) => <span className="text-xs">{formatDateTime(v as string)}</span> },
]

const fields: Field[] = [
  { key: 'prestamoId',         label: 'ID del préstamo', type: 'number', required: true },
  { key: 'cuentaDesembolsoId', label: 'ID cuenta destino', type: 'number', required: true },
  { key: 'montoPrincipal',     label: 'Monto principal', type: 'money', required: true },
  { key: 'montoIntereses',     label: 'Monto intereses', type: 'money', required: true },
  { key: 'montoCargos',        label: 'Monto cargos',    type: 'money', required: true },
]

export default function PrestamoDesembolsosPage() {
  return (
    <div>
      <PageHeader
        title="Desembolsos de Préstamos"
        description="Registro de desembolsos efectivamente realizados"
        icon={<Wallet className="h-5 w-5" />}
      />
      <CrudTable<CrePrestamoDesembolso>
        title=""
        apiPath="/prestamos/desembolsos"
        columns={columns}
        fields={fields}
      />
    </div>
  )
}
