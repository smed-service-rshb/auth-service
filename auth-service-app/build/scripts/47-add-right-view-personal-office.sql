set search_path to 'auth_service';

INSERT INTO rights (externalId, description) VALUES ('VIEW_PERSONAL_OFFICE', 'Просмотр личного кабинета клиента');

INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Главный администратор системы'), 'VIEW_PERSONAL_OFFICE');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Администратор системы'), 'VIEW_PERSONAL_OFFICE');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Пользователь'), 'VIEW_PERSONAL_OFFICE');