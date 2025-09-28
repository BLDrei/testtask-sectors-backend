CREATE TABLE SECTOR (
    id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code varchar2(16) NOT NULL,
    order_no integer NOT NULL,
    parent_sector_code varchar2(16) NULL,

    created_at date  NOT NULL DEFAULT SYSDATE,
    updated_at date  NULL,
    deleted_at date  NULL,
    created_by varchar2(32)  NOT NULL DEFAULT USER,
    updated_by varchar2(32)  NULL,
    deleted_by varchar2(32)  NULL
);

CREATE TABLE TRANSLATION (
    id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    translation_group varchar2(128) NOT NULL,
    translation_key varchar2(256) NOT NULL,
    language varchar2(3) NOT NULL, --different versions of the same language (i.e. ENG-UK vs ENG-US) could get support, but let's not go too deep for now
    translation varchar2(1024) NOT NULL,

    created_at date  NOT NULL DEFAULT SYSDATE,
    updated_at date  NULL,
    deleted_at date  NULL,
    created_by varchar2(32)  NOT NULL DEFAULT USER,
    updated_by varchar2(32)  NULL,
    deleted_by varchar2(32)  NULL
);
