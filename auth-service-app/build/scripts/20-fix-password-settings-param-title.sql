set search_path to 'auth_service';

update settings
set description = 'Признак, показывающий надо ли выполнять проверку сложности паролей'
where key = 'PASSWORD_SETTINGS_CHECK_ENABLED';

commit;