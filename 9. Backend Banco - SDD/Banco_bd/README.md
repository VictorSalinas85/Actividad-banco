# BASE.BD - Scripts finales MySQL

## Orden de ejecucion
1. 00_create_database.sql
2. 01_create_catalog_tables.sql
3. 02_seed_catalogs.sql
4. 03_create_security_tables.sql
5. 04_create_client_tables.sql
6. 05_create_product_tables.sql
7. 06_create_account_tables.sql
8. 07_create_loan_tables.sql
9. 08_create_transfer_tables.sql
10. 09_create_audit_tables.sql
11. 10_create_constraints.sql
12. 11_create_triggers.sql
13. 12_create_stored_procedures.sql
14. 13_create_indexes.sql
15. 14_create_test_data.sql
16. 15_test_cases.sql

## Notas
- Objetivo: MySQL 8.0+
- Motor: InnoDB
- Charset: utf8mb4
- Zona horaria: UTC
- Los procedimientos devuelven out_code, out_message, out_reference.
- El script de pruebas esta orientado a validaciones funcionales minimas de SDD.
