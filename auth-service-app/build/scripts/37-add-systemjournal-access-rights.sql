set search_path to 'auth_service';

INSERT INTO rights (externalId, description) VALUES ('SYSTEM_JOURNAL_VIEW', 'Просмотр содержимого системного журнала');
INSERT INTO rights (externalId, description) VALUES ('SYSTEM_JOURNAL_SETTINGS', 'Настройка и управление системным журналом');

INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Главный администратор системы'), 'SYSTEM_JOURNAL_VIEW');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Администратор системы'), 'SYSTEM_JOURNAL_VIEW');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Главный администратор системы'), 'SYSTEM_JOURNAL_SETTINGS');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Администратор системы'), 'SYSTEM_JOURNAL_SETTINGS');