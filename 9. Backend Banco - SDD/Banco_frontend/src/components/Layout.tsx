import { Outlet } from 'react-router-dom'
import Sidebar from './Sidebar'
import { useAuth } from '../auth/AuthContext'

export default function Layout() {
  const { user, logout } = useAuth()

  const initials = (user?.nombreCompleto ?? user?.username ?? '?')
    .split(' ')
    .filter(Boolean)
    .slice(0, 2)
    .map((w) => w[0]?.toUpperCase())
    .join('')

  return (
    <div className="flex min-h-screen">
      <Sidebar />
      <div className="flex-1 flex flex-col min-w-0">
        <header className="bg-white border-b border-navy-100 px-8 py-3 flex items-center justify-between shadow-sm relative">
          {/* Línea dorada decorativa inferior */}
          <div className="absolute bottom-0 left-0 right-0 h-[2px] bg-gradient-to-r from-transparent via-gold-400/60 to-transparent" />

          <div className="flex items-center gap-3">
            <img src="/logo.png" alt="" className="h-9 w-9 object-contain" />
            <div className="leading-tight">
              <p className="brand-title text-sm">WOLFSTREET <span className="text-gold-600">BANK</span></p>
              <p className="text-[10px] text-ink-500 uppercase tracking-widest">Panel</p>
            </div>
          </div>

          <div className="flex items-center gap-4">
            <div className="text-right">
              <p className="text-sm font-semibold text-navy-900">{user?.nombreCompleto}</p>
              <p className="text-xs text-ink-500">{user?.roles.join(' · ')}</p>
            </div>
            <div className="h-10 w-10 rounded-full bg-gold-gradient text-navy-900 font-bold flex items-center justify-center shadow-sm ring-2 ring-gold-200">
              {initials}
            </div>
            <button
              onClick={logout}
              className="text-sm text-red-600 hover:text-red-800 font-semibold transition-colors px-3 py-1.5 rounded-md hover:bg-red-50"
            >
              Cerrar sesión
            </button>
          </div>
        </header>
        <main className="flex-1 p-8 overflow-auto">
          <Outlet />
        </main>
      </div>
    </div>
  )
}
