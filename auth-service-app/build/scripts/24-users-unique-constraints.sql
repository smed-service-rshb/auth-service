set search_path to 'auth_service';

CREATE UNIQUE INDEX USERS_PERSONNEL_DATA_UNQ ON users (firstname, middleName, secondName, birthdate, mobilephone)
;
CREATE UNIQUE INDEX USERS_LOGIN_UNQ ON users (login)
;