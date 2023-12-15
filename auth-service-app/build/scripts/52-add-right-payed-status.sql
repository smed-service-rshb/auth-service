set search_path to 'auth_service';

INSERT INTO rights (externalId, description) VALUES ('SET_PAYED_AND_FINISHED_STATUS', 'Переход в статус оплачен и расторгнут');

INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Главный администратор системы'), 'SET_PAYED_AND_FINISHED_STATUS');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Администратор системы'), 'SET_PAYED_AND_FINISHED_STATUS');
