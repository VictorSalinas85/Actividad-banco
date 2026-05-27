import { useState } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import toast from 'react-hot-toast'

export default function Login() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const from = (location.state as { from?: { pathname: string } })?.from?.pathname ?? '/dashboard'

  const [form, setForm] = useState({ username: '', password: '' })
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)
    try {
      await login(form)
      navigate(from, { replace: true })
    } catch (err: unknown) {
      const msg =
        (err as { response?: { data?: { message?: string } } }).response?.data?.message
        ?? 'Credenciales incorrectas'
      toast.error(msg)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen relative flex items-center justify-center bg-navy-gradient overflow-hidden">
      {/* Halos decorativos dorados */}
      <div className="absolute -top-32 -left-32 w-96 h-96 bg-gold-500/20 rounded-full blur-3xl pointer-events-none" />
      <div className="absolute -bottom-32 -right-32 w-[28rem] h-[28rem] bg-gold-400/10 rounded-full blur-3xl pointer-events-none" />
      <div className="absolute inset-0 bg-[radial-gradient(circle_at_50%_120%,rgba(212,162,78,0.18),transparent_55%)] pointer-events-none" />

      <div className="relative w-full max-w-5xl mx-4 grid lg:grid-cols-5 gap-8 items-center">
        {/* Lado izquierdo: logo protagonista (el logo ya incluye 'WOLFSTREET BANK') */}
        <div className="lg:col-span-3 flex flex-col items-center text-center">
          <div className="relative">
            <div className="absolute -inset-8 bg-gold-gradient opacity-20 rounded-full blur-3xl" />
            <img
              src="/logo.png"
              alt="Wolfstreet Bank"
              className="relative w-80 h-80 lg:w-[28rem] lg:h-[28rem] object-contain drop-shadow-[0_15px_45px_rgba(212,162,78,0.45)] select-none"
              draggable={false}
            />
          </div>
          <div className="mt-2 h-[2px] w-56 bg-gradient-to-r from-transparent via-gold-400 to-transparent" />
          <p className="mt-4 text-navy-100 text-base lg:text-lg max-w-md">
            Sistema integral de gestión bancaria. Operaciones, clientes y auditoría
            en una sola plataforma.
          </p>
        </div>

        {/* Lado derecho: formulario */}
        <div className="lg:col-span-2">
          <div className="bg-white/95 backdrop-blur rounded-2xl shadow-2xl border-t-4 border-gold-500 p-8">
            <div className="text-center mb-6">
              <p className="brand-sub">Acceso seguro</p>
              <h2 className="text-2xl font-bold text-navy-900 mt-1">Iniciar sesión</h2>
              <div className="divider-gold mt-3" />
            </div>

            <form onSubmit={handleSubmit} className="space-y-5">
              <div>
                <label className="label">Usuario</label>
                <input
                  type="text"
                  className="input"
                  placeholder="Ingrese su usuario"
                  value={form.username}
                  onChange={(e) => setForm({ ...form, username: e.target.value })}
                  required
                  autoFocus
                />
              </div>

              <div>
                <label className="label">Contraseña</label>
                <input
                  type="password"
                  className="input"
                  placeholder="Ingrese su contraseña"
                  value={form.password}
                  onChange={(e) => setForm({ ...form, password: e.target.value })}
                  required
                />
              </div>

              <button
                type="submit"
                className="btn-primary w-full py-2.5 text-base"
                disabled={loading}
              >
                {loading ? 'Iniciando sesión...' : 'Entrar al sistema'}
              </button>
            </form>

            <p className="text-center text-xs text-ink-400 mt-6">
              Wolfstreet Bank &copy; {new Date().getFullYear()} · Todos los derechos reservados
            </p>
          </div>
        </div>
      </div>
    </div>
  )
}
