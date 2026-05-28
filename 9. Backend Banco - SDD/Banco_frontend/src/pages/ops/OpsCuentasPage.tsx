import { useState } from 'react'
import { CreditCard } from 'lucide-react'
import toast from 'react-hot-toast'
import {
  PageHeader,
  IdentificacionLookup,
  CuentaNumeroLookup,
  CuentaPorIdentificacionPicker,
  invalidateDirectorioCache,
  invalidateCuentaCache,
  type CuentaDir,
} from '../../components/common'
import OpSection from '../../components/OpSection'
import { opsApi } from '../../api/resources'
import { useAuth } from '../../auth/AuthContext'
import { useTiposCuenta, useMonedas, useMotivosBloqueo, useCanalesOp } from '../../lib/useEntityList'
import type { Field, SpResultado } from '../../types'

interface Cat { id: number; codigo?: string; nombre?: string }

export default function OpsCuentasPage() {
  const tipos    = useTiposCuenta()
  const monedas  = useMonedas()
  const motivos  = useMotivosBloqueo()
  const canales  = useCanalesOp()

  // ── Abrir cuenta — identificacion del titular + tipo cuenta + moneda ──
  return (
    <div>
      <PageHeader
        title="Operaciones · Cuentas"
        description="Apertura, bloqueo, consignación y retiro vía stored procedures"
        icon={<CreditCard className="h-5 w-5" />}
      />

      <AbrirCuentaForm tipos={tipos.data} monedas={monedas.data} />

      <ConsignarForm canales={canales.data} />
      <RetirarForm   canales={canales.data} />

      <BloquearCuentaForm motivos={motivos.data} />
      <CancelarCuentaForm />
    </div>
  )
}

// ─────────────────────────────────────────────────────────────────────────
// Abrir cuenta — identificacion del cliente
// ─────────────────────────────────────────────────────────────────────────

function AbrirCuentaForm({ tipos, monedas }: { tipos: Cat[]; monedas: Cat[] }) {
  const { user } = useAuth()
  const [identificacion, setIdentificacion] = useState('')
  const [resolved, setResolved] = useState<{ tipo: 'PERSONA' | 'EMPRESA', id: number, nombre: string } | null>(null)
  const [tipoCuentaCodigo, setTipoCuentaCodigo] = useState('')
  const [monedaCodigo, setMonedaCodigo] = useState('')
  const [limiteSobregiro, setLimiteSobregiro] = useState<number | ''>('')
  const [submitting, setSubmitting] = useState(false)
  const [result, setResult] = useState<SpResultado | null>(null)

  const reset = () => {
    setIdentificacion(''); setResolved(null)
    setTipoCuentaCodigo(''); setMonedaCodigo(''); setLimiteSobregiro('')
  }

  const submit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!resolved) { toast.error('Identificación no encontrada'); return }
    if (!tipoCuentaCodigo || !monedaCodigo) { toast.error('Complete tipo y moneda'); return }
    setSubmitting(true); setResult(null)
    try {
      const res = await opsApi.abrirCuenta({
        titularTipo: resolved.tipo,
        titularId: resolved.id,
        tipoCuentaCodigo,
        monedaCodigo,
        limiteSobregiro: limiteSobregiro || undefined,
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
      <h2 className="text-lg font-bold text-navy-900 mb-1">Abrir cuenta</h2>
      <p className="text-sm text-ink-500 mb-4">Crea una nueva cuenta para el cliente identificado por su CC o NIT.</p>
      <form onSubmit={submit} className="space-y-3">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
          <div>
            <label className="label">Identificación del titular <span className="text-red-500">*</span></label>
            <IdentificacionLookup
              value={identificacion}
              onChange={setIdentificacion}
              onResolve={setResolved}
              required
            />
          </div>
          <div>
            <label className="label">Nombre del titular</label>
            <input className="input bg-navy-50" value={resolved?.nombre ?? ''} readOnly placeholder="Se completa automáticamente" />
          </div>
          <div>
            <label className="label">Tipo de cuenta <span className="text-red-500">*</span></label>
            <select className="input" value={tipoCuentaCodigo} onChange={(e) => setTipoCuentaCodigo(e.target.value)} required>
              <option value="">-- Seleccionar --</option>
              {tipos.map((t) => <option key={t.id} value={t.codigo ?? ''}>{t.codigo} — {t.nombre}</option>)}
            </select>
          </div>
          <div>
            <label className="label">Moneda <span className="text-red-500">*</span></label>
            <select className="input" value={monedaCodigo} onChange={(e) => setMonedaCodigo(e.target.value)} required>
              <option value="">-- Seleccionar --</option>
              {monedas.map((m) => <option key={m.id} value={m.codigo ?? ''}>{m.codigo} — {m.nombre}</option>)}
            </select>
          </div>
          <div className="md:col-span-2">
            <label className="label">Límite de sobregiro</label>
            <input type="number" className="input" value={limiteSobregiro}
              onChange={(e) => setLimiteSobregiro(e.target.value ? Number(e.target.value) : '')} />
          </div>
        </div>
        <SubmitRow submitting={submitting} result={result} />
      </form>
    </div>
  )
}

// ─────────────────────────────────────────────────────────────────────────
// Consignar / Retirar — numero de cuenta + titular visible
// ─────────────────────────────────────────────────────────────────────────

function MovimientoCuentaForm({
  titulo, descripcion, accion, canales,
}: {
  titulo: string
  descripcion: string
  accion: (payload: Record<string, unknown>) => Promise<{ data: { data?: SpResultado | unknown } }>
  canales: Cat[]
}) {
  const { user } = useAuth()
  const [numeroCuenta, setNumeroCuenta] = useState('')
  const [cuenta, setCuenta] = useState<CuentaDir | null>(null)
  const [monto, setMonto] = useState<number | ''>('')
  const [canalCodigo, setCanalCodigo] = useState('')
  const [observaciones, setObservaciones] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const [result, setResult] = useState<SpResultado | null>(null)

  const reset = () => {
    setNumeroCuenta(''); setCuenta(null); setMonto(''); setCanalCodigo(''); setObservaciones('')
  }

  const submit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!cuenta) { toast.error('Cuenta no encontrada'); return }
    if (!monto || Number(monto) <= 0) { toast.error('Monto inválido'); return }
    if (!canalCodigo) { toast.error('Seleccione el canal'); return }
    setSubmitting(true); setResult(null)
    try {
      const res = await accion({
        cuentaId: cuenta.id,
        monto: Number(monto),
        canalCodigo,
        observaciones: observaciones || undefined,
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
      <h2 className="text-lg font-bold text-navy-900 mb-1">{titulo}</h2>
      <p className="text-sm text-ink-500 mb-4">{descripcion}</p>
      <form onSubmit={submit} className="space-y-3">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
          <div>
            <label className="label">Número de cuenta <span className="text-red-500">*</span></label>
            <CuentaNumeroLookup value={numeroCuenta} onChange={setNumeroCuenta} onResolve={setCuenta} required />
          </div>
          <div>
            <label className="label">Titular</label>
            <input className="input bg-navy-50" value={cuenta?.titularNombre ?? ''} readOnly placeholder="Se completa automáticamente" />
            {cuenta && (
              <p className="help-text">
                Identificación: <span className="font-mono">{cuenta.titularIdent}</span> · Saldo actual: ${Number(cuenta.saldoActual).toLocaleString('es-CO')}
              </p>
            )}
          </div>
          <div>
            <label className="label">Monto <span className="text-red-500">*</span></label>
            <input type="number" className="input" value={monto}
              onChange={(e) => setMonto(e.target.value ? Number(e.target.value) : '')}
              min={0.01} step={0.01} required />
          </div>
          <div>
            <label className="label">Canal <span className="text-red-500">*</span></label>
            <select className="input" value={canalCodigo} onChange={(e) => setCanalCodigo(e.target.value)} required>
              <option value="">-- Seleccionar --</option>
              {canales.map((c) => <option key={c.id} value={c.codigo ?? ''}>{c.codigo} — {c.nombre}</option>)}
            </select>
          </div>
          <div className="md:col-span-2">
            <label className="label">Observaciones</label>
            <textarea className="input" rows={2} value={observaciones} onChange={(e) => setObservaciones(e.target.value)}
              placeholder="Notas internas sobre la operación (opcional)" />
          </div>
        </div>
        <SubmitRow submitting={submitting} result={result} />
      </form>
    </div>
  )
}

function ConsignarForm({ canales }: { canales: Cat[] }) {
  return <MovimientoCuentaForm
    titulo="Consignar"
    descripcion="Deposita un monto en la cuenta especificada."
    accion={opsApi.consignar}
    canales={canales}
  />
}

function RetirarForm({ canales }: { canales: Cat[] }) {
  return <MovimientoCuentaForm
    titulo="Retirar"
    descripcion="Retira un monto de la cuenta especificada."
    accion={opsApi.retirar}
    canales={canales}
  />
}

// ─────────────────────────────────────────────────────────────────────────
// Bloquear cuenta — identificacion del titular → lista de sus cuentas
// ─────────────────────────────────────────────────────────────────────────

function BloquearCuentaForm({ motivos }: { motivos: Cat[] }) {
  const { user } = useAuth()
  const [identificacion, setIdentificacion] = useState('')
  const [cuentaId, setCuentaId] = useState<number | null>(null)
  const [motivoCodigo, setMotivoCodigo] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const [result, setResult] = useState<SpResultado | null>(null)

  const reset = () => { setIdentificacion(''); setCuentaId(null); setMotivoCodigo('') }

  const submit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!cuentaId) { toast.error('Seleccione una cuenta'); return }
    if (!motivoCodigo) { toast.error('Seleccione el motivo'); return }
    setSubmitting(true); setResult(null)
    try {
      const res = await opsApi.bloquearCuenta({
        cuentaId, motivoCodigo, actorUsuarioId: user?.id,
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
      <h2 className="text-lg font-bold text-navy-900 mb-1">Bloquear cuenta</h2>
      <p className="text-sm text-ink-500 mb-4">Identifique al titular y seleccione cuál de sus cuentas bloquear.</p>
      <form onSubmit={submit} className="space-y-3">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
          <div className="md:col-span-2">
            <label className="label">Identificación del titular <span className="text-red-500">*</span></label>
            <CuentaPorIdentificacionPicker
              identificacion={identificacion}
              onIdentificacionChange={setIdentificacion}
              cuentaId={cuentaId}
              onCuentaIdChange={setCuentaId}
              required
            />
          </div>
          <div className="md:col-span-2">
            <label className="label">Motivo del bloqueo <span className="text-red-500">*</span></label>
            <select className="input" value={motivoCodigo} onChange={(e) => setMotivoCodigo(e.target.value)} required>
              <option value="">-- Seleccionar --</option>
              {motivos.map((m) => <option key={m.id} value={m.codigo ?? ''}>{m.codigo} — {m.nombre}</option>)}
            </select>
          </div>
        </div>
        <SubmitRow submitting={submitting} result={result} />
      </form>
    </div>
  )
}

// ─────────────────────────────────────────────────────────────────────────
// Cancelar cuenta — identificacion del titular → lista de sus cuentas
// ─────────────────────────────────────────────────────────────────────────

function CancelarCuentaForm() {
  const { user } = useAuth()
  const [identificacion, setIdentificacion] = useState('')
  const [cuentaId, setCuentaId] = useState<number | null>(null)
  const [submitting, setSubmitting] = useState(false)
  const [result, setResult] = useState<SpResultado | null>(null)

  const reset = () => { setIdentificacion(''); setCuentaId(null) }

  const submit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!cuentaId) { toast.error('Seleccione una cuenta'); return }
    if (!confirm('¿Confirma cancelar esta cuenta? Esta operación no se puede revertir.')) return
    setSubmitting(true); setResult(null)
    try {
      const res = await opsApi.cancelarCuenta({ cuentaId, actorUsuarioId: user?.id })
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
      <h2 className="text-lg font-bold text-navy-900 mb-1">Cancelar cuenta</h2>
      <p className="text-sm text-ink-500 mb-4">Identifique al titular y seleccione cuál de sus cuentas cerrar definitivamente.</p>
      <form onSubmit={submit} className="space-y-3">
        <CuentaPorIdentificacionPicker
          identificacion={identificacion}
          onIdentificacionChange={setIdentificacion}
          cuentaId={cuentaId}
          onCuentaIdChange={setCuentaId}
          required
        />
        <SubmitRow submitting={submitting} result={result} />
      </form>
    </div>
  )
}

// ─────────────────────────────────────────────────────────────────────────

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

// Mantener compatibilidad con campos no usados (suprime warning de unused imports en algunos builds)
type _Unused = Field
