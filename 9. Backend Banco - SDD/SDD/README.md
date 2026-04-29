# SDD - Especificaciones para Base de Datos Bancaria en MySQL

## Objetivo
Esta carpeta contiene la especificacion tecnica completa para construir el proyecto de base de datos MySQL de una aplicacion bancaria con enfoque:

- DDD (Domain-Driven Design)
- SDD (Spec-Driven Design)
- Modelo transaccional ACID
- Auditoria y trazabilidad
- Seguridad por rol
- Escalabilidad operacional

La documentacion esta pensada para que otros agentes de IA implementen el proyecto sin ambiguedades.

## Orden de lectura recomendado
1. 00_contexto_y_objetivos.md
2. 01_glosario_y_convenciones.md
3. 02_bounded_contexts_y_agregados.md
4. 03_modelo_logico_relacional.md
5. 04_catalogos_y_estados.md
6. 05_reglas_de_negocio_e_invariantes.md
7. 06_diseno_fisico_mysql.md
8. 07_estrategia_triggers.md
9. 08_estrategia_procedimientos.md
10. 09_auditoria_bitacora_nosql.md
11. 10_seguridad_y_control_acceso.md
12. 11_transacciones_concurrencia_errores.md
13. 12_pruebas_sql_y_criterios_aceptacion.md
14. 13_plan_migraciones_versionado.md
15. 14_plan_entrega_para_agentes_ai.md
16. 15_backlog_iteraciones.md

## Definicion de hecho
Se considera completada la base de datos cuando:

- Existe DDL inicial versionado.
- Catalogos y estados estan cargados.
- Restricciones estructurales estan activas.
- Procedimientos criticos estan implementados y probados.
- Triggers de auditoria y control minimo estan activos.
- Flujos criticos (transferencia, prestamo, desembolso) pasan pruebas funcionales y de concurrencia.
- Se emite matriz final de trazabilidad especificacion -> implementacion -> prueba.
