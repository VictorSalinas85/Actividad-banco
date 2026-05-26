# Frontend - Implementación de Interfaz de Usuario

## 1. Introducción

El frontend de la Aplicación de Gestión de Información de un Banco ha sido implementado utilizando **React** con **TypeScript** y **Vite** como herramienta de construcción. La interfaz proporciona una experiencia de usuario moderna y responsiva, diseñada siguiendo las restricciones de acceso por rol definidas en los requisitos de negocio.

---

## 2. Stack Tecnológico

### Dependencias Principales

- **React 18.2.0** - Framework UI
- **TypeScript** - Lenguaje tipado para JavaScript
- **Vite** - Bundler moderno y herramienta de desarrollo
- **React Router** - Enrutamiento de aplicación
- **Tailwind CSS** - Framework de utilidad CSS
- **Axios** - Cliente HTTP para consumir APIs REST

### Configuración del Proyecto

```json
{
  "name": "banco-frontend",
  "version": "1.0.0",
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "tsc && vite build",
    "preview": "vite preview"
  }
}
```

---

## 3. Estructura de Carpetas

```
Banco_frontend/
├── src/
│   ├── api/                 # Capa de integración con backend
│   │   ├── client.ts        # Configuración de Axios
│   │   └── resources.ts     # Endpoints y recursos
│   │
│   ├── auth/                # Autenticación y autorización
│   │   ├── AuthContext.tsx  # Contexto de autenticación
│   │   └── RequireAuth.tsx  # Protección de rutas
│   │
│   ├── components/          # Componentes reutilizables
│   │   ├── CrudTable.tsx    # Tabla genérica CRUD
│   │   ├── Layout.tsx       # Layout principal
│   │   ├── Modal.tsx        # Modal genérico
│   │   ├── OpSection.tsx    # Secciones de operación
│   │   ├── Sidebar.tsx      # Barra lateral de navegación
│   │   └── Spinner.tsx      # Indicador de carga
│   │
│   ├── pages/               # Páginas principales
│   │   ├── Dashboard.tsx    # Panel principal
│   │   ├── Login.tsx        # Página de login
│   │   ├── NoAutorizado.tsx # Página de acceso denegado
│   │   ├── catalogos/       # Gestión de catálogos
│   │   ├── empresas/        # Gestión de empresas
│   │   ├── personas/        # Gestión de personas
│   │   └── usuarios/        # Gestión de usuarios y roles
│   │
│   ├── types/
│   │   └── index.ts         # Tipos y interfaces TypeScript
│   │
│   ├── index.css            # Estilos globales
│   ├── main.tsx             # Punto de entrada
│   └── App.tsx              # Componente raíz
│
├── index.html               # HTML base
├── vite.config.ts           # Configuración de Vite
├── tailwind.config.js       # Configuración de Tailwind
├── tsconfig.json            # Configuración de TypeScript
└── package.json             # Dependencias
```

---

## 4. Módulos Principales

### 4.1 Autenticación (`auth/`)

#### AuthContext.tsx
- Gestiona el estado global de autenticación
- Almacena información del usuario autenticado
- Proporciona métodos de login y logout
- Persiste el token en localStorage

**Funcionalidades:**
- Autenticación basada en tokens JWT
- Contexto React para acceso global
- Validación de sesión activa

#### RequireAuth.tsx
- Protege rutas que requieren autenticación
- Valida permisos según rol de usuario
- Redirige a login si no está autenticado
- Redirige a "No Autorizado" si no tiene permisos

### 4.2 Capa API (`api/`)

#### client.ts
- Configuración centralizada de Axios
- Interceptores para agregar token a peticiones
- Manejo de errores de respuesta
- Base URL configurable

#### resources.ts
- Definición de endpoints REST
- Funciones para cada operación CRUD
- Métodos para:
  - Usuarios y autenticación
  - Personas naturales
  - Empresas
  - Cuentas bancarias
  - Préstamos
  - Transferencias
  - Catálogos
  - Bitácora de operaciones

### 4.3 Componentes Reutilizables (`components/`)

#### Layout.tsx
- Estructura base de la aplicación
- Incluye Sidebar y área de contenido
- Gestiona la navegación principal

#### Sidebar.tsx
- Menú de navegación lateral
- Opciones dinámicas según rol del usuario
- Enlaces a secciones principales

#### CrudTable.tsx
- Tabla genérica para listar datos
- Acciones CRUD (Create, Read, Update, Delete)
- Paginación y búsqueda
- Filtros por columnas

#### Modal.tsx
- Modal genérico para formularios
- Creación y edición de registros
- Validación de campos
- Mensajes de confirmación

#### OpSection.tsx
- Secciones específicas para operaciones
- Flujos de aprobación visualizados
- Estados de transferencias y préstamos

#### Spinner.tsx
- Indicador de carga
- Overlay durante peticiones HTTP

### 4.4 Páginas (`pages/`)

#### Login.tsx
- Formulario de autenticación
- Validación de credenciales
- Redirección tras login exitoso
- Manejo de errores

#### Dashboard.tsx
- Panel principal con resumen
- Widgets informativos por rol
- Acceso rápido a operaciones comunes

#### NoAutorizado.tsx
- Página de acceso denegado
- Información sobre permisos requeridos
- Opción de volver a dashboard

#### catalogos/ - CatalogosPage.tsx
- CRUD de productos bancarios
- Gestión de tipos de cuenta, préstamo, etc.

#### empresas/ - EmpresasPage.tsx
- Listado de empresas clientes
- Creación de nuevas empresas
- Visualización de detalles

#### empresas/ - EmpresaUsuariosPage.tsx
- Gestión de usuarios dentro de empresas
- Asignación de roles operativos
- Supervisores de empresa

#### personas/ - PersonasPage.tsx
- CRUD de personas naturales
- Validación de identificación
- Datos de contacto y dirección

#### usuarios/ - UsuariosPage.tsx
- Gestión centralizada de usuarios
- Asignación de roles
- Cambio de estados (Activo, Inactivo, Bloqueado)

#### usuarios/ - RolesPage.tsx
- Definición de roles del sistema
- Asignación de permisos por rol
- Roles predefinidos según banco.md

#### usuarios/ - SesionesPage.tsx
- Historial de sesiones activas
- Monitoreo de acceso
- Cierre de sesiones

---

## 5. Tipos TypeScript (`types/index.ts`)

Define interfaces para todas las entidades principales:

```typescript
interface Usuario {
  ID_Usuario: number;
  ID_Relacionado: string;
  Nombre_Completo: string;
  ID_Identificacion: string;
  Correo_Electronico: string;
  Telefono: string;
  Fecha_Nacimiento: string;
  Direccion: string;
  Rol_Sistema: string;
  Estado_Usuario: string;
}

interface Persona {
  ID_Identificacion: string;
  Nombre_Completo: string;
  Correo_Electronico: string;
  Telefono: string;
  Fecha_Nacimiento: string;
  Direccion: string;
}

interface Empresa {
  NIT: string;
  Razon_Social: string;
  Correo_Electronico: string;
  Telefono: string;
  Direccion: string;
  ID_Representante_Legal: string;
}

interface Cuenta {
  Numero_Cuenta: string;
  Tipo_Cuenta: string;
  ID_Titular: string;
  Saldo_Actual: number;
  Moneda: string;
  Estado_Cuenta: string;
  Fecha_Apertura: string;
}

interface Prestamo {
  ID_Prestamo: number;
  Tipo_Prestamo: string;
  ID_Cliente_Solicitante: string;
  Monto_Solicitado: number;
  Monto_Aprobado: number;
  Tasa_Interes: number;
  Plazo_Meses: number;
  Estado_Prestamo: string;
  Fecha_Aprobacion?: string;
  Fecha_Desembolso?: string;
  Cuenta_Destino_Desembolso?: string;
}

interface Transferencia {
  ID_Transferencia: number;
  Cuenta_Origen: string;
  Cuenta_Destino: string;
  Monto: number;
  Fecha_Creacion: string;
  Fecha_Aprobacion?: string;
  Estado_Transferencia: string;
  ID_Usuario_Creador: number;
  ID_Usuario_Aprobador?: number;
}
```

---

## 6. Control de Acceso por Rol

El frontend implementa restricciones de acceso basadas en roles:

### Roles Soportados

1. **Cliente Persona Natural** - Acceso a sus propios productos
2. **Cliente Empresa** - Gestión de productos empresariales
3. **Empleado de Ventanilla** - Operaciones de caja
4. **Empleado Comercial** - Gestión de clientes y productos
5. **Empleado de Empresa** - Operaciones diarias de empresa
6. **Supervisor de Empresa** - Aprobación de transferencias
7. **Analista Interno del Banco** - Análisis y aprobación de préstamos

### Implementación

- **RequireAuth.tsx** valida permisos antes de renderizar
- **Sidebar.tsx** muestra opciones según rol
- **CrudTable.tsx** oculta acciones no permitidas
- Mensaje "No Autorizado" para accesos denegados

---

## 7. Flujos de Aprobación Visualizados

### Flujo de Préstamos
```
Solicitud → En Estudio → Aprobado/Rechazado → Desembolso
```

### Flujo de Transferencias Empresariales
```
Creación → Monto > Umbral? 
  ├─ Sí → Espera de Aprobación → Aprobado/Rechazado/Vencido
  └─ No → Ejecutada
```

---

## 8. Estilos y Diseño

### Tailwind CSS
- Configuración completa de utilidades
- Tema personalizado según marca
- Responsive en todos los dispositivos

### PostCSS
- Procesamiento de estilos
- Optimización de producción

---

## 9. Configuración de Desarrollo

### Vite
- Hot Module Replacement (HMR) para desarrollo rápido
- Optimización automática para producción
- Soporte para TypeScript nativo

### Scripts Disponibles

```bash
npm run dev      # Inicia servidor de desarrollo (puerto 5173)
npm run build    # Construye para producción
npm run preview  # Vista previa de producción local
```

---

## 10. Variables de Entorno

Archivo `.env.example`:
```
VITE_API_URL=http://localhost:8080/api
VITE_APP_NAME=Banco SDD
```

---

## 11. Características Implementadas

### Autenticación y Seguridad
- ✓ Login con validación de credenciales
- ✓ Token JWT en localStorage
- ✓ Protección de rutas por rol
- ✓ Logout con limpieza de sesión

### Gestión de Usuarios
- ✓ CRUD de usuarios
- ✓ Asignación de roles
- ✓ Estados de usuario (Activo, Inactivo, Bloqueado)

### Gestión de Clientes
- ✓ CRUD de personas naturales
- ✓ CRUD de empresas
- ✓ Gestión de usuarios de empresa

### Operaciones Bancarias
- ✓ Creación de cuentas
- ✓ Solicitud de préstamos
- ✓ Transferencias entre cuentas
- ✓ Visualización de saldos

### Aprobaciones
- ✓ Interfaz para análistas: aprobar/rechazar préstamos
- ✓ Interfaz para supervisores: aprobar transferencias
- ✓ Estados visuales del flujo de aprobación

### Auditoría
- ✓ Visualización de bitácora de operaciones
- ✓ Filtrado por tipo de operación y usuario
- ✓ Acceso según restricciones de rol

---

## 12. Próximos Pasos Recomendados

1. **Testing** - Implementar tests unitarios y E2E con Vitest y Playwright
2. **Internacionalización** - Soporte para múltiples idiomas
3. **Offline Support** - Service Workers para funcionalidad sin conexión
4. **Analytics** - Integración de herramientas de monitoreo
5. **Documentación Interactiva** - Storybook para componentes

---

## 13. Conclusión

El frontend proporciona una interfaz completa y segura para la gestión de operaciones bancarias, con énfasis en:

- **Seguridad** - Control de acceso basado en roles
- **Usabilidad** - Interfaz intuitiva y responsiva
- **Mantenibilidad** - Código modular con TypeScript
- **Escalabilidad** - Arquitectura preparada para crecimiento

La aplicación está lista para integración con el backend Java/Spring Boot definido en los requisitos.
