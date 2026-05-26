import api from './client'
import type { ApiResponse, LoginRequest, LoginResponse, AuthUser, SpResultado, TransferenciaPendienteResponse } from '../types'

// ─── Auth ──────────────────────────────────────────────────────────────────

export const authApi = {
  login: (data: LoginRequest) =>
    api.post<ApiResponse<LoginResponse>>('/auth/login', data),
  me: () => api.get<ApiResponse<AuthUser>>('/auth/me'),
}

// ─── Generic CRUD factory ─────────────────────────────────────────────────

export function crudFactory(path: string) {
  return {
    list: <T>() => api.get<ApiResponse<T[]>>(path),
    get: <T>(id: number) => api.get<ApiResponse<T>>(`${path}/${id}`),
    create: <T>(data: unknown) => api.post<ApiResponse<T>>(path, data),
    update: <T>(id: number, data: unknown) => api.put<ApiResponse<T>>(`${path}/${id}`, data),
    remove: (id: number) => api.delete(`${path}/${id}`),
  }
}

// ─── Catálogos ─────────────────────────────────────────────────────────────

export const catalogosApi = {
  'estados-usuario': crudFactory('/catalogos/estados-usuario'),
  'estados-cuenta': crudFactory('/catalogos/estados-cuenta'),
  'tipos-cuenta': crudFactory('/catalogos/tipos-cuenta'),
  'monedas': crudFactory('/catalogos/monedas'),
  'estados-prestamo': crudFactory('/catalogos/estados-prestamo'),
  'tipos-prestamo': crudFactory('/catalogos/tipos-prestamo'),
  'estados-transferencia': crudFactory('/catalogos/estados-transferencia'),
  'tipos-operacion': crudFactory('/catalogos/tipos-operacion'),
  'canales-operacion': crudFactory('/catalogos/canales-operacion'),
  'motivos-rechazo': crudFactory('/catalogos/motivos-rechazo'),
  'motivos-bloqueo': crudFactory('/catalogos/motivos-bloqueo'),
  'estados-sesion': crudFactory('/catalogos/estados-sesion'),
  'roles-empresa': crudFactory('/catalogos/roles-empresa'),
  'tipos-identificacion': crudFactory('/catalogos/tipos-identificacion'),
  'tipos-movimiento': crudFactory('/catalogos/tipos-movimiento'),
  'parametros-negocio': crudFactory('/catalogos/parametros-negocio'),
  'transiciones-estado': crudFactory('/catalogos/transiciones-estado'),
}

// ─── Recursos CRUD ─────────────────────────────────────────────────────────

export const usuariosApi = crudFactory('/usuarios')
export const rolesApi = crudFactory('/usuarios/roles')
export const asignacionesRolApi = crudFactory('/usuarios/asignaciones-rol')
export const sesionesApi = {
  list: <T>() => api.get<ApiResponse<T[]>>('/usuarios/sesiones'),
  get: <T>(id: number) => api.get<ApiResponse<T>>(`/usuarios/sesiones/${id}`),
}

export const personasApi = crudFactory('/personas')

export const empresasApi = crudFactory('/empresas')
export const empresaUsuariosApi = crudFactory('/empresas/usuarios')
export const empresaUsuarioRolesApi = crudFactory('/empresas/usuarios/roles')

export const cuentasApi = crudFactory('/cuentas')
export const movimientosApi = crudFactory('/cuentas/movimientos')

export const prestamosApi = crudFactory('/prestamos')
export const prestamoAprobacionesApi = crudFactory('/prestamos/aprobaciones')
export const prestamoDesembolsosApi = crudFactory('/prestamos/desembolsos')

export const transferenciasApi = crudFactory('/transferencias')
export const transferenciaAprobacionesApi = crudFactory('/transferencias/aprobaciones')

export const productosApi = crudFactory('/productos')

export const bitacoraApi = sesionesApi  // solo lectura, mismo patrón
export const bitacoraListApi = {
  list: <T>() => api.get<ApiResponse<T[]>>('/auditoria/bitacora'),
  get: <T>(id: number) => api.get<ApiResponse<T>>(`/auditoria/bitacora/${id}`),
}
export const cambiosApi = {
  list: <T>() => api.get<ApiResponse<T[]>>('/auditoria/cambios'),
  get: <T>(id: number) => api.get<ApiResponse<T>>(`/auditoria/cambios/${id}`),
}
export const erroresApi = {
  list: <T>() => api.get<ApiResponse<T[]>>('/auditoria/errores'),
  get: <T>(id: number) => api.get<ApiResponse<T>>(`/auditoria/errores/${id}`),
}

// ─── Operaciones (Stored Procedures) ──────────────────────────────────────

export const opsApi = {
  // Clientes
  crearPersona: (data: unknown) =>
    api.post<ApiResponse<SpResultado>>('/ops/clientes/crear-persona', data),
  crearEmpresa: (data: unknown) =>
    api.post<ApiResponse<SpResultado>>('/ops/clientes/crear-empresa', data),
  cambiarEstadoCliente: (data: unknown) =>
    api.post<ApiResponse<SpResultado>>('/ops/clientes/cambiar-estado', data),
  asociarUsuario: (data: unknown) =>
    api.post<ApiResponse<SpResultado>>('/ops/clientes/asociar-usuario', data),
  asignarRolEmpresa: (data: unknown) =>
    api.post<ApiResponse<SpResultado>>('/ops/clientes/asignar-rol', data),

  // Cuentas
  abrirCuenta: (data: unknown) =>
    api.post<ApiResponse<SpResultado>>('/ops/cuentas/abrir', data),
  bloquearCuenta: (data: unknown) =>
    api.post<ApiResponse<SpResultado>>('/ops/cuentas/bloquear', data),
  cancelarCuenta: (data: unknown) =>
    api.post<ApiResponse<SpResultado>>('/ops/cuentas/cancelar', data),
  consignar: (data: unknown) =>
    api.post<ApiResponse<SpResultado>>('/ops/cuentas/consignar', data),
  retirar: (data: unknown) =>
    api.post<ApiResponse<SpResultado>>('/ops/cuentas/retirar', data),

  // Préstamos
  solicitarPrestamo: (data: unknown) =>
    api.post<ApiResponse<SpResultado>>('/ops/prestamos/solicitar', data),
  aprobarPrestamo: (data: unknown) =>
    api.post<ApiResponse<SpResultado>>('/ops/prestamos/aprobar', data),
  rechazarPrestamo: (data: unknown) =>
    api.post<ApiResponse<SpResultado>>('/ops/prestamos/rechazar', data),
  desembolsarPrestamo: (data: unknown) =>
    api.post<ApiResponse<SpResultado>>('/ops/prestamos/desembolsar', data),

  // Transferencias
  crearTransferencia: (data: unknown) =>
    api.post<ApiResponse<SpResultado>>('/ops/transferencias/crear', data),
  aprobarTransferencia: (data: unknown) =>
    api.post<ApiResponse<SpResultado>>('/ops/transferencias/aprobar', data),
  rechazarTransferencia: (data: unknown) =>
    api.post<ApiResponse<SpResultado>>('/ops/transferencias/rechazar', data),
  ejecutarTransferenciaDirecta: (data: unknown) =>
    api.post<ApiResponse<SpResultado>>('/ops/transferencias/ejecutar-directa', data),
  vencerTransferenciasPendientes: (data: unknown) =>
    api.post<ApiResponse<SpResultado>>('/ops/transferencias/vencer-pendientes', data),
  pendientesEmpresa: (data: unknown) =>
    api.post<ApiResponse<TransferenciaPendienteResponse[]>>('/ops/transferencias/pendientes-empresa', data),

  // Sesiones
  validarSesion: (data: unknown) =>
    api.post<ApiResponse<SpResultado>>('/ops/sesiones/validar', data),
  revocarSesion: (data: unknown) =>
    api.post<ApiResponse<SpResultado>>('/ops/sesiones/revocar', data),

  // Auditoría
  registrarEvento: (data: unknown) =>
    api.post<ApiResponse<void>>('/ops/auditoria/registrar-evento', data),
  registrarError: (data: unknown) =>
    api.post<ApiResponse<void>>('/ops/auditoria/registrar-error', data),
}
