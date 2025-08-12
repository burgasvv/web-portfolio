
--liquibase formatted sql

--changeset burgasvv:1
create table if not exists portfolio (
    id uuid default gen_random_uuid() unique not null ,
    name varchar not null ,
    description text not null ,
    identity_id uuid references identity(id) on delete cascade on update cascade ,
    profession_id uuid references profession(id) on delete set null on update cascade ,
    opened boolean not null ,
    created_at timestamp not null ,
    updated_at timestamp not null
);