-- Создание таблицы для хранения настроек
set search_path to 'auth_service';

ALTER TABLE user_locks ADD COLUMN IF NOT EXISTS type VARCHAR(50);
ALTER TABLE user_locks ADD COLUMN IF NOT EXISTS end_date_time TIMESTAMP;

update user_locks set type='BY_HAND' where type is null;

create table user_login_attempt (
  id            bigserial not null,
  user_id       bigint    not null,
  creation_date timestamp not null,
  constraint pk_user_login_attempt primary key (id)
);

alter table user_login_attempt
  add constraint user_login_attempt_user_id_fk
foreign key (user_id)
references users (id);

create index user_login_attempt_user_idx
  on user_login_attempt (user_id, creation_date);


insert into settings(key, value, description) values ('WRONG_PASSWORD_TIMEOUT', 120, 'Таймаут в секундах блокировки пользователя при превышении максимально допустимого количества попыток ввода пароля (-1 навсегда, по умолчанию 120)');
insert into settings(key, value, description) values ('WRONG_PASSWORD_PERIOD',  300, 'Период в секундах, за который подсчитываются количество неудачных попыток (-1 навсегда, по умолчанию 300)');
insert into settings(key, value, description) values ('WRONG_PASSWORD_MAX_ATTEMPT_COUNT', '5', 'Максимальная количество попыток ввода неправильного пароля');
