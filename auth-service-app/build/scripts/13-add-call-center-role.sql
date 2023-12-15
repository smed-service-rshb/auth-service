set search_path to 'auth_service';
INSERT INTO rights (externalId, description) VALUES ('VIEW_CONTRACT_EXECUTED_IN_CALL_CENTER', 'Просмотр договоров, оформленных через колл-центр');
INSERT INTO rights (externalId, description) VALUES ('CREATE_CONTRACT_IN_CALL_CENTER', 'Создание договора в колл-центре');

SELECT setup_role('Сотрудник КЦ', ARRAY [
'VIEW_CLIENT',
'VIEW_CLIENT_CONTRACTS',
'CREATE_CONTRACT',
'CREATE_CONTRACT_IN_CALL_CENTER',
'VIEW_CONTRACT_LIST_OWNER'
]);

SELECT setup_role('Сотрудник кассы', ARRAY [
'VIEW_CLIENT',
'VIEW_CLIENT_CONTRACTS',
'EDIT_CONTRACT',
'VIEW_CONTRACT_EXECUTED_IN_CALL_CENTER',
'VIEW_CONTRACT_LIST_OWNER'
]);