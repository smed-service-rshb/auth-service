set search_path to 'auth_service';
INSERT INTO rights (externalId, description) VALUES ('NON_RESIDENT_REPORT', 'Формирование отчета о налоговых нерезидентах');

INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Главный администратор системы'), 'NON_RESIDENT_REPORT');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Администратор системы'), 'NON_RESIDENT_REPORT');