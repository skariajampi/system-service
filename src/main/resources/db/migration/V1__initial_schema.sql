CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE adaas_system
(
    id          UUID NOT NULL DEFAULT uuid_generate_v4(),
    code        VARCHAR(4) NOT NULL,
    name        VARCHAR(255),
    description VARCHAR(255)
);
ALTER TABLE adaas_system ADD PRIMARY KEY (id);
ALTER TABLE adaas_system ADD CONSTRAINT code UNIQUE (code);