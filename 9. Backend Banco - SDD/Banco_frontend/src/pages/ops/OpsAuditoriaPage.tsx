import { ClipboardList } from 'lucide-react'
import { PageHeader } from '../../components/common'
import OpSection from '../../components/OpSection'
import { opsApi } from '../../api/resources'
import type { Field } from '../../types'

const evento: Field[] = [
  { key: 'tipoEventoId',    label: 'Tipo evento (id)', type: 'number', required: true },
  { key: 'entidadAfectada', label: 'Entidad afectada', type: 'text' },
  { key: 'entidadId',       label: 'ID entidad',        type: 'number' },
  { key: 'descripcion',     label: 'Descripción',       type: 'textarea' },
  { key: 'ipOrigen',        label: 'IP origen',         type: 'text' },
]

const error: Field[] = [
  { key: 'tipoErrorId', label: 'Tipo error (id)', type: 'number', required: true },
  { key: 'operacion',   label: 'Operación',        type: 'text' },
  { key: 'descripcion', label: 'Descripción',      type: 'textarea' },
  { key: 'stackTrace',  label: 'Stack trace',      type: 'textarea' },
  { key: 'ipOrigen',    label: 'IP origen',        type: 'text' },
]

export default function OpsAuditoriaPage() {
  return (
    <div>
      <PageHeader
        title="Operaciones · Auditoría"
        description="Registro manual de eventos y errores para trazabilidad"
        icon={<ClipboardList className="h-5 w-5" />}
      />
      <OpSection title="Registrar evento" fields={evento} onSubmit={opsApi.registrarEvento} />
      <OpSection title="Registrar error"  fields={error}  onSubmit={opsApi.registrarError} />
    </div>
  )
}
