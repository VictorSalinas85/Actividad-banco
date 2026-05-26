import { createContext, useContext, useState, useCallback, type ReactNode } from 'react'
import type { AuthUser, LoginRequest, LoginResponse } from '../types'
import { authApi } from '../api/resources'

interface AuthContextValue {
  user: AuthUser | null
  token: string | null
  login: (req: LoginRequest) => Promise<LoginResponse>
  logout: () => void
  hasRole: (...roles: string[]) => boolean
  isAuthenticated: boolean
}

const AuthContext = createContext<AuthContextValue | null>(null)

function loadUser(): AuthUser | null {
  try {
    const raw = localStorage.getItem('auth_user')
    return raw ? (JSON.parse(raw) as AuthUser) : null
  } catch {
    return null
  }
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(loadUser)
  const [token, setToken] = useState<string | null>(
    () => localStorage.getItem('jwt_token')
  )

  const login = useCallback(async (req: LoginRequest): Promise<LoginResponse> => {
    const res = await authApi.login(req)
    const payload = res.data.data
    localStorage.setItem('jwt_token', payload.token)
    const authUser: AuthUser = {
      id: payload.userId,
      username: payload.username,
      nombreCompleto: payload.nombreCompleto,
      email: payload.email,
      roles: payload.roles,
    }
    localStorage.setItem('auth_user', JSON.stringify(authUser))
    setToken(payload.token)
    setUser(authUser)
    return payload
  }, [])

  const logout = useCallback(() => {
    localStorage.removeItem('jwt_token')
    localStorage.removeItem('auth_user')
    setToken(null)
    setUser(null)
  }, [])

  const hasRole = useCallback(
    (...roles: string[]) => {
      if (!user) return false
      return roles.some((r) => user.roles.includes(r))
    },
    [user]
  )

  return (
    <AuthContext.Provider
      value={{ user, token, login, logout, hasRole, isAuthenticated: !!token }}
    >
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used inside AuthProvider')
  return ctx
}
