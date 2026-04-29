# Actividad: Funcionamiento de la Aplicación de Gestión de Información de un Banco

## Introducción y Objetivo del Proyecto

El presente enunciado define los requisitos funcionales y de negocio para el desarrollo de un sistema de información enfocado en la gestión de clientes, productos y operaciones clave de una entidad bancaria.

El objetivo de este proyecto es que el estudiante diseñe e implemente una aplicación robusta, segura y escalable que cumpla con las normativas y flujos de trabajo descritos, infiriendo el modelo de datos relacional y no relacional, las validaciones y los casos de uso a partir de la narrativa de negocio.

La aplicación servirá como el core transaccional y de gestión de la información fundamental del banco, permitiendo a distintos roles interactuar con los datos de clientes, personas naturales y empresas, cuentas, préstamos y transferencias, siempre bajo estrictas reglas de negocio y flujos de aprobación bien definidos.

---

## Descripción de los Roles

El sistema debe contemplar un modelo de acceso basado en roles, donde la visibilidad de la información y la capacidad de realizar operaciones están estrictamente limitadas por las responsabilidades de cada usuario dentro del banco o como cliente. A continuación, se detallan los roles principales y su interacción con la aplicación.

---

### Cliente Persona Natural

Este rol corresponde a los usuarios individuales del banco. Su interacción se centra en la consulta y operación sobre sus propios productos. Puede solicitar nuevos productos, como préstamos, y realizar transferencias.

| Campo | Descripción | Restricciones y Formato |
|---|---|---|
| Nombre completo | Nombre y apellidos de la persona. | Obligatorio. |
| Número de identificación | Cédula, DNI u otro identificador nacional. | Único en todo el aplicativo. |
| Correo electrónico | Dirección de contacto principal. | Obligatorio. Debe contener `@` y un dominio. |
| Número de teléfono | Teléfono de contacto. | Obligatorio. Longitud mínima de 7 dígitos y máxima de 15. |
| Fecha de nacimiento | Fecha de nacimiento de la persona. | Obligatorio. Formato `DD/MM/YYYY`. Debe ser mayor de edad, al menos 18 años. |
| Dirección | Domicilio registrado. | Obligatorio. |
| Rol | Cliente Persona Natural. | Valor fijo. |

**Visibilidad y Operaciones:**  
Solo puede visualizar el estado de sus cuentas, sus préstamos, detalles y pagos, y el historial de sus propias transferencias. Puede crear solicitudes de nuevos préstamos y transferencias entre cuentas propias o a terceros. No puede ver información de otros clientes ni cuentas empresariales.

---

### Cliente Empresa — Representante Legal / Usuario Administrador de Empresa

Representa a la entidad legal que es cliente del banco. El representante legal, o el usuario administrador delegado, tiene la capacidad de gestionar los productos de la empresa.

**Visibilidad y Operaciones:**  
Puede visualizar todas las cuentas y préstamos asociados a la empresa. Puede delegar permisos a otros usuarios operativos de la empresa. Las transferencias de alto valor que cree el Empleado de Empresa podrían requerir su aprobación, según los flujos de aprobación.

| Campo | Descripción | Restricciones y Formato |
|---|---|---|
| Razón Social | Nombre legal de la empresa. | Obligatorio. |
| Número de identificación Fiscal — NIT | Identificador tributario de la empresa. | Único en todo el aplicativo. |
| Correo electrónico | Correo de contacto corporativo. | Obligatorio. Debe contener `@` y un dominio. |
| Número de teléfono | Teléfono de contacto de la empresa. | Obligatorio. Longitud mínima de 7 dígitos y máxima de 15. |
| Dirección | Domicilio fiscal. | Obligatorio. |
| Representante Legal | Identificador del representante legal, referencia a Persona Natural. | Obligatorio. |
| Rol | Cliente Empresa. | Valor fijo. |

---

### Empleado de Ventanilla — Cajero / Asesor de Servicios

Personal de contacto directo con el público. Sus operaciones son principalmente transaccionales y de soporte básico.

**Visibilidad y Operaciones:**  
Puede consultar el saldo y el estado de cualquier cuenta de cliente para realizar operaciones de caja, como depósitos y retiros, siempre que el cliente se identifique correctamente. Puede registrar la apertura de nuevas cuentas para clientes existentes o nuevos. No puede ver información detallada de riesgos crediticios ni aprobar préstamos.

---

### Empleado Comercial — Asesor de Productos / Ejecutivo de Cuenta

Personal dedicado a la venta de productos bancarios.

**Visibilidad y Operaciones:**  
Puede consultar información detallada de los clientes bajo su gestión, crear solicitudes de nuevos productos, como préstamos y cuentas, en nombre del cliente. Puede realizar seguimiento del estado de las solicitudes. No puede realizar aprobaciones de riesgo ni modificar saldos directamente.

---

### Empleado de Empresa — Usuario Operativo de Empresa

Usuario autorizado por la empresa cliente para realizar operaciones diarias.

**Visibilidad y Operaciones:**  
Puede crear transferencias y pagos masivos, por ejemplo, nómina, desde las cuentas de la empresa. Solo ve los productos y operaciones de la empresa a la que está asociado. Las transferencias que superen un umbral predefinido deben pasar a estado `En espera de aprobación`. No tiene capacidad de aprobación.

---

### Supervisor de Empresa — Usuario Aprobador / Administrador de Empresa

Rol designado por la empresa para supervisar y autorizar operaciones críticas.

**Visibilidad y Operaciones:**  
Ve todas las operaciones y productos de su empresa. Su función principal es aprobar o rechazar las transferencias y pagos creados por el Empleado de Empresa que requieran autorización. También puede gestionar los usuarios operativos de la empresa.

---

### Analista Interno del Banco — Riesgo / Cumplimiento / Back-Office

Personal especializado en la revisión y gestión de riesgos y normativas.

**Visibilidad y Operaciones:**  
Tiene acceso a la información de clientes, productos y operaciones para fines de análisis, cumplimiento y auditoría. Es el rol responsable de aprobar o rechazar solicitudes de préstamos. Puede consultar la bitácora completa de operaciones. No puede realizar operaciones transaccionales de ventanilla ni crear transferencias.

---

## Información de los Usuarios del Sistema

Todos los usuarios del sistema, sin importar su rol, deben registrar la siguiente información básica, la cual debe estar centralizada.

| Nombre del Campo | Descripción | Tipo de Dato |
|---|---|---|
| ID_Usuario | Identificador único del usuario en el sistema. | Numérico — Entero |
| ID_Relacionado | Identificador de la Entidad Cliente, Persona Natural o Empresa, asociada. | Texto/Numérico |
| Nombre_Completo | Nombre del usuario. | Texto |
| ID_Identificacion | Número de identificación, DNI, Cédula, NIT. | Texto |
| Correo_Electronico | Email del usuario. | Texto |
| Telefono | Número de teléfono. | Texto |
| Fecha_Nacimiento | Fecha de nacimiento. | Fecha |
| Direccion | Dirección de residencia/contacto. | Texto |
| Rol_Sistema | Rol asignado en el aplicativo. | Texto — Catálogo |
| Estado_Usuario | Estado del usuario en el sistema. | Texto — Catálogo |

---

## Productos y Servicios Bancarios manejados por la aplicación

El sistema debe gestionar las siguientes entidades fundamentales del negocio, cuya información es estructurada y debe ser almacenada en una **Base de Datos Relacional — SQL**.

---

### Cliente Persona Natural

Representa a la persona física que es titular o beneficiaria de productos. La estructura de datos es la ya definida en la tabla anterior.

---

### Cliente Empresa

Representa a la entidad jurídica. La estructura de datos es la ya definida en la tabla anterior, enfocada en los datos corporativos.

---

### Cuenta Bancaria

Representa los depósitos de dinero del cliente. Pueden ser de tipo `Ahorros`, `Corriente`, `Personal` o `Empresarial`.

| Nombre del Campo | Descripción | Tipo de Dato |
|---|---|---|
| Numero_Cuenta | Identificador único de la cuenta bancaria. | Texto/Numérico |
| Tipo_Cuenta | Tipo específico de cuenta. | Texto — Catálogo |
| ID_Titular | Referencia al ID_Identificacion del Cliente, Persona o Empresa. | Texto |
| Saldo_Actual | Monto de dinero disponible en la cuenta. | Numérico — Decimal |
| Moneda | Moneda en la que opera la cuenta. | Texto — Catálogo |
| Estado_Cuenta | Estado operativo de la cuenta. | Texto — Catálogo |
| Fecha_Apertura | Fecha en que se creó la cuenta. | Fecha |

---

### Préstamo / Crédito

Representa un producto de endeudamiento otorgado al cliente.

| Nombre del Campo | Descripción | Tipo de Dato |
|---|---|---|
| ID_Prestamo | Identificador único del préstamo. | Numérico — Entero |
| Tipo_Prestamo | Categoría del préstamo. | Texto — Catálogo |
| ID_Cliente_Solicitante | Referencia al ID_Identificacion del Cliente. | Texto |
| Monto_Solicitado | Cantidad de dinero que el cliente pidió. | Numérico — Decimal |
| Monto_Aprobado | Cantidad de dinero que el banco aprobó. | Numérico — Decimal |
| Tasa_Interes | Tasa de interés anual del préstamo. | Numérico — Decimal |
| Plazo_Meses | Duración del préstamo en meses. | Numérico — Entero |
| Estado_Prestamo | Estado actual del proceso del préstamo. | Texto — Catálogo |
| Fecha_Aprobacion | Fecha en que el Analista Interno aprobó. | Fecha |
| Fecha_Desembolso | Fecha en que se realizó el abono a la cuenta. | Fecha |
| Cuenta_Destino_Desembolso | Número de cuenta donde se abona el dinero. | Texto/Numérico |

---

### Transferencia

Representa el movimiento de fondos entre cuentas.

| Nombre del Campo | Descripción | Tipo de Dato |
|---|---|---|
| ID_Transferencia | Identificador único de la transferencia. | Numérico — Entero |
| Cuenta_Origen | Número de la cuenta de la que sale el dinero. | Texto/Numérico |
| Cuenta_Destino | Número de la cuenta a la que llega el dinero. | Texto/Numérico |
| Monto | Cantidad de dinero a transferir. | Numérico — Decimal |
| Fecha_Creacion | Fecha y hora en que se registró la solicitud. | Fecha/Hora |
| Fecha_Aprobacion | Fecha y hora de la aprobación, si aplica. | Fecha/Hora |
| Estado_Transferencia | Estado del proceso de transferencia. | Texto — Catálogo |
| ID_Usuario_Creador | ID del usuario que generó la transferencia. | Numérico — Entero |
| ID_Usuario_Aprobador | ID del usuario que aprobó, si aplica. | Numérico — Entero |

---

### Producto Bancario General — Catálogo

Entidad que define los tipos de productos y servicios ofrecidos.

| Nombre del Campo | Descripción | Tipo de Dato |
|---|---|---|
| Codigo_Producto | Código único del producto/servicio. | Texto |
| Nombre_Producto | Nombre descriptivo del producto. | Texto |
| Categoria | Categoría: Cuentas, Préstamos, Servicios. | Texto — Catálogo |
| Requiere_Aprobacion | Indica si la creación de este producto requiere un flujo de aprobación. | Booleano |

---

## Flujos de Aprobación de Operaciones

Ciertas operaciones críticas o de alto valor requieren una validación humana adicional para su ejecución, lo cual se gestiona mediante cambios de estado y la intervención de roles específicos.

### Principio General de Aprobación

Toda operación que requiera aprobación debe registrar explícitamente:

- Quién la creó.
- En qué momento fue creada.
- Quién la aprobó o rechazó.
- En qué momento fue aprobada o rechazada.

Cada cambio de estado debe ser registrado en la **Bitácora de Operaciones**.

---

## Flujo de Aprobación de Préstamos

El proceso de un préstamo comienza con una solicitud y culmina con el desembolso o el rechazo.

1. **Creación de la Solicitud:**  
   Un Cliente Persona Natural, un Cliente Empresa, a través de su administrador, o un Empleado Comercial puede ingresar una solicitud de préstamo.

2. **Estado Inicial:**  
   La solicitud se registra con el `Estado_Prestamo` en `En estudio`.

3. **Aprobación/Rechazo:**  
   El Analista Interno del Banco es el único rol con permiso para revisar y tomar la decisión final.

   - Si el Analista Interno aprueba, el estado cambia a `Aprobado`.
   - Si el Analista Interno rechaza, el estado cambia a `Rechazado`.

4. **Desembolso — Tras Aprobación:**  
   Una vez en estado `Aprobado`, un proceso interno, ejecutado por el Back-Office y representado por una acción del Analista Interno, debe marcar el préstamo como `Desembolsado` y:

   - Validar que el campo `Cuenta_Destino_Desembolso` esté definido y activo.
   - Aumentar el `Saldo_Actual` de la cuenta destino por el `Monto_Aprobado`.
   - Registrar el cambio de estado y el desembolso de fondos en la Bitácora de Operaciones.

---

## Flujo de Aprobación de Transferencias de Empresa — Alto Monto

Las transferencias empresariales que superen un umbral de monto predefinido, según una regla de negocio, deben ser revisadas.

1. **Creación de la Solicitud:**  
   Un Empleado de Empresa crea una transferencia o un lote de pagos desde una cuenta de la empresa.

2. **Estado Inicial:**  
   Si la transferencia supera el umbral, el `Estado_Transferencia` es `En espera de aprobación`. Si no lo supera, se ejecuta directamente y pasa a estado `Ejecutada`.

3. **Aprobación/Rechazo:**  
   El Supervisor de Empresa, o el rol administrador de la empresa, es el único responsable de aprobar o rechazar estas transferencias.

   Si el Supervisor de Empresa aprueba:

   - Se valida la existencia de `Saldo_Actual` suficiente en la `Cuenta_Origen`.
   - Se ejecuta la transferencia, actualizando los saldos de las cuentas involucradas.
   - El estado final cambia a `Ejecutada`.
   - Se registra la aprobación y la ejecución en la Bitácora de Operaciones.

   Si el Supervisor de Empresa rechaza:

   - El estado final cambia a `Rechazada`.

4. **Condición de Vencimiento:**  
   Si una transferencia permanece en estado `En espera de aprobación` por un período superior a una hora, es decir, 60 minutos desde su creación, el sistema debe cambiar su estado automáticamente a `Vencida`.

   - No se realiza movimiento de fondos.
   - Se debe registrar el evento de vencimiento en la Bitácora, indicando el motivo: `vencida por falta de aprobación en el tiempo establecido`.

---

## Bitácora de Operaciones — Almacenamiento NoSQL

Para propósitos de auditoría, trazabilidad y cumplimiento, todas las operaciones significativas realizadas en el sistema deben ser registradas en un histórico inmutable, denominado **Bitácora de Operaciones**.

Debido a la variabilidad del contenido de los registros, una transferencia no tiene los mismos datos relevantes que la aprobación de un préstamo. Por esto, la Bitácora debe ser diseñada para ser almacenada en una **Base de Datos No Relacional**, utilizando un modelo de documento o diccionario.

**Propósito:**  
La Bitácora se utiliza para el seguimiento histórico y la auditoría. No se utiliza para calcular el `Saldo_Actual` de las cuentas, el cual debe ser gestionado en la Base de Datos Relacional.

---

## Campos Mínimos del Registro de Bitácora

Cada documento de la Bitácora debe incluir los siguientes campos estructurados, además de un objeto o diccionario de `Datos_Detalle` que contenga la información específica de la operación.

| Nombre del Campo | Descripción | Tipo de Dato |
|---|---|---|
| ID_Bitacora | Identificador único del registro de bitácora. | Texto/Numérico |
| Tipo_Operacion | Categoría de la operación realizada. | Texto |
| Fecha_Hora_Operacion | Momento exacto en que se generó el registro. | Fecha/Hora |
| ID_Usuario | Identificador del usuario que ejecutó la acción. | Numérico — Entero |
| Rol_Usuario | Rol del usuario en el momento de la operación. | Texto |
| ID_Producto_Afectado | Referencia al producto principal de la operación: Cuenta, Préstamo, Transferencia. | Texto/Numérico |
| Datos_Detalle | Objeto/Documento con datos variables según `Tipo_Operacion`. | Documento — JSON/Diccionario |

### Contenido de `Datos_Detalle` — Ejemplos

- **Para Transferencia Ejecutada:**  
  Monto involucrado, `Saldo_Antes_Origen`, `Saldo_Despues_Origen`, `Saldo_Antes_Destino`, `Saldo_Despues_Destino`.

- **Para Aprobación de Préstamo:**  
  Monto aprobado, tasa de interés, estado anterior `En estudio`, nuevo estado `Aprobado`, ID del Analista Aprobador.

- **Para Vencimiento de Transferencia:**  
  Motivo de vencimiento `Falta de aprobación a tiempo`, fecha y hora de vencimiento, ID del usuario creador.

---

## Reglas de Negocio y Condiciones de Validación

El sistema debe garantizar el cumplimiento de las siguientes reglas de negocio mediante validaciones en el código.

---

### Reglas Generales de Clientes y Productos

- **Unicidad de Identificación:**  
  El `Número de identificación` — DNI, Cédula o NIT — debe ser único para cualquier cliente, Persona Natural o Empresa, en la base de datos.

- **Apertura de Cuentas:**

  - No se puede abrir una cuenta a un cliente cuyo `Estado_Usuario` esté como `Inactivo` o `Bloqueado`.
  - Cada Cuenta Bancaria debe tener un `Numero_Cuenta` único.
  - El `Tipo_Cuenta` debe ser un valor válido del Producto Bancario General — catálogo.
  - No se permiten operaciones, como transferencias o retiros, en cuentas con `Estado_Cuenta` `Bloqueada` o `Cancelada`, salvo procesos internos de cierre.

---

### Reglas de Préstamos / Créditos

- **Asociación de Clientes:**  
  Todo préstamo debe estar asociado a un `ID_Cliente_Solicitante` que sea válido y activo.

- **Transiciones de Estado:**

  - Un préstamo solo puede pasar de `En estudio` a `Aprobado` o `Rechazado`.
  - Solo el Analista Interno puede realizar la aprobación o rechazo.
  - El paso a `Desembolsado` solo es posible desde el estado `Aprobado`.

- **Validación de Desembolso:**  
  No se puede marcar un préstamo como `Desembolsado` sin que se haya definido y validado la `Cuenta_Destino_Desembolso`, la cual debe ser una cuenta activa del cliente.

- **Impacto Financiero del Desembolso:**  
  Al realizar el desembolso:

  - Se debe validar que el `Monto_Aprobado` sea mayor a cero.
  - El `Saldo_Actual` de la cuenta destino debe aumentar en el `Monto_Aprobado`.
  - Se debe generar un registro en la Bitácora.

---

### Reglas de Transferencias

- **Unicidad y Monto:**  
  Toda transferencia debe tener un `ID_Transferencia` único y el `Monto` a transferir debe ser estrictamente mayor que cero.

- **Disponibilidad de Fondos:**  
  No se permite ejecutar transferencias, es decir, pasar a estado `Ejecutada`, desde `Cuenta_Origen` con `Saldo_Actual` insuficiente, a menos que exista una regla específica de sobregiro autorizado que deba ser validada.

- **Cuentas Operativas:**  
  No se permiten transferencias desde `Cuenta_Origen` bloqueadas o canceladas.

- **Regla de Vencimiento de Aprobación:**  
  Si una transferencia que requiere aprobación, empresa de alto monto, permanece en estado `En espera de aprobación` por más de una hora, debe cambiar automáticamente a `Vencida` y registrar el evento en la Bitácora.

- **Impacto Financiero de la Ejecución:**  
  Toda transferencia ejecutada debe:

  - Disminuir el `Saldo_Actual` de la `Cuenta_Origen`.
  - Aumentar el `Saldo_Actual` de la `Cuenta_Destino`, si es interna.
  - Registrar la operación en la Base de Datos Relacional y en la Bitácora NoSQL.

---

## Restricciones de Acceso por Rol — Seguridad de la Información

Todos los usuarios cuentan con nombre de usuario y contraseña. Para generar cualquier tipo de operación deben estar debidamente logueados y tener permisos. En caso de tratar de acceder a un flujo para el cual no tengan permisos, se le debe impedir el desarrollo del mismo.

Las siguientes reglas definen quién puede ver qué información dentro de la aplicación.

---

### Clientes — Persona Natural y Empresa

- Solo pueden consultar y operar sobre sus propios productos, como Cuenta Bancaria y Préstamo / Crédito.
- Pueden ver el historial de sus propias operaciones registradas en la Bitácora, filtrado por su `ID_Producto_Afectado`.
- No pueden ver ninguna información o producto asociado a otros clientes.

---

### Empleado de Ventanilla

- Puede consultar el `Saldo_Actual` y el `Estado_Cuenta` de cualquier cliente para propósitos de transacciones de caja.
- Puede registrar la apertura de nuevos productos de cuenta.
- No puede acceder a información de análisis de riesgo, datos crediticios detallados, como la tasa de interés, o la Bitácora completa de operaciones.

---

### Empleado Comercial

- Puede acceder a la información completa de los clientes que tiene asignados o que está gestionando para una solicitud.
- Puede consultar el estado de los préstamos en `En estudio` o `Rechazado` para dar seguimiento, pero no puede modificar su estado.
- No puede realizar operaciones que impacten saldos directamente, salvo la solicitud inicial de productos.

---

### Empleado de Empresa — Operativo

- Acceso limitado estrictamente a las cuentas, préstamos y transferencias de la empresa a la que pertenece.
- Puede crear transferencias.

---

### Supervisor de Empresa — Aprobador

- Acceso a la información de su empresa, con énfasis en las transferencias que están en estado `En espera de aprobación`.
- Es el único rol fuera del banco con capacidad de modificar el estado de las transferencias: aprobar o rechazar.

---

### Analista Interno del Banco

- Tiene el acceso más amplio a la información de productos, clientes y operaciones.
- Es el rol primario para modificar el estado de los préstamos: aprobar, rechazar o desembolsar.
- Tiene acceso completo de consulta a la Bitácora de Operaciones.
- Crucialmente, no puede modificar saldos de cuentas de forma arbitraria; solo puede hacerlo como resultado de un flujo de negocio definido, por ejemplo, el desembolso de préstamo.

---

## Conclusiones para el Desarrollo

El desarrollo de esta **Aplicación de Gestión de Información de un Banco** requiere la comprensión profunda de los procesos de negocio y la segregación de la información.

El estudiante deberá deducir la estructura de la base de datos **Relacional — SQL** para clientes, cuentas, préstamos y transferencias a partir de los campos y restricciones proporcionados.

Adicionalmente, se debe diseñar el esquema de documentos para la **Bitácora de Operaciones — NoSQL**, asegurando que capture toda la trazabilidad necesaria para auditoría.

El éxito del proyecto radicará en la correcta implementación de los **Flujos de Aprobación** y de las **Reglas de Negocio y Validaciones** en cada caso de uso, garantizando que el sistema sea consistente, seguro y que respete las restricciones de acceso por rol.
