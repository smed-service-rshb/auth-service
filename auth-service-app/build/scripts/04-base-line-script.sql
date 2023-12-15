set search_path to 'auth_service';

CREATE TABLE roles (
  id          BIGSERIAL     NOT NULL,
  name        varchar(1024) NOT NULL,
  description varchar(1024) NOT NULL,
  PRIMARY KEY (id)
);

CREATE UNIQUE INDEX roles_name_idx
  ON roles (name);

CREATE TABLE rights (
  externalId  varchar(60)   NOT NULL,
  description varchar(1024) NOT NULL,
  PRIMARY KEY (externalId)
);

CREATE TABLE rights_to_roles (
  role_id  bigint      not null,
  right_id varchar(60) not null,
  PRIMARY KEY (role_id, right_id)
);

ALTER TABLE rights_to_roles
  ADD CONSTRAINT right_to_roles_fk
FOREIGN KEY (role_id)
REFERENCES roles (id);

ALTER TABLE rights_to_roles
  ADD CONSTRAINT roles_to_right_fk
FOREIGN KEY (right_id)
REFERENCES rights (externalId);

CREATE TABLE sessions (
  uid                  varchar(40)   NOT NULL,
  creationDate         timestamp     NOT NULL,
  state                varchar(10)   NOT NULL,
  ownerId              BIGINT        NOT NULL,
  ownerLogin           varchar(256)  NOT NULL,
  ownerFirstName       varchar(120)  NOT NULL,
  ownerSecondName      varchar(120)  NOT NULL,
  ownerMiddleName      varchar(120),
  ownerMobilePhone     varchar(18),
  ownerEmail           varchar(256),
  ownerPosition        varchar(256),
  ownerOffice          varchar(256)  NOT NULL,
  ownerBranch          varchar(256)  NOT NULL,
  ownerPersonnelNumber varchar(256)  NOT NULL,
  ownerRights          varchar(2048) NOT NULL,
  changePassword       boolean,

  PRIMARY KEY (uid)
);

CREATE TABLE segment (
  id      BIGSERIAL    NOT NULL,
  version BIGINT       NOT NULL,
  code    VARCHAR(10)  NOT NULL,
  name    VARCHAR(255) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE orgunit (
  id      BIGINT       NOT NULL,
  version BIGINT       NOT NULL,
  parent  BIGINT       NULL,
  type    VARCHAR(50)  NOT NULL,
  name    VARCHAR(255) NULL,
  address VARCHAR(255) NULL,
  city    VARCHAR(255) NULL,

  PRIMARY KEY (ID)
);

CREATE TABLE users (
  id              BIGSERIAL    NOT NULL,
  login           varchar(256) NOT NULL,
  passwordHash    varchar(256)  NOT NULL,
  firstName       varchar(150) NOT NULL,
  middleName      varchar(150),
  secondName      varchar(150) NOT NULL,
  birthdate       date,
  mobilePhone     varchar(20),
  email           varchar(256),
  position        varchar(256),
  personnelNumber varchar(20)  NOT NULL,
  segment         BIGINT       NOT NULL,
  orgunit         BIGINT       NOT NULL,
  changePassword  boolean      NOT NULL,
  deleted         boolean      NOT NULL,

  PRIMARY KEY (id)
);

ALTER TABLE users
  ADD CONSTRAINT users_segment_fk
FOREIGN KEY (segment)
REFERENCES segment (id);

ALTER TABLE users
  ADD CONSTRAINT users_orgunit_fk
FOREIGN KEY (orgunit)
REFERENCES orgunit (id);

CREATE TABLE user_roles (
  user_id bigint not null,
  role_id bigint not null,
  PRIMARY KEY (user_id, role_id)
);

ALTER TABLE user_roles
  ADD CONSTRAINT user_fk
FOREIGN KEY (user_id)
REFERENCES users (id);

ALTER TABLE rights_to_roles
  ADD CONSTRAINT role_fk
FOREIGN KEY (role_id)
REFERENCES roles (id);

CREATE OR REPLACE FUNCTION setup_role(i_description VARCHAR(1024), i_rights varchar [])
  RETURNS void AS $$
DECLARE
  savedRoleId bigint;
  rightItem   varchar;
BEGIN
  INSERT INTO roles (name, description)
  VALUES (i_description, i_description);

  select id
  into savedRoleId
  from roles
  where name = i_description;

  FOREACH rightItem IN ARRAY i_rights LOOP
    INSERT INTO rights_to_roles (role_id, right_id) VALUES (savedRoleId, rightItem);
  END LOOP;
END;
$$
LANGUAGE plpgsql;