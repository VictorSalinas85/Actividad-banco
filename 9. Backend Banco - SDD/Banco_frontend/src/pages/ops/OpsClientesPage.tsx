import { useState } from 'react'
import { Users } from 'lucide-react'
import toast from 'react-hot-toast'
import {
  PageHeader, IdentificacionLookup,
  invalidateDirectorioCache, useDirectorio,
} from '../../components/common'
import OpSection from '../../components/OpSection'
import { opsApi } from '../../api/resources'
import { useAuth } from '../../auth/AuthContext'
import { useTiposIdent, useEstadosUsuario, useRolesEmpresa } from '../../lib/useEntityList'
import type { Field, SpResultado } from '../../types'

export default function OpsClientesPage() {
  const { hasRole } = useAuth()
  const tiposIdent = useTiposIdent()
  const estados    = useEstadosUsuario()
  const rolesEmp   = useRolesEmpresa()
  const puedeCrearPersona  = hasRole('ANALISTA_INTERNO', 'EMPLEADO_VENTANILLA', 'EMPLEADO_COMERCIAL')
  const puedeCrearEmpresa  = hasRole('ANALISTA_INTERNO', 'EMPLEADO_COMERCIAL')
  const puedeCambiarEstado = hasRole('ANALISTA_INTERNO', 'EMPLEADO_VENTANILLA', 'EMPLEADO_COMERCIAL')
  const puedeAsociar       = hasRole('ANALISTA_INTERNO', 'EMPLEADO_COMERCIAL')
  const puedeAsignarRol    = hasRole('ANALISTA_INTERNO', 'CLIENTE_EMPRESA_ADMIN')

  // ── Crear persona / Crear empresa (siguen siendo OpSection genericos) ──
  const crearPersona: Field[] = [
    { key: 'tipoIdentCodigo', label: 'Tipo identificación', type: 'select', required: true,
      options: tiposIdent.data
        .filter((t) => !t.aplicaA || t.aplicaA === 'PERSONA' || t.aplicaA === 'AMBOS')
        .map((t) => ({ value: t.codigo, label: `${t.codigo} — ${t.nombre}` })) },
    { key: 'identificacion',  label: 'Identificación', type: 'text', required: true },
    { key: 'nombreCompleto',  label: 'Nombre completo', type: 'text', required: true },
    { key: 'fechaNacimiento', label: 'Fecha nacimiento', type: 'date', required: true,
      help: 'Cliente debe ser mayor de edad' },
    { key: 'email',           label: 'Email', type: 'email', required: true },
    { key: 'telefono',        label: 'Teléfono', type: 'text', required: true },
    { key: 'direccion',       label: 'Dirección', type: 'textarea', required: true },
  ]

  const crearEmpresa: Field[] = [
    { key: 'tipoIdentCodigo', label: 'Tipo identificación', type: 'select', required: true,
      options: tiposIdent.data
        .filter((t) => !t.aplicaA || t.aplicaA === 'EMPRESA' || t.aplicaA === 'AMBOS')
        .map((t) => ({ value: t.codigo, label: `${t.codigo} — ${t.nombre}` })) },
    { key: 'nit',                     label: 'NIT', type: 'text', required: true },
    { key: 'razonSocial',             label: 'Razón social', type: 'text', required: true },
    { key: 'representantePersonaId',  label: 'Representante legal', type: 'person-picker', required: true,
      help: 'Persona natural ya registrada' },
    { key: 'email',                   label: 'Email', type: 'email', required: true },
    { key: 'telefono',                label: 'Teléfono', type: 'text', required: true },
    { key: 'direccion',               label: 'Dirección', type: 'textarea', required: true },
  ]

  return (
    <div>
      <PageHeader
        title="Operaciones · Clientes"
        description="Ejecución directa de stored procedures de gestión de clientes"
        icon={<Users className="h-5 w-5" />}
      />

      {puedeCrearPersona && <OpSection title="Crear persona natural"  fields={crearPersona}
        description="Crea el registro completo de una persona natural en la base de datos."
        onSubmit={opsApi.crearPersona} />}

      {puedeCrearEmpresa && <OpSection title="Crear empresa"          fields={crearEmpresa}
        description="Crea una empresa cliente. Requiere que el representante legal ya exista como persona natural."
        onSubmit={opsApi.crearEmpresa} />}

      {puedeCambiarEstado && <CambiarEstadoClienteForm estados={estados.data} />}

      {puedeAsociar && <AsociarUsuarioEmpresaForm />}

      {puedeAsignarRol && <AsignarRolEmpresaForm rolesEmp={rolesEmp.data} />}
    </div>
  )
}

// ─────────────────────────────────────────────────────────────────────────
// Cambiar estado de cliente — identificacion + nombre resolved
// ─────────────────────────────────────────────────────────────────────────

interface Cat { id: number; codigo?: string; nombre?: string }

function CambiarEstadoClienteForm({ estados }: { estados: Cat[] }) {
  const { user } = useAuth()
  const [identificacion, setIdentificacion] = useState('')
  const [resolved, setResolved] = useState<{ tipo: 'PERSONA' | 'EMPRESA', id: number, nombre: string } | null>(null)
  const [estadoCodigo, setEstadoCodigo] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const [result, setResult] = useState<SpResultado | null>(null)

  const reset = () => {
    setIdentificacion('')
    setResolved(null)
    setEstadoCodigo('')
  }

  const submit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!resolved) { toast.error('Identificación no encontrada'); return }
    if (!estadoCodigo) { toast.error('Seleccione el estado nuevo'); return }
    setSubmitting(true); setResult(null)
    try {
      const res = await opsApi.cambiarEstadoCliente({
        clienteTipo: resolved.tipo,
        clienteId: resolved.id,
        estadoCodigo,
        actorUsuarioId: user?.id,
      })
      const data = res.data.data as SpResultado | undefined
      if (data && 'code' in data) {
        setResult(data)
        if (data.code === 'OK') { toast.success(data.message ?? 'Estado actualizado'); invalidateDirectorioCache(); reset() }
        else toast.error(`[${data.code}] ${data.message ?? 'Error'}`)
      }
    } catch (err: unknown) {
      const msg = (err as { response?: { data?: { message?: string } } }).response?.data?.message ?? 'Error al ejecutar'
      toast.error(msg)
    } finally { setSubmitting(false) }
  }

  return (
    <div className="card mb-6">
      <h2 className="text-lg font-bold text-navy-900 mb-1">Cambiar estado de cliente</h2>
      <p className="text-sm text-ink-500 mb-4">Activa, bloquea o suspende a un cliente identificado por su número de documento.</p>
      <form onSubmit={submit} className="space-y-3">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
          <div>
            <label className="label">Identificación del cliente <span className="text-red-500">*</span></label>
            <IdentificacionLookup
              value={identificacion}
              onChange={setIdentificacion}
              onResolve={setResolved}
              required
            />
          </div>
          <div>
            <label className="label">Nombre del cliente</label>
            <input className="input bg-navy-50" value={resolved?.nombre ?? ''} readOnly placeholder="Se completa automáticamente" />
            {resolved && <p className="help-text">Tipo: <code className="bg-navy-50 px-1.5 rounded">{resolved.tipo}</code> · ID interno: #{resolved.id}</p>}
          </div>
          <div>
            <label className="label">Estado nuevo <span className="text-red-500">*</span></label>
            <select className="input" value={estadoCodigo} onChange={(e) => setEstadoCodigo(e.target.value)} required>
              <option value="">-- Seleccionar --</option>
              {estados.map((e) => (
                <option key={e.id} value={e.codigo ?? ''}>{e.codigo} — {e.nombre}</option>
              ))}
            </select>
          </div>
        </div>
        <SubmitRow submitting={submitting} result={result} />
      </form>
    </div>
  )
}

// ─────────────────────────────────────────────────────────────────────────
// Asociar usuario a empresa — empresa por NIT + usuario por identificacion
// ─────────────────────────────────────────────────────────────────────────

import { EntityPicker } from '../../components/common'

function AsociarUsuarioEmpresaForm() {
  const { user } = useAuth()
  const [empresaIdent, setEmpresaIdent] = useState('')
  const [empresaResolved, setEmpresaResolved] = useState<{ tipo: 'PERSONA' | 'EMPRESA', id: number, nombre: string } | null>(null)
  const [usuarioId, setUsuarioId] = useState<number | null>(null)
  const [submitting, setSubmitting] = useState(false)
  const [result, setResult] = useState<SpResultado | null>(null)

  const reset = () => { setEmpresaIdent(''); setEmpresaResolved(null); setUsuarioId(null) }

  const submit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!empresaResolved || empresaResolved.tipo !== 'EMPRESA') { toast.error('NIT de empresa inválido'); return }
    if (!usuarioId) { toast.error('Seleccione el usuario del sistema'); return }
    setSubmitting(true); setResult(null)
    try {
      const res = await opsApi.asociarUsuario({
        empresaId: empresaResolved.id,
        usuarioId,
        actorUsuarioId: user?.id,
      })
      const data = res.data.data as SpResultado | undefined
      if (data && 'code' in data) {
        setResult(data)
        if (data.code === 'OK') { toast.success(data.message ?? 'Usuario asociado'); invalidateDirectorioCache(); reset() }
        else toast.error(`[${data.code}] ${data.message ?? 'Error'}`)
      }
    } catch (err: unknown) {
      const msg = (err as { response?: { data?: { message?: string } } }).response?.data?.message ?? 'Error al ejecutar'
      toast.error(msg)
    } finally { setSubmitting(false) }
  }

  return (
    <div className="card mb-6">
      <h2 className="text-lg font-bold text-navy-900 mb-1">Asociar usuario a empresa</h2>
      <p className="text-sm text-ink-500 mb-4">Vincula un usuario del sistema con una empresa para que pueda operar en su nombre.</p>
      <form onSubmit={submit} className="space-y-3">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
          <div>
            <label className="label">NIT de la empresa <span className="text-red-500">*</span></label>
            <IdentificacionLookup
              value={empresaIdent}
              onChange={setEmpresaIdent}
              onResolve={setEmpresaResolved}
              tipo="EMPRESA"
              required
              placeholder="Digite el NIT"
            />
          </div>
          <div>
            <label className="label">Razón social</label>
            <input className="input bg-navy-50" value={empresaResolved?.nombre ?? ''} readOnly placeholder="Se completa automáticamente" />
          </div>
          <div className="md:col-span-2">
            <label className="label">Usuario del sistema <span className="text-red-500">*</span></label>
            <EntityPicker kind="usuario" value={usuarioId} onChange={setUsuarioId} />
          </div>
        </div>
        <SubmitRow submitting={submitting} result={result} />
      </form>
    </div>
  )
}

// ─────────────────────────────────────────────────────────────────────────
// Asignar rol de empresa — empresa por NIT + usuario, resuelve empresaUsuarioId
// ─────────────────────────────────────────────────────────────────────────

function AsignarRolEmpresaForm({ rolesEmp }: { rolesEmp: Cat[] }) {
  const { user } = useAuth()
  const { data: dir } = useDirectorio()
  const [empresaIdent, setEmpresaIdent] = useState('')
  const [empresaResolved, setEmpresaResolved] = useState<{ tipo: 'PERSONA' | 'EMPRESA', id: number, nombre: string } | null>(null)
  const [usuarioId, setUsuarioId] = useState<number | null>(null)
  const [rolEmpresaCodigo, setRolEmpresaCodigo] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const [result, setResult] = useState<SpResultado | null>(null)

  const reset = () => { setEmpresaIdent(''); setEmpresaResolved(null); setUsuarioId(null); setRolEmpresaCodigo('') }

  const submit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!empresaResolved || empresaResolved.tipo !== 'EMPRESA') { toast.error('NIT de empresa inválido'); return }
    if (!usuarioId) { toast.error('Seleccione el usuario'); return }
    if (!rolEmpresaCodigo) { toast.error('Seleccione el rol'); return }

    // Resolver empresaUsuarioId consultando /empresas/usuarios
    setSubmitting(true); setResult(null)
    try {
      const { data: list } = await (await import('../../api/client')).default.get<{ data: { id: number, empresaId: number, usuarioId: number }[] }>('/empresas/usuarios')
      const match = list.data.find((eu) => eu.empresaId === empresaResolved.id && eu.usuarioId === usuarioId)
      if (!match) {
        toast.error('Ese usuario no está asociado a la empresa. Asócielo primero.')
        setSubmitting(false); return
      }
      const res = await opsApi.asignarRolEmpresa({
        empresaUsuarioId: match.id,
        rolEmpresaCodigo,
        actorUsuarioId: user?.id,
      })
      const data = res.data.data as SpResultado | undefined
      if (data && 'code' in data) {
        setResult(data)
        if (data.code === 'OK') { toast.success(data.message ?? 'Rol asignado'); invalidateDirectorioCache(); reset() }
        else toast.error(`[${data.code}] ${data.message ?? 'Error'}`)
      }
    } catch (err: unknown) {
      const msg = (err as { response?: { data?: { message?: string } } }).response?.data?.message ?? 'Error al ejecutar'
      toast.error(msg)
    } finally { setSubmitting(false) }
  }

  return (
    <div className="card mb-6">
      <h2 className="text-lg font-bold text-navy-900 mb-1">Asignar rol de empresa</h2>
      <p className="text-sm text-ink-500 mb-4">
        Define qué rol tiene un usuario dentro de la empresa. El usuario debe estar previamente asociado a la empresa.
      </p>
      <form onSubmit={submit} className="space-y-3">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
          <div>
            <label className="label">NIT de la empresa <span className="text-red-500">*</span></label>
            <IdentificacionLookup
              value={empresaIdent}
              onChange={setEmpresaIdent}
              onResolve={setEmpresaResolved}
              tipo="EMPRESA"
              required
              placeholder="Digite el NIT"
            />
          </div>
          <div>
            <label className="label">Razón social</label>
            <input className="input bg-navy-50" value={empresaResolved?.nombre ?? ''} readOnly placeholder="Se completa automáticamente" />
          </div>
          <div>
            <label className="label">Usuario del sistema <span className="text-red-500">*</span></label>
            <EntityPicker kind="usuario" value={usuarioId} onChange={setUsuarioId} />
          </div>
          <div>
            <label className="label">Rol a asignar <span className="text-red-500">*</span></label>
            <select className="input" value={rolEmpresaCodigo} onChange={(e) => setRolEmpresaCodigo(e.target.value)} required>
              <option value="">-- Seleccionar --</option>
              {rolesEmp.map((r) => (
                <option key={r.id} value={r.codigo ?? ''}>{r.codigo} — {r.nombre}</option>
              ))}
            </select>
          </div>
        </div>
        <SubmitRow submitting={submitting} result={result} />
        {dir && empresaResolved && (
          <p className="help-text">
            Si no encuentra el usuario, verifique que esté asociado a la empresa en el formulario anterior.
          </p>
        )}
      </form>
    </div>
  )
}

// ─────────────────────────────────────────────────────────────────────────
// Boton submit + display de resultado (reutilizable)
// ─────────────────────────────────────────────────────────────────────────

function SubmitRow({ submitting, result }: { submitting: boolean; result: SpResultado | null }) {
  return (
    <div className="flex items-center gap-4 pt-2">
      <button type="submit" className="btn-primary" disabled={submitting}>
        {submitting ? 'Ejecutando…' : 'Ejecutar'}
      </button>
      {result && (
        <div
          className={`text-sm px-3 py-1.5 rounded-md ${
            result.code === 'OK'
              ? 'bg-emerald-50 text-emerald-700 border border-emerald-200'
              : 'bg-red-50 text-red-700 border border-red-200'
          }`}
        >
          <span className="font-semibold">Resultado:</span>{' '}
          [{result.code}] {result.message}
          {result.reference && ` · Ref: ${result.reference}`}
        </div>
      )}
    </div>
  )
}
