import { ClipboardList } from 'lucide-react'
import { PageHeader } from '../../components/common'
import OpSection from '../../components/OpSection'
import { opsApi } from '../../api/resources'
import { useTiposOp } from '../../lib/useEntityList'
import type { Field } from '../../types'

export default function OpsAuditoriaPage() {
  const tiposOp = useTiposOp()

  const evento: Field[] = [
    { key: 'tipoOperacionCodigo', label: 'Tipo de operación', type: 'select', required: true,
      options: tiposOp.data.map((t) => ({ value: t.codigo ?? '', label: `${t.codigo ?? ''} — ${t.nombre ?? ''}` })) },
    { key: 'productoTipo', label: 'Tipo producto (opcional)', type: 'select',
      options: [
        { value: '', label: '— ninguno —' },
        { value: 'CUENTA', label: 'Cuenta' },
        { value: 'PRESTAMO', label: 'Préstamo' },
        { value: 'TRANSFERENCIA', label: 'Transferencia' },
        { value: 'USUARIO', label: 'Usuario' },
        { value: 'EMPRESA', label: 'Empresa' },
        { value: 'PERSONA', label: 'Persona' },
      ] },
    { key: 'productoId',  label: 'ID del producto afectado', type: 'text' },
    { key: 'datosDetalle', label: 'Datos detalle (JSON)', type: 'textarea',
      help: 'Información adicional en formato JSON (opcional)' },
  ]

  const error: Field[] = [
    { key: 'codigoError', label: 'Código de error', type: 'text', required: true },
    { key: 'modulo',      label: 'Módulo',            type: 'text', required: true },
    { key: 'mensaje',     label: 'Mensaje',           type: 'textarea', required: true },
    { key: 'referencia',  label: 'Referencia',        type: 'text' },
    { key: 'payload',     label: 'Payload (JSON)',    type: 'textarea' },
  ]

  return (
    <div>
      <PageHeader
        title="Operaciones · Auditoría"
        description="Registro manual de eventos y errores para trazabilidad"
        icon={<ClipboardList className="h-5 w-5" />}
      />
      <OpSection title="Registrar evento" fields={evento}
        description="Anota manualmente un evento de bitácora." onSubmit={opsApi.registrarEvento}
        actorKeys={['idUsuario', 'actorUsuarioId']} />
      <OpSection title="Registrar error"  fields={error}
        description="Registra manualmente un error operacional." onSubmit={opsApi.registrarError} />
    </div>
  )
}
