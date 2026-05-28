import { ShieldCheck } from 'lucide-react'
import { PageHeader } from '../../components/common'
import OpSection from '../../components/OpSection'
import { opsApi } from '../../api/resources'
import type { Field } from '../../types'

const validar: Field[] = [
  { key: 'token', label: 'Token / hash sesión', type: 'text', required: true },
]

const revocar: Field[] = [
  { key: 'token', label: 'Token de la sesión a revocar', type: 'text', required: true },
]

export default function OpsSesionesPage() {
  return (
    <div>
      <PageHeader
        title="Operaciones · Sesiones"
        description="Validación y revocación manual de sesiones"
        icon={<ShieldCheck className="h-5 w-5" />}
      />
      <OpSection title="Validar sesión" fields={validar}
        description="Comprueba si un token de sesión sigue activo y vigente." onSubmit={opsApi.validarSesion} />
      <OpSection title="Revocar sesión" fields={revocar}
        description="Cierra una sesión activa identificada por su token." onSubmit={opsApi.revocarSesion} />
    </div>
  )
}
