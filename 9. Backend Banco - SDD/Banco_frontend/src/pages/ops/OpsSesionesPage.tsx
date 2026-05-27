import { ShieldCheck } from 'lucide-react'
import { PageHeader } from '../../components/common'
import OpSection from '../../components/OpSection'
import { opsApi } from '../../api/resources'
import type { Field } from '../../types'

const validar: Field[] = [
  { key: 'token',   label: 'Token / hash sesión', type: 'text', required: true },
  { key: 'ipOrigen', label: 'IP origen', type: 'text' },
]

const revocar: Field[] = [
  { key: 'sesionId', label: 'ID sesión', type: 'number', required: true },
  { key: 'motivo',   label: 'Motivo',     type: 'textarea' },
]

export default function OpsSesionesPage() {
  return (
    <div>
      <PageHeader
        title="Operaciones · Sesiones"
        description="Validación y revocación manual de sesiones"
        icon={<ShieldCheck className="h-5 w-5" />}
      />
      <OpSection title="Validar sesión" fields={validar} onSubmit={opsApi.validarSesion} />
      <OpSection title="Revocar sesión" fields={revocar} onSubmit={opsApi.revocarSesion} />
    </div>
  )
}
