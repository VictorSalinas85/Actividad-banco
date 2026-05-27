import { useAuth } from '../auth/AuthContext'

const ROLE_LABELS: Record<string, string> = {
  ANALISTA_INTERNO: 'Analista Interno',
  EMPLEADO_VENTANILLA: 'Empleado de Ventanilla',
  EMPLEADO_COMERCIAL: 'Empleado Comercial',
  CLIENTE_PERSONA: 'Cliente Persona Natural',
  CLIENTE_EMPRESA_ADMIN: 'Administrador de Empresa',
  EMPLEADO_EMPRESA_OPERATIVO: 'Operativo de Empresa',
  SUPERVISOR_EMPRESA: 'Supervisor de Empresa',
}

const ROLE_SHORTCUTS: Record<string, { label: string; path: string; icon: string }[]> = {
  ANALISTA_INTERNO: [
    { label: 'Catálogos', path: '/catalogos', icon: '📋' },
    { label: 'Usuarios', path: '/usuarios', icon: '👤' },
    { label: 'Bitácora', path: '/auditoria/bitacora', icon: '📝' },
    { label: 'Ops. Auditoría', path: '/ops/auditoria', icon: '⚙️' },
  ],
  EMPLEADO_VENTANILLA: [
    { label: 'Cuentas', path: '/cuentas', icon: '💳' },
    { label: 'Ops. Cuentas', path: '/ops/cuentas', icon: '⚙️' },
    { label: 'Transferencias', path: '/transferencias', icon: '↔️' },
    { label: 'Ops. Sesiones', path: '/ops/sesiones', icon: '🔐' },
  ],
  EMPLEADO_COMERCIAL: [
    { label: 'Personas', path: '/personas', icon: '🧑' },
    { label: 'Empresas', path: '/empresas', icon: '🏢' },
    { label: 'Préstamos', path: '/prestamos', icon: '💰' },
    { label: 'Ops. Préstamos', path: '/ops/prestamos', icon: '⚙️' },
  ],
  CLIENTE_PERSONA: [
    { label: 'Mis cuentas', path: '/cuentas', icon: '💳' },
    { label: 'Transferencias', path: '/ops/transferencias', icon: '↔️' },
    { label: 'Préstamos', path: '/ops/prestamos', icon: '💰' },
    { label: 'Productos', path: '/productos', icon: '🏦' },
  ],
  CLIENTE_EMPRESA_ADMIN: [
    { label: 'Mi empresa', path: '/empresas', icon: '🏢' },
    { label: 'Usuarios empresa', path: '/empresa-usuarios', icon: '👥' },
    { label: 'Cuentas', path: '/cuentas', icon: '💳' },
    { label: 'Transferencias', path: '/ops/transferencias', icon: '↔️' },
  ],
  EMPLEADO_EMPRESA_OPERATIVO: [
    { label: 'Cuentas', path: '/cuentas', icon: '💳' },
    { label: 'Ops. Cuentas', path: '/ops/cuentas', icon: '⚙️' },
    { label: 'Ops. Transferencias', path: '/ops/transferencias', icon: '↔️' },
  ],
  SUPERVISOR_EMPRESA: [
    { label: 'Transferencias', path: '/transferencias', icon: '↔️' },
    { label: 'Aprobar trans.', path: '/ops/transferencias', icon: '✅' },
    { label: 'Cuentas', path: '/cuentas', icon: '💳' },
  ],
}

export default function Dashboard() {
  const { user, hasRole } = useAuth()

  const shortcuts = user?.roles.flatMap((r) => ROLE_SHORTCUTS[r] ?? [])
    .filter((s, i, arr) => arr.findIndex((x) => x.path === s.path) === i)
    .slice(0, 8) ?? []

  return (
    <div className="max-w-5xl mx-auto">
      {/* Hero card con branding */}
      <div className="relative overflow-hidden rounded-2xl bg-navy-gradient text-white shadow-navy-lg mb-8">
        <div className="absolute -right-16 -top-16 w-72 h-72 bg-gold-500/25 rounded-full blur-3xl" />
        <div className="absolute right-4 top-1/2 -translate-y-1/2 opacity-90 hidden md:block">
          <img src="/logo.png" alt="" className="h-48 w-48 object-contain drop-shadow-[0_10px_30px_rgba(212,162,78,0.35)]" />
        </div>
        <div className="relative p-8 md:p-10 md:pr-56">
          <p className="brand-sub text-gold-300">Wolfstreet Bank</p>
          <h1 className="text-3xl md:text-4xl font-extrabold mt-2">
            Bienvenido, <span className="text-gold-300">{user?.nombreCompleto?.split(' ')[0]}</span>
          </h1>
          <p className="text-navy-100 mt-2 max-w-xl">
            {user?.roles.map((r) => ROLE_LABELS[r] ?? r).join(' · ')}
          </p>
          <div className="mt-4 h-[2px] w-32 bg-gradient-to-r from-gold-400 to-transparent" />
        </div>
      </div>

      {shortcuts.length > 0 && (
        <div className="mb-8">
          <h2 className="text-lg font-bold text-navy-900 mb-1">Accesos rápidos</h2>
          <p className="text-sm text-ink-500 mb-4">Operaciones más frecuentes según tu rol</p>
          <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
            {shortcuts.map((s) => (
              <a
                key={s.path}
                href={s.path}
                className="group bg-white rounded-xl border border-navy-100 shadow-sm
                           flex flex-col items-center py-6 gap-3 text-center
                           hover:border-gold-400 hover:shadow-gold hover:-translate-y-0.5
                           transition-all cursor-pointer"
              >
                <span className="text-3xl group-hover:scale-110 transition-transform">{s.icon}</span>
                <span className="text-sm font-semibold text-navy-800">{s.label}</span>
              </a>
            ))}
          </div>
        </div>
      )}

      <div className="card-accent">
        <h2 className="text-lg font-bold text-navy-900 mb-1">Información de sesión</h2>
        <p className="text-xs text-ink-500 mb-4">Datos del usuario autenticado</p>
        <div className="grid grid-cols-2 gap-4 text-sm">
          <div>
            <p className="text-ink-500 text-xs uppercase tracking-wider">Usuario</p>
            <p className="font-semibold text-navy-900">{user?.username}</p>
          </div>
          <div>
            <p className="text-ink-500 text-xs uppercase tracking-wider">Email</p>
            <p className="font-semibold text-navy-900">{user?.email}</p>
          </div>
          <div className="col-span-2">
            <p className="text-ink-500 text-xs uppercase tracking-wider mb-2">Roles asignados</p>
            <div className="flex flex-wrap gap-2">
              {user?.roles.map((r) => (
                <span key={r} className="badge badge-gold">{r}</span>
              ))}
            </div>
          </div>
        </div>
      </div>

      {hasRole('ANALISTA_INTERNO') && (
        <div className="mt-6 p-4 bg-gold-50 border-l-4 border-gold-500 rounded-lg shadow-sm">
          <p className="text-navy-900 text-sm font-semibold">
            ⚡ Modo Analista Interno — Tienes acceso completo al sistema
          </p>
        </div>
      )}
    </div>
  )
}
