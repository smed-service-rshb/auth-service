set search_path to 'auth_service';

-- Главный администратор системы
insert into users (login, passwordHash, firstName, middleName, secondName, mobilePhone,
                   email, position, personnelNumber, segment, changePassword, deleted)
values ('chief-admin', '356a192b7913b04c54574d18c28d46e6395428ab', 'Иван', 'Иванович', 'Иванов', '+79110000001',
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
                                                  2002310);

-- Администратор системы
insert into users (login, passwordHash, firstName, middleName, secondName, mobilePhone,
                   email, position, personnelNumber, segment, changePassword,  deleted)
values ('admin', '356a192b7913b04c54574d18c28d46e6395428ab', 'Пётр', 'Петрович', 'Петров', '+79110000002',
                 'petrov@example.org', 'Администратор системы', '10002', (select id
                                                                          from segment
                                                                          where name = 'Массовый'), false,
        false);

insert into user_roles (user_id, role_id) values ((select id
                                                   from users
                                                   where login = 'admin'),
                                                  (select id
                                                   from roles
                                                   where name = 'Администратор системы'));

insert into user_orgunits (user_id, orgunit_id) values ((select id
                                                   from users
                                                   where login = 'admin'),
                                                  2002310);

-- Продуктовый администратор
insert into users (login, passwordHash, firstName, middleName, secondName, mobilePhone,
                   email, position, personnelNumber, segment, changePassword,  deleted)
values ('pr-admin', '356a192b7913b04c54574d18c28d46e6395428ab', 'Сидор', 'Сидорович', 'Сидоров', '+79110000003',
                    'sidorov@example.org', 'Продуктовый администратор', '10003', (select id
                                                                                  from segment
                                                                                  where name = 'Массовый'), false,
         false);

insert into user_roles (user_id, role_id) values ((select id
                                                   from users
                                                   where login = 'pr-admin'),
                                                  (select id
                                                   from roles
                                                   where name = 'Продуктовый администратор'));

insert into user_orgunits (user_id, orgunit_id) values ((select id
                                                   from users
                                                   where login = 'pr-admin'),
                                                  2002310);


-- Руководитель ВСП
insert into users (login, passwordHash, firstName, middleName, secondName, mobilePhone,
                   email, position, personnelNumber, segment, changePassword, deleted)
values ('vsp-head', '356a192b7913b04c54574d18c28d46e6395428ab', 'Антон', 'Антонович', 'Антонов', '+79110000008',
                    'antonov@example.org', 'Руководитель ВСП', '10008', (select id
                                                                         from segment
                                                                         where name = 'Массовый'), false,
        false);

insert into user_roles (user_id, role_id) values ((select id
                                                   from users
                                                   where login = 'vsp-head'),
                                                  (select id
                                                   from roles
                                                   where name = 'Руководитель ВСП'));

insert into user_orgunits (user_id, orgunit_id) values ((select id
                                                   from users
                                                   where login = 'vsp-head'),
                                                  2002310);

-- Пользователь
insert into users (login, passwordHash, firstName, middleName, secondName, mobilePhone,
                   email, position, personnelNumber, segment, changePassword, deleted)
values ('user', '356a192b7913b04c54574d18c28d46e6395428ab', 'Фёдор', 'Фёдорович', 'Фёдоров', '+79110000009',
                'fedorov@example.org', 'Пользователь', '10009', (select id
                                                                 from segment
                                                                 where name = 'Массовый'), false, false);

insert into user_roles (user_id, role_id) values ((select id
                                                   from users
                                                   where login = 'user'),
                                                  (select id
                                                   from roles
                                                   where name = 'Пользователь'));

insert into user_orgunits (user_id, orgunit_id) values ((select id
                                                   from users
                                                   where login = 'user'),
                                                  2002310);

-- Супер пользователь, который обладает всеми ролями

insert into users (login, passwordHash, firstName, middleName, secondName, mobilePhone,
                   email, position, personnelNumber, segment, changePassword, deleted)
values ('1', '356a192b7913b04c54574d18c28d46e6395428ab', 'Админ', 'Админович', 'Админов', '+79111234567',
             'ivanov@example.org', 'Супер администратор', '12345', (select id
                                                                    from segment
                                                                    where name = 'Массовый'), false, false);


insert into user_roles (user_id, role_id) values ((select id
                                                   from users
                                                   where login = '1'),
                                                  (select id
                                                   from roles
                                                   where name = 'Главный администратор системы'));
insert into user_roles (user_id, role_id) values ((select id
                                                   from users
                                                   where login = '1'),
                                                  (select id
                                                   from roles
                                                   where name = 'Администратор системы'));
insert into user_roles (user_id, role_id) values ((select id
                                                   from users
                                                   where login = '1'),
                                                  (select id
                                                   from roles
                                                   where name = 'Продуктовый администратор'));
insert into user_roles (user_id, role_id) values ((select id
                                                   from users
                                                   where login = '1'),
                                                  (select id
                                                   from roles
                                                   where name = 'Руководитель ВСП'));
insert into user_roles (user_id, role_id) values ((select id
                                                   from users
                                                   where login = '1'),
                                                  (select id
                                                   from roles
                                                   where name = 'Пользователь'));

insert into user_orgunits (user_id, orgunit_id) values ((select id
                                                   from users
                                                   where login = '1'),
                                                  2002310);

-- Пользователь, которому при первом входе надо будет сменить пароль
insert into users (login, passwordHash, firstName, middleName, secondName, mobilePhone,
                   email, position, personnelNumber, segment, changePassword, deleted)
values
  ('change-password', '356a192b7913b04c54574d18c28d46e6395428ab', 'Артём', 'Артёмович', 'Артёмов', '+79110000014',
                      'artyomov@example.org', 'Пользователь', '10014', (select id
                                                                        from segment
                                                                        where name = 'Массовый'), true, false);

insert into user_roles (user_id, role_id) values ((select id
                                                   from users
                                                   where login = 'change-password'),
                                                  (select id
                                                   from roles
                                                   where name = 'Пользователь'));

insert into user_orgunits (user_id, orgunit_id) values ((select id
                                                   from users
                                                   where login = 'change-password'),
                                                  2002310);

commit;