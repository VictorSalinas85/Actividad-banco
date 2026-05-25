-- 00_create_database.sql
CREATE DATABASE IF NOT EXISTS banco_core
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE banco_core;

SET NAMES utf8mb4;
SET time_zone = '+00:00';
SET SESSION sql_mode = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION';
SET SESSION tx_isolation = 'REPEATABLE-READ';
