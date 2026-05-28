import { Building2 } from 'lucide-react'
import CrudTable from '../../components/CrudTable'
import { PageHeader, StatusBadge, IdentificationDisplay } from '../../components/common'
import type { Column, Field, CliEmpresa } from '../../types'
import { useAuth } from '../../auth/AuthContext'
import { useTiposIdent, useEstadosUsuario } from '../../lib/useEntityList'

export default function EmpresasPage() {
  const { hasRole } = useAuth()
  const canDelete = hasRole('ANALISTA_INTERNO')
  const tipos     = useTiposIdent()
  const estados   = useEstadosUsuario()

  const columns: Column<CliEmpresa>[] = [
    { key: 'id', header: 'ID' },
    { key: 'tipoIdentificacionId', header: 'Tipo',
      render: (v) => {
        const t = tipos.data.find((x) => x.id === v)
        return <code className="text-xs bg-navy-50 px-1.5 py-0.5 rounded">{t?.codigo ?? `#${v}`}</code>
      } },
    { key: 'nit', header: 'NIT',
      render: (v) => <span className="font-mono text-xs">{String(v ?? '')}</span> },
    { key: 'razonSocial', header: 'Razón social' },
    { key: 'representantePersonaId', header: 'Representante',
      render: (v) => v ? <IdentificationDisplay kind="persona" id={Number(v)} compact /> : '—' },
    { key: 'email', header: 'Email' },
    { key: 'telefono', header: 'Teléfono' },
    { key: 'estadoId', header: 'Estado',
      render: (v) => {
        const e = estados.data.find((x) => x.id === v)
        return <StatusBadge label={e?.codigo ?? String(v)} status={e?.codigo ?? String(v)} />
      } },
  ]

  const fields: Field[] = [
    { key: 'tipoIdentificacionId', label: 'Tipo de identificación', type: 'select', required: true,
      options: tipos.data
        .filter((t) => !t.aplicaA || t.aplicaA === 'EMPRESA' || t.aplicaA === 'AMBOS')
        .map((t) => ({ value: t.id, label: `${t.codigo} — ${t.nombre}` })) },
    { key: 'nit',          label: 'NIT', type: 'text', required: true },
    { key: 'razonSocial',  label: 'Razón social', type: 'text', required: true },
    { key: 'representantePersonaId', label: 'Representante legal', type: 'person-picker', required: true,
      help: 'Persona natural ya registrada' },
    { key: 'email',        label: 'Email', type: 'email' },
    { key: 'telefono',     label: 'Teléfono', type: 'text' },
    { key: 'direccion',    label: 'Dirección', type: 'textarea' },
  ]

  return (
    <div>
      <PageHeader
        title="Empresas Cliente"
        description="Personas jurídicas registradas como clientes del banco"
        icon={<Building2 className="h-5 w-5" />}
      />
      <CrudTable<CliEmpresa>
        title=""
        apiPath="/empresas"
        columns={columns}
        fields={fields}
        canDelete={canDelete}
      />
    </div>
  )
}
