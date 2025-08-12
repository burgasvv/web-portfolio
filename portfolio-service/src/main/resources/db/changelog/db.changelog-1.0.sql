
--liquibase formatted sql

--changeset burgasvv:1
create table if not exists image (
    id uuid default gen_random_uuid() unique not null ,
    name varchar not null ,
    content_type varchar not null ,
    format varchar not null ,
    size bigint not null ,
    data bytea not null
);

--changeset burgasvv:2
create table if not exists video (
    id uuid default gen_random_uuid() unique not null ,
    name varchar not null ,
    content_type varchar not null ,
    format varchar not null ,
    size bigint not null ,
    data bytea not null
);

--changeset burgasvv:3
create table if not exists document (
    id uuid default gen_random_uuid() unique not null ,
    name varchar not null ,
    content_type varchar not null ,
    format varchar not null ,
    size bigint not null ,
    data bytea not null
);