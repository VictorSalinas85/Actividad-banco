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
    <div className="max-w-4xl">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-slate-800">
          Bienvenido, {user?.nombreCompleto?.split(' ')[0]}
        </h1>
        <p className="text-slate-500 mt-1">
          {user?.roles.map((r) => ROLE_LABELS[r] ?? r).join(' · ')}
        </p>
      </div>

      {shortcuts.length > 0 && (
        <div className="mb-8">
          <h2 className="text-lg font-semibold text-slate-700 mb-4">Accesos rápidos</h2>
          <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
            {shortcuts.map((s) => (
              <a
                key={s.path}
                href={s.path}
                className="card flex flex-col items-center py-6 gap-3 hover:border-blue-300
                           hover:shadow-md transition-all cursor-pointer text-center"
              >
                <span className="text-3xl">{s.icon}</span>
                <span className="text-sm font-medium text-slate-700">{s.label}</span>
              </a>
            ))}
          </div>
        </div>
      )}

      <div className="card">
        <h2 className="text-lg font-semibold text-slate-700 mb-4">Información de sesión</h2>
        <div className="grid grid-cols-2 gap-4 text-sm">
          <div>
            <p className="text-slate-500">Usuario</p>
            <p className="font-medium text-slate-800">{user?.username}</p>
          </div>
          <div>
            <p className="text-slate-500">Email</p>
            <p className="font-medium text-slate-800">{user?.email}</p>
          </div>
          <div className="col-span-2">
            <p className="text-slate-500 mb-2">Roles asignados</p>
            <div className="flex flex-wrap gap-2">
              {user?.roles.map((r) => (
                <span
                  key={r}
                  className="bg-blue-100 text-blue-800 text-xs font-semibold px-3 py-1 rounded-full"
                >
                  {r}
                </span>
              ))}
            </div>
          </div>
        </div>
      </div>

      {hasRole('ANALISTA_INTERNO') && (
        <div className="mt-6 p-4 bg-amber-50 border border-amber-200 rounded-lg">
          <p className="text-amber-800 text-sm font-medium">
            ⚡ Modo Analista Interno — Tienes acceso completo al sistema
          </p>
        </div>
      )}
    </div>
  )
}
