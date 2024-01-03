create table if not exists main.rar_files
(
    id        INTEGER           not null
    constraint PK_RAR_FILES
    primary key autoincrement,
    file_name VARCHAR(256)      not null,
    path      TEXT              not null,
    extracted BOOLEAN default 0 not null
    );

drop table if exists DATABASECHANGELOG;
drop table if exists DATABASECHANGELOGLOCK;