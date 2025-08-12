
--liquibase formatted sql

--changeset burgasvv:1
create table if not exists identity (
    id uuid default gen_random_uuid() unique not null ,
    username varchar unique not null ,
    password varchar not null ,
    email varchar unique not null ,
    phone varchar unique not null ,
    enabled boolean not null ,
    image_id uuid unique references image(id) on delete cascade on update cascade ,
    created_at timestamp not null ,
    updated_at timestamp not null
);

--changeset burgasvv:2
alter table if exists identity add column authority varchar ;
alter table if exists identity alter column authority set not null ;

--changeset burgasvv:3
insert into identity(username, password, email, phone, enabled, image_id, created_at, updated_at, authority)
values ('burgasvv','$2a$10$WmE8tNPF8gdr4.p6/InkN.VXUO/0xmh9uMigbVN.GubRCjS5loxNy','burgasvv@gmail.com',
        '+7(913)-485-96-39',true,null,'2025-08-08 12:30:30','2025-08-08 12:30:30','ADMIN');