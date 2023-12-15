-- Создание пользователя и предоставление прав
DROP USER IF EXISTS auth_service;
CREATE USER auth_service WITH PASSWORD 'auth_service';
GRANT ALL ON SCHEMA auth_service TO auth_service;
ALTER DEFAULT PRIVILEGES IN SCHEMA auth_service GRANT ALL ON TABLES TO auth_service;
ALTER DEFAULT PRIVILEGES IN SCHEMA auth_service GRANT ALL ON SEQUENCES TO auth_service;

set search_path to 'auth_service';

CREATE TABLE rights_to_permission (
	right_id	varchar(60) not null,
	permission_id	varchar(255) not null,
	PRIMARY KEY(right_id, permission_id)
);

CREATE OR REPLACE FUNCTION setup_permission_rights(i_permission VARCHAR(255), i_rights varchar[])
	RETURNS void AS $$
DECLARE
	rightItem varchar;
BEGIN
	FOREACH rightItem IN ARRAY i_rights LOOP
		INSERT INTO rights_to_permission(right_id, permission_id) VALUES (rightItem, i_permission);
	END LOOP;
END;
$$ LANGUAGE plpgsql;
