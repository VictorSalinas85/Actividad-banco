import { ArrowLeftRight } from 'lucide-react'
import { PageHeader } from '../../components/common'
import OpSection from '../../components/OpSection'
import { opsApi } from '../../api/resources'
import { useAuth } from '../../auth/AuthContext'
import { useCanalesOp, useMotivosRechazo } from '../../lib/useEntityList'
import type { Field } from '../../types'

export default function OpsTransferenciasPage() {
  const { hasRole } = useAuth()
  const canales  = useCanalesOp()
  const motivos  = useMotivosRechazo()
  const puedeCrear   = hasRole('ANALISTA_INTERNO', 'EMPLEADO_VENTANILLA', 'CLIENTE_PERSONA', 'CLIENTE_EMPRESA_ADMIN', 'EMPLEADO_EMPRESA_OPERATIVO')
  const puedeDecidir = hasRole('ANALISTA_INTERNO', 'SUPERVISOR_EMPRESA')
  const puedeDirecta = hasRole('ANALISTA_INTERNO', 'EMPLEADO_VENTANILLA', 'CLIENTE_PERSONA')
  const puedeVencer  = hasRole('ANALISTA_INTERNO')
  const puedePendientes = hasRole('ANALISTA_INTERNO', 'SUPERVISOR_EMPRESA', 'CLIENTE_EMPRESA_ADMIN')

  const crear: Field[] = [
    { key: 'cuentaOrigenId',  label: 'Cuenta origen',  type: 'cuenta-picker', required: true },
    { key: 'cuentaDestinoId', label: 'Cuenta destino', type: 'cuenta-picker', required: true },
    { key: 'empresaId',       label: 'Empresa (si aplica)', type: 'empresa-picker',
      help: 'Solo si la transferencia es a nombre de una empresa cliente' },
    { key: 'monto',           label: 'Monto', type: 'money', required: true },
    { key: 'canalCodigo',     label: 'Canal', type: 'select', required: true,
      options: canales.data.map((c) => ({ value: c.codigo ?? '', label: `${c.codigo ?? ''} — ${c.nombre ?? ''}` })) },
    { key: 'idempotencyKey',  label: 'Clave de idempotencia (opcional)', type: 'text' },
  ]

  const aprobar: Field[] = [
    { key: 'transferenciaId', label: 'ID transferencia', type: 'number', required: true },
  ]

  const rechazar: Field[] = [
    { key: 'transferenciaId',     label: 'ID transferencia', type: 'number', required: true },
    { key: 'motivoRechazoCodigo', label: 'Motivo de rechazo', type: 'select', required: true,
      options: motivos.data.map((m) => ({ value: m.codigo ?? '', label: `${m.codigo ?? ''} — ${m.nombre ?? ''}` })) },
    { key: 'comentario',          label: 'Comentario', type: 'textarea' },
  ]

  const directa: Field[] = [
    { key: 'cuentaOrigenId',  label: 'Cuenta origen',  type: 'cuenta-picker', required: true },
    { key: 'cuentaDestinoId', label: 'Cuenta destino', type: 'cuenta-picker', required: true },
    { key: 'monto',           label: 'Monto', type: 'money', required: true },
    { key: 'canalCodigo',     label: 'Canal', type: 'select', required: true,
      options: canales.data.map((c) => ({ value: c.codigo ?? '', label: `${c.codigo ?? ''} — ${c.nombre ?? ''}` })) },
    { key: 'idempotencyKey',  label: 'Clave de idempotencia (opcional)', type: 'text' },
  ]

  const vencer: Field[] = []

  const pendientes: Field[] = [
    { key: 'empresaId', label: 'Empresa', type: 'empresa-picker', required: true },
  ]

  return (
    <div>
      <PageHeader
        title="Operaciones · Transferencias"
        description="Crear, aprobar, rechazar y ejecutar transferencias"
        icon={<ArrowLeftRight className="h-5 w-5" />}
      />
      {puedeCrear && <OpSection title="Crear transferencia"    fields={crear}
        description="Crea una transferencia que requerirá aprobación si supera el umbral." onSubmit={opsApi.crearTransferencia} />}
      {puedeDecidir && <OpSection title="Aprobar transferencia"  fields={aprobar}
        description="Aprueba una transferencia pendiente. Solo Supervisor de Empresa o Analista." onSubmit={opsApi.aprobarTransferencia} />}
      {puedeDecidir && <OpSection title="Rechazar transferencia" fields={rechazar}
        description="Rechaza una transferencia pendiente indicando motivo." onSubmit={opsApi.rechazarTransferencia} />}
      {puedeDirecta && <OpSection title="Ejecutar transferencia directa" fields={directa}
        description="Ejecuta una transferencia inmediata (sin pasar por aprobación)." onSubmit={opsApi.ejecutarTransferenciaDirecta} />}
      {puedeVencer && <OpSection title="Vencer transferencias pendientes" fields={vencer}
        description="Marca como vencidas las transferencias que llevan demasiado tiempo pendientes." onSubmit={opsApi.vencerTransferenciasPendientes} />}
      {puedePendientes && <OpSection title="Pendientes por empresa"  fields={pendientes}
        description="Consulta las transferencias pendientes de aprobación de la empresa." onSubmit={opsApi.pendientesEmpresa} />}
    </div>
  )
}
