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

CREATE TABLE adaas_event
(
    id          UUID NOT NULL DEFAULT uuid_generate_v4(),
    code        VARCHAR(4) NOT NULL,
    name        VARCHAR(255),
    description VARCHAR(255),
    system_code VARCHAR(4) NOT NULL
);
ALTER TABLE adaas_event ADD PRIMARY KEY (id);
ALTER TABLE adaas_event ADD CONSTRAINT event_code UNIQUE (code);
ALTER TABLE adaas_event ADD CONSTRAINT fk_adaas_event_system_code
    FOREIGN KEY (system_code) REFERENCES adaas_system(code);

CREATE TABLE adaas_event_attributes
(
    id          UUID NOT NULL DEFAULT uuid_generate_v4(),
    name        VARCHAR(255),
    description VARCHAR(255),
    data_type VARCHAR(255),
    system_code VARCHAR(4) NOT NULL,
    event_code VARCHAR(4) NOT NULL
);
ALTER TABLE adaas_event_attributes ADD PRIMARY KEY (id);
ALTER TABLE adaas_event_attributes ADD CONSTRAINT event_attributes_code
    UNIQUE (system_code, event_code);
ALTER TABLE adaas_event_attributes ADD CONSTRAINT fk_adaas_event_event_attributes
    FOREIGN KEY (system_code) REFERENCES adaas_event(code);
ALTER TABLE adaas_event_attributes ADD CONSTRAINT fk_adaas_system_event_attributes
    FOREIGN KEY (system_code) REFERENCES adaas_system(code);

