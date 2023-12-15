-- Скрипт изменения типов полей в таблице sessions в связи с изменением процесса аутентификации
set search_path to 'auth_service';

alter table sessions alter column owneroffice drop not null;
alter table sessions alter column ownerbranch drop not null;
alter table sessions alter column ownerrights type varchar(8192);
