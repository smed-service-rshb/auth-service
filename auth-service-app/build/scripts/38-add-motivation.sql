set search_path to 'auth_service';

CREATE TABLE motivation_program (
     id             			BIGSERIAL           NOT NULL,
     user_id        			bigint           NOT NULL references users (id),
     start_date     			timestamp,
     end_date 		 			timestamp,
     account_number 			varchar(256),
     bik_code       			varchar(256),
     bank_name		 			varchar(256),
     inn			 			varchar(256),
     registration_address		varchar(256),
     index 					    varchar(25),
     comment                    varchar(1024),
     is_checked				    boolean,
     is_active 				    boolean,
     PRIMARY KEY (ID)
);

CREATE TABLE motivation_program_attachments (
     id             			BIGSERIAL           NOT NULL,
     user_id        			bigint              NOT NULL references users (id),
     motivation_program_id      bigint              NOT NULL references motivation_program (id),
     fileName                   VARCHAR(254),
     content                    BYTEA,
     is_deleted 			    boolean,
     PRIMARY KEY (ID)
);

INSERT INTO rights (externalId, description) VALUES ('CONTROL_MOTIVATION_PROGRAMS', 'Управление программами мотиваций');
INSERT INTO rights (externalId, description) VALUES ('VIEW_MOTIVATION_PROGRAMS', 'Просмотр программ мотиваций');
INSERT INTO rights (externalId, description) VALUES ('TAKE_PART_IN_MOTIVATION_PROGRAMS', 'Участие в программах мотивации');

INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Главный администратор системы'), 'CONTROL_MOTIVATION_PROGRAMS');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Администратор системы'), 'CONTROL_MOTIVATION_PROGRAMS');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Главный администратор системы'), 'VIEW_MOTIVATION_PROGRAMS');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Администратор системы'), 'VIEW_MOTIVATION_PROGRAMS');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Пользователь'), 'VIEW_MOTIVATION_PROGRAMS');
INSERT INTO rights_to_roles (role_id, right_id) VALUES ((select  id from roles where name = 'Пользователь'), 'TAKE_PART_IN_MOTIVATION_PROGRAMS');

insert into settings(key, value, description) values ('MOTIVATION_SETTINGS_IS_ENABLED', 'true', 'Программа мотиваций активна');
insert into settings(key, value, description) values ('MOTIVATION_SETTINGS_EXPIRE_TIME', '1', 'Время действия программы мотивации в месяцах');
insert into settings(key, value, description) values ('MOTIVATION_DOCUMENT_SETTINGS_ID', 'motivation_static_print', 'Идентификатор печатной формы в сервисе хранения шаблонов');
insert into settings(key, value, description) values ('MOTIVATION_DOCUMENT_SETTINGS_NAME', 'РСХБ_Программа_Мотивации_сотрудников', 'Имя возвращаемого пользователю файла');