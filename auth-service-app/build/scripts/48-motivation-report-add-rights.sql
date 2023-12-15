set search_path to 'auth_service';

INSERT INTO rights (externalId, description) VALUES ('MOTIVATION_REPORT_CREATE', 'Создания отчета по мотивациям');

INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Главный администратор системы'), 'MOTIVATION_REPORT_CREATE');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Администратор системы'), 'MOTIVATION_REPORT_CREATE');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Коуч'), 'MOTIVATION_REPORT_CREATE');