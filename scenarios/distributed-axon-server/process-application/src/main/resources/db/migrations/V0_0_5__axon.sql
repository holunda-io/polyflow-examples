CREATE SEQUENCE hibernate_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE association_value_entry (
    id                BIGINT       NOT NULL,
    association_key   VARCHAR(255) NOT NULL,
    association_value VARCHAR(255),
    saga_id           VARCHAR(255) NOT NULL,
    saga_type         VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE saga_entry (
    saga_id         VARCHAR(255) NOT NULL,
    revision        VARCHAR(255),
    saga_type       VARCHAR(255),
    serialized_saga BYTEA,
    PRIMARY KEY (saga_id)
);

CREATE TABLE token_entry (
    processor_name VARCHAR(255) NOT NULL,
    segment        INTEGER      NOT NULL,
    owner          VARCHAR(255),
    timestamp      VARCHAR(255) NOT NULL,
    token          BYTEA,
    token_type     VARCHAR(255),
    PRIMARY KEY (processor_name, segment)
);

CREATE INDEX IDXk45eqnxkgd8hpdn6xixn8sgft ON association_value_entry(saga_type, association_key, association_value);
CREATE INDEX IDXgv5k1v2mh6frxuy5c0hgbau94 ON association_value_entry(saga_id, saga_type);
