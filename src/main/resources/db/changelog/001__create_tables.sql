CREATE TABLE SECTOR (
    id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code varchar2(16) NOT NULL,
    order_no integer NOT NULL,
    parent_sector_code varchar2(16) NULL,

    created_at date  NOT NULL DEFAULT SYSDATE,
    updated_at date  NULL,
    deleted_at date  NULL
);

CREATE TABLE TRANSLATION (
    id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    translation_group varchar2(128) NOT NULL,
    translation_key varchar2(256) NOT NULL,
    language varchar2(3) NOT NULL, --different versions of the same language (i.e. ENG-UK vs ENG-US) could get support, but let's not go too deep for now
    translation varchar2(1024) NOT NULL,

    created_at date  NOT NULL DEFAULT SYSDATE,
    updated_at date  NULL,
    deleted_at date  NULL
);

CREATE TABLE PERSON (
    id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar2(512),

    created_at date  NOT NULL DEFAULT SYSDATE,
    updated_at date  NULL,
    deleted_at date  NULL
);

CREATE TABLE PERSON_SECTOR (
    id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    person_id integer NOT NULL,
    CONSTRAINT PERSON_SECTOR_PERSON
        FOREIGN KEY (person_id)
            REFERENCES PERSON (id),
    sector_id integer NOT NULL,
    CONSTRAINT PERSON_SECTOR_SECTOR
        FOREIGN KEY (sector_id)
            REFERENCES SECTOR (id),

    created_at date  NOT NULL DEFAULT SYSDATE,
    updated_at date  NULL,
    deleted_at date  NULL
);
