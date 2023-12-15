set search_path to 'auth_service';

INSERT INTO rights (externalId, description) VALUES ('VIEW_USERS', 'Разрешение на просмотр профилей пользователей');
INSERT INTO rights_to_permission(permission_id, right_id) VALUES ('get-employees', 'VIEW_USERS');