set search_path to 'auth_service';

INSERT INTO rights (externalId, description) VALUES ('RESET_CLIENT_PASSWORD', 'Установка технического пароля клиенту');

INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Главный администратор системы'), 'RESET_CLIENT_PASSWORD');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Администратор системы'), 'RESET_CLIENT_PASSWORD');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Пользователь'), 'RESET_CLIENT_PASSWORD');
