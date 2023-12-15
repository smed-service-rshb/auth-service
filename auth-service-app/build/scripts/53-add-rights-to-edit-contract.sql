set search_path to 'auth_service';

INSERT INTO rights (externalId, description) VALUES ('SET_DRAFT_FROM_MADE', 'Редактирование в статусе ОФОРМЛЕН');
INSERT INTO rights (externalId, description) VALUES ('SET_DRAFT_FROM_CRM_IMPORTED', 'Редактирование в статусе ВЫГРУЖЕН В CRM');

INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Главный администратор системы'), 'SET_DRAFT_FROM_MADE');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Главный администратор системы'), 'SET_DRAFT_FROM_CRM_IMPORTED');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Администратор системы'), 'SET_DRAFT_FROM_MADE');