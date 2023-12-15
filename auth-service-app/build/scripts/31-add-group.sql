set search_path to 'auth_service';

CREATE TABLE employee_group (
  id      BIGSERIAL    NOT NULL,
  version BIGINT       NOT NULL,
  code    VARCHAR(40)  NOT NULL,
  name    VARCHAR(255) NOT NULL,
  PRIMARY KEY (ID),
  UNIQUE (code)
);

CREATE TABLE user_to_group (
  user_id bigint not null,
  group_id bigint not null,
  PRIMARY KEY (user_id, group_id)
);

SELECT setup_permission_rights('edit-employee-groups', ARRAY ['EDIT_USERS']);