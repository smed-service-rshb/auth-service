INSERT INTO settings
(key, value, description)
  SELECT 'WRONG_PASSWORD_TIMEOUT', 120, 'Таймаут в секундах блокировки пользователя при превышении максимально допустимого количества попыток ввода пароля (-1 навсегда, по умолчанию 120)'
  WHERE
    NOT EXISTS (
        SELECT key FROM settings WHERE key = 'WRONG_PASSWORD_TIMEOUT'
    );

INSERT INTO settings
(key, value, description)
  SELECT 'WRONG_PASSWORD_PERIOD',  300, 'Период в секундах, за который подсчитываются количество неудачных попыток (-1 навсегда, по умолчанию 300)'
  WHERE
    NOT EXISTS (
        SELECT key FROM settings WHERE key = 'WRONG_PASSWORD_PERIOD'
    );

INSERT INTO settings
(key, value, description)
  SELECT 'WRONG_PASSWORD_MAX_ATTEMPT_COUNT', '5', 'Максимальная количество попыток ввода неправильного пароля'
  WHERE
    NOT EXISTS (
        SELECT key FROM settings WHERE key = 'WRONG_PASSWORD_MAX_ATTEMPT_COUNT'
    );

update settings set value = '-1' where key = 'WRONG_PASSWORD_TIMEOUT';
update settings set value = '-1' where key = 'WRONG_PASSWORD_PERIOD';
update settings set value = '1' where key = 'WRONG_PASSWORD_MAX_ATTEMPT_COUNT';
