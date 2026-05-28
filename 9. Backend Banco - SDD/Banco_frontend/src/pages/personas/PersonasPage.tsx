import { User } from 'lucide-react'
import CrudTable from '../../components/CrudTable'
import { PageHeader, StatusBadge } from '../../components/common'
import type { Column, Field, CliPersonaNatural } from '../../types'
import { useAuth } from '../../auth/AuthContext'
import { useTiposIdent, useEstadosUsuario } from '../../lib/useEntityList'
import { formatDate } from '../../lib/formatters'

export default function PersonasPage() {
  const { hasRole } = useAuth()
  const canDelete = hasRole('ANALISTA_INTERNO')
  const tipos     = useTiposIdent()
  const estados   = useEstadosUsuario()

  const columns: Column<CliPersonaNatural>[] = [
    { key: 'id', header: 'ID' },
    { key: 'tipoIdentificacionId', header: 'Tipo Ident.',
      render: (v) => {
        const t = tipos.data.find((x) => x.id === v)
        return <code className="text-xs bg-navy-50 px-1.5 py-0.5 rounded">{t?.codigo ?? `#${v}`}</code>
      } },
    { key: 'identificacion', header: 'Identificación',
      render: (v) => <span className="font-mono text-xs">{String(v ?? '')}</span> },
    { key: 'nombreCompleto', header: 'Nombre completo' },
    { key: 'email', header: 'Email' },
    { key: 'telefono', header: 'Teléfono' },
    { key: 'fechaNacimiento', header: 'F. Nacim.',
      render: (v) => v ? formatDate(String(v)) : '—' },
    { key: 'estadoId', header: 'Estado',
      render: (v) => {
        const e = estados.data.find((x) => x.id === v)
        return <StatusBadge label={e?.codigo ?? String(v)} status={e?.codigo ?? String(v)} />
      } },
  ]

  // Form de creación/edición — el ID es autogenerado por el backend.
  // El estado se omite (el SP/backend asigna ACTIVO por defecto).
  const fields: Field[] = [
    { key: 'tipoIdentificacionId', label: 'Tipo de identificación', type: 'select', required: true,
      options: tipos.data
        .filter((t) => !t.aplicaA || t.aplicaA === 'PERSONA' || t.aplicaA === 'AMBOS')
        .map((t) => ({ value: t.id, label: `${t.codigo} — ${t.nombre}` })) },
    { key: 'identificacion',  label: 'Número de identificación', type: 'text', required: true },
    { key: 'nombreCompleto',  label: 'Nombre completo',          type: 'text', required: true },
    { key: 'fechaNacimiento', label: 'Fecha de nacimiento',      type: 'date' },
    { key: 'email',           label: 'Email',                    type: 'email' },
    { key: 'telefono',        label: 'Teléfono',                 type: 'text' },
    { key: 'direccion',       label: 'Dirección',                type: 'textarea' },
  ]

  return (
    <div>
      <PageHeader
        title="Personas Naturales"
        description="Clientes persona natural registrados en el banco"
        icon={<User className="h-5 w-5" />}
      />
      <CrudTable<CliPersonaNatural>
        title=""
        apiPath="/personas"
        columns={columns}
        fields={fields}
        canDelete={canDelete}
      />
    </div>
  )
}
