CREATE TABLE resume
(
    uuid      CHAR(36) PRIMARY KEY NOT NULL,
    full_name TEXT                 NOT NULL
);

CREATE TABLE contact
(
    id            SERIAL,
    resume_uuid   CHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
    contact_type  TEXT     NOT NULL,
    contact_value TEXT     NOT NULL
);
CREATE UNIQUE INDEX contact_uuid_type_index
    ON contact (resume_uuid, contact_type);

CREATE TABLE section
(
    id            SERIAL PRIMARY KEY,
    resume_uuid   CHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
    section_type  TEXT     NOT NULL,
    section_value TEXT     NOT NULL
);
CREATE UNIQUE INDEX section_uuid_type_index
    ON section (resume_uuid, section_type);