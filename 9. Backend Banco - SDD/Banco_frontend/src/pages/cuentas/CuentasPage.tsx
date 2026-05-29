import { CreditCard } from 'lucide-react'
import CrudTable from '../../components/CrudTable'
import { PageHeader, StatusBadge, MoneyValue, ParticipanteDisplay } from '../../components/common'
import type { Column, Field, CtaCuenta } from '../../types'
import { useAuth } from '../../auth/AuthContext'
import { ROLES } from '../../lib/constants'
import { useEstadosCuenta, useTiposCuenta, useMonedas } from '../../lib/useEntityList'
import { cuentasApi } from '../../api/resources'

export default function CuentasPage() {
  const { hasRole } = useAuth()
  const canDelete = hasRole(ROLES.ADMIN)
  const tipos    = useTiposCuenta()
  const monedas  = useMonedas()
  const estados  = useEstadosCuenta()

  const columns: Column<CtaCuenta>[] = [
    { key: 'id', header: 'ID' },
    { key: 'numeroCuenta', header: 'Número',
      render: (v) => <span className="font-mono text-xs">{String(v)}</span> },
    { key: 'tipoCuentaId', header: 'Tipo',
      render: (v) => {
        const t = tipos.data.find((x) => x.id === v)
        return <code className="text-xs bg-navy-50 px-1.5 py-0.5 rounded">{t?.codigo ?? `#${v}`}</code>
      } },
    { key: 'titularPersonaId', header: 'Titular',
      render: (_v, row) => <ParticipanteDisplay personaId={row.titularPersonaId} empresaId={row.titularEmpresaId} compact /> },
    { key: 'monedaId', header: 'Moneda',
      render: (v) => {
        const m = monedas.data.find((x) => x.id === v)
        return <span className="text-xs">{m?.codigo ?? `#${v}`}</span>
      } },
    { key: 'saldoActual', header: 'Saldo',
      render: (v) => <MoneyValue value={Number(v)} /> },
    { key: 'estadoId', header: 'Estado',
      render: (v) => {
        const e = estados.data.find((x) => x.id === v)
        return <StatusBadge label={e?.codigo ?? `Estado ${v}`} status={e?.codigo ?? String(v)} />
      } },
  ]

  // CRUD usa IDs (no códigos). Para crear cuentas en producción, usar
  // mejor "Operaciones → Cuentas → Abrir cuenta" (SP con validaciones de negocio).
  // El número de cuenta lo asigna el sistema automáticamente (consecutivo anual
  // AAAA0000000, p.ej. 20260000000); se muestra como solo-lectura y no se edita.
  const fields: Field[] = [
    { key: 'numeroCuenta', label: 'Número de cuenta (asignado por el sistema)', type: 'text',
      readOnly: true, help: 'Se asigna automáticamente al crear la cuenta.' },
    { key: 'tipoCuentaId', label: 'Tipo de cuenta', type: 'select', required: true,
      options: tipos.data.map((t) => ({ value: t.id, label: `${t.codigo} — ${t.nombre}` })) },
    { key: 'monedaId', label: 'Moneda', type: 'select', required: true,
      options: monedas.data.map((m) => ({ value: m.id, label: `${m.codigo} — ${m.nombre}` })) },
    { key: 'titularPersonaId', label: 'Titular persona (uno u otro)', type: 'person-picker' },
    { key: 'titularEmpresaId', label: 'Titular empresa (uno u otro)', type: 'empresa-picker' },
    { key: 'saldoActual',  label: 'Saldo inicial', type: 'money' },
    { key: 'limiteSobregirosAutorizado', label: 'Límite sobregiro', type: 'money' },
  ]

  return (
    <div>
      <PageHeader
        title="Cuentas Bancarias"
        description="Cuentas activas, bloqueadas y canceladas. Para abrir cuentas con validaciones, usar Operaciones · Cuentas."
        icon={<CreditCard className="h-5 w-5" />}
      />
      <CrudTable<CtaCuenta>
        title=""
        apiPath="/cuentas"
        columns={columns}
        fields={fields}
        canDelete={canDelete}
        onOpenCreate={async () => {
          const res = await cuentasApi.proximoNumero()
          return { numeroCuenta: res.data.data ?? '' }
        }}
      />
    </div>
  )
}
