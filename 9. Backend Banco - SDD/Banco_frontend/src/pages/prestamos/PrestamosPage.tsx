import { Banknote } from 'lucide-react'
import CrudTable from '../../components/CrudTable'
import { PageHeader, StatusBadge, MoneyValue, ParticipanteDisplay } from '../../components/common'
import type { Column, Field, CrePrestamo } from '../../types'
import { useAuth } from '../../auth/AuthContext'
import { ROLES } from '../../lib/constants'
import { useEstadosPrest, useTiposPrest } from '../../lib/useEntityList'

export default function PrestamosPage() {
  const { hasRole } = useAuth()
  const canDelete = hasRole(ROLES.ADMIN)
  const tipos   = useTiposPrest()
  const estados = useEstadosPrest()

  const columns: Column<CrePrestamo>[] = [
    { key: 'id', header: 'ID' },
    { key: 'clientePersonaId', header: 'Cliente',
      render: (_v, row) => <ParticipanteDisplay personaId={row.clientePersonaId} empresaId={row.clienteEmpresaId} compact /> },
    { key: 'tipoCreditoId', header: 'Tipo',
      render: (v) => {
        const t = tipos.data.find((x) => x.id === v)
        return <code className="text-xs bg-ink-100 px-1.5 py-0.5 rounded">{t?.codigo ?? `#${v}`}</code>
      } },
    { key: 'montoSolicitado', header: 'Monto', render: (v) => <MoneyValue value={Number(v)} /> },
    { key: 'tasaInteres', header: 'Tasa %',
      render: (v) => <span className="font-mono text-xs">{Number(v).toFixed(2)}</span> },
    { key: 'plazoMeses', header: 'Plazo (m)' },
    { key: 'cuotaMensual', header: 'Cuota', render: (v) => <MoneyValue value={Number(v ?? 0)} /> },
    { key: 'estadoId', header: 'Estado',
      render: (v) => {
        const e = estados.data.find((x) => x.id === v)
        return <StatusBadge label={e?.codigo ?? `Estado ${v}`} status={e?.codigo ?? String(v)} />
      } },
  ]

  const fields: Field[] = [
    { key: 'clientePersonaId', label: 'Cliente persona (opcional)', type: 'person-picker' },
    { key: 'clienteEmpresaId', label: 'Cliente empresa (opcional)', type: 'empresa-picker' },
    { key: 'tipoCreditoId', label: 'Tipo de crédito', type: 'select', required: true,
      options: tipos.data.map((t) => ({ value: t.id, label: `${t.codigo} — ${t.nombre}` })) },
    { key: 'montoSolicitado', label: 'Monto solicitado', type: 'number', required: true },
    { key: 'tasaInteres',     label: 'Tasa interés (%)', type: 'number', required: true },
    { key: 'plazoMeses',      label: 'Plazo (meses)',    type: 'number', required: true },
    { key: 'proposito',       label: 'Propósito',         type: 'textarea' },
    { key: 'estadoId',        label: 'Estado', type: 'select', required: true,
      options: estados.data.map((e) => ({ value: e.id, label: `${e.codigo ?? ''} — ${e.nombre ?? ''}` })) },
  ]

  return (
    <div>
      <PageHeader
        title="Préstamos"
        description="Solicitudes de crédito y su ciclo de aprobación"
        icon={<Banknote className="h-5 w-5" />}
      />
      <CrudTable<CrePrestamo>
        title=""
        apiPath="/prestamos"
        columns={columns}
        fields={fields}
        canDelete={canDelete}
      />
    </div>
  )
}
