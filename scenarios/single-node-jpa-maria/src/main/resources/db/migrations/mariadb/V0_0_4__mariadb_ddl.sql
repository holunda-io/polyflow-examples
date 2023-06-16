create sequence hibernate_sequence start with 1 increment by 1;

create table association_value_entry
(
  id                bigint auto_increment not null,
  association_key   varchar(255) not null,
  association_value varchar(255),
  saga_id           varchar(255) not null,
  saga_type         varchar(255),
  primary key (id)
);

create table dead_letter_entry
(
  dead_letter_id       varchar(255) not null,
  cause_message        varchar(255),
  cause_type           varchar(255),
  diagnostics          longblob,
  enqueued_at          datetime(6) not null,
  last_touched         datetime(6),
  aggregate_identifier varchar(255),
  event_identifier     varchar(255) not null,
  message_type         varchar(255) not null,
  meta_data            longblob,
  payload              longblob     not null,
  payload_revision     varchar(255),
  payload_type         varchar(255) not null,
  sequence_number      bigint,
  time_stamp           varchar(255) not null,
  token                longblob,
  token_type           varchar(255),
  type                 varchar(255),
  processing_group     varchar(255) not null,
  processing_started   datetime(6),
  sequence_identifier  varchar(255) not null,
  sequence_index       bigint       not null,
  primary key (dead_letter_id)
);

create table domain_event_entry
(
  global_index         bigint auto_increment not null,
  event_identifier     varchar(255) not null,
  meta_data            longblob,
  payload              longblob     not null,
  payload_revision     varchar(255),
  payload_type         varchar(255) not null,
  time_stamp           varchar(255) not null,
  aggregate_identifier varchar(255) not null,
  sequence_number      bigint       not null,
  type                 varchar(255),
  primary key (global_index)
);

create table plf_data_entry
(
  entry_id           varchar(255) not null,
  entry_type         varchar(255) not null,
  application_name   varchar(255) not null,
  date_created       datetime(6) not null,
  date_deleted       datetime(6),
  description        varchar(2048),
  form_key           varchar(255),
  date_last_modified datetime(6) not null,
  name               varchar(255) not null,
  payload            longtext,
  revision           bigint,
  processing_type    varchar(255) not null,
  state              varchar(255) not null,
  type               varchar(255) not null,
  primary key (entry_id, entry_type)
);

create table plf_data_entry_authorizations
(
  entry_id             varchar(255) not null,
  entry_type           varchar(255) not null,
  authorized_principal varchar(255) not null,
  primary key (entry_id, entry_type, authorized_principal)
);

create table plf_data_entry_payload_attributes
(
  entry_id   varchar(64)  not null,
  entry_type varchar(128) not null,
  path       varchar(255) not null,
  value      varchar(128) not null,
  primary key (entry_id, entry_type, path, value)
);

create table plf_data_entry_protocol
(
  id              varchar(255) not null,
  log_details     varchar(255),
  log_message     varchar(255),
  processing_type varchar(255) not null,
  state           varchar(255) not null,
  time            datetime(6) not null,
  username        varchar(255),
  entry_id        varchar(255) not null,
  entry_type      varchar(255) not null,
  primary key (id)
);

create table plf_proc_def
(
  proc_def_id             varchar(255) not null,
  application_name        varchar(255) not null,
  description             varchar(2048),
  name                    varchar(255) not null,
  proc_def_key            varchar(255) not null,
  proc_def_version        integer      not null,
  start_form_key          varchar(255),
  startable_from_tasklist bit,
  version_tag             varchar(255),
  primary key (proc_def_id)
);

create table plf_proc_def_authorizations
(
  proc_def_id                  varchar(255) not null,
  authorized_starter_principal varchar(255) not null,
  primary key (proc_def_id, authorized_starter_principal)
);

create table plf_proc_instance
(
  instance_id         varchar(255) not null,
  business_key        varchar(255),
  delete_reason       varchar(255),
  end_activity_id     varchar(255),
  application_name    varchar(255) not null,
  source_def_id       varchar(255) not null,
  source_def_key      varchar(255) not null,
  source_execution_id varchar(255) not null,
  source_instance_id  varchar(255) not null,
  source_name         varchar(255) not null,
  source_type         varchar(255) not null,
  source_tenant_id    varchar(255),
  start_activity_id   varchar(255),
  start_user_id       varchar(255),
  run_state           varchar(255) not null,
  super_instance_id   varchar(255),
  primary key (instance_id)
);

create table plf_task
(
  task_id             varchar(255) not null,
  assignee_id         varchar(255),
  business_key        varchar(255),
  date_created        datetime(6) not null,
  description         varchar(2048),
  date_due            datetime(6),
  date_follow_up      datetime(6),
  form_key            varchar(255),
  name                varchar(255) not null,
  owner_id            varchar(255),
  payload             longtext,
  priority            integer,
  application_name    varchar(255) not null,
  source_def_id       varchar(255) not null,
  source_def_key      varchar(255) not null,
  source_execution_id varchar(255) not null,
  source_instance_id  varchar(255) not null,
  source_name         varchar(255) not null,
  source_type         varchar(255) not null,
  source_tenant_id    varchar(255),
  task_def_key        varchar(255) not null,
  primary key (task_id)
);

create table plf_task_authorizations
(
  task_id              varchar(255) not null,
  authorized_principal varchar(255) not null,
  primary key (task_id, authorized_principal)
);

create table plf_task_correlations
(
  task_id    varchar(255) not null,
  entry_id   varchar(255) not null,
  entry_type varchar(255) not null,
  primary key (task_id, entry_id, entry_type)
);

create table plf_task_payload_attributes
(
  task_id varchar(255) not null,
  path    varchar(255) not null,
  value   varchar(255) not null,
  primary key (task_id, path, value)
);

create table saga_entry
(
  saga_id         varchar(255) not null,
  revision        varchar(255),
  saga_type       varchar(255),
  serialized_saga longblob,
  primary key (saga_id)
);

create table snapshot_event_entry
(
  aggregate_identifier varchar(255) not null,
  sequence_number      bigint       not null,
  type                 varchar(255) not null,
  event_identifier     varchar(255) not null,
  meta_data            longblob,
  payload              longblob     not null,
  payload_revision     varchar(255),
  payload_type         varchar(255) not null,
  time_stamp           varchar(255) not null,
  primary key (aggregate_identifier, sequence_number, type)
);

create table token_entry
(
  processor_name varchar(255) not null,
  segment        integer      not null,
  owner          varchar(255),
  timestamp      varchar(255) not null,
  token          longblob,
  token_type     varchar(255),
  primary key (processor_name, segment)
);
create index IDXk45eqnxkgd8hpdn6xixn8sgft on association_value_entry (saga_type, association_key, association_value);
create index IDXgv5k1v2mh6frxuy5c0hgbau94 on association_value_entry (saga_id, saga_type);
create index IDXe67wcx5fiq9hl4y4qkhlcj9cg on dead_letter_entry (processing_group);
create index IDXrwucpgs6sn93ldgoeh2q9k6bn on dead_letter_entry (processing_group, sequence_identifier);

alter table dead_letter_entry
  add constraint UKhlr8io86j74qy298xf720n16v unique (processing_group, sequence_identifier, sequence_index);

alter table domain_event_entry
  add constraint UK8s1f994p4la2ipb13me2xqm1w unique (aggregate_identifier, sequence_number);

alter table domain_event_entry
  add constraint UK_fwe6lsa8bfo6hyas6ud3m8c7x unique (event_identifier);

alter table snapshot_event_entry
  add constraint UK_e1uucjseo68gopmnd0vgdl44h unique (event_identifier);

alter table plf_data_entry_authorizations
  add constraint FKayik2ifqf0heiwvgs0ts1pwl5
    foreign key (entry_id, entry_type)
      references plf_data_entry (entry_id, entry_type);

alter table plf_data_entry_payload_attributes
  add constraint FKrsbl8su01epxoepmn8h761dnt
    foreign key (entry_id, entry_type)
      references plf_data_entry (entry_id, entry_type);

alter table plf_data_entry_protocol
  add constraint FKh2e52nrdrr6x8fpu7n040760h
    foreign key (entry_id, entry_type)
      references plf_data_entry (entry_id, entry_type);

alter table plf_proc_def_authorizations
  add constraint FKe2ic13vswywsbaj8u3gr0px0k
    foreign key (proc_def_id)
      references plf_proc_def (proc_def_id);

alter table plf_task_authorizations
  add constraint FKi2132ws8a4ruun3iysbog3uc7
    foreign key (task_id)
      references plf_task (task_id);

alter table plf_task_correlations
  add constraint FKe19ae2b8uib1r0jpvl4obdopl
    foreign key (task_id)
      references plf_task (task_id);

alter table plf_task_payload_attributes
  add constraint FK44yjugbfiv7y92657vp868l7l
    foreign key (task_id)
      references plf_task (task_id);
