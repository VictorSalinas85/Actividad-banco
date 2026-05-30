import { ArrowLeftRight } from 'lucide-react'
import CrudTable from '../../components/CrudTable'
import { PageHeader, StatusBadge, MoneyValue, IdentificationDisplay } from '../../components/common'
import type { Column, Field, TrfTransferencia } from '../../types'
import { useAuth } from '../../auth/AuthContext'
import { ROLES } from '../../lib/constants'
import { useEstadosTrans, useTiposTrans, useMonedas } from '../../lib/useEntityList'

export default function TransferenciasPage() {
  const { hasRole } = useAuth()
  const canEdit = hasRole(ROLES.ADMIN, ROLES.SUPERVISOR_EMPRESA)
  const canDelete = hasRole(ROLES.ADMIN)
  const tipos    = useTiposTrans()
  const monedas  = useMonedas()
  const estados  = useEstadosTrans()

  const columns: Column<TrfTransferencia>[] = [
    { key: 'id', header: 'ID' },
    { key: 'cuentaOrigenId',  header: 'Origen' },
    { key: 'cuentaDestinoId', header: 'Destino' },
    { key: 'tipoTransferenciaId', header: 'Tipo',
      render: (v) => {
        const t = tipos.data.find((x) => x.id === v)
        return <code className="text-xs bg-ink-100 px-1.5 py-0.5 rounded">{t?.codigo ?? `#${v}`}</code>
      } },
    { key: 'monto', header: 'Monto', render: (v) => <MoneyValue value={Number(v)} /> },
    { key: 'monedaId', header: 'Moneda',
      render: (v) => {
        const m = monedas.data.find((x) => x.id === v)
        return <span className="text-xs">{m?.codigo ?? `#${v}`}</span>
      } },
    { key: 'usuarioSolicitanteId', header: 'Solicitante',
      render: (v) => <IdentificationDisplay kind="usuario" id={v as number} compact /> },
    { key: 'estadoId', header: 'Estado',
      render: (v) => {
        const e = estados.data.find((x) => x.id === v)
        return <StatusBadge label={e?.codigo ?? `Estado ${v}`} status={e?.codigo ?? String(v)} />
      } },
  ]

  const fields: Field[] = [
    { key: 'cuentaOrigenId',  label: 'ID cuenta origen',  type: 'number', required: true },
    { key: 'cuentaDestinoId', label: 'ID cuenta destino', type: 'number', required: true },
    { key: 'tipoTransferenciaId', label: 'Tipo', type: 'select', required: true,
      options: tipos.data.map((t) => ({ value: t.id, label: `${t.codigo} — ${t.nombre}` })) },
    { key: 'monto',    label: 'Monto', type: 'money', required: true },
    { key: 'monedaId', label: 'Moneda', type: 'select', required: true,
      options: monedas.data.map((m) => ({ value: m.id, label: `${m.codigo} — ${m.nombre}` })) },
    { key: 'usuarioSolicitanteId', label: 'Usuario solicitante', type: 'usuario-picker', required: true },
    { key: 'descripcion', label: 'Descripción', type: 'textarea' },
    { key: 'estadoId', label: 'Estado', type: 'select', required: true,
      options: estados.data.map((e) => ({ value: e.id, label: `${e.codigo ?? ''} — ${e.nombre ?? ''}` })) },
  ]

  return (
    <div>
      <PageHeader
        title="Transferencias"
        description="Movimientos entre cuentas y flujo de aprobaciones"
        icon={<ArrowLeftRight className="h-5 w-5" />}
      />
      <CrudTable<TrfTransferencia>
        title=""
        apiPath="/transferencias"
        columns={columns}
        fields={fields}
        canEdit={canEdit}
        canDelete={canDelete}
      />
    </div>
  )
}
