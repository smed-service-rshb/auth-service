set search_path to 'auth_service';
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Сотрудник кассы'), 'ACCESS_CLIENTS_EXCEPT_MAIN_OFFICE');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Сотрудник кассы'), 'ACCESS_CLIENTS_MAIN_OFFICE');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Сотрудник КЦ'), 'ACCESS_CLIENTS_EXCEPT_MAIN_OFFICE');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Сотрудник КЦ'), 'ACCESS_CLIENTS_MAIN_OFFICE');