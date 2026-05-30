import { Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './auth/AuthContext'
import RequireAuth from './auth/RequireAuth'
import Layout from './components/Layout'

import Login from './pages/Login'
import NoAutorizado from './pages/NoAutorizado'
import Dashboard from './pages/Dashboard'

import CatalogosPage from './pages/catalogos/CatalogosPage'
import UsuariosPage from './pages/usuarios/UsuariosPage'
import RolesPage from './pages/usuarios/RolesPage'
import SesionesPage from './pages/usuarios/SesionesPage'

import PersonasPage from './pages/personas/PersonasPage'
import EmpresasPage from './pages/empresas/EmpresasPage'
import EmpresaUsuariosPage from './pages/empresas/EmpresaUsuariosPage'

import CuentasPage from './pages/cuentas/CuentasPage'
import MovimientosPage from './pages/cuentas/MovimientosPage'

import PrestamosPage from './pages/prestamos/PrestamosPage'
import PrestamoAprobacionesPage from './pages/prestamos/PrestamoAprobacionesPage'
import PrestamoDesembolsosPage from './pages/prestamos/PrestamoDesembolsosPage'

import TransferenciasPage from './pages/transferencias/TransferenciasPage'
import TransferenciaAprobacionesPage from './pages/transferencias/TransferenciaAprobacionesPage'

import ProductosPage from './pages/productos/ProductosPage'

import BitacoraPage from './pages/auditoria/BitacoraPage'
import CambiosPage from './pages/auditoria/CambiosPage'
import ErroresPage from './pages/auditoria/ErroresPage'

import OpsClientesPage from './pages/ops/OpsClientesPage'
import OpsCuentasPage from './pages/ops/OpsCuentasPage'
import OpsPrestamosPage from './pages/ops/OpsPrestamosPage'
import OpsTransferenciasPage from './pages/ops/OpsTransferenciasPage'
import OpsSesionesPage from './pages/ops/OpsSesionesPage'
import OpsAuditoriaPage from './pages/ops/OpsAuditoriaPage'

const ANALISTA = 'ANALISTA_INTERNO'
const VENTANILLA = 'EMPLEADO_VENTANILLA'
const COMERCIAL = 'EMPLEADO_COMERCIAL'
const CLIENTE = 'CLIENTE_PERSONA'
const EMP_ADMIN = 'CLIENTE_EMPRESA_ADMIN'
const EMP_OP = 'EMPLEADO_EMPRESA_OPERATIVO'
const EMP_SUP = 'SUPERVISOR_EMPRESA'
const ALL = [ANALISTA, VENTANILLA, COMERCIAL, CLIENTE, EMP_ADMIN, EMP_OP, EMP_SUP]

export default function App() {
  return (
    <AuthProvider>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/no-autorizado" element={<NoAutorizado />} />

        <Route
          path="/"
          element={
            <RequireAuth>
              <Layout />
            </RequireAuth>
          }
        >
          <Route index element={<Navigate to="/dashboard" replace />} />
          <Route path="dashboard" element={<RequireAuth roles={ALL}><Dashboard /></RequireAuth>} />

          {/* Configuración */}
          <Route path="catalogos" element={<RequireAuth roles={[ANALISTA]}><CatalogosPage /></RequireAuth>} />
          <Route path="usuarios"  element={<RequireAuth roles={[ANALISTA]}><UsuariosPage /></RequireAuth>} />
          <Route path="roles"     element={<RequireAuth roles={[ANALISTA]}><RolesPage /></RequireAuth>} />
          <Route path="sesiones"  element={<RequireAuth roles={[ANALISTA]}><SesionesPage /></RequireAuth>} />

          {/* Clientes */}
          <Route path="personas" element={<RequireAuth roles={[ANALISTA, VENTANILLA, COMERCIAL]}><PersonasPage /></RequireAuth>} />
          <Route path="empresas" element={<RequireAuth roles={[ANALISTA, COMERCIAL]}><EmpresasPage /></RequireAuth>} />
          <Route path="empresa-usuarios" element={<RequireAuth roles={[ANALISTA, COMERCIAL]}><EmpresaUsuariosPage /></RequireAuth>} />

          {/* Banca */}
          <Route path="cuentas"        element={<RequireAuth roles={[ANALISTA, VENTANILLA, COMERCIAL, EMP_SUP]}><CuentasPage /></RequireAuth>} />
          <Route path="movimientos"    element={<RequireAuth roles={[ANALISTA, VENTANILLA]}><MovimientosPage /></RequireAuth>} />
          <Route path="prestamos"      element={<RequireAuth roles={[ANALISTA, COMERCIAL]}><PrestamosPage /></RequireAuth>} />
          <Route path="prestamos/aprobaciones" element={<RequireAuth roles={[ANALISTA, COMERCIAL]}><PrestamoAprobacionesPage /></RequireAuth>} />
          <Route path="prestamos/desembolsos"  element={<RequireAuth roles={[ANALISTA, COMERCIAL]}><PrestamoDesembolsosPage /></RequireAuth>} />
          <Route path="transferencias" element={<RequireAuth roles={[ANALISTA, VENTANILLA, EMP_SUP, EMP_OP]}><TransferenciasPage /></RequireAuth>} />
          <Route path="transferencias/aprobaciones" element={<RequireAuth roles={[ANALISTA, EMP_SUP]}><TransferenciaAprobacionesPage /></RequireAuth>} />
          <Route path="productos"      element={<RequireAuth roles={ALL}><ProductosPage /></RequireAuth>} />

          {/* Operaciones (SPs) */}
          <Route path="ops/clientes"       element={<RequireAuth roles={[ANALISTA, VENTANILLA, COMERCIAL, EMP_ADMIN]}><OpsClientesPage /></RequireAuth>} />
          <Route path="ops/cuentas"        element={<RequireAuth roles={[ANALISTA, VENTANILLA, COMERCIAL, CLIENTE, EMP_ADMIN, EMP_OP]}><OpsCuentasPage /></RequireAuth>} />
          <Route path="ops/prestamos"      element={<RequireAuth roles={[ANALISTA, COMERCIAL, VENTANILLA, CLIENTE, EMP_ADMIN]}><OpsPrestamosPage /></RequireAuth>} />
          <Route path="ops/transferencias" element={<RequireAuth roles={[ANALISTA, VENTANILLA, CLIENTE, EMP_SUP, EMP_ADMIN, EMP_OP]}><OpsTransferenciasPage /></RequireAuth>} />
          <Route path="ops/sesiones"       element={<RequireAuth roles={[ANALISTA, VENTANILLA, COMERCIAL]}><OpsSesionesPage /></RequireAuth>} />
          <Route path="ops/auditoria"      element={<RequireAuth roles={[ANALISTA]}><OpsAuditoriaPage /></RequireAuth>} />

          {/* Auditoría */}
          <Route path="auditoria/bitacora" element={<RequireAuth roles={[ANALISTA]}><BitacoraPage /></RequireAuth>} />
          <Route path="auditoria/cambios"  element={<RequireAuth roles={[ANALISTA]}><CambiosPage /></RequireAuth>} />
          <Route path="auditoria/errores"  element={<RequireAuth roles={[ANALISTA]}><ErroresPage /></RequireAuth>} />
        </Route>

        <Route path="*" element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </AuthProvider>
  )
}
