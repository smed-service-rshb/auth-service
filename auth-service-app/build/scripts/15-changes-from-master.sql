-- Скрипт для обновления разработческой БД до промышленной БД
set search_path to 'auth_service';

create table current_state
(
  id               bigserial not null constraint current_state_pkey primary key,
  version          bigint    not null,
  name             varchar(100),
  created          timestamp default now(),
  isfinished       boolean,
  statecode        varchar(20),
  statedescription varchar(1000),
  modified         timestamp default now()
);

create function update_modified_column()
  returns trigger
language plpgsql
as $$
BEGIN
  NEW.modified = now();
  RETURN NEW;
END;
$$;

create trigger update_current_state
  before update
  on current_state
  for each row
execute procedure update_modified_column();