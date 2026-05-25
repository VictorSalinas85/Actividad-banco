# Generación del Backend — Banco Core API

**Proyecto:** Actividad Banco — Bases de Datos 2  
**Semestre:** 4  
**Fecha:** Mayo 2026  
**Tecnología:** Spring Boot 3 · Java 17 · Spring Data JPA · MariaDB

---

## Índice

1. [Configuración inicial del proyecto](#1-configuración-inicial-del-proyecto)
2. [Configuración de seguridad y helpers](#2-configuración-de-seguridad-y-helpers)
3. [Enumeraciones de dominio](#3-enumeraciones-de-dominio)
4. [Excepciones de dominio](#4-excepciones-de-dominio)
5. [Entidades JPA](#5-entidades-jpa)
6. [Repositorios JPA](#6-repositorios-jpa)
7. [DTOs de procedimientos almacenados](#7-dtos-de-procedimientos-almacenados)
8. [Servicios de procedimientos almacenados](#8-servicios-de-procedimientos-almacenados)
9. [Excepciones CRUD](#9-excepciones-crud)
10. [DTOs CRUD](#10-dtos-crud)
11. [Servicios CRUD](#11-servicios-crud)
12. [Historial de commits](#12-historial-de-commits)

---

## 1. Configuración inicial del proyecto

### 1.1 `pom.xml` — Corrección de dependencias de prueba

El archivo `pom.xml` original contenía dependencias de test inválidas que no existen como artefactos Maven:
- `spring-boot-starter-data-jpa-test`
- `spring-boot-starter-security-test`
- `spring-boot-starter-webmvc-test`

**Corrección:** Se reemplazaron por las dependencias oficiales:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
```

### 1.2 `application.properties` — Variables de entorno y configuración JPA

Se configuró el datasource con variables de entorno con valores por defecto para desarrollo local:

```properties
spring.datasource.url=jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:banco_bd}?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=${DB_USERNAME:banco_user}
spring.datasource.password=${DB_PASSWORD:Banco123}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.open-in-view=false
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.database-platform=org.hibernate.dialect.MariaDBDialect
```

**Nota:** Los scripts SQL usan `banco_core` como nombre de base de datos (`USE banco_core;`), mientras que `application.properties` usa `banco_bd`. Se mantuvo `banco_bd` como nombre de conexión según la intención del proyecto.

**Decisión clave — `ddl-auto=validate`:** Hibernate valida el esquema contra las entidades JPA sin modificar la base de datos. El esquema real es gestionado exclusivamente por los scripts SQL del proyecto.

---

## 2. Configuración de seguridad y helpers

### 2.1 `BancoSecurityConfig.java`

Configuración provisional de Spring Security. CSRF deshabilitado y todos los endpoints permitidos sin autenticación (placeholder para JWT futuro):

```
Banco/src/main/java/bd2/Banco/config/BancoSecurityConfig.java
```

- `@Configuration`
- `SecurityFilterChain` bean con `csrf.disable()` y `anyRequest().permitAll()`

### 2.2 `ProcedureCallHelper.java`

Helper centralizado que encapsula el uso de `EntityManager` para llamar procedimientos almacenados con parámetros OUT. Centralizar aquí fue la decisión de diseño correcta porque:

- Varios SPs comparten el mismo contrato de salida (`out_code`, `out_message`, `out_reference`)
- Poner `@NamedStoredProcedureQuery` en las entidades violaría SRP
- `JpaRepository` no soporta OUT parameters sin este puente

```
Banco/src/main/java/bd2/Banco/config/ProcedureCallHelper.java
```

**Dos métodos:**

| Método | Descripción | Usado por |
|--------|-------------|-----------|
| `ejecutar(nombre, params)` | Registra parámetros IN + 3 OUT, ejecuta, retorna `SpResultado` | 22 SP services |
| `ejecutarConResultado(nombre, params)` | Ejecuta sin registrar OUT params, retorna `StoredProcedureQuery` (result set) | 2 SP services sin OUT params |

**Contrato estándar de salida de SPs:**
```java
public class SpResultado {
    private String code;      // "OK" = éxito
    private String message;   // mensaje descriptivo
    private String reference; // referencia de entidad creada/afectada
    public boolean exitoso()  { return "OK".equals(code); }
    public boolean fallido()  { return !exitoso(); }
}
```

---

## 3. Enumeraciones de dominio

**Ubicación:** `Banco/src/main/java/bd2/Banco/domain/enums/`  
**Total:** 17 enumeraciones

Se crearon dos categorías de enums:

### 3.1 Enums mapeados a restricciones CHECK de la base de datos

Valores exactos según los `CHECK` constraints de los scripts SQL:

| Enum | Valores |
|------|---------|
| `TipoParticipante` | `PERSONA`, `EMPRESA` |
| `NaturalezaMovimiento` | `DEBITO`, `CREDITO` |
| `DecisionAprobacionPrestamo` | `APROBADO`, `RECHAZADO` |
| `DecisionAprobacionTransferencia` | `APROBADO`, `RECHAZADO` |
| `AplicaTipoIdentificacion` | `PERSONA_NATURAL`, `EMPRESA`, `AMBOS` |
| `ProductoTipo` | `CUENTA`, `PRESTAMO` |
| `AccionAuditoria` | `INSERT`, `UPDATE`, `DELETE`, `LOGIN`, `LOGOUT`, `SP_CALL`, `BLOQUEO`, `DESBLOQUEO` |
| `EntidadTransicion` | `CUENTA`, `PRESTAMO`, `TRANSFERENCIA`, `USUARIO`, `SESION` |
| `CategoriaProducto` | `CORRIENTE`, `AHORRO`, `PERSONAL`, `HIPOTECARIO`, `EMPRESARIAL` |

### 3.2 Enums para códigos de catálogos (tablas cat_*)

Valores de negocio presentes en las tablas de catálogo:

| Enum | Valores representativos |
|------|------------------------|
| `CodigoEstadoUsuario` | `ACTIVO`, `INACTIVO`, `BLOQUEADO`, `PENDIENTE_VERIFICACION` |
| `CodigoEstadoCuenta` | `ACTIVA`, `INACTIVA`, `BLOQUEADA`, `CANCELADA`, `EN_MORA` |
| `CodigoEstadoPrestamo` | `SOLICITADO`, `EN_REVISION`, `APROBADO`, `RECHAZADO`, `DESEMBOLSADO`, `EN_MORA`, `PAGADO`, `CASTIGADO` |
| `CodigoEstadoTransferencia` | `PENDIENTE_APROBACION`, `APROBADA`, `RECHAZADA`, `EJECUTADA`, `FALLIDA`, `VENCIDA` |
| `CodigoEstadoSesion` | `ACTIVA`, `EXPIRADA`, `CERRADA`, `REVOCADA` |
| `CodigoCanal` | `WEB`, `MOVIL`, `API`, `CAJERO`, `SUCURSAL` |
| `CodigoRolEmpresa` | `INICIADOR`, `APROBADOR`, `CONSULTOR`, `ADMINISTRADOR` |
| `CodigoRolSistema` | `SOPORTE_INFORMACION`, `AUDITOR`, `OPERADOR`, `ADMINISTRADOR` |

**Anotación usada en entidades:**
```java
@Enumerated(EnumType.STRING)
@Column(name = "naturaleza", nullable = false, length = 10)
private NaturalezaMovimiento naturaleza;
```

---

## 4. Excepciones de dominio

**Ubicación:** `Banco/src/main/java/bd2/Banco/domain/exceptions/`  
**Total:** 17 excepciones (9 originales + 8 CRUD)

### 4.1 Jerarquía base

```
RuntimeException
  └── DomainException(String code, String message)
        ├── ProcedimientoBancoException
        ├── ClienteNoEncontradoException
        ├── IdentificacionDuplicadaException
        ├── CuentaInactivaException
        ├── SaldoInsuficienteException
        ├── TransferenciaInvalidaException
        ├── PrestamoNoAprobableException
        ├── PermisoInsuficienteException
        └── [excepciones CRUD — ver sección 9]
```

### 4.2 Excepciones originales del dominio de negocio

| Excepción | Código | Descripción |
|-----------|--------|-------------|
| `DomainException` | (base abstracta) | Superclase de todas las excepciones del dominio |
| `ProcedimientoBancoException` | `DOM-SP-500` | Error retornado por un SP (code ≠ "OK") |
| `ClienteNoEncontradoException` | `DOM-CLI-404` | Cliente o usuario no existe |
| `IdentificacionDuplicadaException` | `DOM-CLI-409` | Número de identificación ya registrado |
| `CuentaInactivaException` | `DOM-CTA-403` | Operación sobre cuenta sin estado ACTIVA |
| `SaldoInsuficienteException` | `DOM-CTA-422` | Fondos insuficientes para la operación |
| `TransferenciaInvalidaException` | `DOM-TRF-400` | Transferencia en estado que no permite la acción |
| `PrestamoNoAprobableException` | `DOM-CRE-422` | Préstamo no cumple condiciones de aprobación |
| `PermisoInsuficienteException` | `DOM-SEC-403` | Usuario sin el rol requerido para la operación |

---

## 5. Entidades JPA

**Ubicación:** `Banco/src/main/java/bd2/Banco/domain/entities/`  
**Total:** 36 entidades

### Convenciones aplicadas en todas las entidades

- `@Entity`, `@Table(name = "nombre_tabla")` en cada clase
- `@Id @GeneratedValue(strategy = GenerationType.IDENTITY)` para `BIGINT UNSIGNED AUTO_INCREMENT`
- `@EqualsAndHashCode(onlyExplicitlyIncluded = true)` + `@EqualsAndHashCode.Include` en el ID (JPA-safe, evita problemas con proxies Hibernate)
- `@ManyToOne(fetch = FetchType.LAZY)` para todas las FK (evita N+1 queries por defecto)
- `@CreationTimestamp` / `@UpdateTimestamp` para `fechaCreacion` / `fechaActualizacion`
- `@Enumerated(EnumType.STRING)` para campos mapeados a enums
- `DECIMAL(18,2)` → `BigDecimal`
- `DATETIME(6)` → `LocalDateTime`
- `DATE` → `LocalDate`
- `TINYINT(1)` → `Boolean`
- `JSON` → `String` con `@Column(columnDefinition = "json")`

### 5.1 Catálogos (17 entidades)

Tablas de referencia y configuración del sistema:

| Clase | Tabla | Campos clave |
|-------|-------|-------------|
| `CatEstadoUsuario` | `cat_estado_usuario` | `codigo` (CodigoEstadoUsuario) |
| `CatEstadoCuenta` | `cat_estado_cuenta` | `codigo` (CodigoEstadoCuenta) |
| `CatTipoCuenta` | `cat_tipo_cuenta` | `codigo`, `tasaInteres` (BigDecimal) |
| `CatMoneda` | `cat_moneda` | `codigo`, `simbolo` |
| `CatEstadoPrestamo` | `cat_estado_prestamo` | `codigo` (CodigoEstadoPrestamo) |
| `CatTipoPrestamo` | `cat_tipo_prestamo` | `tasaInteresAnual` (BigDecimal), `plazoMaxMeses` |
| `CatEstadoTransferencia` | `cat_estado_transferencia` | `codigo` (CodigoEstadoTransferencia) |
| `CatTipoOperacion` | `cat_tipo_operacion` | `naturaleza` (NaturalezaMovimiento) |
| `CatParametroNegocio` | `cat_parametro_negocio` | `valorTexto`, `valorNumerico`, `valorBoolean` |
| `CatRolEmpresa` | `cat_rol_empresa` | `codigo` (CodigoRolEmpresa) |
| `CatTipoIdentificacion` | `cat_tipo_identificacion` | `aplica` (AplicaTipoIdentificacion) |
| `CatCanalOperacion` | `cat_canal_operacion` | `codigo` (CodigoCanal) |
| `CatMotivoRechazo` | `cat_motivo_rechazo` | `descripcion` |
| `CatMotivoBloqueo` | `cat_motivo_bloqueo` | `descripcion` |
| `CatEstadoSesion` | `cat_estado_sesion` | `codigo` (CodigoEstadoSesion) |
| `CatTipoMovimiento` | `cat_tipo_movimiento` | `tipoOperacion` (@ManyToOne) |
| `CatTransicionEstado` | `cat_transicion_estado` | `entidad` (EntidadTransicion), `estadoOrigenId`, `estadoDestinoId` |

### 5.2 Seguridad — contexto `sec` (4 entidades)

| Clase | Tabla | Notas |
|-------|-------|-------|
| `SecRol` | `sec_rol` | `codigo` (CodigoRolSistema) |
| `SecUsuario` | `sec_usuario` | `hashPassword` nunca expuesto en Response |
| `SecUsuarioRol` | `sec_usuario_rol` | Clave compuesta lógica: usuario + rol |
| `SecSesion` | `sec_sesion` | `tokenHash`, `estadoSesion` (@ManyToOne) |

### 5.3 Clientes — contexto `cli` (4 entidades)

| Clase | Tabla | Notas |
|-------|-------|-------|
| `CliPersonaNatural` | `cli_persona_natural` | `fechaNacimiento` (LocalDate), `tipoIdentificacion` (@ManyToOne) |
| `CliEmpresa` | `cli_empresa` | `tipoIdentificacion` (@ManyToOne) |
| `CliEmpresaUsuario` | `cli_empresa_usuario` | Asociación empresa ↔ usuario con estado |
| `CliEmpresaUsuarioRol` | `cli_empresa_usuario_rol` | Rol del usuario dentro de la empresa |

### 5.4 Productos y cuentas — contextos `prd` y `cta` (3 entidades)

| Clase | Tabla | Notas |
|-------|-------|-------|
| `PrdProductoBancario` | `prd_producto_bancario` | `tipo` (ProductoTipo), `categoria` (CategoriaProducto) |
| `CtaCuenta` | `cta_cuenta` | `titularTipo` (TipoParticipante) + `titularPersonaId` / `titularEmpresaId` (Long nullable) |
| `CtaMovimiento` | `cta_movimiento` | `naturaleza` (NaturalezaMovimiento), `saldoAnterior`, `saldoPosterior` (BigDecimal) |

**Decisión de diseño — titular polimórfico en `CtaCuenta` y `CrePrestamo`:**  
Las tablas usan un patrón polymorphic-by-column: `titular_tipo` es `PERSONA` o `EMPRESA`, y sólo uno de `titular_persona_id` / `titular_empresa_id` tiene valor. Se mapearon como dos campos `Long` nullable en lugar de usar `@ManyToOne` para evitar joins imposibles (Hibernate no puede hacer un FK join condicional).

### 5.5 Créditos — contexto `cre` (3 entidades)

| Clase | Tabla | Notas |
|-------|-------|-------|
| `CrePrestamo` | `cre_prestamo` | `titularTipo` + `titularPersonaId` / `titularEmpresaId` (igual que CtaCuenta) |
| `CrePrestamoAprobacion` | `cre_prestamo_aprobacion` | `decision` (DecisionAprobacionPrestamo) |
| `CrePrestamoDesembolso` | `cre_prestamo_desembolso` | `montoDesembolsado` (BigDecimal) |

### 5.6 Transferencias — contexto `trf` (2 entidades)

| Clase | Tabla | Notas |
|-------|-------|-------|
| `TrfTransferencia` | `trf_transferencia` | `monto` (BigDecimal), `idempotencyKey` (UNIQUE) |
| `TrfTransferenciaAprobacion` | `trf_transferencia_aprobacion` | `decision` (DecisionAprobacionTransferencia) |

### 5.7 Auditoría — contexto `aud` (3 entidades)

| Clase | Tabla | Notas |
|-------|-------|-------|
| `AudBitacoraEvento` | `aud_bitacora_evento` | `accion` (AccionAuditoria), `detalle` (json) |
| `AudCambioDato` | `aud_cambio_dato` | `valorAnterior`, `valorNuevo` (json) |
| `AudErrorOperacion` | `aud_error_operacion` | Inmutable por diseño — CRUD service protegido |

---

## 6. Repositorios JPA

**Ubicación:** `Banco/src/main/java/bd2/Banco/domain/repositories/`  
**Total:** 36 repositorios

Todos extienden `JpaRepository<Entidad, Long>`. Se añadieron métodos derivados (Spring Data query derivation) según las necesidades de cada entidad:

```java
public interface CatEstadoCuentaRepository extends JpaRepository<CatEstadoCuenta, Long> {
    Optional<CatEstadoCuenta> findByCodigo(CodigoEstadoCuenta codigo);
    boolean existsByCodigo(CodigoEstadoCuenta codigo);
}
```

**Ejemplos de métodos derivados añadidos por módulo:**

| Repositorio | Métodos derivados destacados |
|-------------|------------------------------|
| `SecUsuarioRepository` | `findByEmail`, `existsByEmail`, `findByEstadoUsuario_Codigo` |
| `CliPersonaNaturalRepository` | `findByNumeroIdentificacion`, `existsByNumeroIdentificacion` |
| `CliEmpresaRepository` | `findByNumeroIdentificacion`, `findByRazonSocialContainingIgnoreCase` |
| `CtaCuentaRepository` | `findByNumeroCuenta`, `findByTitularPersonaId`, `findByTitularEmpresaId` |
| `CtaMovimientoRepository` | `findByCuenta_IdOrderByFechaMovimientoDesc` |
| `CrePrestamoRepository` | `findByTitularPersonaId`, `findByTitularEmpresaId` |
| `TrfTransferenciaRepository` | `findByIdempotencyKey`, `findByCuentaOrigen_Id` |
| `AudBitacoraEventoRepository` | `findByUsuario_IdOrderByFechaEventoDesc` |

**Decisión clave:** Se usó exclusivamente `JpaRepository` (Spring Data). No se usó `JdbcTemplate`, `NamedParameterJdbcTemplate` ni `@Query` con SQL nativo, ya que toda la lógica de negocio transaccional está en los stored procedures.

---

## 7. DTOs de procedimientos almacenados

**Ubicación:** `Banco/src/main/java/bd2/Banco/domain/dto/`

### 7.1 DTOs de Request para SPs (24 clases)

Cada DTO encapsula exactamente los parámetros de entrada del SP correspondiente:

| DTO | SP destino |
|-----|-----------|
| `RegistrarErrorRequest` | `sp_aud_registrar_error` |
| `RegistrarEventoRequest` | `sp_aud_registrar_evento` |
| `ValidarSesionRequest` | `sp_sec_validar_sesion` |
| `RevocarSesionRequest` | `sp_sec_revocar_sesion` |
| `CrearPersonaRequest` | `sp_cli_crear_persona_natural` |
| `CrearEmpresaRequest` | `sp_cli_crear_empresa` |
| `AsociarUsuarioEmpresaRequest` | `sp_cli_asociar_usuario_empresa` |
| `CambiarEstadoClienteRequest` | `sp_cli_cambiar_estado_cliente` |
| `AsignarRolEmpresaUsuarioRequest` | `sp_cli_asignar_rol_empresa_usuario` |
| `AbrirCuentaRequest` | `sp_cta_abrir_cuenta` |
| `ConsignarRequest` | `sp_cta_consignar` |
| `RetirarRequest` | `sp_cta_retirar` |
| `BloquearCuentaRequest` | `sp_cta_bloquear_cuenta` |
| `CancelarCuentaRequest` | `sp_cta_cancelar_cuenta` |
| `SolicitarPrestamoRequest` | `sp_cre_solicitar_prestamo` |
| `AprobarPrestamoRequest` | `sp_cre_aprobar_prestamo` |
| `RechazarPrestamoRequest` | `sp_cre_rechazar_prestamo` |
| `DesembolsarPrestamoRequest` | `sp_cre_desembolsar_prestamo` |
| `CrearTransferenciaRequest` | `sp_trf_crear_transferencia` |
| `AprobarTransferenciaRequest` | `sp_trf_aprobar_transferencia` |
| `RechazarTransferenciaRequest` | `sp_trf_rechazar_transferencia` |
| `EjecutarTransferenciaDirectaRequest` | `sp_trf_ejecutar_transferencia_directa` |
| `VencerTransferenciasPendientesRequest` | `sp_trf_vencer_transferencias_pendientes` |
| `ConsultarPendientesEmpresaRequest` | `sp_trf_consultar_pendientes_aprobacion_empresa` |

### 7.2 DTOs de Response para SPs (2 clases)

| DTO | Descripción |
|-----|-------------|
| `SpResultado` | Contrato estándar: `code`, `message`, `reference`. Retornado por 23 SPs |
| `TransferenciaPendienteResponse` | Resultado de `sp_trf_consultar_pendientes_aprobacion_empresa` (result set) |

---

## 8. Servicios de procedimientos almacenados

**Ubicación:** `Banco/src/main/java/bd2/Banco/domain/services/procedures/`  
**Total:** 24 servicios

**Principio de diseño:** Un `@Service` por stored procedure (SOLID SRP estricto). Cada clase tiene un único método público `ejecutar(XxxRequest)`.

### 8.1 Auditoría (2 servicios)

Caso especial: `sp_aud_registrar_error` y `sp_aud_registrar_evento` no tienen parámetros OUT. Usan `procedureCallHelper.ejecutarConResultado()` en lugar de `ejecutar()`.

| Servicio | SP |
|----------|----|
| `SpAudRegistrarErrorService` | `sp_aud_registrar_error` |
| `SpAudRegistrarEventoService` | `sp_aud_registrar_evento` |

### 8.2 Seguridad (2 servicios)

| Servicio | SP |
|----------|----|
| `SpSecValidarSesionService` | `sp_sec_validar_sesion` |
| `SpSecRevocarSesionService` | `sp_sec_revocar_sesion` |

### 8.3 Clientes (5 servicios)

| Servicio | SP |
|----------|----|
| `SpCliCrearPersonaService` | `sp_cli_crear_persona_natural` |
| `SpCliCrearEmpresaService` | `sp_cli_crear_empresa` |
| `SpCliAsociarUsuarioEmpresaService` | `sp_cli_asociar_usuario_empresa` |
| `SpCliCambiarEstadoClienteService` | `sp_cli_cambiar_estado_cliente` |
| `SpCliAsignarRolEmpresaUsuarioService` | `sp_cli_asignar_rol_empresa_usuario` |

### 8.4 Cuentas (5 servicios)

| Servicio | SP |
|----------|----|
| `SpCtaAbrirCuentaService` | `sp_cta_abrir_cuenta` |
| `SpCtaConsignarService` | `sp_cta_consignar` |
| `SpCtaRetirarService` | `sp_cta_retirar` |
| `SpCtaBloquearCuentaService` | `sp_cta_bloquear_cuenta` |
| `SpCtaCancelarCuentaService` | `sp_cta_cancelar_cuenta` |

### 8.5 Créditos (4 servicios)

| Servicio | SP |
|----------|----|
| `SpCreSolicitarPrestamoService` | `sp_cre_solicitar_prestamo` |
| `SpCreAprobarPrestamoService` | `sp_cre_aprobar_prestamo` |
| `SpCreRechazarPrestamoService` | `sp_cre_rechazar_prestamo` |
| `SpCreDesembolsarPrestamoService` | `sp_cre_desembolsar_prestamo` |

### 8.6 Transferencias (6 servicios)

| Servicio | SP |
|----------|----|
| `SpTrfCrearTransferenciaService` | `sp_trf_crear_transferencia` |
| `SpTrfAprobarTransferenciaService` | `sp_trf_aprobar_transferencia` |
| `SpTrfRechazarTransferenciaService` | `sp_trf_rechazar_transferencia` |
| `SpTrfEjecutarTransferenciaDirectaService` | `sp_trf_ejecutar_transferencia_directa` |
| `SpTrfVencerTransferenciasPendientesService` | `sp_trf_vencer_transferencias_pendientes` |
| `SpTrfConsultarPendientesEmpresaService` | `sp_trf_consultar_pendientes_aprobacion_empresa` |

**Patrón uniforme de cada servicio SP:**
```java
@Service
@RequiredArgsConstructor
public class SpXxxService {
    private final ProcedureCallHelper procedureCallHelper;

    @Transactional
    public SpResultado ejecutar(XxxRequest request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("p_param1", request.getParam1());
        // ...
        return procedureCallHelper.ejecutar("sp_xxx_nombre", params);
    }
}
```

**Nota sobre `LinkedHashMap`:** Se usa en lugar de `HashMap` para preservar el orden de inserción de parámetros, que en algunos drivers JDBC afecta la posición ordinal de los parámetros.

---

## 9. Excepciones CRUD

Se añadieron 8 excepciones específicas para el rol `SOPORTE_INFORMACION` (operaciones CRUD directas):

| Excepción | Código | Cuándo se lanza |
|-----------|--------|-----------------|
| `EntidadNoEncontradaException` | `CRUD-404` | ID no existe en ninguna entidad genérica |
| `CuentaNoEncontradaException` | `CRUD-CTA-404` | ID de cuenta no existe |
| `PrestamoNoEncontradoException` | `CRUD-CRE-404` | ID de préstamo no existe |
| `TransferenciaNoEncontradaException` | `CRUD-TRF-404` | ID de transferencia no existe |
| `UsuarioNoEncontradoException` | `CRUD-USR-404` | ID de usuario no existe |
| `EmpresaNoEncontradaException` | `CRUD-EMP-404` | ID de empresa no existe |
| `RegistroDuplicadoException` | `CRUD-409` | Violación de unicidad al crear/actualizar |
| `OperacionCrudNoPermitidaException` | `DOM-CRUD-403` | Operación no permitida sobre la entidad |

**Uso especial de `OperacionCrudNoPermitidaException`:**  
Las entidades de auditoría (`AudBitacoraEvento`, `AudCambioDato`, `AudErrorOperacion`) son inmutables por diseño (append-only). Sus servicios CRUD lanzan esta excepción en los métodos `actualizar` y `eliminarPorId`:

```java
@Override
public void actualizar(Long id, AudBitacoraEventoUpdateRequest request) {
    throw new OperacionCrudNoPermitidaException("AudBitacoraEvento", "UPDATE");
}
```

---

## 10. DTOs CRUD

**Ubicación:** `Banco/src/main/java/bd2/Banco/domain/dto/`  
**Total:** 108 clases (36 Create + 36 Update + 36 Response)

Cada una de las 36 entidades tiene su trío completo de DTOs:

### 10.1 Patrón por entidad

```
XxxCreateRequest  — campos necesarios para crear un registro nuevo
XxxUpdateRequest  — campos modificables (ID excluido, nunca se modifica el PK)
XxxResponse       — proyección segura para exposición (sin hashPassword, etc.)
```

### 10.2 Tecnología usada

- `@Getter @Builder @NoArgsConstructor @AllArgsConstructor` (Lombok) en todos
- `@Setter` en Create/Update (necesario para deserialización Jackson futura)
- Sin anotaciones de validación por ahora (Jakarta Validation se añadirá en la capa de controller)
- `SecUsuarioResponse` excluye `hashPassword` explícitamente — campo de seguridad

### 10.3 Entidades y sus DTOs

| Entidad | CreateRequest | UpdateRequest | Response |
|---------|--------------|--------------|---------|
| `CatEstadoUsuario` | ✓ | ✓ | ✓ |
| `CatEstadoCuenta` | ✓ | ✓ | ✓ |
| `CatTipoCuenta` | ✓ | ✓ | ✓ |
| `CatMoneda` | ✓ | ✓ | ✓ |
| `CatEstadoPrestamo` | ✓ | ✓ | ✓ |
| `CatTipoPrestamo` | ✓ | ✓ | ✓ |
| `CatEstadoTransferencia` | ✓ | ✓ | ✓ |
| `CatTipoOperacion` | ✓ | ✓ | ✓ |
| `CatParametroNegocio` | ✓ | ✓ | ✓ |
| `CatRolEmpresa` | ✓ | ✓ | ✓ |
| `CatTipoIdentificacion` | ✓ | ✓ | ✓ |
| `CatCanalOperacion` | ✓ | ✓ | ✓ |
| `CatMotivoRechazo` | ✓ | ✓ | ✓ |
| `CatMotivoBloqueo` | ✓ | ✓ | ✓ |
| `CatEstadoSesion` | ✓ | ✓ | ✓ |
| `CatTipoMovimiento` | ✓ | ✓ | ✓ |
| `CatTransicionEstado` | ✓ | ✓ | ✓ |
| `SecRol` | ✓ | ✓ | ✓ |
| `SecUsuario` | ✓ | ✓ | ✓ (sin hashPassword) |
| `SecUsuarioRol` | ✓ | ✓ | ✓ |
| `SecSesion` | ✓ | ✓ | ✓ |
| `CliPersonaNatural` | ✓ | ✓ | ✓ |
| `CliEmpresa` | ✓ | ✓ | ✓ |
| `CliEmpresaUsuario` | ✓ | ✓ | ✓ |
| `CliEmpresaUsuarioRol` | ✓ | ✓ | ✓ |
| `PrdProductoBancario` | ✓ | ✓ | ✓ |
| `CtaCuenta` | ✓ | ✓ | ✓ |
| `CtaMovimiento` | ✓ | ✓ | ✓ |
| `CrePrestamo` | ✓ | ✓ | ✓ |
| `CrePrestamoAprobacion` | ✓ | ✓ | ✓ |
| `CrePrestamoDesembolso` | ✓ | ✓ | ✓ |
| `TrfTransferencia` | ✓ | ✓ | ✓ |
| `TrfTransferenciaAprobacion` | ✓ | ✓ | ✓ |
| `AudBitacoraEvento` | ✓ | ✓ (protegido) | ✓ |
| `AudCambioDato` | ✓ | ✓ (protegido) | ✓ |
| `AudErrorOperacion` | ✓ | ✓ (protegido) | ✓ |

---

## 11. Servicios CRUD

**Ubicación:** `Banco/src/main/java/bd2/Banco/domain/services/crud/`  
**Total:** 36 servicios

### 11.1 Rol de destino

Todos los servicios CRUD son para el rol `SOPORTE_INFORMACION`, que tiene acceso de gestión directa sobre registros (sin pasar por stored procedures).

### 11.2 Contrato de cada servicio

```java
@Service
@RequiredArgsConstructor
public class XxxCrudService {
    private final XxxRepository repository;

    public XxxResponse crear(XxxCreateRequest request) { ... }
    public XxxResponse actualizar(Long id, XxxUpdateRequest request) { ... }
    public void eliminarPorId(Long id) { ... }
    public XxxResponse buscarPorId(Long id) { ... }
    public List<XxxResponse> buscarTodos() { ... }
}
```

### 11.3 Servicios por módulo

**Catálogos (17 servicios):**
`CatEstadoUsuarioCrudService`, `CatEstadoCuentaCrudService`, `CatTipoCuentaCrudService`, `CatMonedaCrudService`, `CatEstadoPrestamoCrudService`, `CatTipoPrestamoCrudService`, `CatEstadoTransferenciaCrudService`, `CatTipoOperacionCrudService`, `CatParametroNegocioCrudService`, `CatRolEmpresaCrudService`, `CatTipoIdentificacionCrudService`, `CatCanalOperacionCrudService`, `CatMotivoRechazoCrudService`, `CatMotivoBloqueCrudService`, `CatEstadoSesionCrudService`, `CatTipoMovimientoCrudService`, `CatTransicionEstadoCrudService`

**Seguridad (4 servicios):**
`SecRolCrudService`, `SecUsuarioCrudService`, `SecUsuarioRolCrudService`, `SecSesionCrudService`

**Clientes (4 servicios):**
`CliPersonaNaturalCrudService`, `CliEmpresaCrudService`, `CliEmpresaUsuarioCrudService`, `CliEmpresaUsuarioRolCrudService`

**Operacional (8 servicios):**
`PrdProductoBancarioCrudService`, `CtaCuentaCrudService`, `CtaMovimientoCrudService`, `CrePrestamoCrudService`, `CrePrestamoAprobacionCrudService`, `CrePrestamoDesembolsoCrudService`, `TrfTransferenciaCrudService`, `TrfTransferenciaAprobacionCrudService`

**Auditoría (3 servicios — protegidos):**
`AudBitacoraEventoCrudService`, `AudCambioDatoCrudService`, `AudErrorOperacionCrudService`

### 11.4 Protección de entidades de auditoría

Los servicios de auditoría tienen `actualizar` y `eliminarPorId` bloqueados:

```java
// AudBitacoraEventoCrudService.java
public AudBitacoraEventoResponse actualizar(Long id, AudBitacoraEventoUpdateRequest request) {
    throw new OperacionCrudNoPermitidaException("AudBitacoraEvento", "UPDATE");
}

public void eliminarPorId(Long id) {
    throw new OperacionCrudNoPermitidaException("AudBitacoraEvento", "DELETE");
}
```

Esto garantiza que las tablas de auditoría sean **append-only** incluso si alguien con rol `SOPORTE_INFORMACION` intenta modificarlas directamente.

---

## 12. Historial de commits

### Commit 1 — Capa de dominio completa

```
feat: complete domain layer - entities, enums, repositories, SP services and DTOs
```

**Contenido:**
- `application.properties` actualizado con env vars
- `pom.xml` corregido (dependencias de test)
- `BancoSecurityConfig.java`
- `ProcedureCallHelper.java`
- 17 enums en `domain/enums/`
- 9 excepciones de dominio en `domain/exceptions/`
- 36 entidades JPA en `domain/entities/`
- 36 repositorios en `domain/repositories/`
- 24 DTOs Request para SPs
- 2 DTOs Response para SPs
- 24 servicios SP en `domain/services/procedures/`

### Commit 2 — Capa CRUD completa

```
feat: CRUD layer for SOPORTE_INFORMACION role - services, DTOs and exceptions
```

**Contenido:**
- 8 excepciones CRUD en `domain/exceptions/`
- 108 DTOs CRUD en `domain/dto/` (36×Create + 36×Update + 36×Response)
- 36 servicios CRUD en `domain/services/crud/`

### Repositorio

```
https://github.com/VictorSalinas85/Actividad-banco
Rama: main
```

---

## Resumen de artefactos generados

| Categoría | Cantidad |
|-----------|----------|
| Enumeraciones de dominio | 17 |
| Excepciones de dominio | 17 |
| Entidades JPA | 36 |
| Repositorios JPA | 36 |
| DTOs Request (SPs) | 24 |
| DTOs Response (SPs) | 2 |
| Servicios SP | 24 |
| DTOs CreateRequest (CRUD) | 36 |
| DTOs UpdateRequest (CRUD) | 36 |
| DTOs Response (CRUD) | 36 |
| Servicios CRUD | 36 |
| Clases de configuración | 2 |
| **Total de clases Java** | **302** |

---

## Decisiones de arquitectura clave

| Decisión | Alternativa descartada | Razón |
|----------|----------------------|-------|
| `ProcedureCallHelper` centralizado | `@NamedStoredProcedureQuery` en entidades | Evita contaminar entidades con metadata de SPs; SRP |
| Un service por SP | Service por módulo | SOLID SRP estricto; facilita testing unitario |
| `LinkedHashMap` para params SP | `HashMap` | Preserva orden de parámetros para drivers JDBC que usan posición |
| `ejecutarConResultado` para SPs sin OUT | Variante de `ejecutar` | SPs de auditoría no tienen OUT params; registrarlos causaría error |
| Campos `Long` nullable para titular polimórfico | `@ManyToOne` condicional | JPA no soporta FK joins condicionales |
| `@EqualsAndHashCode(onlyExplicitlyIncluded)` | `@Data` | `@Data.equals` con lazy collections puede causar N+1 o `LazyInitializationException` |
| `ddl-auto=validate` | `update` / `create` | El esquema lo gestiona el DBA mediante scripts SQL; Hibernate solo valida |
| Audit entities bloqueadas en CRUD | Sin restricción | Las tablas audit son append-only por diseño del sistema |
