import { useState } from 'react'
import { Banknote } from 'lucide-react'
import toast from 'react-hot-toast'
import {
  PageHeader, IdentificacionLookup, CuentaPicker, MoneyInput,
  invalidateDirectorioCache, invalidateCuentaCache,
} from '../../components/common'
import OpSection from '../../components/OpSection'
import { opsApi } from '../../api/resources'
import { useAuth } from '../../auth/AuthContext'
import { useTiposPrest, useMotivosRechazo } from '../../lib/useEntityList'
import type { Field, SpResultado } from '../../types'

interface Cat { id: number; codigo?: string; nombre?: string }

export default function OpsPrestamosPage() {
  const tipos    = useTiposPrest()
  const motivos  = useMotivosRechazo()

  // Aprobar / Rechazar / Desembolsar — todavia usan prestamoId (es la
  // forma natural de identificar un prestamo una vez creado).
  const aprobar: Field[] = [
    { key: 'prestamoId',   label: 'ID préstamo', type: 'number', required: true },
    { key: 'montoAprobado', label: 'Monto aprobado', type: 'money', required: true },
    { key: 'tasaInteres',  label: 'Tasa interés (%)', type: 'number', required: true },
  ]
  const rechazar: Field[] = [
    { key: 'prestamoId',          label: 'ID préstamo', type: 'number', required: true },
    { key: 'motivoRechazoCodigo', label: 'Motivo de rechazo', type: 'select', required: true,
      options: motivos.data.map((m) => ({ value: m.codigo ?? '', label: `${m.codigo ?? ''} — ${m.nombre ?? ''}` })) },
    { key: 'comentario',          label: 'Comentario', type: 'textarea' },
  ]
  const desembolsar: Field[] = [
    { key: 'prestamoId', label: 'ID préstamo', type: 'number', required: true },
  ]

  return (
    <div>
      <PageHeader
        title="Operaciones · Préstamos"
        description="Ciclo completo: solicitud → aprobación / rechazo → desembolso"
        icon={<Banknote className="h-5 w-5" />}
      />

      <SolicitarPrestamoForm tipos={tipos.data} />

      <OpSection title="Aprobar préstamo"   fields={aprobar}
        description="Aprueba la solicitud con monto y tasa definitivos." onSubmit={opsApi.aprobarPrestamo} />
      <OpSection title="Rechazar préstamo"  fields={rechazar}
        description="Rechaza la solicitud indicando el motivo." onSubmit={opsApi.rechazarPrestamo} />
      <OpSection title="Desembolsar"        fields={desembolsar}
        description="Ejecuta el desembolso del préstamo a la cuenta destino registrada." onSubmit={opsApi.desembolsarPrestamo} />
    </div>
  )
}

function SolicitarPrestamoForm({ tipos }: { tipos: Cat[] }) {
  const { user } = useAuth()
  const [identificacion, setIdentificacion] = useState('')
  const [cliente, setCliente] = useState<{ tipo: 'PERSONA' | 'EMPRESA', id: number, nombre: string } | null>(null)
  const [tipoPrestamoCodigo, setTipoPrestamoCodigo] = useState('')
  const [montoSolicitado, setMontoSolicitado] = useState<number | ''>('')
  const [plazoMeses, setPlazoMeses] = useState<number | ''>('')
  const [cuentaDestinoId, setCuentaDestinoId] = useState<number | null>(null)
  const [submitting, setSubmitting] = useState(false)
  const [result, setResult] = useState<SpResultado | null>(null)

  const reset = () => {
    setIdentificacion(''); setCliente(null); setTipoPrestamoCodigo('')
    setMontoSolicitado(''); setPlazoMeses(''); setCuentaDestinoId(null)
  }

  const submit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!cliente) { toast.error('Identificación no encontrada'); return }
    if (!tipoPrestamoCodigo) { toast.error('Seleccione tipo de préstamo'); return }
    if (!cuentaDestinoId) { toast.error('Seleccione cuenta destino'); return }
    setSubmitting(true); setResult(null)
    try {
      const res = await opsApi.solicitarPrestamo({
        clienteTipo: cliente.tipo,
        clienteId: cliente.id,
        tipoPrestamoCodigo,
        montoSolicitado: Number(montoSolicitado),
        plazoMeses: Number(plazoMeses),
        cuentaDestinoId,
        actorUsuarioId: user?.id,
      })
      const data = res.data.data as SpResultado | undefined
      if (data && 'code' in data) {
        setResult(data)
        if (data.code === 'OK') { toast.success(data.message); invalidateCuentaCache(); invalidateDirectorioCache(); reset() }
        else toast.error(`[${data.code}] ${data.message}`)
      }
    } catch (err: unknown) {
      toast.error((err as { response?: { data?: { message?: string } } }).response?.data?.message ?? 'Error al ejecutar')
    } finally { setSubmitting(false) }
  }

  return (
    <div className="card mb-6">
      <h2 className="text-lg font-bold text-navy-900 mb-1">Solicitar préstamo</h2>
      <p className="text-sm text-ink-500 mb-4">Registra una solicitud de crédito en estado "en estudio".</p>
      <form onSubmit={submit} className="space-y-3">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
          <div>
            <label className="label">Identificación del solicitante <span className="text-red-500">*</span></label>
            <IdentificacionLookup value={identificacion} onChange={setIdentificacion} onResolve={setCliente} required />
          </div>
          <div>
            <label className="label">Nombre del solicitante</label>
            <input className="input bg-navy-50" value={cliente?.nombre ?? ''} readOnly placeholder="Se completa automáticamente" />
          </div>
          <div>
            <label className="label">Tipo de crédito <span className="text-red-500">*</span></label>
            <select className="input" value={tipoPrestamoCodigo} onChange={(e) => setTipoPrestamoCodigo(e.target.value)} required>
              <option value="">-- Seleccionar --</option>
              {tipos.map((t) => <option key={t.id} value={t.codigo ?? ''}>{t.codigo} — {t.nombre}</option>)}
            </select>
          </div>
          <div>
            <label className="label">Monto solicitado <span className="text-red-500">*</span></label>
            <MoneyInput value={montoSolicitado} onChange={setMontoSolicitado} required />
          </div>
          <div>
            <label className="label">Plazo (meses) <span className="text-red-500">*</span></label>
            <input type="number" className="input" value={plazoMeses}
              onChange={(e) => setPlazoMeses(e.target.value ? Number(e.target.value) : '')}
              min={1} required />
          </div>
          <div>
            <label className="label">Cuenta de desembolso <span className="text-red-500">*</span></label>
            <CuentaPicker value={cuentaDestinoId} onChange={setCuentaDestinoId} />
          </div>
        </div>
        <SubmitRow submitting={submitting} result={result} />
      </form>
    </div>
  )
}

function SubmitRow({ submitting, result }: { submitting: boolean; result: SpResultado | null }) {
  return (
    <div className="flex items-center gap-4 pt-2">
      <button type="submit" className="btn-primary" disabled={submitting}>
        {submitting ? 'Ejecutando…' : 'Ejecutar'}
      </button>
      {result && (
        <div className={`text-sm px-3 py-1.5 rounded-md ${
          result.code === 'OK'
            ? 'bg-emerald-50 text-emerald-700 border border-emerald-200'
            : 'bg-red-50 text-red-700 border border-red-200'
        }`}>
          <span className="font-semibold">Resultado:</span>{' '}
          [{result.code}] {result.message}
          {result.reference && ` · Ref: ${result.reference}`}
        </div>
      )}
    </div>
  )
}
