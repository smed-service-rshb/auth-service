set search_path to 'auth_service';

INSERT INTO rights (externalId, description) VALUES ('STATEMENT_CONTROL', 'Возможность управления заявлениями');

INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Главный администратор системы'), 'STATEMENT_CONTROL');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Администратор системы'), 'STATEMENT_CONTROL');