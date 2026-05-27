import { NavLink } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

interface NavItem {
  label: string
  path: string
  roles?: string[]
  icon: string
}

interface NavGroup {
  group: string
  items: NavItem[]
}

const ALL_ROLES = ['ANALISTA_INTERNO', 'EMPLEADO_VENTANILLA', 'EMPLEADO_COMERCIAL',
  'CLIENTE_PERSONA', 'CLIENTE_EMPRESA_ADMIN', 'EMPLEADO_EMPRESA_OPERATIVO', 'SUPERVISOR_EMPRESA']

const NAV: NavGroup[] = [
  {
    group: 'Principal',
    items: [
      { label: 'Dashboard', path: '/dashboard', roles: ALL_ROLES, icon: '🏠' },
    ],
  },
  {
    group: 'Configuración',
    items: [
      { label: 'Catálogos', path: '/catalogos', roles: ['ANALISTA_INTERNO'], icon: '📋' },
      { label: 'Usuarios', path: '/usuarios', roles: ['ANALISTA_INTERNO'], icon: '👤' },
      { label: 'Roles', path: '/roles', roles: ['ANALISTA_INTERNO'], icon: '🔑' },
      { label: 'Sesiones', path: '/sesiones', roles: ['ANALISTA_INTERNO'], icon: '🔐' },
    ],
  },
  {
    group: 'Clientes',
    items: [
      {
        label: 'Personas Naturales',
        path: '/personas',
        roles: ['ANALISTA_INTERNO', 'EMPLEADO_VENTANILLA', 'EMPLEADO_COMERCIAL'],
        icon: '🧑',
      },
      {
        label: 'Empresas',
        path: '/empresas',
        roles: ['ANALISTA_INTERNO', 'EMPLEADO_COMERCIAL', 'CLIENTE_EMPRESA_ADMIN'],
        icon: '🏢',
      },
      {
        label: 'Usuarios Empresa',
        path: '/empresa-usuarios',
        roles: ['ANALISTA_INTERNO', 'EMPLEADO_COMERCIAL'],
        icon: '👥',
      },
    ],
  },
  {
    group: 'Banca',
    items: [
      {
        label: 'Cuentas',
        path: '/cuentas',
        roles: ['ANALISTA_INTERNO', 'EMPLEADO_VENTANILLA', 'EMPLEADO_COMERCIAL', 'SUPERVISOR_EMPRESA'],
        icon: '💳',
      },
      {
        label: 'Movimientos',
        path: '/movimientos',
        roles: ['ANALISTA_INTERNO', 'EMPLEADO_VENTANILLA'],
        icon: '📊',
      },
      {
        label: 'Préstamos',
        path: '/prestamos',
        roles: ['ANALISTA_INTERNO', 'EMPLEADO_COMERCIAL'],
        icon: '💰',
      },
      {
        label: 'Transferencias',
        path: '/transferencias',
        roles: ['ANALISTA_INTERNO', 'EMPLEADO_VENTANILLA', 'SUPERVISOR_EMPRESA', 'EMPLEADO_EMPRESA_OPERATIVO'],
        icon: '↔️',
      },
      {
        label: 'Productos Bancarios',
        path: '/productos',
        roles: ALL_ROLES,
        icon: '🏦',
      },
    ],
  },
  {
    group: 'Operaciones',
    items: [
      {
        label: 'Ops. Clientes',
        path: '/ops/clientes',
        roles: ['ANALISTA_INTERNO', 'EMPLEADO_VENTANILLA', 'EMPLEADO_COMERCIAL', 'CLIENTE_EMPRESA_ADMIN'],
        icon: '⚙️',
      },
      {
        label: 'Ops. Cuentas',
        path: '/ops/cuentas',
        roles: ['ANALISTA_INTERNO', 'EMPLEADO_VENTANILLA', 'CLIENTE_PERSONA', 'CLIENTE_EMPRESA_ADMIN', 'EMPLEADO_EMPRESA_OPERATIVO'],
        icon: '⚙️',
      },
      {
        label: 'Ops. Préstamos',
        path: '/ops/prestamos',
        roles: ['ANALISTA_INTERNO', 'EMPLEADO_COMERCIAL', 'EMPLEADO_VENTANILLA', 'CLIENTE_PERSONA', 'CLIENTE_EMPRESA_ADMIN'],
        icon: '⚙️',
      },
      {
        label: 'Ops. Transferencias',
        path: '/ops/transferencias',
        roles: ['ANALISTA_INTERNO', 'EMPLEADO_VENTANILLA', 'CLIENTE_PERSONA', 'SUPERVISOR_EMPRESA', 'CLIENTE_EMPRESA_ADMIN', 'EMPLEADO_EMPRESA_OPERATIVO'],
        icon: '⚙️',
      },
      {
        label: 'Ops. Sesiones',
        path: '/ops/sesiones',
        roles: ['ANALISTA_INTERNO', 'EMPLEADO_VENTANILLA', 'EMPLEADO_COMERCIAL'],
        icon: '⚙️',
      },
      {
        label: 'Ops. Auditoría',
        path: '/ops/auditoria',
        roles: ['ANALISTA_INTERNO'],
        icon: '⚙️',
      },
    ],
  },
  {
    group: 'Auditoría',
    items: [
      { label: 'Bitácora', path: '/auditoria/bitacora', roles: ['ANALISTA_INTERNO'], icon: '📝' },
      { label: 'Cambios de datos', path: '/auditoria/cambios', roles: ['ANALISTA_INTERNO'], icon: '🔄' },
      { label: 'Errores', path: '/auditoria/errores', roles: ['ANALISTA_INTERNO'], icon: '⚠️' },
    ],
  },
]

export default function Sidebar() {
  const { hasRole } = useAuth()

  return (
    <aside className="w-72 bg-navy-gradient text-white flex flex-col min-h-screen shadow-navy-lg relative">
      {/* Brand block con logo protagonista (PNG con fondo transparente) */}
      <div className="px-6 pt-6 pb-4 border-b border-navy-700/60 relative">
        <div className="flex flex-col items-center text-center">
          <div className="relative">
            <div className="absolute -inset-4 bg-gold-gradient opacity-25 rounded-full blur-2xl" />
            <img
              src="/logo.png"
              alt="Wolfstreet Bank"
              className="relative w-48 h-48 object-contain drop-shadow-[0_10px_30px_rgba(212,162,78,0.35)] select-none"
              draggable={false}
            />
          </div>
          <p className="brand-sub text-gold-300 mt-1">Panel de administración</p>
        </div>
      </div>

      {/* Línea dorada decorativa */}
      <div className="h-[2px] bg-gradient-to-r from-transparent via-gold-500 to-transparent" />

      <nav className="flex-1 overflow-y-auto py-4">
        {NAV.map((group) => {
          const visibleItems = group.items.filter(
            (item) => !item.roles || item.roles.some((r) => hasRole(r))
          )
          if (visibleItems.length === 0) return null
          return (
            <div key={group.group} className="mb-3">
              <p className="px-6 py-1.5 text-[11px] font-bold text-gold-400/90 uppercase tracking-[0.2em]">
                {group.group}
              </p>
              {visibleItems.map((item) => (
                <NavLink
                  key={item.path}
                  to={item.path}
                  className={({ isActive }) =>
                    `group flex items-center gap-3 px-6 py-2.5 text-sm transition-all border-l-2 ${
                      isActive
                        ? 'bg-navy-700/70 text-white font-semibold border-gold-400'
                        : 'text-navy-100 hover:bg-navy-700/40 hover:text-white border-transparent hover:border-gold-500/50'
                    }`
                  }
                >
                  <span className="text-base">{item.icon}</span>
                  <span className="truncate">{item.label}</span>
                </NavLink>
              ))}
            </div>
          )
        })}
      </nav>

      <div className="px-6 py-3 border-t border-navy-700/60 text-center">
        <p className="text-[10px] text-navy-300 uppercase tracking-widest">
          Wolfstreet Bank &copy; {new Date().getFullYear()}
        </p>
      </div>
    </aside>
  )
}
