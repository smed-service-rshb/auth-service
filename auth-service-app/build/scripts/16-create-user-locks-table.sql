-- Скрипт добавления таблицы блокировок учётных записей
set search_path to 'auth_service';

create table user_locks (
  id            bigserial not null,
  user_id       bigint    not null,
  creation_date timestamp,
  locked        boolean   not null,
  description   varchar(256),

  primary key (id)
);

alter table user_locks
  add constraint user_locks_employee_fk
foreign key (user_id)
references users (id);

create index user_locks_user_locked_idx
  on user_locks (locked, user_id);