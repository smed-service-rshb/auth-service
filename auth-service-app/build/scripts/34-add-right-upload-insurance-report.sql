set search_path to 'auth_service';
INSERT INTO rights (externalId, description) VALUES ('UPLOAD_INSURANCE_REPORT', 'Импорт договоров из учетной системы');

INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Главный администратор системы'), 'UPLOAD_INSURANCE_REPORT');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Администратор системы'), 'UPLOAD_INSURANCE_REPORT');