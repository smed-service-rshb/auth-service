CREATE TABLE rights_to_permission (
	id BIGSERIAL not null,
	right_id	varchar(60) not null,
	permission_id	varchar(255) not null,
	PRIMARY KEY(id)
);

CREATE UNIQUE INDEX rights_to_permission_idx ON rights_to_permission (right_id, permission_id);
