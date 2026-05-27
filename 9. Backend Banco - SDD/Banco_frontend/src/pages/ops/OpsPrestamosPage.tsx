import { Banknote } from 'lucide-react'
import { PageHeader } from '../../components/common'
import OpSection from '../../components/OpSection'
import { opsApi } from '../../api/resources'
import { useTiposPrest, useMotivosRechazo } from '../../lib/useEntityList'
import type { Field } from '../../types'

export default function OpsPrestamosPage() {
  const tipos    = useTiposPrest()
  const motivos  = useMotivosRechazo()

  const solicitar: Field[] = [
    { key: 'clientePersonaId', label: 'Cliente persona (opcional)', type: 'person-picker' },
    { key: 'clienteEmpresaId', label: 'Cliente empresa (opcional)', type: 'empresa-picker' },
    { key: 'tipoCreditoId',    label: 'Tipo de crédito', type: 'select', required: true,
      options: tipos.data.map((t) => ({ value: t.id, label: `${t.codigo} — ${t.nombre}` })) },
    { key: 'montoSolicitado',  label: 'Monto solicitado', type: 'number', required: true },
    { key: 'tasaInteres',      label: 'Tasa interés (%)', type: 'number', required: true },
    { key: 'plazoMeses',       label: 'Plazo (meses)',    type: 'number', required: true },
    { key: 'proposito',        label: 'Propósito',         type: 'textarea' },
  ]

  const aprobar: Field[] = [
    { key: 'prestamoId',  label: 'ID préstamo', type: 'number', required: true },
    { key: 'aprobadorId', label: 'Aprobador',   type: 'usuario-picker', required: true },
    { key: 'observaciones', label: 'Observaciones', type: 'textarea' },
  ]

  const rechazar: Field[] = [
    { key: 'prestamoId',  label: 'ID préstamo', type: 'number', required: true },
    { key: 'aprobadorId', label: 'Aprobador',   type: 'usuario-picker', required: true },
    { key: 'motivoId',    label: 'Motivo de rechazo', type: 'select', required: true,
      options: motivos.data.map((m) => ({ value: m.id, label: `${m.codigo ?? ''} — ${m.nombre ?? ''}` })) },
    { key: 'observaciones', label: 'Observaciones', type: 'textarea' },
  ]

  const desembolsar: Field[] = [
    { key: 'prestamoId',         label: 'ID préstamo', type: 'number', required: true },
    { key: 'cuentaDesembolsoId', label: 'ID cuenta destino', type: 'number', required: true },
    { key: 'montoPrincipal',     label: 'Monto principal', type: 'number', required: true },
    { key: 'montoIntereses',     label: 'Monto intereses', type: 'number' },
    { key: 'montoCargos',        label: 'Monto cargos',    type: 'number' },
  ]

  return (
    <div>
      <PageHeader
        title="Operaciones · Préstamos"
        description="Ciclo completo: solicitud → aprobación / rechazo → desembolso"
        icon={<Banknote className="h-5 w-5" />}
      />
      <OpSection title="Solicitar préstamo" fields={solicitar}   onSubmit={opsApi.solicitarPrestamo} />
      <OpSection title="Aprobar préstamo"   fields={aprobar}     onSubmit={opsApi.aprobarPrestamo} />
      <OpSection title="Rechazar préstamo"  fields={rechazar}    onSubmit={opsApi.rechazarPrestamo} />
      <OpSection title="Desembolsar"        fields={desembolsar} onSubmit={opsApi.desembolsarPrestamo} />
    </div>
  )
}
