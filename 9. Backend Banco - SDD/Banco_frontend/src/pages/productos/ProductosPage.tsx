import { Package } from 'lucide-react'
import CrudTable from '../../components/CrudTable'
import { PageHeader, MoneyValue } from '../../components/common'
import type { Column, Field, PrdProductoBancario } from '../../types'
import { useAuth } from '../../auth/AuthContext'
import { ROLES } from '../../lib/constants'
import { useTiposCuenta } from '../../lib/useEntityList'

export default function ProductosPage() {
  const { hasRole } = useAuth()
  const canEdit = hasRole(ROLES.ADMIN)
  const tipos = useTiposCuenta()

  const columns: Column<PrdProductoBancario>[] = [
    { key: 'id', header: 'ID' },
    { key: 'nombre', header: 'Nombre' },
    { key: 'descripcion', header: 'Descripción' },
    { key: 'tipoCuentaId', header: 'Tipo cuenta',
      render: (v) => {
        const t = tipos.data.find((x) => x.id === v)
        return t ? <code className="text-xs bg-ink-100 px-1.5 py-0.5 rounded">{t.codigo}</code> : <span className="text-slate-400">—</span>
      } },
    { key: 'tasaInteres', header: 'Tasa %',
      render: (v) => v != null ? <span className="font-mono text-xs">{Number(v).toFixed(2)}</span> : '—' },
    { key: 'saldoMinimo', header: 'Saldo mín.',
      render: (v) => v != null ? <MoneyValue value={Number(v)} /> : '—' },
    { key: 'activo', header: 'Activo',
      render: (v) => (
        <span className={`badge ${v ? 'badge-success' : 'badge-muted'}`}>{v ? 'Sí' : 'No'}</span>
      ) },
  ]

  const fields: Field[] = [
    { key: 'nombre', label: 'Nombre', type: 'text', required: true },
    { key: 'descripcion', label: 'Descripción', type: 'textarea' },
    { key: 'tipoCuentaId', label: 'Tipo de cuenta asociado', type: 'select',
      options: tipos.data.map((t) => ({ value: t.id, label: `${t.codigo} — ${t.nombre}` })) },
    { key: 'tasaInteres', label: 'Tasa interés (%)', type: 'number' },
    { key: 'comisionMantenimiento', label: 'Comisión mantenimiento', type: 'number' },
    { key: 'saldoMinimo', label: 'Saldo mínimo', type: 'number' },
    { key: 'requiereAprobacion', label: 'Requiere aprobación', type: 'checkbox' },
    { key: 'activo', label: 'Activo', type: 'checkbox' },
  ]

  return (
    <div>
      <PageHeader
        title="Productos Bancarios"
        description="Catálogo de productos disponibles para clientes"
        icon={<Package className="h-5 w-5" />}
      />
      <CrudTable<PrdProductoBancario>
        title=""
        apiPath="/productos"
        columns={columns}
        fields={fields}
        canCreate={canEdit}
        canEdit={canEdit}
        canDelete={canEdit}
      />
    </div>
  )
}
