set search_path to 'auth_service';

INSERT INTO rights (externalId, description) VALUES ('EDIT_CLIENT_TEMPLATES', 'Настройка справочников шаблонов и инструкций');
INSERT INTO rights (externalId, description) VALUES ('VIEW_CLIENT_TEMPLATES_LIST', 'Просмотр списка шаблонов и инструкций');

INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Главный администратор системы'), 'EDIT_CLIENT_TEMPLATES');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Администратор системы'), 'EDIT_CLIENT_TEMPLATES');

INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Клиент, полный доступ'), 'VIEW_CLIENT_TEMPLATES_LIST');