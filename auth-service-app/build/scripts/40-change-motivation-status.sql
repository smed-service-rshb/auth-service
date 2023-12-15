set search_path to 'auth_service';

ALTER TABLE motivation_program ADD COLUMN motivation_status VARCHAR(10);

UPDATE motivation_program
SET motivation_status = 'CORRECT'
WHERE is_checked = true;

UPDATE motivation_program
SET motivation_status = 'INCORRECT'
WHERE is_checked = false;

ALTER TABLE motivation_program DROP COLUMN is_checked;