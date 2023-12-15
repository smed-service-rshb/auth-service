set search_path to 'auth_service';
INSERT INTO rights (externalId, description) VALUES ('SET_INDIVIDUAL_CURRENCY_RATE', 'Установка индивидуального курса валют для договора');

INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Главный администратор системы'), 'SET_INDIVIDUAL_CURRENCY_RATE');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Администратор системы'), 'SET_INDIVIDUAL_CURRENCY_RATE');
