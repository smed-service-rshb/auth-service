-- Справочник сегментов
INSERT INTO segment (id, code, name, version) VALUES (1, 'retail', 'Массовый', 1);
INSERT INTO segment (id, code, name, version) VALUES (2, 'vip', 'Премиальный', 1);

-- Справочник подразделений организации
INSERT INTO orgunit (id, version, parent, type, name, address, city)
VALUES (1000001, 1, null, 'BRANCH', 'ДРБ', null, null);
INSERT INTO orgunit (id, version, parent, type, name, address, city)
VALUES (2000001, 1, 1000001, 'OFFICE', '0001', null, 'Москва');
INSERT INTO orgunit (id, version, parent, type, name, address, city)
VALUES (2000002, 1, 1000001, 'OFFICE', '0002', null, 'Москва');

-- Справочник ролей
INSERT INTO roles (id, name, description) VALUES (1, 'Главный администратор системы', 'Главный администратор системы');
INSERT INTO roles (id, name, description) VALUES (2, 'Руководитель ВСП', 'Руководитель ВСП');
INSERT INTO roles (id, name, description) VALUES (3, 'Пользователь', 'Пользователь');

-- Настройка связи "Разрешение - Права" (должно соответствовать auth-service-app/build/scripts/03-auth-service-support-permissions.sql)
insert into rights_to_permission(permission_id, right_id) values ('get-rights', 'EDIT_ROLES');
insert into rights_to_permission(permission_id, right_id) values ('get-rights', 'VIEW_ROLES');
insert into rights_to_permission(permission_id, right_id) values ('get-roles', 'EDIT_ROLES');
insert into rights_to_permission(permission_id, right_id) values ('get-roles', 'VIEW_ROLES');
insert into rights_to_permission(permission_id, right_id) values ('get-role', 'EDIT_ROLES');
insert into rights_to_permission(permission_id, right_id) values ('get-role', 'VIEW_ROLES');
insert into rights_to_permission(permission_id, right_id) values ('create-role', 'EDIT_ROLES');
insert into rights_to_permission(permission_id, right_id) values ('delete-role', 'EDIT_ROLES');
insert into rights_to_permission(permission_id, right_id) values ('update-role', 'EDIT_ROLES');
insert into rights_to_permission(permission_id, right_id) values ('get-employees', 'EDIT_USERS');
insert into rights_to_permission(permission_id, right_id) values ('get-employee', 'EDIT_USERS');
insert into rights_to_permission(permission_id, right_id) values ('create-employee', 'EDIT_USERS');
insert into rights_to_permission(permission_id, right_id) values ('delete-employee', 'EDIT_USERS');
insert into rights_to_permission(permission_id, right_id) values ('update-employee', 'EDIT_USERS');
insert into rights_to_permission(permission_id, right_id) values ('lock-employee', 'EDIT_USERS');
insert into rights_to_permission(permission_id, right_id) values ('unlock-employee', 'EDIT_USERS');
insert into rights_to_permission(permission_id, right_id) values ('reset-password', 'EDIT_USERS');
insert into rights_to_permission(permission_id, right_id) values ('edit-employee-groups', 'EDIT_USERS');

-- Тестовые пользователи
insert into users (id, login, passwordHash, firstName, middleName, secondName, mobilePhone,
                   email, position, personnelNumber, segment, changePassword, deleted)
values (1, 'chief-admin', '356a192b7913b04c54574d18c28d46e6395428ab', 'Иван', 'Иванович', 'Иванов', '+79110000001',
           'ivanov@example.org', 'Главный администратор системы', '10001', (select id
                                                                            from segment
                                                                            where name = 'Массовый'), false,
        false);

insert into user_roles (user_id, role_id) values ((select id
                                                   from users
                                                   where login = 'chief-admin'),
                                                  (select id
                                                   from roles
                                                   where name = 'Главный администратор системы'));

insert into user_orgunits (user_id, orgunit_id) values ((select id
                                                   from users
                                                   where login = 'chief-admin'),
                                                  2000001);


insert into users (id, login, passwordHash, firstName, middleName, secondName, mobilePhone,
                   email, position, personnelNumber, segment, changePassword,  deleted)
values (2, 'vsp-head', '356a192b7913b04c54574d18c28d46e6395428ab', 'Антон', 'Антонович', 'Антонов', '+79110000008',
           'antonov@example.org', 'Руководитель ВСП', '10008', (select id
                                                                from segment
                                                                where name = 'Массовый'), false,
        false);

insert into motivation_program(id, user_id, motivation_status, is_active)
values (1, 2, 'CORRECT', true);

insert into user_orgunits (user_id, orgunit_id) values ((select id
                                                   from users
                                                   where login = 'vsp-head'),
                                                  2000001);

insert into user_orgunits (user_id, orgunit_id) values ((select id
                                                         from users
                                                         where login = 'vsp-head'),
                                                        2000002);

insert into user_roles (user_id, role_id) values ((select id
                                                   from users
                                                   where login = 'vsp-head'),
                                                  (select id
                                                   from roles
                                                   where name = 'Руководитель ВСП'));

insert into users (id, login, passwordHash, firstName, middleName, secondName, mobilePhone,
                   email, position, personnelNumber, segment, changePassword, deleted)
values (3, 'user', '356a192b7913b04c54574d18c28d46e6395428ab', 'Фёдор', 'Фёдорович', 'Фёдоров', '+79110000009',
           'fedorov@example.org', 'Пользователь', '10009', (select id
                                                            from segment
                                                            where name = 'Массовый'), false,  false);

insert into user_orgunits (user_id, orgunit_id) values ((select id
                                                         from users
                                                         where login = 'user'),
                                                        2000001);

insert into user_roles (user_id, role_id) values ((select id
                                                   from users
                                                   where login = 'user'),
                                                  (select id
                                                   from roles
                                                   where name = 'Пользователь'));

insert into users (id, login, passwordHash, firstName, middleName, secondName, mobilePhone,
                   email, position, personnelNumber, segment, changePassword,  deleted)
values (4, 'user_locked', '356a192b7913b04c54574d18c28d46e6395123yy', 'Циня', 'Центр', 'Целевой', '+79110000010',
           'lockuser@example.org', 'Пользователь', '10019', (select id
                                                            from segment
                                                            where name = 'Массовый'), false, false);

insert into users (id, login, passwordHash, firstName, middleName, secondName, mobilePhone,
                   email, position, personnelNumber, segment, changePassword, deleted)
values (5, 'user0', '58e9d0e4808c7b43fdc17aa52cd1ed46d7012fc8', 'Фёдор', 'Фёдорович', 'Фёдоров', '+79110000009',
        'fedorov@example.org', 'Пользователь', '10209', (select id
                                                         from segment
                                                         where name = 'Массовый'), false,  false);

insert into user_roles (user_id, role_id) values ((select id
                                                   from users
                                                   where login = 'user_locked'),
                                                  (select id
                                                   from roles
                                                   where name = 'Пользователь'));

insert into users (id, login, passwordHash, firstName, middleName, secondName, mobilePhone,
                   email, segment,  changePassword, deleted)
values (9, 'client', '356a192b7913b04c54574d18c28d46e6395428ab', 'Клиент', 'Тестовый', 'Лк', '+79110000010',
        'client@example.org',(select id
                              from segment
                              where name = 'Массовый'), false,  false);


insert into employee_group (id, version, name, code) values (1, 0, 'NAME_1', 'CODE_1');
insert into employee_group (id, version, name, code) values (2, 0, 'NAME_2', 'CODE_2');
insert into user_to_group (user_id, group_id) values (1, 2);
-- Настройки сервисы
insert into settings(key, value, description) values ('PASSWORD_SETTINGS_CHECK_ENABLED', 'true', 'Максимальная длина пароля пользователя');
insert into settings(key, value, description) values ('PASSWORD_SETTINGS_MAX_LENGTH', '10', 'Максимальная длина пароля пользователя');
insert into settings(key, value, description) values ('PASSWORD_SETTINGS_MIN_LENGTH', '6', 'Минимальная длина пароля пользователя');
insert into settings(key, value, description) values ('PASSWORD_SETTINGS_NUMBER_OF_DIFFERENT_CHARACTERS', '3', 'Минимальное количество различных символов в пароле');
insert into settings(key, value, description) values ('PASSWORD_SETTINGS_ENABLED_CHARSETS', 'DIGIT,LOWERCASE_LATIN,UPPERCASE_LATIN,SPECIAL', 'Набор разрешённых множеств символов');
insert into settings(key, value, description) values ('PASSWORD_SETTINGS_REQUIRED_CHARSETS', 'LOWERCASE_LATIN,UPPERCASE_LATIN,DIGIT', 'Набор обязательных множеств символов');
insert into settings(key, value, description) values ('PASSWORD_SETTINGS_SPECIAL_CHARSETS', '!@#$%^&*()-_+=;:,./?|`~[]{}', 'Набор разрешённых специальных символов');
insert into settings(key, value, description) values ('MOTIVATION_SETTINGS_IS_ENABLED', 'true', 'Программа мотиваций активна');
insert into settings(key, value, description) values ('MOTIVATION_SETTINGS_EXPIRE_TIME', '1', 'Время действия программы мотивации');
insert into settings(key, value, description) values ('MOTIVATION_DOCUMENT_SETTINGS_ID', 'motivation_static_print', 'Идентификатор печатной формы в сервисе хранения шаблонов');
insert into settings(key, value, description) values ('MOTIVATION_DOCUMENT_SETTINGS_NAME', 'РСХБ_Программа_Мотивации_сотрудников', 'Имя возвращаемого пользователю файла');


ALTER TABLE motivation_program_attachments
    DROP COLUMN IF EXISTS content;

ALTER TABLE motivation_program_attachments
    ADD COLUMN content BYTEA;