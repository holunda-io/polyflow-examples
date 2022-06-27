CREATE USER polyflow_user WITH PASSWORD 'S3Cr3T!';

CREATE DATABASE process;
GRANT ALL PRIVILEGES ON DATABASE process to polyflow_user;

CREATE DATABASE polyflow;
GRANT ALL PRIVILEGES ON DATABASE polyflow to polyflow_user;
