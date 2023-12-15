set search_path to 'auth_service';
INSERT INTO rights (externalId, description) VALUES ('MANUAL_CHECK_CLIENT', 'Запуск проверки клиентов в ручном режиме');

INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Главный администратор системы'), 'MANUAL_CHECK_CLIENT');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Администратор системы'), 'MANUAL_CHECK_CLIENT');