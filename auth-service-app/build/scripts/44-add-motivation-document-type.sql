set search_path to 'auth_service';

ALTER TABLE motivation_program_attachments ADD COLUMN motivation_document_type VARCHAR(30);

UPDATE motivation_program_attachments
SET motivation_document_type = 'CHECK_FORM'
WHERE motivation_document_type = null;