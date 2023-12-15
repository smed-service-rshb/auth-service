set search_path to 'auth_service';

ALTER TABLE motivation_program_attachments ADD COLUMN IF NOT EXISTS creation_date timestamp;
