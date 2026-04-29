# 00 - Contexto y Objetivos

## Contexto de negocio
El sistema bancario debe gestionar clientes persona natural y empresa, cuentas bancarias, prestamos y transferencias, con flujos de aprobacion y control estricto por rol.

## Objetivos tecnicos
1. Garantizar consistencia de datos y transacciones ACID.
2. Centralizar invariantes de dominio criticas en MySQL.
3. Proveer trazabilidad completa para auditoria y cumplimiento.
4. Separar responsabilidades por contextos de dominio.
5. Facilitar despliegue evolutivo por migraciones versionadas.

## Alcance del proyecto de base de datos
1. Modelado relacional de entidades de negocio.
2. Catalogos de estados, tipos y parametros.
3. Reglas en constraints, triggers y procedimientos.
4. Auditoria tecnica y bitacora de eventos de negocio.
5. Seguridad por roles y permisos SQL.
6. Estrategia de pruebas de integridad y concurrencia.

## No alcance
1. UI, APIs HTTP y autenticacion externa.
2. Integraciones interbancarias en tiempo real.
3. Reporteria BI avanzada fuera del core transaccional.

## Principios de arquitectura
1. Source of truth transaccional en MySQL.
2. Especificacion primero, implementacion despues.
3. Reglas simples en constraints, reglas complejas en SP.
4. Triggers minimos y controlados.
5. Auditoria inmutable para operaciones criticas.
