set search_path to 'auth_service';

delete from rights_to_roles where role_id = (select  id from roles where name = 'Андеррайтер');
delete from roles where name = 'Андеррайтер';
delete from rights_to_roles where role_id = (select  id from roles where name = 'Главный андеррайтер');
delete from roles where name = 'Главный андеррайтер';
delete from rights_to_roles where role_id = (select  id from roles where name = 'Заместитель директора филиала');
delete from roles where name = 'Заместитель директора филиала';
delete from rights_to_roles where role_id = (select  id from roles where name = 'Коуч');
delete from roles where name = 'Коуч';
delete from rights_to_roles where role_id = (select  id from roles where name = 'Куратор ГО Банка');
delete from roles where name = 'Куратор ГО Банка';
delete from rights_to_roles where role_id = (select  id from roles where name = 'Начальник отдела розничных продаж');
delete from roles where name = 'Начальник отдела розничных продаж';
delete from rights_to_roles where role_id = (select  id from roles where name = 'Клиент, полный доступ');
delete from roles where name = 'Клиент, полный доступ';
delete from rights_to_roles where role_id = (select  id from roles where name = 'Региональный менеджер');
delete from roles where name = 'Региональный менеджер';
delete from rights_to_roles where role_id = (select  id from roles where name = 'Сотрудник КЦ');
delete from roles where name = 'Сотрудник КЦ';
delete from rights_to_roles where role_id = (select  id from roles where name = 'Сотрудник кассы');
delete from roles where name = 'Сотрудник кассы';
delete from rights_to_roles where role_id = (select  id from roles where name = 'Пользователь ОПеру');
delete from roles where name = 'Пользователь ОПеру';
delete from rights_to_roles where role_id = (select  id from roles where name = 'Коуч оформление Альфа');
delete from roles where name = 'Коуч оформление Альфа';

delete from rights_to_roles where role_id = (select  id from roles where name = 'Продуктовый администратор');
INSERT INTO rights_to_roles (role_id, right_id)
VALUES ((select  id from roles where name = 'Продуктовый администратор'), 'EDIT_PRODUCT_SETTINGS');

delete from rights_to_roles where role_id = (select  id from roles where name = 'Руководитель ВСП');
INSERT INTO rights_to_roles (role_id, right_id)
VALUES ((select  id from roles where name = 'Руководитель ВСП'), 'VIEW_CLIENT');
INSERT INTO rights_to_roles (role_id, right_id)
VALUES ((select  id from roles where name = 'Руководитель ВСП'), 'VIEW_CLIENT_CONTRACTS');
INSERT INTO rights_to_roles (role_id, right_id)
VALUES ((select  id from roles where name = 'Руководитель ВСП'), 'CREATE_CONTRACT');
INSERT INTO rights_to_roles (role_id, right_id)
VALUES ((select  id from roles where name = 'Руководитель ВСП'), 'DELETE_CONTRACT');
INSERT INTO rights_to_roles (role_id, right_id)
VALUES ((select  id from roles where name = 'Руководитель ВСП'), 'EDIT_CONTRACT');
INSERT INTO rights_to_roles (role_id, right_id)
VALUES ((select  id from roles where name = 'Руководитель ВСП'), 'VIEW_CONTRACT_LIST_VSP');
INSERT INTO rights_to_roles (role_id, right_id)
VALUES ((select  id from roles where name = 'Руководитель ВСП'), 'VIEW_CONTRACT_REPORT_VSP');
INSERT INTO rights_to_roles (role_id, right_id)
VALUES ((select  id from roles where name = 'Руководитель ВСП'), 'EXECUTE_CREATE_CLIENT');
INSERT INTO rights_to_roles (role_id, right_id)
VALUES ((select  id from roles where name = 'Руководитель ВСП'), 'EXECUTE_EDIT_CLIENT');
INSERT INTO rights_to_roles (role_id, right_id)
VALUES ((select  id from roles where name = 'Руководитель ВСП'), 'ACCESS_CLIENTS_EXCEPT_MAIN_OFFICE');
INSERT INTO rights_to_roles (role_id, right_id)
VALUES ((select  id from roles where name = 'Руководитель ВСП'), 'ACCESS_CLIENTS_MAIN_OFFICE');

delete from rights_to_roles where role_id = (select  id from roles where name = 'Пользователь');
INSERT INTO rights_to_roles (role_id, right_id)
VALUES ((select  id from roles where name = 'Пользователь'), 'VIEW_CLIENT');
INSERT INTO rights_to_roles (role_id, right_id)
VALUES ((select  id from roles where name = 'Пользователь'), 'EXECUTE_CREATE_CLIENT');
INSERT INTO rights_to_roles (role_id, right_id)
VALUES ((select  id from roles where name = 'Пользователь'), 'CREATE_CONTRACT');
INSERT INTO rights_to_roles (role_id, right_id)
VALUES ((select  id from roles where name = 'Пользователь'), 'VIEW_CLIENT_CONTRACTS');
INSERT INTO rights_to_roles (role_id, right_id)
VALUES ((select  id from roles where name = 'Пользователь'), 'EDIT_CONTRACT');
INSERT INTO rights_to_roles (role_id, right_id)
VALUES ((select  id from roles where name = 'Пользователь'), 'VIEW_CONTRACT_REPORT_OWNER');