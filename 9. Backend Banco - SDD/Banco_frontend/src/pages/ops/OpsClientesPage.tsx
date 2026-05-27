import { Users } from 'lucide-react'
import { PageHeader } from '../../components/common'
import OpSection from '../../components/OpSection'
import { opsApi } from '../../api/resources'
import { useTiposIdent, useEstadosUsuario, useRolesEmpresa } from '../../lib/useEntityList'
import type { Field } from '../../types'

export default function OpsClientesPage() {
  const tiposIdent = useTiposIdent()
  const estados    = useEstadosUsuario()
  const rolesEmp   = useRolesEmpresa()

  const crearPersona: Field[] = [
    { key: 'tipoIdentificacionId', label: 'Tipo identificación', type: 'select', required: true,
      options: tiposIdent.data
        .filter((t) => !t.aplicaA || t.aplicaA === 'PERSONA' || t.aplicaA === 'AMBOS')
        .map((t) => ({ value: t.id, label: `${t.codigo} — ${t.nombre}` })) },
    { key: 'identificacion',  label: 'Identificación', type: 'text', required: true },
    { key: 'nombreCompleto',  label: 'Nombre completo', type: 'text', required: true },
    { key: 'fechaNacimiento', label: 'Fecha nacimiento', type: 'date' },
    { key: 'email',           label: 'Email', type: 'email' },
    { key: 'telefono',        label: 'Teléfono', type: 'text' },
    { key: 'direccion',       label: 'Dirección', type: 'textarea' },
  ]

  const crearEmpresa: Field[] = [
    { key: 'tipoIdentificacionId', label: 'Tipo identificación', type: 'select', required: true,
      options: tiposIdent.data
        .filter((t) => !t.aplicaA || t.aplicaA === 'EMPRESA' || t.aplicaA === 'AMBOS')
        .map((t) => ({ value: t.id, label: `${t.codigo} — ${t.nombre}` })) },
    { key: 'nit',                     label: 'NIT', type: 'text', required: true },
    { key: 'razonSocial',             label: 'Razón social', type: 'text', required: true },
    { key: 'representantePersonaId',  label: 'Representante legal', type: 'person-picker', required: true },
    { key: 'email',                   label: 'Email', type: 'email' },
    { key: 'telefono',                label: 'Teléfono', type: 'text' },
    { key: 'direccion',               label: 'Dirección', type: 'textarea' },
  ]

  const cambiarEstado: Field[] = [
    { key: 'tipoCliente', label: 'Tipo cliente (PERSONA/EMPRESA)', type: 'text', required: true },
    { key: 'clienteId',   label: 'ID interno cliente', type: 'number', required: true },
    { key: 'estadoNuevoId', label: 'Estado nuevo', type: 'select', required: true,
      options: estados.data.map((e) => ({ value: e.id, label: `${e.codigo ?? ''} — ${e.nombre ?? ''}` })) },
    { key: 'motivo',      label: 'Motivo', type: 'textarea' },
  ]

  const asociar: Field[] = [
    { key: 'empresaId',  label: 'Empresa', type: 'empresa-picker', required: true },
    { key: 'usuarioId',  label: 'Usuario del sistema', type: 'usuario-picker', required: true },
    { key: 'cargo',      label: 'Cargo', type: 'text' },
  ]

  const asignarRol: Field[] = [
    { key: 'empresaUsuarioId', label: 'ID empresa-usuario', type: 'number', required: true },
    { key: 'rolEmpresaId',     label: 'Rol de empresa', type: 'select', required: true,
      options: rolesEmp.data.map((r) => ({ value: r.id, label: `${r.codigo ?? ''} — ${r.nombre ?? ''}` })) },
  ]

  return (
    <div>
      <PageHeader
        title="Operaciones · Clientes"
        description="Ejecución directa de stored procedures de gestión de clientes"
        icon={<Users className="h-5 w-5" />}
      />
      <OpSection title="Crear persona natural"  fields={crearPersona}  onSubmit={opsApi.crearPersona} />
      <OpSection title="Crear empresa"          fields={crearEmpresa}  onSubmit={opsApi.crearEmpresa} />
      <OpSection title="Cambiar estado cliente" fields={cambiarEstado} onSubmit={opsApi.cambiarEstadoCliente} />
      <OpSection title="Asociar usuario a empresa" fields={asociar}    onSubmit={opsApi.asociarUsuario} />
      <OpSection title="Asignar rol de empresa" fields={asignarRol}    onSubmit={opsApi.asignarRolEmpresa} />
    </div>
  )
}
