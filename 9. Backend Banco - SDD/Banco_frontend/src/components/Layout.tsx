import { Outlet } from 'react-router-dom'
import Sidebar from './Sidebar'
import { useAuth } from '../auth/AuthContext'

export default function Layout() {
  const { user, logout } = useAuth()

  return (
    <div className="flex min-h-screen">
      <Sidebar />
      <div className="flex-1 flex flex-col min-w-0">
        <header className="bg-white border-b border-slate-200 px-8 py-3 flex items-center justify-between shadow-sm">
          <div />
          <div className="flex items-center gap-4">
            <div className="text-right">
              <p className="text-sm font-semibold text-slate-800">{user?.nombreCompleto}</p>
              <p className="text-xs text-slate-500">{user?.roles.join(', ')}</p>
            </div>
            <button
              onClick={logout}
              className="text-sm text-red-600 hover:text-red-800 font-medium transition-colors"
            >
              Cerrar sesión
            </button>
          </div>
        </header>
        <main className="flex-1 p-8 bg-slate-50 overflow-auto">
          <Outlet />
        </main>
      </div>
    </div>
  )
}
