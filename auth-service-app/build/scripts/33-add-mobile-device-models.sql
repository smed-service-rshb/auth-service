set search_path to 'auth_service';

CREATE TABLE user_mobile_devices (
  id             BIGSERIAL           NOT NULL,
  user_id        BIGSERIAL           NOT NULL references users (id),
  device_type    varchar(20)         NOT NULL,
  code           varchar(256)        UNIQUE NOT NULL,
  hashed_pin     varchar(256)        NOT NULL,
  token_uuid     varchar(256)        UNIQUE NOT NULL,
  is_active      BOOLEAN             NOT NULL,
  PRIMARY KEY (ID)
);
