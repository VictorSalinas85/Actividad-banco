import { CreditCard } from 'lucide-react'
import { PageHeader } from '../../components/common'
import OpSection from '../../components/OpSection'
import { opsApi } from '../../api/resources'
import { useTiposCuenta, useMonedas, useMotivosBloqueo, useCanalesOp } from '../../lib/useEntityList'
import type { Field } from '../../types'

export default function OpsCuentasPage() {
  const tipos     = useTiposCuenta()
  const monedas   = useMonedas()
  const motivos   = useMotivosBloqueo()
  const canales   = useCanalesOp()

  const abrir: Field[] = [
    { key: 'titularPersonaId', label: 'Titular persona (opcional)', type: 'person-picker' },
    { key: 'titularEmpresaId', label: 'Titular empresa (opcional)', type: 'empresa-picker' },
    { key: 'tipoCuentaId', label: 'Tipo de cuenta', type: 'select', required: true,
      options: tipos.data.map((t) => ({ value: t.id, label: `${t.codigo} — ${t.nombre}` })) },
    { key: 'monedaId', label: 'Moneda', type: 'select', required: true,
      options: monedas.data.map((m) => ({ value: m.id, label: `${m.codigo} — ${m.nombre}` })) },
    { key: 'saldoInicial', label: 'Saldo inicial', type: 'number' },
  ]

  const bloquear: Field[] = [
    { key: 'cuentaId', label: 'ID cuenta', type: 'number', required: true },
    { key: 'motivoId', label: 'Motivo', type: 'select', required: true,
      options: motivos.data.map((m) => ({ value: m.id, label: `${m.codigo ?? ''} — ${m.nombre ?? ''}` })) },
    { key: 'observaciones', label: 'Observaciones', type: 'textarea' },
  ]

  const cancelar: Field[] = [
    { key: 'cuentaId', label: 'ID cuenta', type: 'number', required: true },
    { key: 'motivo',   label: 'Motivo',    type: 'textarea' },
  ]

  const consignar: Field[] = [
    { key: 'cuentaId', label: 'ID cuenta destino', type: 'number', required: true },
    { key: 'monto',    label: 'Monto a consignar', type: 'number', required: true },
    { key: 'canalId',  label: 'Canal', type: 'select',
      options: canales.data.map((c) => ({ value: c.id, label: `${c.codigo ?? ''} — ${c.nombre ?? ''}` })) },
    { key: 'descripcion', label: 'Descripción', type: 'text' },
  ]

  const retirar: Field[] = [
    { key: 'cuentaId', label: 'ID cuenta origen', type: 'number', required: true },
    { key: 'monto',    label: 'Monto a retirar', type: 'number', required: true },
    { key: 'canalId',  label: 'Canal', type: 'select',
      options: canales.data.map((c) => ({ value: c.id, label: `${c.codigo ?? ''} — ${c.nombre ?? ''}` })) },
    { key: 'descripcion', label: 'Descripción', type: 'text' },
  ]

  return (
    <div>
      <PageHeader
        title="Operaciones · Cuentas"
        description="Apertura, bloqueo, consignación y retiro vía stored procedures"
        icon={<CreditCard className="h-5 w-5" />}
      />
      <OpSection title="Abrir cuenta"    fields={abrir}     onSubmit={opsApi.abrirCuenta} />
      <OpSection title="Consignar"       fields={consignar} onSubmit={opsApi.consignar} />
      <OpSection title="Retirar"         fields={retirar}   onSubmit={opsApi.retirar} />
      <OpSection title="Bloquear cuenta" fields={bloquear}  onSubmit={opsApi.bloquearCuenta} />
      <OpSection title="Cancelar cuenta" fields={cancelar}  onSubmit={opsApi.cancelarCuenta} />
    </div>
  )
}
