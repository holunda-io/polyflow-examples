CREATE USER polyflow_user WITH PASSWORD 'S3Cr3T!' CREATEDB;

CREATE DATABASE polyflow
  WITH
  OWNER = polyflow_user
  ENCODING = 'UTF8'
  LC_COLLATE = 'en_US.utf8'
  LC_CTYPE = 'en_US.utf8'
  TABLESPACE = pg_default
  CONNECTION LIMIT = -1;

CREATE DATABASE process
  WITH
  OWNER = polyflow_user
  ENCODING = 'UTF8'
  LC_COLLATE = 'en_US.utf8'
  LC_CTYPE = 'en_US.utf8'
  TABLESPACE = pg_default
  CONNECTION LIMIT = -1;
