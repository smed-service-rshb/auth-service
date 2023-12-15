set search_path to 'auth_service';

SELECT setup_permission_rights('get-rights', ARRAY ['EDIT_ROLES', 'VIEW_ROLES']);
SELECT setup_permission_rights('get-roles', ARRAY ['EDIT_ROLES', 'VIEW_ROLES']);
SELECT setup_permission_rights('get-role', ARRAY ['EDIT_ROLES', 'VIEW_ROLES']);
SELECT setup_permission_rights('create-role', ARRAY ['EDIT_ROLES']);
SELECT setup_permission_rights('delete-role', ARRAY ['EDIT_ROLES']);
SELECT setup_permission_rights('update-role', ARRAY ['EDIT_ROLES']);
SELECT setup_permission_rights('get-employees', ARRAY ['EDIT_USERS']);
SELECT setup_permission_rights('get-employee', ARRAY ['EDIT_USERS']);
SELECT setup_permission_rights('create-employee', ARRAY ['EDIT_USERS']);
SELECT setup_permission_rights('delete-employee', ARRAY ['EDIT_USERS']);
SELECT setup_permission_rights('update-employee', ARRAY ['EDIT_USERS']);
SELECT setup_permission_rights('lock-employee', ARRAY ['EDIT_USERS']);
SELECT setup_permission_rights('unlock-employee', ARRAY ['EDIT_USERS']);
