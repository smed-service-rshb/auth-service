set search_path to 'auth_service';

ALTER TABLE motivation_program
    ALTER COLUMN motivation_status TYPE VARCHAR(15);
