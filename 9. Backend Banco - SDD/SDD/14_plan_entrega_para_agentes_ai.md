# 14 - Plan de Entrega para Agentes de IA

## Objetivo
Definir como deben trabajar los agentes para construir el proyecto SQL completo sin perder consistencia.

## Secuencia de ejecucion recomendada
1. Agente A - Generar DDL de catalogos y tablas core.
2. Agente B - Generar constraints, FK e indices.
3. Agente C - Generar SP por modulo (clientes, cuentas, prestamos, transferencias).
4. Agente D - Generar triggers de auditoria y control minimo.
5. Agente E - Generar datos semilla de catalogos y parametros.
6. Agente F - Generar suite de pruebas SQL y fixtures.
7. Agente G - Generar migraciones versionadas.

## Contratos de salida por agente
Cada agente debe entregar:
1. Scripts SQL versionados.
2. Documento de decisiones tecnicas.
3. Lista de reglas cubiertas.
4. Casos de prueba asociados.
5. Riesgos abiertos y supuestos.

## Reglas de colaboracion entre agentes
1. No redefinir tablas ya aprobadas sin nueva migracion.
2. No mezclar logica de negocio compleja en triggers.
3. Todo SP debe cumplir contrato de salida estandar.
4. Toda operacion critica debe registrar bitacora.

## Criterio de aceptacion de cada entrega
1. Script ejecutable en entorno limpio.
2. Pruebas verdes del modulo.
3. Documentacion actualizada en SDD.
