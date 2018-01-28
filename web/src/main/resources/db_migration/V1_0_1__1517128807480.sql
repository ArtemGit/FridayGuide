
    create table check_in (
        id  bigserial not null,
        city varchar(255),
        country varchar(255),
        hostname varchar(255),
        ip varchar(255),
        latitude varchar(255),
        longitude varchar(255),
        region varchar(255),
        primary key (id)
    );

    create index UK_2haa2hh2ctx3mw15js9j2t83 on check_in (longitude, latitude);
