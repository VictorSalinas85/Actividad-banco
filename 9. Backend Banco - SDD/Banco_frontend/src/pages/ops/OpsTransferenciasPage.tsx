import { ArrowLeftRight } from 'lucide-react'
import { PageHeader } from '../../components/common'
import OpSection from '../../components/OpSection'
import { opsApi } from '../../api/resources'
import { useTiposTrans, useMonedas, useMotivosRechazo } from '../../lib/useEntityList'
import type { Field } from '../../types'

export default function OpsTransferenciasPage() {
  const tipos    = useTiposTrans()
  const monedas  = useMonedas()
  const motivos  = useMotivosRechazo()

  const crear: Field[] = [
    { key: 'cuentaOrigenId',     label: 'ID cuenta origen', type: 'number', required: true },
    { key: 'cuentaDestinoId',    label: 'ID cuenta destino', type: 'number', required: true },
    { key: 'tipoTransferenciaId', label: 'Tipo', type: 'select', required: true,
      options: tipos.data.map((t) => ({ value: t.id, label: `${t.codigo} — ${t.nombre}` })) },
    { key: 'monto',    label: 'Monto', type: 'number', required: true },
    { key: 'monedaId', label: 'Moneda', type: 'select', required: true,
      options: monedas.data.map((m) => ({ value: m.id, label: `${m.codigo} — ${m.nombre}` })) },
    { key: 'descripcion', label: 'Descripción', type: 'textarea' },
  ]

  const aprobar: Field[] = [
    { key: 'transferenciaId', label: 'ID transferencia', type: 'number', required: true },
    { key: 'aprobadorId',     label: 'Aprobador', type: 'usuario-picker', required: true },
    { key: 'observaciones',   label: 'Observaciones', type: 'textarea' },
  ]

  const rechazar: Field[] = [
    { key: 'transferenciaId', label: 'ID transferencia', type: 'number', required: true },
    { key: 'aprobadorId',     label: 'Aprobador', type: 'usuario-picker', required: true },
    { key: 'motivoId',        label: 'Motivo de rechazo', type: 'select', required: true,
      options: motivos.data.map((m) => ({ value: m.id, label: `${m.codigo ?? ''} — ${m.nombre ?? ''}` })) },
    { key: 'observaciones',   label: 'Observaciones', type: 'textarea' },
  ]

  const directa: Field[] = [
    { key: 'cuentaOrigenId',  label: 'ID cuenta origen', type: 'number', required: true },
    { key: 'cuentaDestinoId', label: 'ID cuenta destino', type: 'number', required: true },
    { key: 'monto',           label: 'Monto', type: 'number', required: true },
    { key: 'descripcion',     label: 'Descripción', type: 'text' },
  ]

  const vencer: Field[] = [
    { key: 'horasVencimiento', label: 'Horas antigüedad para vencer', type: 'number', required: true },
  ]

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
      <OpSection title="Crear transferencia"    fields={crear}      onSubmit={opsApi.crearTransferencia} />
      <OpSection title="Aprobar transferencia"  fields={aprobar}    onSubmit={opsApi.aprobarTransferencia} />
      <OpSection title="Rechazar transferencia" fields={rechazar}   onSubmit={opsApi.rechazarTransferencia} />
      <OpSection title="Ejecutar directa"        fields={directa}    onSubmit={opsApi.ejecutarTransferenciaDirecta} />
      <OpSection title="Vencer pendientes"       fields={vencer}     onSubmit={opsApi.vencerTransferenciasPendientes} />
      <OpSection title="Pendientes por empresa"  fields={pendientes} onSubmit={opsApi.pendientesEmpresa} />
    </div>
  )
}
