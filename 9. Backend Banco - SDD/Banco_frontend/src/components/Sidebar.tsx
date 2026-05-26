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
    <aside className="w-64 bg-slate-800 text-white flex flex-col min-h-screen">
      <div className="px-6 py-5 border-b border-slate-700">
        <div className="flex items-center gap-2">
          <span className="text-2xl">🏛️</span>
          <div>
            <p className="font-bold text-white leading-tight">Sistema Bancario</p>
            <p className="text-xs text-slate-400">Panel de administración</p>
          </div>
        </div>
      </div>

      <nav className="flex-1 overflow-y-auto py-4">
        {NAV.map((group) => {
          const visibleItems = group.items.filter(
            (item) => !item.roles || item.roles.some((r) => hasRole(r))
          )
          if (visibleItems.length === 0) return null
          return (
            <div key={group.group} className="mb-2">
              <p className="px-6 py-1 text-xs font-semibold text-slate-500 uppercase tracking-wider">
                {group.group}
              </p>
              {visibleItems.map((item) => (
                <NavLink
                  key={item.path}
                  to={item.path}
                  className={({ isActive }) =>
                    `flex items-center gap-3 px-6 py-2 text-sm transition-colors ${
                      isActive
                        ? 'bg-blue-700 text-white font-medium'
                        : 'text-slate-300 hover:bg-slate-700 hover:text-white'
                    }`
                  }
                >
                  <span className="text-base">{item.icon}</span>
                  {item.label}
                </NavLink>
              ))}
            </div>
          )
        })}
      </nav>
    </aside>
  )
}
