-- Создание таблицы для хранения настроек
set search_path to 'auth_service';

create table settings (
  id          bigserial    not null,
  key         varchar(50)  not null unique,
  value       varchar(255) not null,
  description varchar(255),
  constraint pk_settings primary key (id)
);

insert into settings(key, value, description) values ('PASSWORD_SETTINGS_CHECK_ENABLED', 'true', 'Максимальная длина пароля пользователя');
insert into settings(key, value, description) values ('PASSWORD_SETTINGS_MAX_LENGTH', '10', 'Максимальная длина пароля пользователя');
insert into settings(key, value, description) values ('PASSWORD_SETTINGS_MIN_LENGTH', '6', 'Минимальная длина пароля пользователя');
insert into settings(key, value, description) values ('PASSWORD_SETTINGS_NUMBER_OF_DIFFERENT_CHARACTERS', '3', 'Минимальное количество различных символов в пароле');
insert into settings(key, value, description) values ('PASSWORD_SETTINGS_ENABLED_CHARSETS', 'DIGIT,LOWERCASE_LATIN,UPPERCASE_LATIN,SPECIAL', 'Набор разрешённых множеств символов');
insert into settings(key, value, description) values ('PASSWORD_SETTINGS_REQUIRED_CHARSETS', 'LOWERCASE_LATIN,UPPERCASE_LATIN,DIGIT', 'Набор обязательных множеств символов');
insert into settings(key, value, description) values ('PASSWORD_SETTINGS_SPECIAL_CHARSETS', '!@#$%^&*()-_+=;:,./?|`~[]{}', 'Набор разрешённых специальных символов');