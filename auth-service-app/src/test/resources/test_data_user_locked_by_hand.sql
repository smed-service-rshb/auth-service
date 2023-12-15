insert into user_locks (user_id, creation_date, locked, description, type) values (
  (select id from users where login = 'user_locked'),
  TIMESTAMP '2018-01-02 12:12:12',
  true,
  'test',
  'BY_HAND');
