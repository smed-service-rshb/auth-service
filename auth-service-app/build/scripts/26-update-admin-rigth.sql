set search_path to 'auth_service';
INSERT INTO rights (externalId, description) VALUES ('UPDATE_FULL_SET_DOCUMENT', 'Установка признака получения полного комплекта документов');

INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Главный администратор системы'), 'UPDATE_FULL_SET_DOCUMENT');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Администратор системы'), 'UPDATE_FULL_SET_DOCUMENT');