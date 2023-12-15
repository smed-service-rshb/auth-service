CREATE TABLE IF NOT EXISTS sessions
(
    uid                  varchar(40)   not null constraint sessions_pkey primary key,
    creationdate         timestamp     not null,
    state                varchar(10)   not null,
    ownerid              bigint        not null,
    ownerlogin           varchar(256)  not null,
    ownerfirstname       varchar(120)  not null,
    ownersecondname      varchar(120)  not null,
    ownermiddlename      varchar(120),
    ownermobilephone     varchar(18),
    owneremail           varchar(256),
    ownerposition        varchar(256),
    owneroffice          varchar(256),
    ownerbranch          varchar(256),
    ownerpersonnelnumber varchar(256)  not null,
    ownerrights          varchar(8192) not null,
    changepassword       boolean
);

ALTER TABLE sessions
    ALTER COLUMN ownerrights TYPE VARCHAR(8192);

INSERT INTO sessions (uid, creationdate, state, ownerid, ownerlogin,
                      ownerfirstname, ownersecondname, ownermiddlename, ownermobilephone, owneremail,
                      ownerposition, owneroffice, ownerbranch, ownerpersonnelnumber,
                      ownerrights,
                      changepassword)
VALUES ('17f7479f-2583-4bc9-9ed0-5c922aaf4c99', '2019-01-01 18:19:14.848000', '1', 3, 'testuser',
        'Федоров', 'Федор', 'Федорович', '+77777777777', 'testuser@mailforspam.com',
        'Пользователь', '0001', 'Адский', '1009',
        'VIEW_CLIENT_CONTRACTS,CREATE_CONTRACT,DELETE_CONTRACT,EDIT_CONTRACT,VIEW_CONTRACT_LIST_OWNER,VIEW_MOTIVATION_PROGRAMS,TAKE_PART_IN_MOTIVATION_PROGRAMS,ACCESS_CLIENTS_EXCEPT_MAIN_OFFICE,ACCESS_CLIENTS_MAIN_OFFICE,VIEW_BUSINESS_DICTIONARIES,VIEW_CONTRACT_EXECUTED_IN_CALL_CENTER',
        false)