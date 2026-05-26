import axios from 'axios'
import toast from 'react-hot-toast'

const api = axios.create({
  baseURL: '/api/v1',
  headers: { 'Content-Type': 'application/json' },
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('jwt_token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

api.interceptors.response.use(
  (res) => res,
  (error) => {
    const status = error.response?.status
    const message = error.response?.data?.message || 'Error de conexión'
    if (status === 401) {
      localStorage.removeItem('jwt_token')
      localStorage.removeItem('auth_user')
      window.location.href = '/login'
    } else if (status === 403) {
      toast.error('No tienes permisos para esta acción')
    } else if (status >= 500) {
      toast.error('Error del servidor: ' + message)
    }
    return Promise.reject(error)
  }
)

export default api
