create table app_approval_request
(
    id        varchar(255) not null,
    amount    decimal(10, 2),
    applicant varchar(255),
    currency  varchar(255),
    subject   varchar(255),
    primary key (id)
);
