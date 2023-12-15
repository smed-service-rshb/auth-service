-- Скрипт изменения способа хранения информации о связанных с пользователем элементов организационной структуры организации.
-- Теперь пользователь может быть привязан сразу к нескольким подразделениям.
set search_path to 'auth_service';

create table user_orgunits (
  user_id bigint not null,
  orgunit_id bigint not null,
  primary key (user_id, orgunit_id)
);

alter table user_orgunits
  add constraint user_fk
foreign key (user_id)
references users (id);

-- переносим данные о связанных с пользователем подразделениях в таблицу user_orgunits
do $$
declare
  r users%rowtype;
begin
  for r in
  select * from users
  loop
    insert into user_orgunits(user_id, orgunit_id) values (r.id, r.orgunit);
  end loop;
  return;
end$$;

alter table users drop constraint users_orgunit_fk;
alter table users drop column orgunit;

alter table users add innerphone varchar(20);