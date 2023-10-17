CREATE TABLE dead_letter_entry (
    dead_letter_id       VARCHAR(255) NOT NULL,
    cause_message        VARCHAR(255),
    cause_type           VARCHAR(255),
    diagnostics          BYTEA,
    enqueued_at          TIMESTAMP    NOT NULL,
    last_touched         TIMESTAMP,
    aggregate_identifier VARCHAR(255),
    event_identifier     VARCHAR(255) NOT NULL,
    message_type         VARCHAR(255) NOT NULL,
    meta_data            BYTEA,
    payload              BYTEA         NOT NULL,
    payload_revision     VARCHAR(255),
    payload_type         VARCHAR(255) NOT NULL,
    sequence_number      INT8,
    time_stamp           VARCHAR(255) NOT NULL,
    token                BYTEA,
    token_type           VARCHAR(255),
    type                 VARCHAR(255),
    processing_group     VARCHAR(255) NOT NULL,
    processing_started   TIMESTAMP,
    sequence_identifier  VARCHAR(255) NOT NULL,
    sequence_index       INT8         NOT NULL,
    PRIMARY KEY (dead_letter_id)
);

create index IDX_dead_letter_entry_pg on dead_letter_entry (processing_group);
create index IDX_dead_letter_entry_pgsi on dead_letter_entry (processing_group, sequence_identifier);

alter table dead_letter_entry
  add constraint UC_dead_letter_entry_pgsisi unique (processing_group, sequence_identifier, sequence_index);
