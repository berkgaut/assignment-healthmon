--liquibase formatted sql

--changeset berkgaut@gmail.com:1

create table service (
  -- metadata
  id int auto_increment primary key,
  version int, -- row version for optimistic locking
  created timestamp default current_timestamp,
  updated timestamp null on update current_timestamp, -- default null

  -- service info
  name varchar(64),

  -- healthcheck config
  healthcheck_url varchar(1024),
  healthcheck_timeout_ms int
);


--changeset berkgaut@gmail.com:2

-- Healthcheck state and the last healthcheck result
create table healthcheck (
  -- metadata
  id int auto_increment primary key,
  updated timestamp default current_timestamp on update current_timestamp,

  service_id int,
  foreign key (service_id) references service(id),

  service_status int default 0, -- UNKNOWN=0, OK=1, FAILURE=2
  check_status   int default 0  -- PENDING=0, IN_PROGRESS=1, COMPLETE=2
)

