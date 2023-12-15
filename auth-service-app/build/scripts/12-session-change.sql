set search_path to 'auth_service';
ALTER TABLE sessions ALTER COLUMN ownerMiddleName DROP NOT NULL;
ALTER TABLE sessions ALTER COLUMN ownerMobilePhone DROP NOT NULL;