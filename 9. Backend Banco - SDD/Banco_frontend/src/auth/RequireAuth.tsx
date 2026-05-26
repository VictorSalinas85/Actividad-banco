import { Navigate, useLocation } from 'react-router-dom'
import { useAuth } from './AuthContext'
import type { ReactNode } from 'react'

interface Props {
  children: ReactNode
  roles?: string[]
}

export default function RequireAuth({ children, roles }: Props) {
  const { isAuthenticated, hasRole } = useAuth()
  const location = useLocation()

  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />
  }

  if (roles && roles.length > 0 && !hasRole(...roles)) {
    return <Navigate to="/no-autorizado" replace />
  }

  return <>{children}</>
}
