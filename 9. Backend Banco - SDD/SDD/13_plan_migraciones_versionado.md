# 13 - Plan de Migraciones y Versionado

## Objetivo
Controlar evolucion del esquema y de la logica SQL con trazabilidad y rollback seguro.

## Estrategia recomendada
1. Herramienta de migracion: Flyway o Liquibase.
2. Scripts inmutables por version.
3. Convencion: VNNN__descripcion.sql.
4. Separar migraciones de datos semilla y de estructura.

## Fases de migracion
1. Baseline de schema y catalogos.
2. Carga inicial de parametros y transiciones.
3. SP core transaccionales.
4. Triggers y auditoria.
5. Optimizaciones e indices secundarios.

## Politica de cambios
1. Nunca editar una migracion aplicada en produccion.
2. Cambios por nuevas migraciones incrementales.
3. Rollback por script complementario o estrategia expand-contract.

## Control de version de SP
1. Archivo por SP con firma versionada.
2. Registro interno de version en tabla aud_version_objeto.
3. Pruebas obligatorias tras actualizar SP.

## Gate de despliegue
1. Pasar pruebas SQL automatizadas.
2. Aprobacion tecnica DBA + arquitectura.
3. Validar ventana de mantenimiento y plan de contingencia.
