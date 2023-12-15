set search_path to 'auth_service';

INSERT INTO rights (externalId, description) VALUES ('CLIENT_REQUEST_CREATE', 'Создание обращения клиента');
INSERT INTO rights (externalId, description) VALUES ('CLIENT_REQUEST_VIEW', 'Просмотр обращения клиента');
INSERT INTO rights (externalId, description) VALUES ('CLIENT_REQUEST_PROCESSING', 'Обработка обращения клиента');

INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Главный администратор системы'), 'CLIENT_REQUEST_PROCESSING');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Главный администратор системы'), 'CLIENT_REQUEST_VIEW');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Администратор системы'), 'CLIENT_REQUEST_PROCESSING');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Администратор системы'), 'CLIENT_REQUEST_VIEW');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Клиент, полный доступ'), 'CLIENT_REQUEST_CREATE');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Клиент, полный доступ'), 'CLIENT_REQUEST_VIEW');