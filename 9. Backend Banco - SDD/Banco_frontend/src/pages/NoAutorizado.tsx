import { Link } from 'react-router-dom'

export default function NoAutorizado() {
  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-50">
      <div className="text-center">
        <div className="text-6xl mb-4">🔒</div>
        <h1 className="text-2xl font-bold text-slate-800 mb-2">Acceso no autorizado</h1>
        <p className="text-slate-500 mb-6">No tienes permisos para acceder a esta sección.</p>
        <Link to="/dashboard" className="btn-primary inline-block">
          Volver al dashboard
        </Link>
      </div>
    </div>
  )
}
