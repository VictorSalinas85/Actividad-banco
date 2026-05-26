// ─── Generic API wrapper ───────────────────────────────────────────────────

export interface ApiResponse<T> {
  status: string
  data: T
  message?: string
  timestamp?: string
}

export interface ApiError {
  timestamp: string
  codigoError: string
  message: string
  details?: string[]
}

export interface SpResultado {
  codigo: number
  mensaje: string
  dato?: string | number | null
}

// ─── Auth ──────────────────────────────────────────────────────────────────

export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  type: string
  userId: number
  username: string
  nombreCompleto: string
  email: string
  roles: string[]
  expiresIn: number
}

export interface AuthUser {
  id: number
  username: string
  nombreCompleto: string
  email: string
  telefono?: string
  estadoId?: number
  roles: string[]
  ultimoLoginAt?: string
  createdAt?: string
}

// ─── Seguridad ─────────────────────────────────────────────────────────────

export interface SecUsuario {
  id: number
  username: string
  nombreCompleto: string
  email: string
  telefono?: string
  estadoId: number
  ultimoLoginAt?: string
  createdAt?: string
}

export interface SecRol {
  id: number
  codigoRolSistema: string
  nombreRol: string
  descripcion?: string
  activo: boolean
}

export interface SecUsuarioRol {
  id: number
  usuarioId: number
  rolId: number
  asignadoPor?: string
  asignadoEn?: string
}

export interface SecSesion {
  id: number
  usuarioId: number
  tokenHash: string
  ipOrigen?: string
  userAgent?: string
  estadoId: number
  creadaEn: string
  expiraEn: string
  cerradaEn?: string
}

// ─── Clientes persona natural ──────────────────────────────────────────────

export interface CliPersonaNatural {
  id: number
  usuarioId?: number
  tipoIdentificacionId: number
  numeroIdentificacion: string
  primerNombre: string
  segundoNombre?: string
  primerApellido: string
  segundoApellido?: string
  fechaNacimiento?: string
  email?: string
  telefono?: string
  direccion?: string
  estadoId: number
  createdAt?: string
}

// ─── Clientes empresa ──────────────────────────────────────────────────────

export interface CliEmpresa {
  id: number
  razonSocial: string
  nit: string
  sectorEconomico?: string
  email?: string
  telefono?: string
  direccion?: string
  estadoId: number
  representanteLegalId?: number
  createdAt?: string
}

export interface CliEmpresaUsuario {
  id: number
  empresaId: number
  usuarioId: number
  cargo?: string
  activo: boolean
  vinculadoEn?: string
}

export interface CliEmpresaUsuarioRol {
  id: number
  empresaUsuarioId: number
  rolEmpresaId: number
  asignadoPor?: string
  asignadoEn?: string
}

// ─── Cuentas ───────────────────────────────────────────────────────────────

export interface CtaCuenta {
  id: number
  numeroCuenta: string
  tipoCuentaId: number
  monedaId: number
  usuarioId?: number
  empresaId?: number
  saldo: number
  estadoId: number
  fechaApertura?: string
  createdAt?: string
}

export interface CtaMovimiento {
  id: number
  cuentaId: number
  tipoMovimientoId: number
  monto: number
  saldoAnterior: number
  saldoNuevo: number
  descripcion?: string
  referenciaExterna?: string
  creadoEn?: string
}

// ─── Préstamos ─────────────────────────────────────────────────────────────

export interface CrePrestamo {
  id: number
  usuarioId?: number
  empresaId?: number
  tipoCreditoId: number
  montoSolicitado: number
  tasaInteres: number
  plazoMeses: number
  cuotaMensual?: number
  proposito?: string
  estadoId: number
  creadoEn?: string
}

export interface CrePrestamoAprobacion {
  id: number
  prestamoId: number
  aprobadorId: number
  decision: string
  observaciones?: string
  fechaDecision?: string
}

export interface CrePrestamoDesembolso {
  id: number
  prestamoId: number
  cuentaDesembolsoId: number
  montoPrincipal: number
  montoIntereses: number
  montoCargos: number
  fechaDesembolso?: string
}

// ─── Transferencias ────────────────────────────────────────────────────────

export interface TrfTransferencia {
  id: number
  cuentaOrigenId: number
  cuentaDestinoId: number
  tipoTransferenciaId: number
  monto: number
  monedaId: number
  descripcion?: string
  estadoId: number
  usuarioSolicitanteId: number
  fechaCreacion?: string
}

export interface TrfTransferenciaAprobacion {
  id: number
  transferenciaId: number
  aprobadorId: number
  decision: string
  observaciones?: string
  fechaDecision?: string
}

export interface TransferenciaPendienteResponse {
  transferenciaId: number
  monto: number
  cuentaOrigenId: number
  cuentaDestinoId: number
  empresaId: number
  estadoId: number
  fechaCreacion: string
}

// ─── Productos bancarios ───────────────────────────────────────────────────

export interface PrdProductoBancario {
  id: number
  nombre: string
  descripcion?: string
  tipoCuentaId?: number
  tasaInteres?: number
  comisionMantenimiento?: number
  saldoMinimo?: number
  requiereAprobacion: boolean
  activo: boolean
}

// ─── Auditoría (solo lectura) ──────────────────────────────────────────────

export interface AudBitacoraEvento {
  id: number
  tipoEventoId: number
  usuarioId?: number
  entidadAfectada?: string
  entidadId?: number
  descripcion?: string
  ipOrigen?: string
  creadoEn?: string
}

export interface AudCambioDato {
  id: number
  bitacoraId: number
  tabla?: string
  columna?: string
  valorAnterior?: string
  valorNuevo?: string
}

export interface AudErrorOperacion {
  id: number
  tipoErrorId: number
  usuarioId?: number
  operacion?: string
  descripcion?: string
  stackTrace?: string
  ipOrigen?: string
  creadoEn?: string
}

// ─── Catálogos (todos con id, nombre, descripcion, activo) ────────────────

export interface Catalogo {
  id: number
  nombre?: string
  descripcion?: string
  codigo?: string
  activo?: boolean
  [key: string]: unknown
}

// ─── Tipos para CrudTable ──────────────────────────────────────────────────

export interface Column<T> {
  key: keyof T | string
  header: string
  render?: (value: unknown, row: T) => React.ReactNode
}

export interface Field {
  key: string
  label: string
  type: 'text' | 'number' | 'email' | 'date' | 'select' | 'textarea' | 'checkbox'
  required?: boolean
  options?: { value: string | number; label: string }[]
  readOnly?: boolean
}
