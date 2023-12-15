set search_path to 'auth_service';
INSERT INTO rights (externalId, description) VALUES ('CHANGE_REPORT', 'Формирование отчёта по изменениям');

INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Главный администратор системы'), 'CHANGE_REPORT');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Администратор системы'), 'CHANGE_REPORT');