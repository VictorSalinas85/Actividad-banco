-- 00_orquestador_mysql.sql
-- Orquestador principal de despliegue MySQL (MySQL 8.0+)
-- Ejecutar desde cliente mysql ubicado en la carpeta BASE.BD:
-- mysql -u <usuario> -p < 00_orquestador_mysql.sql
--
-- Nota:
-- - Este script usa SOURCE, por lo que se recomienda ejecutarlo desde el cliente mysql
--   con directorio de trabajo en BASE.BD.
-- - Para rutas absolutas, reemplace cada SOURCE con la ruta completa del archivo.

SELECT '==== INICIO ORQUESTADOR MYSQL ====' AS status;
SELECT NOW(6) AS inicio_ejecucion_utc;

SELECT 'Ejecutando 00_create_database.sql' AS paso;
SOURCE 00_create_database.sql;

SELECT 'Ejecutando 01_create_catalog_tables.sql' AS paso;
SOURCE 01_create_catalog_tables.sql;

SELECT 'Ejecutando 02_seed_catalogs.sql' AS paso;
SOURCE 02_seed_catalogs.sql;

SELECT 'Ejecutando 03_create_security_tables.sql' AS paso;
SOURCE 03_create_security_tables.sql;

SELECT 'Ejecutando 04_create_client_tables.sql' AS paso;
SOURCE 04_create_client_tables.sql;

SELECT 'Ejecutando 05_create_product_tables.sql' AS paso;
SOURCE 05_create_product_tables.sql;

SELECT 'Ejecutando 06_create_account_tables.sql' AS paso;
SOURCE 06_create_account_tables.sql;

SELECT 'Ejecutando 07_create_loan_tables.sql' AS paso;
SOURCE 07_create_loan_tables.sql;

SELECT 'Ejecutando 08_create_transfer_tables.sql' AS paso;
SOURCE 08_create_transfer_tables.sql;

SELECT 'Ejecutando 09_create_audit_tables.sql' AS paso;
SOURCE 09_create_audit_tables.sql;

SELECT 'Ejecutando 10_create_constraints.sql' AS paso;
SOURCE 10_create_constraints.sql;

SELECT 'Ejecutando 11_create_triggers.sql' AS paso;
SOURCE 11_create_triggers.sql;

SELECT 'Ejecutando 12_create_stored_procedures.sql' AS paso;
SOURCE 12_create_stored_procedures.sql;

SELECT 'Ejecutando 13_create_indexes.sql' AS paso;
SOURCE 13_create_indexes.sql;

SELECT 'Ejecutando 14_create_test_data.sql' AS paso;
SOURCE 14_create_test_data.sql;

SELECT 'Ejecutando 15_test_cases.sql' AS paso;
SOURCE 15_test_cases.sql;

SELECT '==== FIN ORQUESTADOR MYSQL ====' AS status;
SELECT NOW(6) AS fin_ejecucion_utc;
