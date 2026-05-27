import { Link } from 'react-router-dom'

export default function NoAutorizado() {
  return (
    <div className="min-h-screen flex items-center justify-center bg-navy-gradient relative overflow-hidden">
      <div className="absolute -top-32 -right-32 w-96 h-96 bg-gold-500/20 rounded-full blur-3xl" />
      <div className="absolute -bottom-32 -left-32 w-96 h-96 bg-gold-400/10 rounded-full blur-3xl" />
      <div className="relative text-center bg-white/95 backdrop-blur p-10 rounded-2xl shadow-2xl border-t-4 border-gold-500 max-w-md mx-4">
        <img src="/logo.png" alt="Wolfstreet Bank" className="w-28 h-28 mx-auto mb-4 object-contain" />
        <div className="text-5xl mb-3">🔒</div>
        <h1 className="text-2xl font-bold text-navy-900 mb-2">Acceso no autorizado</h1>
        <p className="text-ink-500 mb-6">No tienes permisos para acceder a esta sección.</p>
        <Link to="/dashboard" className="btn-primary inline-block">
          Volver al dashboard
        </Link>
      </div>
    </div>
  )
}
