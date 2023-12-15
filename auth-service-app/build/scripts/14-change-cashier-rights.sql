set search_path to 'auth_service';
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Сотрудник кассы'), 'CREATE_CONTRACT');