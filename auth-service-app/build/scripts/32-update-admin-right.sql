set search_path to 'auth_service';
INSERT INTO rights (externalId, description) VALUES ('EDIT_CLIENT_REPORT_TOPICS', 'Редактирование справочника тем обращений');

INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Главный администратор системы'), 'EDIT_CLIENT_REPORT_TOPICS');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Администратор системы'), 'EDIT_CLIENT_REPORT_TOPICS');
