import { Package } from 'lucide-react'
import CrudTable from '../../components/CrudTable'
import { PageHeader } from '../../components/common'
import type { Column, Field, PrdProductoBancario } from '../../types'
import { useAuth } from '../../auth/AuthContext'
import { ROLES } from '../../lib/constants'

const CATEGORIA_OPTS = [
  { value: 'CUENTAS',   label: 'CUENTAS — Productos de depósito y cuentas' },
  { value: 'PRESTAMOS', label: 'PRESTAMOS — Productos de crédito' },
  { value: 'SERVICIOS', label: 'SERVICIOS — Servicios bancarios complementarios' },
]

const CATEGORIA_BADGE: Record<string, string> = {
  CUENTAS:   'bg-emerald-100 text-emerald-800',
  PRESTAMOS: 'bg-gold-100 text-gold-800',
  SERVICIOS: 'bg-navy-100 text-navy-800',
}

export default function ProductosPage() {
  const { hasRole } = useAuth()
  const canEdit = hasRole(ROLES.ADMIN)

  const columns: Column<PrdProductoBancario>[] = [
    { key: 'id', header: 'ID' },
    { key: 'codigoProducto', header: 'Código',
      render: (v) => <code className="text-xs bg-navy-50 px-1.5 py-0.5 rounded font-mono">{String(v ?? '')}</code> },
    { key: 'nombreProducto', header: 'Nombre' },
    { key: 'categoria', header: 'Categoría',
      render: (v) => (
        <span className={`badge ${CATEGORIA_BADGE[String(v)] ?? 'badge-muted'}`}>{String(v ?? '')}</span>
      ) },
    { key: 'requiereAprobacion', header: 'Aprobación',
      render: (v) => v ? <span className="badge badge-warn">Sí</span> : <span className="badge badge-muted">No</span> },
    { key: 'activo', header: 'Activo',
      render: (v) => v ? <span className="badge badge-success">Sí</span> : <span className="badge badge-danger">No</span> },
  ]

  const fields: Field[] = [
    { key: 'codigoProducto', label: 'Código del producto', type: 'text', required: true,
      help: 'Identificador único corto (ej: CTA_AHORROS_BAS)' },
    { key: 'nombreProducto', label: 'Nombre del producto', type: 'text', required: true },
    { key: 'categoria',      label: 'Categoría', type: 'select', required: true, options: CATEGORIA_OPTS },
    { key: 'requiereAprobacion', label: 'Requiere aprobación', type: 'checkbox' },
    { key: 'activo',         label: 'Activo', type: 'checkbox' },
  ]

  return (
    <div>
      <PageHeader
        title="Productos Bancarios"
        description="Catálogo de productos disponibles para clientes (cuentas, préstamos y servicios)"
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
